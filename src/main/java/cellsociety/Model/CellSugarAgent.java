package cellsociety.Model;

import java.util.ArrayList;
/**
 * Purpose: This class represents a cell in a simulation using the SugarScape rules.
 * <p>
 * Assumptions: Assumed sugar growback rate/interval, patch sugar limit, agent sugar initial value/metabolism/ and vision as default.
 * <p>
 * Dependencies: This class depends on the Cell class and the Neighborhood class.
 *
 * @author Ted Peterson
 */
public class CellSugarAgent extends Cell {
  private static final int AGENT_STATE = 1;
  private static final int SUGAR_STATE = 2;
  private final int SUGAR_GROWBACK_RATE = 2;
  private final int SUGAR_GROWBACK_INTERVAL = 3;
  private int mySugarLimit = 8;
  private int mySugarAmount;
  private int myAgentSugar = 10;
  private int myAgentMetabolism = 2;
  private int myAgentVision = 3;
  private int myCycle;

  public CellSugarAgent(int type, String color, int pos, int width) {
    super(type, color, pos, width);
    mySugarAmount = type;
  }

  /**
   * Purpose: Sets the neighbors of the cell based on sugarScape vision method.
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
    if (this.getNextType() != AGENT_STATE) {
      return;
    }

    int row = this.getRow();
    int col = this.getCol();
    int i = grid.getIndex(this);

    for (int j = 1; j <= myAgentVision; j++) {
      if (row > j - 1) {
        this.neighbors.add(0, grid.getCellPosition(i - width * j));
      }
      if (col > j - 1) {
        this.neighbors.add(0, grid.getCellPosition(i - j));
      }
      if (row < (height - j)) {
        this.neighbors.add(0, grid.getCellPosition(i + width * j));
      }
      if (col < (width - j)) {
        this.neighbors.add(0, grid.getCellPosition(i + j));
      }
    }
  }

  /**
   * Purpose: Updates the state of the cell according to the rules of the SugarScape simulation.
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
  public void apply(Grid grid, int width, int height) {
    if (myCycle == SUGAR_GROWBACK_INTERVAL) {
      mySugarAmount += SUGAR_GROWBACK_RATE;
      if (mySugarAmount > mySugarLimit) {
        mySugarAmount = mySugarLimit;
      }
      myCycle = 0;
    }

    if (this.getType() == AGENT_STATE) {
      if (this.myAgentSugar <= 0) {
        this.setNextState(SUGAR_STATE);
        return;
      }
      int maxPatchIndex = 0;
      int maxPatchAmount = Integer.MIN_VALUE;
      for (int i = 0; i < neighbors.size(); i++) {
        CellSugarAgent c = (CellSugarAgent) neighbors.get(i);
        if (c.getSugar() >= maxPatchAmount && c.getType() != AGENT_STATE) {
          maxPatchIndex = i;
          maxPatchAmount = c.getSugar();
        }
      }
      if (neighbors.isEmpty()) {
        return;
      }
      CellSugarAgent maxPatch = (CellSugarAgent) neighbors.get(maxPatchIndex);
      maxPatch.setNextState(AGENT_STATE);
      maxPatch.myAgentVision = this.myAgentVision;
      maxPatch.myAgentSugar = this.myAgentSugar + maxPatch.mySugarAmount - myAgentMetabolism;
      maxPatch.myAgentMetabolism = this.myAgentMetabolism;

      this.setNextState(SUGAR_STATE);
      maxPatch.setNeighbors(grid, width, height);
      this.setNeighbors(grid, width, height);
    }
    myCycle++;

  }

  public int getSugar() {
    return this.mySugarAmount;
  }
}
