package cellsociety.Controller;

/**
 * Purpose: Data record to be returned by XMLController - Holds basic information about simulation
 * description incl. author and description
 * <p>
 * Assumptions: Information can be recognized to be displayed on pop-up window (alphabetical)
 * <p>
 * Dependencies: No main dependencies
 *
 * @author Jay Yoon
 */
public record AboutData(int simulationId, String author, String desc) {

}
