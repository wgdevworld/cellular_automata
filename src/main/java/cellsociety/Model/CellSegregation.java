package cellsociety.Model;

import java.util.ArrayList;

/**
 * Purpose: This class represents a cell in the Segregation simulation. Assumptions: Assumes that
 * the Cell class has been implemented and that the Grid class exists. Dependencies: Cell class,
 * Grid class, Neighborhood class.
 *
 * @author Jay Yoon,Ted Peterson
 */
public class CellSegregation extends Cell {
  private static final int EMPTY_STATE = 0;
  private ArrayList<Cell> neighbors;
  private ArrayList<Cell> emptyCells;
  private double threshold;

  /**
   * Purpose: Constructor for the CellSegregation object, sets the object's type, color, position,
   * width, and segregation threshold. Assumptions: None. Parameters: type (int): The type of cell.
   * color (String): The color of the cell. pos (int): The position of the cell. width (int): The
   * width of the cell. threshold (double): The segregation threshold of the cell. Exceptions: None.
   * Return Value: None.
   */
  public CellSegregation(int type, String color, int pos, int width, double threshold) {
    super(type, color, pos, width);
    this.threshold = threshold;
  }

  private void relocate() {
    if (!emptyCells.isEmpty()) {
      int index = (int) (Math.random() * emptyCells.size());
      Cell target = emptyCells.get(index);
      target.setNextState(this.getType());
      this.setNextState(0);
    }
  }

  /**
   * Purpose: Applies the segregation rule to the cell. Assumptions: The grid has been initialized
   * and the cell's neighbors have been set. Parameters: grid (Grid): The grid containing the cell
   * and its neighbors. width (int): The width of the grid. height (int): The height of the grid.
   * Exceptions: None. Return Value: None.
   *
   */
  @Override
  public void apply(Grid grid, int width, int height) {
    if (this.getType() == EMPTY_STATE) {
      return;
    }
    emptyCells = (ArrayList<Cell>) grid.getEmptyCells();
    int sameCellCount = 0;
    int total = 0;
    for (Cell c : neighbors) {
      if (c.getType() != EMPTY_STATE) {
        total++;
      }
      if (c.getType() == this.getType()) {
        sameCellCount++;
      }
    }
    double percentage = (double) sameCellCount / total * 100;
    if (percentage < threshold) {
      this.relocate();
    }
  }

  /**
   * Purpose: Sets the neighbors of the cell based on standardMoore method. Assumptions: The grid
   * has been initialized. Parameters: grid (Grid): The grid containing the cell and its neighbors.
   * width (int): The width of the grid. height (int): The height of the grid. Exceptions: None.
   * Return Value: None.
   *
   */
  @Override
  public void setNeighbors(Grid grid, int width, int height) {
    Neighborhood n = new Neighborhood();
    neighbors = n.standardMoore(grid, width, height, this);
  }
}