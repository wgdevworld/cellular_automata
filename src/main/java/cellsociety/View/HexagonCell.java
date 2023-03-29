package cellsociety.View;

import cellsociety.Model.Grid;
import cellsociety.Model.ImmutableCell;
import java.util.ArrayList;
import javafx.scene.shape.Polygon;

/**
 * Purpose:
 * <p> Override methods for drawing Hexagon cells
 * Assumptions:
 * <p> The hexagons are regular hexagons.
 * Dependencies:
 * <p> None
 * @author Woonggyu Jin
 */

public class HexagonCell extends PolygonCell{

  /**
   * Purpose:
   * <p> Draw hexagon cells.
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

      double angle = 60;
      double sideLength =  c.getSize() / (1 + 2 * Math.cos(Math.toRadians(angle)));
      double smallIncrement = sideLength * Math.cos(Math.toRadians(angle));
      double bigIncrement = sideLength * Math.sin(Math.toRadians(angle));
      cell.getPoints().addAll(
          c.getXCoordinate(), c.getYCoordinate(),
          c.getXCoordinate() + sideLength, c.getYCoordinate(),
          c.getXCoordinate() + sideLength + smallIncrement,
          c.getYCoordinate() + bigIncrement,
          c.getXCoordinate() + sideLength,
          c.getYCoordinate() + 2 * bigIncrement,
          c.getXCoordinate(),
          c.getYCoordinate() + 2 * bigIncrement,
          c.getXCoordinate() - smallIncrement,
          c.getYCoordinate() + bigIncrement
          );
      cellList.add(cell);
    }
    return cellList;
  }
}
