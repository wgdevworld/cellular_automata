package cellsociety.Model;
/**
 * Purpose: to pass Cell Collection as an immutable object to be displayed
 *
 * Dependencies: interface implemented by Cell object, used in Grid class
 *
 * @author Jay Yoon
 */
public interface ImmutableCell {

  /**
   * gets size of cell
   * return value: double representing cell size
   */

  double getSize();

  /**
   * gets color of cell
   * return value: String representing cell color hex code
   */
  String getColor();

  /**
   * gets x-position of cell
   * return value: double representing x-position
   */
  double getXCoordinate();

  /**
   * gets y-position of cell
   * return value: double representing y-position
   */
  double getYCoordinate();

  /**
   * gets current state (cell type) of cell
   * return value: int representing cell type
   */
  int getType();
}
