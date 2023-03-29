package cellsociety.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Purpose:
 * <p> Abstraction to accommodate multiple types of buttons
 * Assumptions:
 * <p> None
 * Dependencies:
 * <p> None
 * @author Woonggyu Jin
 */

public abstract class Buttons extends Button {
  private static final int IMAGE_WIDTH = 30;
  private static final int IMAGE_HEIGHT = 30;
  public static final int BUTTON_WIDTH = 50;
  public static final int BUTTON_HEIGHT = 50;

  private void setImgSize(ImageView iv) {
    iv.setFitWidth(IMAGE_WIDTH);
    iv.setFitHeight(IMAGE_HEIGHT);
  }

  public Button buttonFromImage(String path, EventHandler<ActionEvent> evt) {
    ImageView buttonImg = new ImageView(
        new Image(SimulationUI.class.getResourceAsStream(path)));
    setImgSize(buttonImg);
    javafx.scene.control.Button button = new javafx.scene.control.Button(null, buttonImg);
    button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
    button.setOnAction(evt);
    return button;
  }

  private Button addIndividualButton(Group root, String path, int x, int y,
      EventHandler<ActionEvent> evt) {
    Button b = buttonFromImage(path, evt);
    b.setLayoutX(x);
    b.setLayoutY(y);
    root.getChildren().add(b);
    return b;
  }


}
