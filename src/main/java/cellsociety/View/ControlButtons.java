package cellsociety.View;


import cellsociety.Controller.SimulationController;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Purpose:
 * <p> Define & initialize buttons for controlling the simulation displayed
 * Assumptions:
 * <p> There are three buttons; need to move all buttons to the left with each addition
 * Dependencies:
 * <p> None
 * @author Woonggyu Jin
 */

public class ControlButtons extends Buttons {

  private static final String STEP_FORWARD_PATH = "/cellsociety/images/front_frame.png";
  private static final String STEP_BACK_PATH = "/cellsociety/images/back_frame.png";
  private static final String SPEED_UP_PATH = "/cellsociety/images/fast_forward.png";
  private static final String SLOW_DOWN_PATH = "/cellsociety/images/slow_down.png";
  private static final String PLAY_PATH = "/cellsociety/images/play.png";
  private static final String PAUSE_PATH = "/cellsociety/images/pause.png";

  /**
   * Purpose:
   * <p> Initialize buttons for controlling the simulation displayed
   * Assumptions:
   * <p> None
   * Dependencies:
   * <p> None
   * @author Woonggyu Jin
   */

  public void addControlButtons(Group root, SimulationController simController) {
    HBox hBox = new HBox();
    Button playButton = buttonFromImage(PLAY_PATH, e -> simController.handleResume());
    Button pauseButton = buttonFromImage(PAUSE_PATH, e -> simController.handlePause());
    Button stepForwardButton = buttonFromImage(STEP_FORWARD_PATH, e -> simController.step());
    Button stepBackButton = buttonFromImage(STEP_BACK_PATH, null);
    Button speedUpPath = buttonFromImage(SPEED_UP_PATH, e -> simController.handleSpeedUp());
    Button slowDownButton = buttonFromImage(SLOW_DOWN_PATH, e -> simController.handleSlowDown());

    hBox.getChildren()
        .addAll(slowDownButton, stepBackButton, playButton, pauseButton, stepForwardButton,
            speedUpPath);

    hBox.setLayoutY(SimulationUI.SCREEN_HEIGHT - BUTTON_HEIGHT);
    hBox.setLayoutX(SimulationUI.SCREEN_WIDTH / 2 - 3 * BUTTON_WIDTH);
    root.getChildren().add(hBox);
  }

}
