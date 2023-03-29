package cellsociety.Model;

/**
 * /** Purpose: This class represents a cell in a simulation using the Conway's Game of Life rules.
 * <p>
 * Assumptions: This class assumes that the neighbors of a cell have already been set using the
 * setNeighbors() method.
 * <p>
 * Dependencies: This class depends on the Cell class and the Neighborhood class.
 *
 * @author Jay Yoon,Ted Peterson
 */
public class CellConway extends Cell {
  private static final int DEAD_STATE = 0;
  private static final int ALIVE_STATE = 1;

  public CellConway(int type, String color, int pos, int width) {
    super(type, color, pos, width);
  }

  /**
   * Purpose: Updates the state of the cell according to the rules of the Conway simulation.
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
   */
  @Override
  public void apply(Grid grid, int w, int h) {
    int aliveCellCount = 0;
    for (Cell c : neighbors) {
      if (c.getType() == 1) {
        aliveCellCount++;
      }
    }
    if (aliveCellCount < 2) {
      this.setNextState(DEAD_STATE);
    }
    if (aliveCellCount == 2) {
      this.setNextState(this.getType());
    }
    if (aliveCellCount == 3) {
      this.setNextState(ALIVE_STATE);
    }
    if (aliveCellCount > 3) {
      this.setNextState(DEAD_STATE);
    }
  }

  /**
   * Purpose: Sets the neighbors of the cell based on standardMore method.
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
   */
  @Override
  public void setNeighbors(Grid grid, int width, int height) {
    Neighborhood n = new Neighborhood();
    neighbors = n.standardMoore(grid, width, height, this);
  }

}
