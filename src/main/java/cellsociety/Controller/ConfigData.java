package cellsociety.Controller;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Purpose: data record to be returned by XMLController - holds configuration data about initial
 * cell layout, id, parameters, dimension, colors
 * <p>
 * Assumptions: provided color data are valid hex color codes
 * <p>
 * Dependencies: No main dependencies
 *
 * @author Jay Yoon
 */
public record ConfigData(
    HashMap<Integer, String> cellColors,
    ArrayList<Integer> cellLayout, int id,
    int width, int height, int prob, CellShape shape) {

}
