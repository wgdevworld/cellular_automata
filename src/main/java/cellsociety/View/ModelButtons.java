package cellsociety.View;

import cellsociety.Controller.AboutData;
import cellsociety.Controller.SimulationController;
import java.util.ResourceBundle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Purpose:
 * <p> Define & initialize buttons for controlling the model displayed
 * Assumptions:
 * <p> There are four buttons; need to move all buttons to the left with each addition
 * Dependencies:
 * <p> None
 * @author Woonggyu Jin
 */
public class ModelButtons extends Buttons{
  private static final String SAVE_PATH = "/cellsociety/images/save.png";
  private static final String SWITCH_PATH = "/cellsociety/images/switch.png";
  private static final String INFO_PATH = "/cellsociety/images/info.png";
  private static final String NEW_PATH = "/cellsociety/images/new.png";
  private static final String INFO_TITLE = "Simulation Info";

  private static final int INFO_WIDTH = 600;
  private static final int INFO_HEIGHT = 400;
  private static final int LABEL_SPACING = 15;

  /**
   * Purpose:
   * <p> Initialize buttons for controlling the model displayed and any controllers involved.
   * Assumptions:
   * <p> The hexagons are regular hexagons.
   * Dependencies:
   * <p> None
   * @author Woonggyu Jin
   */

  public void addModelButtons(Group root, SimulationController simController,
      ResourceBundle resourceBundle, AboutData aboutData) {
    HBox hBox = new HBox();
    Button newButton = buttonFromImage(NEW_PATH, e -> SimulationUI.startModel());
    Button infoButton = buttonFromImage(INFO_PATH, e -> showInfoPopup(resourceBundle, aboutData));
    Button switchButton = buttonFromImage(SWITCH_PATH, null);
    switchButton.setOnAction(e -> SimulationUI.switchModel(switchButton));
    Button saveButton = buttonFromImage(SAVE_PATH, e -> simController.save());

    hBox.getChildren()
        .addAll(newButton, infoButton, switchButton, saveButton);

    hBox.setLayoutY(0);
    hBox.setLayoutX(SimulationUI.SCREEN_WIDTH - 4 * BUTTON_WIDTH);
    root.getChildren().add(hBox);
  }

  private void showInfoPopup(ResourceBundle resourceBundle, AboutData aboutData) {
    Stage infoPopup = new Stage();
    Group root = new Group();
    Label simId = new Label(resourceBundle.getString("ID") + aboutData.simulationId());
    Label author = new Label(resourceBundle.getString("AUTHOR") + aboutData.author());
    author.setLayoutY(LABEL_SPACING);
    Label description = new Label(resourceBundle.getString("DESCRIPTION") + aboutData.desc());
    description.setLayoutY(2 * LABEL_SPACING);
    root.getChildren().addAll(simId, author, description);
    Scene infoScene = new Scene(root);
    infoPopup.setScene(infoScene);
    infoPopup.initModality(Modality.APPLICATION_MODAL);
    infoPopup.setTitle(INFO_TITLE);
    infoPopup.setWidth(INFO_WIDTH);
    infoPopup.setHeight(INFO_HEIGHT);
    infoPopup.show();
  }
}
