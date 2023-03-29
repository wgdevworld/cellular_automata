package cellsociety.View;

import cellsociety.Model.Grid;
import cellsociety.Model.ImmutableCell;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Purpose:
 * <p> Override methods for drawing Rectangle cells
 * Assumptions:
 * <p> None
 * Dependencies:
 * <p> None
 * @author Woonggyu Jin
 */

public class RectangleCell extends PolygonCell {
  /**
   * Purpose:
   * <p> Draw rectangle cells.
   * Assumptions:
   * <p> The hexagons are regular hexagons.
   * Dependencies:
   * <p> None
   * @author Woonggyu Jin
   */
  @Override
  public ArrayList<Polygon> drawCells(Grid grid) {
    ArrayList<Polygon> cellList = new ArrayList<>();
    for (ImmutableCell c : grid.getCells()) {
      Polygon cell = new Polygon();
      cell.getPoints().addAll(
          c.getXCoordinate(), c.getYCoordinate(),
          c.getXCoordinate() + c.getSize(), c.getYCoordinate(),
          c.getXCoordinate() + c.getSize(), c.getYCoordinate() + c.getSize(),
          c.getXCoordinate(), c.getYCoordinate() + c.getSize()
      );
      cell.setFill(Color.web(c.getColor()));
      cellList.add(cell);
    }

    return cellList;
  }

}
