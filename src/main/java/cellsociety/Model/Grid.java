package cellsociety.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Purpose: The purpose of this Java class is to define a Grid data structure and its associated
 * methods to manage a collection of cells in a simulation.
 * <p>
 * Assumptions: This class assumes that there is a collection of cells to be placed on the grid, and
 * that the grid will be updated according to the rules of a simulation.
 * <p>
 * Dependencies: This class depends on the Cell class to define the behavior of individual cells
 * within the grid, and it uses standard Java utility classes such as ArrayList and Map to manage
 * collections of cells and color mappings, respectively.
 *
 * @author Jay Yoon, Ted Peterson
 */
public class Grid {

  private int width;
  private int height;
  public ArrayList<Cell> cells;
  private ArrayList<Cell> emptyCells;
  private ArrayList<Cell> typeFilledCells;

  public Grid(int width, int height) {
    cells = new ArrayList<>();
    typeFilledCells = new ArrayList<>();
    this.width = width;
    this.height = height;
  }

  /**
   * Purpose: Adds a new cell to the grid. Assumptions: The cell being added is not already in the
   * grid. Parameters: c - a Cell object to be added to the grid. Exceptions: None Return value:
   * None
   *
   * @param c
   */
  public void place(Cell c) {
    cells.add(c);
  }

  /**
   * Purpose: Returns the index of the specified cell in the grid. Assumptions: The cell being
   * searched for is already in the grid. Parameters: c - a Cell object whose index is being
   * searched for. Exceptions: None Return value: The index of the specified cell.
   *
   * @param c
   * @return
   */
  public int getIndex(Cell c) {
    return cells.indexOf(c);
  }

  /**
   * Purpose: Returns the cell at the specified position in the grid. Assumptions: The position
   * specified is within the range of the grid's size. Parameters: pos - an integer representing the
   * position of the desired cell in the grid. Exceptions: IndexOutOfBoundsException if pos is out
   * of range. Return value: The Cell object at the specified position.
   *
   * @param pos
   * @return
   */
  public Cell getCellPosition(int pos) {
    return cells.get(pos);
  }

  /**
   * Purpose: Returns the number of cells in the grid. Assumptions: None Parameters: None
   * Exceptions: None Return value: The number of cells in the grid.
   *
   * @return
   */
  public int getSize() {
    return this.cells.size();
  }

  /**
   * Purpose: Returns a collection of all cells in the grid as immutable cells. Assumptions: None
   * Parameters: None Exceptions: None Return value: A Collection of ImmutableCell objects
   * representing all cells in the grid.
   */
  public Collection<ImmutableCell> getCells() {
    ArrayList<ImmutableCell> cell = new ArrayList<>();
    for (Cell c : cells) {
      cell.add(c);
    }
    return cell;
  }

  /**
   * Purpose: Returns a collection of all empty cells in the grid. Assumptions: None Parameters:
   * None Exceptions: None Return value: A Collection of Cell objects representing all empty cells
   * in the grid.
   *
   * @return
   */
  public Collection<Cell> getEmptyCells() {
    emptyCells = new ArrayList<>();
    for (Cell c : cells) {
      if (c.getNextType() == 0) {
        emptyCells.add(c);
      }
    }
    return this.emptyCells;
  }

  /**
   * Purpose: Returns a collection of all cells that have been marked as "type filled". Assumptions:
   * None Parameters: None Exceptions: None Return value: A Collection of Cell objects representing
   * all cells that have been marked as "type filled".
   */
  public Collection<Cell> getTypeFillCells() {
    return this.typeFilledCells;
  }

  /**
   * urpose: Applies the rules of the simulation to each cell in the grid. Assumptions: None
   * Parameters: None Exceptions: None Return value: None
   */
  public void applyRules() {
    for (Cell c : cells) {
      c.apply(this, width, height);
    }
  }

  /**
   * Purpose: Sets the gap value for each cell in the grid. Assumptions: None Parameters: value - an
   * integer representing the desired gap value. Exceptions: None Return value: None
   *
   * @param value
   */
  public void setGap(int value) {
    for (Cell c : cells) {
      c.setGap(value);
    }
  }

  /**
   * Purpose: Adds the specified cell to the collection of "type filled" cells. Assumptions: The
   * cell being added is not already in the collection. Parameters: c - a Cell object to be added to
   * the collection. Exceptions: None Return value: None
   *
   * @param c
   */
  public void updateTypeFillCells(Cell c) {
    typeFilledCells.add(c);
  }

  /**
   * Purpose: Removes the specified cell from the collection of "type filled" cells. Assumptions:
   * The cell being removed is in the collection. Parameters: c - a Cell object to be removed from
   * the collection. Exceptions: None Return value: None
   *
   * @param c
   */
  public void removeTypeFillCell(Cell c) {
    typeFilledCells.remove(c);
  }

  /**
   * Purpose: Initializes the neighbors for each cell in the grid. Assumptions: None Parameters:
   * None Exceptions: None Return value: None
   */
  public void initiateNeighbors() {
    for (Cell c : cells) {
      c.setNeighbors(this, width, height);
    }
  }

  /**
   * Purpose: Updates the state and color of each cell in the grid based on the provided color map.
   * Assumptions: None Parameters: colorMap - a Map object with keys representing the cell types and
   * values representing the corresponding colors. Exceptions: None Return: None
   *
   * @param colorMap
   */
  public void update(Map<Integer, String> colorMap) {
    for (Cell c : cells) {
      c.transitionState();
      c.transitionColor(colorMap.get(c.getType()));
    }
  }

  /**
   * Purpose: Return grid Width Assumptions: None Parameters: None Exceptions: None Return: None
   */
  public int getWidth() {
    return this.width;
  }
}
