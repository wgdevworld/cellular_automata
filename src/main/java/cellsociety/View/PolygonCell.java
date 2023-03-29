package cellsociety.View;

import cellsociety.Model.Grid;
import java.util.ArrayList;
import javafx.scene.shape.Polygon;

/**
 * Purpose:
 * <p> Abstraction to accommodate multiple types of cell shapes
 * Assumptions:
 * <p> None
 * Dependencies:
 * <p> None
 * @author Woonggyu Jin
 */

public abstract class PolygonCell extends Polygon {

  public abstract ArrayList<Polygon> drawCells(Grid grid);
}
