package cellsociety.Model;

import java.util.ArrayList;
/**
 * Purpose: overrides standard rectangular cell neighbors to support hexagonal cells
 *
 * Assumptions: "standard" Moore neighborhood for hexagonal cells are considered as the 6-surrounding cells
 *
 * Dependencies: extends from Neighborhood
 *
 * @author Jay Yoon
 */
public class HexNeighborhood extends Neighborhood {

  /**
   * Purpose: Override standard Moore neighborhood to be used in hexagonal cell shapes
   * <p>
   * Assumptions: Rectangular Moore neighborhood consists of eight cells, while hexagonal cells consists of six.
   * <p>
   * Parameters:
   * <p>
   * grid - a Grid object that represents the grid in which the cell is located. width - an integer
   * that represents the width of the grid. height - an integer that represents the height of the
   * grid. cell c - current cell to have their neighborhood set. Exceptions: None.
   * <p>
   * Return Value: None.
   */

  @Override
  public ArrayList<Cell> standardMoore(Grid grid, int width, int height, Cell c) {
    ArrayList<Cell> list = new ArrayList<>();
    int row = c.getRow();
    int col = c.getCol();
    int i = grid.getIndex(c);

    if (row > 0 && col > 0) {
      list.add(grid.getCellPosition(i - width - 1));
    }
    if (col < width - 1 && row > 0) {
      list.add(grid.getCellPosition(i - width + 1));
    }
    if (row < height - 1) {
      list.add(grid.getCellPosition(i + width));
    }
    if (col > 0) {
      list.add(grid.getCellPosition(i - 1));
    }
    if (col < width - 1) {
      list.add(grid.getCellPosition(i + 1));
    }
    return list;
  }
}
