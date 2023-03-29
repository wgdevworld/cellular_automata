package cellsociety.Controller;

/**
 * Purpose: Enum type to restrict cell shape - Currently supports rectangular and hexagonal cell
 * shape (can be extended by defining new objects)
 * <p>
 * Assumptions: Standard cell shape is rectangular
 * <p>
 * Dependencies: No main dependencies
 *
 * @author Jay Yoon
 */
public enum CellShape {
  STANDARD_CELL {
    /**
     * represents standard cell object
     * return value: String that identifies standard cell shape
     */
    @Override
    public String toString() {
      return "standard";
    }
  }, HEX_CELL {
    /**
     * represents hexagonal cell object
     * return value: String that identifies hexagonal cell shape
     */
    @Override
    public String toString() {
      return "hex";
    }
  }
}
