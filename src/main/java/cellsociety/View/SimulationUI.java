package cellsociety.View;

import cellsociety.Controller.AboutData;
import cellsociety.Controller.ConfigData;
import cellsociety.Controller.SimulationController;
import cellsociety.Controller.XMLController;
import cellsociety.Controller.XMLException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Purpose:
 * <p> Initialize GUI and controllers to run simulation
 * Assumptions:
 * <p> Number of cells does not exceed a certain threshold
 * Dependencies:
 * <p> Buttons, controllers, and meta data.
 * @author Woonggyu Jin
 */
public class SimulationUI extends Application {

  public static final int SCREEN_WIDTH = 1200;
  public static final int SCREEN_HEIGHT = 800;
  public static final int CHART_HEIGHT = 200;
  public static final double CHART_WIDTH = 400;
  private static final Color BACKGROUND = Color.WHITE;

  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.version";
  private static final String TITLE = "Cell Automata";
  private static final String STYLE_PATH = "/simulator.css";
  private static final String ICON_PATH = "/cellsociety/images/prog_icon.png";

  private static final Image mySimIcon = new Image(ICON_PATH);
  private static final ControlButtons ctrlButtons = new ControlButtons();
  private static final ViewButtons viewButtons = new ViewButtons();
  private static final ModelButtons modelButtons = new ModelButtons();
  private static AboutData about;
  private static ConfigData data;
  private static ArrayList<Group> myRoots = new ArrayList<>();

  @Override
  public void start(Stage primaryStage) {
    startModel();
  }

  private static Scene setUpScene(int width, int height, Paint background, Group root,
      SimulationController simController, ResourceBundle resourceBundle, AboutData about) {
    StackPane charts = createLineChart(simController, root, simController.getTimeline());
    ctrlButtons.addControlButtons(root, simController);
    modelButtons.addModelButtons(root, simController, resourceBundle, about);
    viewButtons.addViewButtons(root, charts);

    Scene scene = new Scene(root, width, height, background);
    scene.getStylesheets().add(String.valueOf(SimulationUI.class.getResource(STYLE_PATH)));
    return scene;
  }

  private static void initializeModel(Button b) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE);
    XMLController xmlController = new XMLController("ENG");
    Stage stage = (b == null) ? new Stage() : (Stage) b.getScene().getWindow();
    File dataFile = xmlController.getChooser().showOpenDialog(stage);
    try {
      data = xmlController.loadConfig(dataFile);
      about = xmlController.loadDescription(dataFile);
      SimulationController simController = new SimulationController(data, about, "ENG");
      Collection<Polygon> cells = simController.initiateSimulation();
      Group newRoot = new Group();
      myRoots.add(newRoot);
      for (Polygon c : cells) {
        newRoot.getChildren().add(c);
      }
      Scene newScene = setUpScene(SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND, newRoot, simController,
          resourceBundle, about);
      stage.setScene(newScene);
      stage.getIcons().add(mySimIcon);
      stage.setTitle(TITLE);
      stage.setResizable(false);
      stage.show();

    } catch (XMLException e) {
      showError(e.getMessage());
    }
  }

  /**
   * Purpose:
   * <p> Start a model from its beginning
   * Assumptions:
   * <p> None
   * Dependencies:
   * <p> None
   * @author Woonggyu Jin
   */
  public static void startModel() {
    initializeModel(null);
  }

  /**
   * Purpose:
   * <p> Switch model being displayed
   * Assumptions:
   * <p> None
   * Dependencies:
   * <p> None
   * @author Woonggyu Jin
   */
  public static void switchModel(Button b) {
    initializeModel(b);
  }

  private static void showError(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private static StackPane createLineChart(SimulationController simController, Group root,
      Timeline timeline) {
    ArrayList<ChartView> charts = new ArrayList<>();
    Canvas canvas = new Canvas(CHART_WIDTH, CHART_HEIGHT);
    StackPane holder = new StackPane();
    holder.getChildren().add(canvas);
    holder.setStyle("-fx-background-color: grey");

    GraphicsContext g = canvas.getGraphicsContext2D();

    for (int type : simController.getCellCount().keySet()) {
      charts.add(new ChartView(g, Color.web(data.cellColors().get(type)), simController, type));
    }

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(SimulationController.SECOND_DELAY), e -> {
      g.clearRect(0, 0, SCREEN_WIDTH, CHART_HEIGHT);
      charts.forEach(chart -> chart.update());
    }));
    timeline.play();
    holder.setLayoutY(2 * Buttons.BUTTON_HEIGHT);
    holder.setLayoutX(SCREEN_WIDTH - CHART_WIDTH);
    root.getChildren().add(holder);
    return holder;
  }

  private static void main(String[] args) {
    launch(args);
  }
}
