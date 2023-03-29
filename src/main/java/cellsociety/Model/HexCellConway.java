package cellsociety.Model;

import cellsociety.Controller.CellShape;

/**
 * Purpose: implements Hexagonal Cell version of Conway Game of Life simulation.
 * It overrides rectangular Moore 8-neighborhood to hexagonal Moore standard 6-neighborhood
 * It overrides setting X and Y coordinates to be placed on grid using offset
 *
 * Assumptions: hexagonal version of Conway applies the same set of rules
 *
 * Dependencies: Extends standard CellConway and Cell abstract class
 *
 * @author Jay Yoon
 */
public class HexCellConway extends CellConway {

  private static final double Y_OFFSET_FACTOR = 0.5;

  public HexCellConway(int type, String color, int pos, int width) {
    super(type, color, pos, width);
  }

  /**
   * Purpose: Sets the neighbors of the cell based on hexagonal version of standard Moore.
   * <p>
   * Assumptions: It assumes neighborhood cells of six.
   * <p>
   * Parameters:
   * <p>
   * grid - a Grid object that represents the grid in which the cell is located. width - an integer
   * that represents the width of the grid. height - an integer that represents the height of the
   * grid. Exceptions: None.
   * <p>
   * Return Value: None.
   */

  @Override
  public void setNeighbors(Grid grid, int width, int height) {
    Neighborhood n = new HexNeighborhood();
    neighbors = n.standardMoore(grid, width, height, this);
  }

  /**
   * sets y-position of hexagonal cell to reflect offset on grid display
   */
  @Override
  public void setYPosition() {
    double offset = this.getSize() * Y_OFFSET_FACTOR;
    double position = (this.getGap() + this.getSize()) * this.getRow();
    if (this.getCol() % 2 == 1) {
      setYCoordinate(position + offset);
    } else {
      setYCoordinate(position);
    }
  }
}
