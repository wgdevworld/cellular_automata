package cellsociety.Model;

import java.util.ArrayList;

/**
 * Purpose: This Java class provides methods to calculate different types of neighborhoods for a
 * given cell in a grid. Assumptions: The grid is represented as a 1-dimensional array, and the
 * cells are represented as objects of the Cell class. The standard Moore and standard Neumann
 * neighborhoods are calculated using the same approach, which is based on the indices of the
 * neighboring cells in the array. The foraging ants and percolation neighborhoods are specialized
 * types of neighborhoods that are not used in all simulations. Dependencies: This class depends on
 * the Grid and Cell classes, which are not shown here.
 *
 * @author Jay Yoon
 */
public class Neighborhood {

  private ArrayList<Cell> full = new ArrayList<>();

  /**
   * Purpose: This method is used to get the standard neighborhood for von Neumann's neighborhood.
   * Assumptions: Assumes that the input grid is not null, and the width and height values are
   * greater than 0. Parameters: Grid grid - the grid to get the neighborhood from, int width - the
   * width of the grid, int height - the height of the grid, Cell c - the cell to get the
   * neighborhood for. Exceptions: N/A Return Value: An ArrayList of Cells representing the
   * neighborhood of the given cell c.
   *
   * @param grid
   * @param width
   * @param height
   * @param c
   * @return
   */
  public ArrayList<Cell> standardNeuMann(Grid grid, int width, int height, Cell c) {
    ArrayList<Cell> list = new ArrayList<>();
    int row = c.getRow();
    int col = c.getCol();
    int i = grid.getIndex(c);
    this.addNorth(grid, list, row, width, i);
    this.addEast(grid, list, col, width, i);
    this.addSouth(grid, list, row, height, width, i);
    this.addWest(grid, list, col, i);
    return list;
  }

  /**
   * Purpose: This method is used to get the standard neighborhood for Moore's neighborhood.
   * Assumptions: Assumes that the input grid is not null, and the width and height values are
   * greater than 0. Parameters: Grid grid - the grid to get the neighborhood from, int width - the
   * width of the grid, int height - the height of the grid, Cell c - the cell to get the
   * neighborhood for. Exceptions: N/A Return Value: An ArrayList of Cells representing the
   * neighborhood of the given cell c.
   *
   * @param grid
   * @param width
   * @param height
   * @param c
   * @return
   */
  public ArrayList<Cell> standardMoore(Grid grid, int width, int height, Cell c) {
    ArrayList<Cell> list = this.standardNeuMann(grid, width, height, c);
    int i = grid.getIndex(c);
    int row = c.getRow();
    int col = c.getCol();
    this.addNE(grid, list, width, row, col, i);
    this.addSE(grid, list, row, col, width, height, i);
    this.addSW(grid, list, row, height, col, width, i);
    this.addNW(grid, list, row, col, width, i);
    return list;
  }

  /**
   * Purpose: This method is used to get the neighborhood for percolation. Assumptions: Assumes that
   * the input grid is not null, and the width and height values are greater than 0. Parameters:
   * Grid grid - the grid to get the neighborhood from, int width - the width of the grid, int
   * height - the height of the grid, Cell c - the cell to get the neighborhood for. Exceptions: N/A
   * Return Value: An ArrayList of Cells representing the neighborhood of the given cell c.
   *
   * @param grid
   * @param width
   * @param c
   * @return
   */
  public ArrayList<Cell> percolationNeighbors(Grid grid, int width, Cell c) {
    ArrayList<Cell> list = new ArrayList<>();
    int row = c.getRow();
    int col = c.getCol();
    int i = grid.getIndex(c);

    this.addNorth(grid, list, row, width, i);
    this.addWest(grid, list, col, i);
    this.addEast(grid, list, col, width, i);
    return list;
  }

  /**
   * Purpose: This method is used to get the neighborhood for foraging ants. Assumptions: Assumes
   * that the input grid is not null, and the width and height values are greater than 0.
   * Parameters: Grid grid - the grid to get the neighborhood from, int width - the width of the
   * grid, int height - the height of the grid, Cell c - the cell to get the neighborhood for.
   * Exceptions: N/A Return Value: An ArrayList of Cells representing the neighborhood of the given
   * cell c.
   *
   * @param grid
   * @param width
   * @param height
   * @param c
   * @return
   */
  public ArrayList<Cell> foragingAntsNeighbors(Grid grid, int width, int height, Cell c) {
    standardMoore(grid, width, height, c);
    return this.full;
  }

  private void addNorth(Grid grid, ArrayList<Cell> list, int row, int width, int i) {
    if (row > 0) {
      list.add(grid.getCellPosition(i - width));
      full.add(grid.getCellPosition(i - width));
    } else {
      full.add(null);
    }
  }

  private void addEast(Grid grid, ArrayList<Cell> list, int col, int width, int i) {
    if (col < width - 1) {
      list.add(grid.getCellPosition(i + 1));
      full.add(grid.getCellPosition(i + 1));
    } else {
      full.add(null);
    }
  }

  private void addSouth(Grid grid, ArrayList<Cell> list, int row, int height, int width, int i) {
    if (row < height - 1) {
      list.add(grid.getCellPosition(i + width));
      full.add(grid.getCellPosition(i + width));
    } else {
      full.add(null);
    }
  }

  private void addWest(Grid grid, ArrayList<Cell> list, int col, int i) {
    if (col > 0) {
      list.add(grid.getCellPosition(i - 1));
      full.add(grid.getCellPosition(i - 1));
    } else {
      full.add(null);
    }
  }

  private void addNE(Grid grid, ArrayList<Cell> list, int width, int row, int col, int i) {
    if (col < width - 1 && row > 0) {
      list.add(grid.getCellPosition(i - width + 1));
      full.add(grid.getCellPosition(i - width + 1));
    } else {
      full.add(null);
    }
  }

  private void addSE(Grid grid, ArrayList<Cell> list, int row, int col, int width, int height, int i) {
    if (row < height - 1 && col < width - 1) {
      list.add(grid.getCellPosition(i + width + 1));
      full.add(grid.getCellPosition(i + width + 1));
    } else {
      full.add(null);
    }
  }

  private void addSW(Grid grid, ArrayList<Cell> list, int row, int height, int col, int width, int i) {
    if (row < height - 1 && col > 0) {
      list.add(grid.getCellPosition(i + width - 1));
      full.add(grid.getCellPosition(i + width - 1));
    } else {
      full.add(null);
    }
  }

  private void addNW(Grid grid, ArrayList<Cell> list, int row, int col, int width, int i) {
    if (row > 0 && col > 0) {
      list.add(grid.getCellPosition(i - width - 1));
      full.add(grid.getCellPosition(i - width - 1));
    } else {
      full.add(null);
    }
  }

}
