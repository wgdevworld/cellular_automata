package cellsociety.Model;

/**
 * Purpose: This Java class, named CellPercolation, represents a single cell in a percolation
 * simulation. The purpose of this class is to define the behavior of a percolation cell, including
 * its state and how it interacts with neighboring cells.
 * <p>
 * Assumptions: It is assumed that the CellPercolation class is part of a larger simulation that
 * includes a Grid object to represent the collection of cells. It is also assumed that the
 * simulation has defined the types of cells that can exist in the grid, with type 1 representing a
 * blocked cell and type 2 representing a percolated cell.
 * <p>
 * Dependencies: The CellPercolation class depends on the Cell class, which is a superclass that
 * provides common functionality for all types of cells in the simulation. Additionally, the class
 * depends on the Neighborhood class, which is responsible for determining the neighbors of each
 * cell in the grid.
 *
 * @author Jay Yoon
 */
public class CellPercolation extends Cell {
  private static final int BLOCKED_STATE = 0;
  private static final int EMPTY_STATE = 1;
  private static final int FLOWING_STATE = 2;

  public CellPercolation(int type, String color, int pos, int width) {
    super(type, color, pos, width);
  }

  /**
   * Purpose: Updates the state of the cell according to the rules of the CellPercolation
   * simulation.
   * <p>
   * Assumptions: The cell's neighbors have been set.
   * <p>
   * Parameters:
   * <p>
   * grid - a Grid object that represents the grid in which the cell is located. width - an integer
   * that represents the width of the grid. height - an integer that represents the height of the
   * grid. Exceptions: None.
   * <p>
   * Return Value: None.
   **/
  @Override
  public void apply(Grid grid, int width, int height) {
    if (this.getRow() == 0 && this.getType() == EMPTY_STATE) {
      this.setNextState(FLOWING_STATE);
    }
    for (Cell c : neighbors) {
      if (c.getType() == FLOWING_STATE && this.getType() == EMPTY_STATE) {
        this.setNextState(FLOWING_STATE);
      }
    }
  }

  /**
   * Purpose: Sets the neighbors of the cell based on percolationNeighbors method.
   * <p>
   * Assumptions: None.
   * <p>
   * Parameters:
   * <p>
   * grid - a Grid object that represents the grid in which the cell is located. width - an integer
   * that represents the width of the grid. height - an integer that represents the height of the
   * grid. Exceptions: None.
   * <p>
   * Return Value: None.
   **/
  @Override
  public void setNeighbors(Grid grid, int width, int height) {
    Neighborhood n = new Neighborhood();
    neighbors = n.percolationNeighbors(grid, width,this);
  }
}
