package cellsociety.Controller;

import cellsociety.Model.*;
import cellsociety.View.HexagonCell;
import cellsociety.View.PolygonCell;
import cellsociety.View.RectangleCell;
import java.util.Collection;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Purpose: handle frontend triggered events (middleware between view and models) - creating new
 * Grid based on initial configuration data - handling button pressed (resume, stop, fasten) -
 * handling step function called every frame
 * <p>
 * Assumptions: - user provides all required configuration through XML
 * <p>
 * Dependencies: - Models: calls Grid and appropriate Cell to initiate and update every step
 *
 * @author Jay Yoon
 */
public class SimulationController {

  public static final double SECOND_DELAY = 0.5;
  private static final double SPEED_FACTOR = 0.5;
  private static final int CONWAY_ID = 1;
  private static final int FIRE_ID = 2;
  private static final int SEGREGATION_ID = 3;
  private static final int PREDATION_ID = 4;
  private static final int PERCOLATION_ID = 5;
  private static final int SAND_ID = 6;
  private static final int SUGAR_ID = 7;
  private static final int ANT_ID = 8;
  private static final int LANGTON_ID = 9;
  private ArrayList<Integer> initialLayout;
  private ArrayList<Polygon> cellList;
  private HashMap<Integer, String> colorMap;
  private Grid grid;
  private int width;
  private int height;
  private double prob;
  private Timeline timeline;
  private int simulationId;
  private XMLController xmlController;
  private ConfigData settings;
  private AboutData about;
  private CellShape shape;
  private PolygonCell cellGUI;

  public SimulationController(ConfigData data, AboutData aboutData, String langOption) {
    this.xmlController = new XMLController(langOption);
    this.settings = data;
    this.about = aboutData;
    this.simulationId = data.id();
    this.initialLayout = data.cellLayout();
    this.grid = new Grid(data.width(), data.height());
    this.cellGUI = new RectangleCell();
    this.cellList = new ArrayList<>();
    this.shape = data.shape();
    this.colorMap = data.cellColors();
    this.width = data.width();
    this.height = data.height();
    this.prob = data.prob();
    this.timeline = new Timeline();
  }

  /**
   * starts simulation by: - populating Grid with cell Model - creating cell GUI list return value:
   * cell GUI list (Collection of Polygons)
   */
  public Collection<Polygon> initiateSimulation() {
    initiateGrid();
    initiateGUI();

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step()));
    timeline.play();

    return cellList;
  }

  private void initiateGrid() {
    for (int i = 0; i < initialLayout.size(); i++) {
      int cellType = initialLayout.get(i);
      switch (simulationId) {
        case CONWAY_ID -> {
          if (shape == CellShape.STANDARD_CELL) {
            grid.place(new CellConway(cellType, colorMap.get(cellType), i, width));
          } else {
            grid.place(new HexCellConway(cellType, colorMap.get(cellType), i, width));
            cellGUI = new HexagonCell();
          }
        }
        case FIRE_ID -> grid.place(new CellFire(cellType, colorMap.get(cellType), i, width, prob));
        case SEGREGATION_ID ->
            grid.place(new CellSegregation(cellType, colorMap.get(cellType), i, width, prob));
        case PREDATION_ID ->
            grid.place(new CellPredation(cellType, colorMap.get(cellType), i, width));
        case PERCOLATION_ID ->
            grid.place(new CellPercolation(cellType, colorMap.get(cellType), i, width));
        case SAND_ID -> grid.place(new CellSand(cellType, colorMap.get(cellType), i, width));
        case SUGAR_ID -> grid.place(new CellSugarAgent(cellType, colorMap.get(cellType), i, width));
        case ANT_ID -> grid.place(new CellAnt(cellType, colorMap.get(cellType), i, width));
        case LANGTON_ID -> grid.place(new CellLangton(cellType, colorMap.get(cellType), i, width));
      }
    }
    grid.initiateNeighbors();
  }

  private void initiateGUI() {
    cellList = cellGUI.drawCells(grid);
  }

  /**
   * updates gap between cells parameter: integer value input by user on window
   */
  public void handleGap(int value) {
    grid.setGap(value);
  }

  /**
   * gets timeline of simulation return value: animation timeline
   */
  public Timeline getTimeline() {
    return this.timeline;
  }

  /**
   * every step of animation timeline: - updates Grid Cell Model states by applying rule - load new
   * cell color display based on updated Cell states
   */
  public void step() {
    grid.applyRules();
    loadNewLayout();
  }

  /**
   * counts cells by state return value: HashMap with cell state as key, cell count as value
   */
  public HashMap<Integer, Integer> getCellCount() {
    HashMap<Integer, Integer> cellTypeMap = new HashMap<>();
    for (ImmutableCell c : grid.getCells()) {
      cellTypeMap.putIfAbsent(c.getType(), 0);
      cellTypeMap.put(c.getType(), cellTypeMap.get(c.getType()) + 1);
    }
    return cellTypeMap;
  }

  /**
   * gets total number of cells present on Grid return value: Integer value of total cell count
   */
  public int getTotalCellCount() {
    return grid.getSize();
  }

  private void loadNewLayout() {
    grid.update(colorMap);
    ArrayList<ImmutableCell> cells = (ArrayList<ImmutableCell>) grid.getCells();
    for (int i = 0; i < cells.size(); i++) {
      String hex = cells.get(i).getColor();
      cellList.get(i).setFill(Color.web(hex));
    }
  }

  /**
   * pauses simulation when Pause button pressed by user
   */
  public void handlePause() {
    this.timeline.pause();
  }

  /**
   * resumes simulation when Resume button pressed by user
   */
  public void handleResume() {
    this.timeline.play();
  }

  /**
   * speeds up simulation speed when speedup button pressed by user
   */
  public void handleSpeedUp() {
    double currentSpeed = this.timeline.getCurrentRate();
    this.timeline.setRate(currentSpeed * (1 + SPEED_FACTOR));
  }

  /**
   * slows down simulation speed when slowdown button pressed by user
   */
  public void handleSlowDown() {
    double currentSpeed = this.timeline.getCurrentRate();
    this.timeline.setRate(currentSpeed * (1 - SPEED_FACTOR));
  }

  /**
   * saves simulation current state when save button pressed by user
   */
  public void save() {
    xmlController.save(grid, settings, about);
  }

}
