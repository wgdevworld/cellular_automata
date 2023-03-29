package cellsociety.View;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Purpose:
 * <p> Initialize ViewButtons
 * Assumptions:
 * <p> There is only one type of view; need to move all buttons to the left with each addition
 * Dependencies:
 * <p> None
 * @author Woonggyu Jin
 */
public class ViewButtons extends Buttons {
  private static final String CHART_PATH = "/cellsociety/images/chart.png";

  /**
   * Purpose:
   * <p> Initialize buttons for controlling the view of simulation
   * Assumptions:
   * <p> None
   * Dependencies:
   * <p> None
   * @author Woonggyu Jin
   */
  public void addViewButtons(Group root, Node node) {
    HBox hBox = new HBox();
    Button graphButton = buttonFromImage(CHART_PATH, e -> toggleView(node));

    hBox.getChildren()
        .addAll(graphButton);

    hBox.setLayoutY(BUTTON_HEIGHT);
    hBox.setLayoutX(SimulationUI.SCREEN_WIDTH - BUTTON_WIDTH);
    root.getChildren().add(hBox);
  }

  private void toggleView(Node node) {
    node.setVisible(!node.isVisible());
  }

}
