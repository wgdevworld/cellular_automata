package cellsociety.Controller;


import cellsociety.Model.Grid;
import cellsociety.Model.ImmutableCell;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SplittableRandom;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Purpose: mainly to read and write XML files. 1. read simulation settings from XML 2. return
 * ConfigData and AboutData Records. 3. write current simulation status (cell layout and
 * configuration) to be saved XML format.
 * <p>
 * Assumptions: error occurs for following situations (handles with XMLException) 1. no file is
 * selected 2. required values not provided in file (width, height, simulation ID) 3. required tags
 * not provided in file (width, height, simulation ID) 4. invalid values for these tags are given
 * (ex. String values for width, non-existent simulation ID) 5. empty or invalid (non-txt,
 * non-existent file) initial layout given 6. initial layout file refers to invalid cell state 7.
 * width and height do not match given initial layout
 * <p>
 * Dependencies: - Resources: resource package to retrieve appropriate error messages (EN/FR) -
 * Models: Grid and ImmutableCell to write current status (save feature)
 *
 * @author Jay Yoon
 */
public class XMLController {

  private static final String DATA_FILE_EXTENSION = "*.xml";
  private static final String LAYOUT_FILE_EXTENSION = ".txt";
  private static final String DATA_FILE_FOLDER = System.getProperty("user.dir") + "/data";
  private static final String DATA_PATH = "data/";
  private FileChooser FILE_CHOOSER;
  private static final String OUTPUT_INDENT_PROPERTY = "yes";
  private static final String OUTPUT_FILE_TYPE = "xml";
  private static final String OUTPUT_EXTENSION = ".xml";
  private static final String LAYOUT_EXTENSION = ".txt";
  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.errors";
  private static final String CHOOSER_DESC = "CHOOSER_DESC";
  private static final String STARTING_TAG = "initialState";
  private static final String ID_TAG = "id";
  private static final String WIDTH_TAG = "width";
  private static final String HEIGHT_TAG = "height";
  private static final String LAYOUT_TAG = "layout";
  private static final String CELL_TAG = "cellType";
  private static final String AUTHOR_TAG = "author";
  private static final String DESC_TAG = "description";
  private static final String PROB_TAG = "probability";
  private static final String COLOR_TAG = "color";
  private static final String ROOT_TAG = "simulation";
  private static final String HEX_TAG = "hex";
  private static final String SHAPE_TAG = "cellShape";
  private static final int MIN_SIMULATION_ID = 1;
  private static final int MAX_SIMULATION_ID = 10;
  private static final String ERROR_LAYOUT_404 = "LAYOUT_404";
  private static final String ERROR_PARSER_EXCEPTION = "PARSER_EXCEPTION";
  private static final String ERROR_LAYOUT_FORMAT = "LAYOUT_FORMAT";
  private static final String ERROR_LAYOUT_EMPTY = "LAYOUT_EMPTY";
  private static final String ERROR_INVALID_CELL = "INVALID_CELL";
  private static final String ERROR_MISSING_VALUE = "MISSING_VALUE";
  private static final String ERROR_NUMERIC = "NUMERIC";
  private static final String ERROR_MISSING_TAG = "MISSING_TAG";
  private static final String ERROR_INVALID_SIMULATION = "INVALID_SIMULATION";
  private static final String ERROR_LAYOUT_OOB = "LAYOUT_OOB";
  private static final String RANDOM = "random";
  private HashMap<Integer, String> cellColors;
  private int width;
  private int height;
  private ResourceBundle resourceBundle;

  public XMLController(String lang) {
    resourceBundle = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + lang);
    FILE_CHOOSER = makeChooser(DATA_FILE_EXTENSION);
  }

  private FileChooser makeChooser(String extensionAccepted) {
    FileChooser result = new FileChooser();
    result.setInitialDirectory(new File(DATA_FILE_FOLDER));
    result.getExtensionFilters()
        .setAll(new FileChooser.ExtensionFilter(resourceBundle.getString(CHOOSER_DESC),
            extensionAccepted));
    return result;
  }

  /**
   * reads input configuration simulation details in XML to initiate simulation parameter: XML file
   * return value: immutable ConfigData record
   */
  public ConfigData loadConfig(File file) {
    try {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
      cellColors = readColors(doc);
      width = readDimension(doc, WIDTH_TAG);
      height = readDimension(doc, HEIGHT_TAG);
      ArrayList<Integer> cellStartingLayout = readLayout(doc);
      int simulationId = readDimension(doc, ID_TAG);
      checkSimulationId(simulationId);
      int probability = readProperties(doc);
      CellShape shape = readShape(doc);

      return new ConfigData(cellColors, cellStartingLayout, simulationId, width, height,
          probability, shape);

    } catch (ParserConfigurationException | IOException | SAXException |
             IllegalArgumentException e) {
      throw new XMLException(e, resourceBundle.getString(ERROR_PARSER_EXCEPTION));
    }
  }

  /**
   * reads simulation description details in XML to be displayed parameter: XML file return type:
   * immutable AboutData record
   */
  public AboutData loadDescription(File file) {
    try {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
      Node node = doc.getElementsByTagName(STARTING_TAG).item(0);
      int simulationId = Integer.parseInt(getValue(node, ID_TAG));
      String author = getValue(node, AUTHOR_TAG);
      String desc = getValue(node, DESC_TAG);

      return new AboutData(simulationId, author, desc);

    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new XMLException(e, resourceBundle.getString(ERROR_PARSER_EXCEPTION));
    }
  }

  private HashMap<Integer, String> readColors(Document doc) {
    HashMap<Integer, String> colorMap = new HashMap<>();
    NodeList nodeList = doc.getElementsByTagName(CELL_TAG);
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element e = (Element) node;
        int cellId = Integer.parseInt(e.getElementsByTagName(ID_TAG).item(0).getTextContent());
        String cellColor = e.getElementsByTagName(COLOR_TAG).item(0).getTextContent();
        colorMap.put(cellId, cellColor);
      }
    }
    return colorMap;
  }

  private ArrayList<Integer> readLayout(Document doc) {
    ArrayList<Integer> layout = new ArrayList<>();
    NodeList nodeList = doc.getElementsByTagName(STARTING_TAG);
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element e = (Element) node;
        String layoutFilePath = e.getElementsByTagName(LAYOUT_TAG).item(0).getTextContent();
        if (layoutFilePath.equals(RANDOM)) {
          randomizeLayout(cellColors.keySet(), layout);
        } else {
          layoutReader(layoutFilePath, layout);
        }
      }
    }
    return layout;
  }

  private void layoutReader(String filePath, ArrayList<Integer> list) throws XMLException {
    try {
      if (filePath.equals("")) {
        throw new XMLException(resourceBundle.getString(ERROR_LAYOUT_EMPTY));
      }
      String fileExtension = filePath.substring(filePath.lastIndexOf('.'));
      if (!fileExtension.equals(LAYOUT_FILE_EXTENSION)) {
        throw new XMLException(resourceBundle.getString(ERROR_LAYOUT_FORMAT));
      }
      Scanner fileReader = new Scanner(new File(DATA_PATH + filePath));
      if (!fileReader.hasNextInt()) {
        throw new XMLException(resourceBundle.getString(ERROR_LAYOUT_EMPTY));
      }
      while (fileReader.hasNextInt()) {
        int position = Integer.parseInt(fileReader.next());
        if (!cellColors.containsKey(position)) {
          throw new XMLException(resourceBundle.getString(ERROR_INVALID_CELL));
        }
        list.add(position);
      }
      fileReader.close();
      if (list.size() != width * height) {
        throw new XMLException(resourceBundle.getString(ERROR_LAYOUT_OOB));
      }

    } catch (FileNotFoundException e) {
      throw new XMLException(e, resourceBundle.getString(ERROR_LAYOUT_404));
    }
  }

  private void randomizeLayout(Set<Integer> cellTypes, ArrayList<Integer> list) {
    ArrayList<Integer> types = new ArrayList<>(cellTypes);
    int totalCells = width * height;
    for (int i = 0; i < totalCells; i++) {
      SplittableRandom random = new SplittableRandom();
      int p = random.nextInt(cellTypes.size());
      list.add(types.get(p));
    }
  }

  private int readDimension(Document doc, String target) {
    NodeList nodeList = doc.getElementsByTagName(STARTING_TAG);
    Node node = nodeList.item(0);
    String s = getValue(node, target);
    if (s == null || s.equals("")) {
      throw new XMLException(String.format(resourceBundle.getString(ERROR_MISSING_VALUE), target));
    }
    try {
      return Integer.parseInt(getValue(node, target));
    } catch (NumberFormatException e) {
      throw new XMLException(String.format(resourceBundle.getString(ERROR_NUMERIC), target));
    }
  }

  private CellShape readShape(Document doc) {
    NodeList nodeList = doc.getElementsByTagName(STARTING_TAG);
    Node node = nodeList.item(0);
    String s = getValue(node, SHAPE_TAG);
    if (s.equals(HEX_TAG)) {
      return CellShape.HEX_CELL;
    } else {
      return CellShape.STANDARD_CELL;
    }
  }

  private int readProperties(Document doc) {
    NodeList nodeList = doc.getElementsByTagName(STARTING_TAG);
    Node node = nodeList.item(0);
    return Integer.parseInt(getValue(node, PROB_TAG));
  }

  private String getValue(Node node, String tagName) {
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      Element element = (Element) node;
      if (element.getElementsByTagName(tagName).item(0) == null) {
        throw new XMLException(String.format(resourceBundle.getString(ERROR_MISSING_TAG), tagName));
      }
      return element.getElementsByTagName(tagName).item(0).getTextContent();
    }
    return "";
  }

  private void checkSimulationId(int id) throws XMLException {
    if (id < MIN_SIMULATION_ID || id > MAX_SIMULATION_ID) {
      throw new XMLException(String.format(resourceBundle.getString(ERROR_INVALID_SIMULATION), id));
    }
  }

  /**
   * gets private FileChooser to be used in starting simulation return value: FileChooser
   */
  public FileChooser getChooser() {
    return this.FILE_CHOOSER;
  }

  /**
   * saves current status of simulation in XML to be later be used as input configuration file
   * parameter: current Grid, simulation details needed to write configuration file
   */
  public void save(Grid grid, ConfigData settings, AboutData about) {
    DirectoryChooser chooser = new DirectoryChooser();
    File loc = null;
    while (loc == null) {
      loc = chooser.showDialog(null);
    }
    String newLayout = saveFormatTxt(loc, grid);

    Document dom;
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      dom = builder.newDocument();
      Element rootEle = dom.createElement(ROOT_TAG);
      writeHeader(dom, rootEle, newLayout, settings, about);
      writeCellType(dom, rootEle, settings.cellColors());
      dom.appendChild(rootEle);

      exportFile(dom, loc);

    } catch (ParserConfigurationException e) {
      throw new XMLException(e, resourceBundle.getString(ERROR_PARSER_EXCEPTION));
    }
  }

  private void exportFile(Document dom, File loc) {
    try {
      Transformer tr = TransformerFactory.newInstance().newTransformer();
      tr.setOutputProperty(OutputKeys.INDENT, OUTPUT_INDENT_PROPERTY);
      tr.setOutputProperty(OutputKeys.METHOD, OUTPUT_FILE_TYPE);
      String fileName = loc + "/" + LocalDateTime.now() + OUTPUT_EXTENSION;
      tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(fileName)));
    } catch (FileNotFoundException | TransformerException e) {
      throw new XMLException(e, resourceBundle.getString(ERROR_LAYOUT_404));
    }
  }

  private void writeHeader(Document dom, Element rootEle, String newLayout, ConfigData settings,
      AboutData about) {
    Element head = dom.createElement(STARTING_TAG);
    writeElement(dom, head, ID_TAG, Integer.toString(settings.id()));
    writeElement(dom, head, WIDTH_TAG, Integer.toString(settings.width()));
    writeElement(dom, head, HEIGHT_TAG, Integer.toString(settings.height()));
    writeElement(dom, head, SHAPE_TAG, settings.shape().toString());
    writeElement(dom, head, LAYOUT_TAG, newLayout.substring(newLayout.lastIndexOf("/") + 1));
    writeElement(dom, head, PROB_TAG, Integer.toString(settings.prob()));
    writeElement(dom, head, AUTHOR_TAG, about.author());
    writeElement(dom, head, DESC_TAG, about.desc());
    rootEle.appendChild(head);
  }

  private void writeCellType(Document dom, Element rootEle, HashMap<Integer, String> colorMap) {
    for (Map.Entry<Integer, String> cellType : colorMap.entrySet()) {
      Element head = dom.createElement(CELL_TAG);
      writeElement(dom, head, ID_TAG, Integer.toString(cellType.getKey()));
      writeElement(dom, head, COLOR_TAG, cellType.getValue());
      rootEle.appendChild(head);
    }
  }

  private void writeElement(Document dom, Element root, String tagName, String content) {
    Element e = dom.createElement(tagName);
    e.appendChild(dom.createTextNode(content));
    root.appendChild(e);
  }

  private String saveFormatTxt(File loc, Grid grid) {
    String fileName = loc + "/" + LocalDateTime.now() + LAYOUT_EXTENSION;
    File file = new File(fileName);
    PrintWriter exportFile;
    try {
      exportFile = new PrintWriter(file);
    } catch (FileNotFoundException e) {
      throw new XMLException(e, resourceBundle.getString(ERROR_LAYOUT_404));
    }
    int lineBreak = 0;
    for (ImmutableCell c : grid.getCells()) {
      lineBreak++;
      exportFile.print(c.getType() + " ");
      if (lineBreak % grid.getWidth() == 0) {
        exportFile.println();
      }
    }
    exportFile.close();

    return fileName;
  }
}
