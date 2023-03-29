package cellsociety.Model;

import java.util.ArrayList;

/**
 * Purpose: This class represents the simulation of the predator-prey interaction in a grid-based
 * environment. It models the behavior of fish and sharks as they move around, eat each other, and
 * breed.
 * Assumptions: The simulation assumes that the grid-based environment is large enough to
 * allow for interesting interactions between cells. It also assumes that there are only 3 cell
 * types: 0=empty cell 1=fish 2=shark.
 * Dependencies: This class depends on the Cell class, which
 * provides the basic properties and methods for a single cell in the grid. It also uses the Grid
 * and Neighborhood classes to obtain information about the surrounding cells and update the grid
 * accordingly.
 *
 * @author Ted Peterson
 */
public class CellPredation extends Cell {
  private static final int EMPTY_STATE = 0;
  private static final int FISH_STATE = 1;
  private static final int SHARK_STATE = 2;

  private static final int FISH_BREED_CYCLE = 5;
  private static final int SHARK_BREED_CYCLE = 5;
  private static final int SHARK_ENERGY = 3;
  private static final int FISH_ENERGY = 2;
  private int energy;
  private int myCycle;
  private ArrayList<Cell> foodFish;
  private ArrayList<Cell> emptyNeighborCells;

  public CellPredation(int type, String color, int pos, int width) {
    super(type, color, pos, width);
  }

  /**
   * Updates the state of the cell according to the rules of the predator-prey simulation. Assumes
   * that the grid and its contents have been initialized and that the cell has valid neighbors.
   *
   * @param grid   the Grid object representing the simulation environment
   * @param width  the width of the grid
   * @param height the height of the grid
   */
  @Override
  public void apply(Grid grid, int width, int height) {
    foodFish = (ArrayList<Cell>) grid.getTypeFillCells();
    findEmptyCellNieghbors();
    switch (this.getType()) {
      case EMPTY_STATE:
        return;
      case FISH_STATE:
        if (emptyNeighborCells.isEmpty()) {
          if (this.getType() == FISH_STATE) {
            grid.updateTypeFillCells(this);
          }
          this.myCycle++;
          break;
        } else if (foodFish.contains(this)) {
          grid.removeTypeFillCell(this);
        }
        handleFish();
        break;
      case SHARK_STATE:
        handleShark();
        break;
    }

  }

  private void findEmptyCellNieghbors() {
    emptyNeighborCells = new ArrayList<>();
    for (Cell c : neighbors) {
      if (c.getType() == EMPTY_STATE && c.getNextType() == EMPTY_STATE) {
        emptyNeighborCells.add(c);
      }
    }
  }

  private void handleFish() {
    CellPredation migratedFish = (CellPredation) emptyNeighborCells.get(
        (int) (Math.random() * emptyNeighborCells.size()));
    migratedFish.setNextState(FISH_STATE);
    migratedFish.myCycle = this.myCycle + 1;

    if (myCycle != FISH_BREED_CYCLE) {
      this.setNextState(EMPTY_STATE);
    }
    this.myCycle = 0;
  }

  private void handleShark() {
    ArrayList<Cell> fishNeighbors = new ArrayList<>();
    for (Cell c : neighbors) {
      if (c.getType() == FISH_STATE && foodFish.contains(c)) {
        fishNeighbors.add(c);
      }
    }
    if (!fishNeighbors.isEmpty()) {
      sharkMoveToEmptyCell(fishNeighbors);

    } else if (!emptyNeighborCells.isEmpty()) {
      sharkEatFish();
    } else {
      this.energy--;
    }
  }

  private void sharkEatFish() {
    CellPredation migratedShark = (CellPredation) emptyNeighborCells.get(
        (int) (Math.random() * emptyNeighborCells.size()));
    migratedShark.setNextState(SHARK_STATE);
    migratedShark.myCycle = this.myCycle + 1;
    migratedShark.energy--;
    this.energy = SHARK_ENERGY;
    if (myCycle != FISH_BREED_CYCLE) {
      this.setNextState(EMPTY_STATE);
    }
    this.myCycle = 0;
  }

  private void sharkMoveToEmptyCell(ArrayList<Cell> fishNeighbors) {
    CellPredation migratedShark = (CellPredation) fishNeighbors.get(
        (int) (Math.random() * fishNeighbors.size()));
    migratedShark.setNextState(SHARK_STATE);
    migratedShark.myCycle = this.myCycle + 1;
    migratedShark.energy = migratedShark.energy + FISH_ENERGY;
    this.energy = SHARK_ENERGY;
    if (myCycle != SHARK_BREED_CYCLE) {
      this.setNextState(EMPTY_STATE);
    }
    this.myCycle = 0;
  }

  /**
   * Sets the neighbors of the cell. Assumes that the grid has been initialized and that the cell's
   * position is valid.
   *
   * @param grid   the Grid object representing the simulation environment
   * @param width  the width of the grid
   * @param height the height of the grid
   */
  @Override
  public void setNeighbors(Grid grid, int width, int height) {
    Neighborhood n = new Neighborhood();
    neighbors = n.standardNeuMann(grid, width, height, this);
  }
}
