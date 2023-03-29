package cellsociety.Model;

import cellsociety.Controller.CellShape;
import cellsociety.View.SimulationUI;

import java.util.ArrayList;
/**
 * Purpose: abstract class to be able to support types of cells with differing rules and neighbors.
 * Every step, cell states are updated by simulation controller.
 *
 * Assumptions: every cell extending from abstract cell will set their own neighbors and define own set of rules.
 * Cell size is automatically scaled with window with default factor of 2.5. Cell gaps are set to 2 as default.
 *
 * Dependencies: No major dependencies.
 *
 * @author Jay Yoon, Ted Peterson
 */
public abstract class Cell implements ImmutableCell {

  private double SIZE_FACTOR = 2.5;
  private int gap = 2;
  private double size;
  private int type;
  private int nextType;
  private String color;
  private int position;
  private double xCoordinate;
  private double yCoordinate;
  private int row;
  private int col;
  protected ArrayList<Cell> neighbors;

  public Cell(int type, String color, int pos, int width) {
    super();

    this.type = type;
    this.nextType = this.type;
    this.position = pos;
    this.color = color;
    this.row = pos / width;
    this.col = pos % width;
    this.size = SimulationUI.SCREEN_WIDTH / SIZE_FACTOR / width;

    this.setXPosition();
    this.setYPosition();
  }

  /**
   * calculates and updates x-position of cell
   */
  public void setXPosition() {
    this.xCoordinate = (gap + this.size) * col;
  }

  /**
   * calculates and updates y-position of cell
   */
  public void setYPosition() {
    this.yCoordinate = (gap + this.size) * row;
  }

  /**
   * updates next state of cell type
   * parameter: int value representing new state
   */
  public void setNextState(int newState) {
    this.nextType = newState;
  }

  /**
   * updates current state to next state
   */
  public void transitionState() {
    this.type = this.nextType;
  }

  /**
   * updates hex color code to stored color
   */
  public void transitionColor(String color) {
    this.color = color;
  }

  /**
   * gets color hex code of cell
   * return value: String value representing cell's current color hex code
   */
  public String getColor() {
    return this.color;
  }

  /**
   * gets row number of cell
   * return value: int value representing its row position on grid
   */
  public int getRow() {
    return this.row;
  }

  /**
   * gets column number of cell
   * return value: int value representing its column position on grid
   */
  public int getCol() {
    return this.col;
  }

  /**
   * gets current type of cell
   * return value: int value representing its current cell state
   */
  public int getType() {
    return this.type;
  }

  /**
   * gets type of cell in next step
   * return value: int value representing cell's next cell type
   */
  public int getNextType() {
    return this.nextType;
  }

  /**
   * gets index position of cell in Grid (ArrayList of Cells)
   * return value: int value representing index position
   */
  public int getPosition() {
    return this.position;
  }

  /**
   * gets x-position of cell to be displayed on window
   * return value: double value representing x-position
   */
  public double getXCoordinate() {
    return this.xCoordinate;
  }

  /**
   * gets y-position of cell to be displayed on window
   * return value: double value representing y-position
   */
  public double getYCoordinate() {
    return this.yCoordinate;
  }

  /**
   * updates y-position of cell to be displayed on window
   * parameter: double value representing y-position
   */
  public void setYCoordinate(double y) {
    this.yCoordinate = y;
  }

  /**
   * updates cell gap width, or border between cells on grid to be displayed
   * parameter: int value representing gap width between cells (border width)
   */
  public void setGap(int gap) {
    this.gap = gap;
  }

  /**
   * gets current gap between cells
   * return value: int value representing gap width between cells
   */
  public int getGap() {
    return this.gap;
  }

  /**
   * abstract method to be overriden by different Cells. Different simulations have cells that interact differently.
   * This allows the Cell to have their own set of rules.
   * parameter: current Grid, width and height of simulation
   */
  public abstract void apply(Grid grid, int width, int height);

  /**
   * abstract method to be overriden by different Cells. In order to apply their own set of rules, Cells define their neighbors differently.
   * most Cells will use the standard Moore or vonNeumann neighborhood, but can define their own.
   * parameter: current Grid, width and height of simulation
   */
  public abstract void setNeighbors(Grid grid, int width, int height);

  /**
   * gets size of cell
   * return value: double value representing size of cell
   */
  public double getSize() {
    return this.size;
  }

  /**
   * sets size of cell
   * parameter: double value representing size of cell
   */
  public void setSize(double size) {
    this.size = size;
  }

  /**
   * overriden toString
   * return value: String that represents current Cell object
   */
  @Override
  public String toString() {
    return Integer.toString(this.position);
  }
}
