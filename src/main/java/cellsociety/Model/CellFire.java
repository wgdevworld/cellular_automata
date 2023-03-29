package cellsociety.Model;

import java.util.SplittableRandom;

/**
 * Purpose: This class represents a cell in the fire simulation. It contains methods to calculate
 * the next state of the cell based on the current state and its neighbors. Assumptions: Assumes
 * that the Cell class and Neighborhood class are implemented and can be used in this class.
 * Dependencies: Cell, Neighborhood
 *
 * @author Jay Yoon,Ted Peterson
 */
public class CellFire extends Cell {
  private static final int EMPTY_STATE = 0;
  private static final int WOOD_STATE = 1;
  private static final int BURNING_STATE = 2;

  private double probCatch;

  /**
   * Purpose: Constructor method to create a new instance of CellFire object Assumptions: None
   * Parameters:
   * <p>
   * type: an integer representing the type of the cell color: a string representing the color of
   * the cell pos: an integer representing the position of the cell in the grid width: an integer
   * representing the width of the grid prob: a double representing the probability of the cell
   * catching fire Exceptions: None Return value: None
   */
  public CellFire(int type, String color, int pos, int width, double prob) {
    super(type, color, pos, width);
    this.probCatch = prob;
  }

  /**
   * Purpose: Method to update the state of the cell based on its neighbors Assumptions: The grid,
   * w, and h parameters are not null Parameters:
   * <p>
   * grid: a Grid object representing the current grid of cells w: an integer representing the width
   * of the grid h: an integer representing the height of the grid Exceptions: None Return value:
   * None
   */
  @Override
  public void apply(Grid grid, int w, int h) {
    SplittableRandom random = new SplittableRandom();
    int p = random.nextInt(100);
    int cellCount = 0;
    for (Cell c : neighbors) {
      if (c.getType() == BURNING_STATE) {
        cellCount++;
      }
    }
    if (this.getType() == WOOD_STATE && cellCount > 0) {
      if (p < probCatch) {
        this.setNextState(BURNING_STATE);
      } else {
        this.setNextState(WOOD_STATE);
      }
    }
    if (this.getType() == BURNING_STATE) {
      this.setNextState(EMPTY_STATE);
    }
  }

  /**
   * Purpose: Method to set the neighbors of the cell based on standardNeuMann method Assumptions:
   * The grid, width, and height parameters are not null Parameters:
   * <p>
   * grid: a Grid object representing the current grid of cells width: an integer representing the
   * width of the grid height: an integer representing the height of the grid Exceptions: None
   * Return value: None
   */
  @Override
  public void setNeighbors(Grid grid, int width, int height) {
    Neighborhood n = new Neighborhood();
    neighbors = n.standardNeuMann(grid, width, height, this);
  }
}
