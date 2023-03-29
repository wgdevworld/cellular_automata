package cellsociety.Model;

import java.util.ArrayList;
/**
 * Purpose: This class represents a cell in a simulation using the Falling Sand and Water rules.
 * <p>
 * Assumptions: Water decides its flow direction using random variable
 * <p>
 * Dependencies: This class depends on the Cell class and the Neighborhood class.
 *
 * @author Jay Yoon
 */
public class CellSand extends Cell {
  private static final int EMPTY_STATE = 0;
  private static final int METAL_STATE = 1;
  private static final int SAND_STATE = 2;
  private static final int WATER_STATE = 3;

  public CellSand(int type, String color, int pos, int width) {
    super(type, color, pos, width);
  }

  /**
   * Purpose: Sets the neighbors of the cell based on its own rules.
   * Different from other cell simulations in that current cell type is needed to determine neighbors.
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
    neighbors = new ArrayList<>();
    int row = this.getRow();
    int col = this.getCol();
    int i = grid.getIndex(this);

    if (this.getNextType() == SAND_STATE) {
      if (row < height - 1) {
        this.neighbors.add(grid.getCellPosition(i + width));
      }
    }
    if (this.getNextType() == WATER_STATE) {
      if (row < height - 1) {
        this.neighbors.add(grid.getCellPosition(i + width));
      }
      if (col > 0) {
        this.neighbors.add(grid.getCellPosition(i - 1));
      }
      if (col < width - 1) {
        this.neighbors.add(grid.getCellPosition(i + 1));
      }
    }
  }

  /**
   * Purpose: Updates the state of the cell according to the rules of the Falling Sand and Water simulation.
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
    if (this.neighbors.isEmpty()) {
      return;
    }
    if (this.getType() == SAND_STATE) {
      Cell down = this.neighbors.get(0);
      if (down.getType() == EMPTY_STATE || down.getType() == SAND_STATE) {
        down.setNextState(SAND_STATE);
        down.setNeighbors(grid, w, h);
      }
      if (down.getType() == WATER_STATE) {
        down.setNextState(SAND_STATE);
        this.setNextState(WATER_STATE);
        down.setNeighbors(grid, w, h);
        this.setNeighbors(grid, w, h);
      }
    }
    if (this.getType() == WATER_STATE) {
      int index = (int) (Math.random() * this.neighbors.size());
      Cell random = this.neighbors.get(index);
      if (random.getType() == EMPTY_STATE || random.getType() == WATER_STATE) {
        random.setNextState(WATER_STATE);
        random.setNeighbors(grid, w, h);
      }
    }
  }
}
