package cellsociety.Model;
/**
 * Purpose: Represents the cell for Langton's Ant model and has methods to determine if the cell is a junction, determine loop positions, apply rules, and rotate loops.
 *
 * Assumptions: Assumes that the Langton's Ant model is a valid cellular automaton.
 *
 * Dependencies: java.util.ArrayList, cellsociety.Model.Cell, cellsociety.Model.Grid, cellsociety.Model.Neighborhood.
 *
 * @author Ted Peterson
 */
import java.util.ArrayList;

public class CellLangton extends Cell {

  private final int INNER_LOOP_SIDE_LENGTH = 5;
  private final int SPACE_BETWEEN_LOOPS = 1;
  private static ArrayList<CellLangton> cellLangtons = new ArrayList<>();
  private ArrayList<Integer> loopPositions = new ArrayList<>();
  private ArrayList<Integer> loopTypeSequences = new ArrayList<>();
  private int branchEndPosition;
  private ArrayList<Integer> replicantPositions = new ArrayList<>();
  private ArrayList<Integer> replicantLoopTypeSequences = new ArrayList<>();
  private int test = 1;
  private boolean checkJunction = true;
  private boolean isJunction = false;
  private String junctionCornerPosition;
  private int tJunctionPosition;
  private int widthOfGrid;
  private int branchIter;
  private int emptyCell=0;
  private int geneBarrierCell=1;
  private int gene1Cell=2;
  private int gene2Cell=3;
  private int gene4Cell=4;



  private int junctionCellType;

  /**
   * Comment: This is a public method that is used to apply the rules of the Langton’s ant to the cell.
   * The method takes in a Grid object, the width and height of the grid. It returns nothing.
   * \It checks if the cell is a junction and then applies the rules to the cell.
   * @param type
   * @param color
   * @param pos
   * @param width
   */
  public CellLangton(int type, String color, int pos, int width) {
    super(type, color, pos, width);
    this.widthOfGrid = width;
    cellLangtons.add(this);
  }

  @Override
  public void apply(Grid grid, int width, int height) {
    if (checkJunction && this.getType() != emptyCell && this.getType() != geneBarrierCell) {
      checkJunction = false;
      determineIf_TJunction();

    }
    if (isJunction && test < 19) {
      loopGeneRotation();
      applyRule();
    }
  }

  /**
   * This is a public method used to set the neighbors of the cell. T
   * he method takes in a Grid object, the width and height of the grid.
   * It returns nothing.
   * It creates a Neighborhood object and sets the neighbors of the cell using the von Neumann neighborhood.
   * @param grid
   * @param width
   * @param height
   */
  @Override
  public void setNeighbors(Grid grid, int width, int height) {
    Neighborhood n = new Neighborhood();
    neighbors = n.standardNeuMann(grid, width, height, this);
  }

  private void determineIf_TJunction() {
    int numLoopSheath = 0;
    for (Cell x : this.neighbors) {
      if (x.getType() == 1) {
        numLoopSheath++;
      }
    }
    if (numLoopSheath == 1) {
      this.isJunction = true;
      tJunctionPosition = this.getPosition();
      junctionCellType = cellLangtons.get(tJunctionPosition).getType();
      determineLoopPositions();
      branchEndPosition();


    }
  }

  private void determineLoopPositions() {
    whatCorner();
    int currentPos = tJunctionPosition;
    String direction = deterimineStartingDirection();
    for (int i = 0; i < 4; i++) {
      for (int x = 0; x < INNER_LOOP_SIDE_LENGTH - 1; x++) {
        int nextPos = nextCounterClockWisePosistionInLoop(direction, currentPos);
        loopPositions.add(nextPos);
        loopTypeSequences.add(cellLangtons.get(nextPos).getType());
        currentPos = nextPos;
      }
      direction = leftTurnDirectionChanger(direction);
    }

  }

  private void whatCorner() {
    if (tJunctionPosition + widthOfGrid * INNER_LOOP_SIDE_LENGTH < cellLangtons.size() - 1) {
      if (cellLangtons.get(tJunctionPosition + INNER_LOOP_SIDE_LENGTH).getType() == 1
          && cellLangtons.get(tJunctionPosition + widthOfGrid * INNER_LOOP_SIDE_LENGTH).getType()
          == geneBarrierCell) {
        junctionCornerPosition = "TopLeft";
      } else if (cellLangtons.get(tJunctionPosition - INNER_LOOP_SIDE_LENGTH).getType() == 1
          && cellLangtons.get(tJunctionPosition + widthOfGrid * INNER_LOOP_SIDE_LENGTH).getType()
          == geneBarrierCell) {
        junctionCornerPosition = "TopRight";
      }
    }
    if (tJunctionPosition - widthOfGrid * INNER_LOOP_SIDE_LENGTH > 0) {
      if (cellLangtons.get(tJunctionPosition + INNER_LOOP_SIDE_LENGTH).getType() == 1
          && cellLangtons.get(tJunctionPosition - widthOfGrid * INNER_LOOP_SIDE_LENGTH).getType()
          == geneBarrierCell) {
        junctionCornerPosition = "BottomLeft";
      } else if (cellLangtons.get(tJunctionPosition - INNER_LOOP_SIDE_LENGTH).getType() == 1
          && cellLangtons.get(tJunctionPosition - widthOfGrid * INNER_LOOP_SIDE_LENGTH).getType()
          == geneBarrierCell) {
        junctionCornerPosition = "BottomRight";
      }
    }

  }

  private String deterimineStartingDirection() {
    switch (junctionCornerPosition) {
      case "TopRight":
        return "East";
      case "TopLeft":
        return "South";
      case "BottomRight":
        return "North";
      case "BottomLeft":
        return "West";
    }
    return "Invalid Corner input";
  }

  private int nextCounterClockWisePosistionInLoop(String Direction, int currentPos) {
    switch (Direction) {
      case "North":
        return currentPos - widthOfGrid;
      case "South":
        return currentPos + widthOfGrid;
      case "East":
        return currentPos - 1;
      case "West":
        return currentPos + 1;
    }
    return (-1);
  }

  private String leftTurnDirectionChanger(String Direction) {
    switch (Direction) {
      case "North":
        return "East";
      case "South":
        return "West";
      case "East":
        return "South";
      case "West":
        return "North";
    }
    return ("Invalid leftTurnDirectionChanger input ");
  }

  private void loopGeneRotation() {
    ArrayList<Integer> newTypeSequence = new ArrayList();
    newTypeSequence.add(loopTypeSequences.get(loopTypeSequences.size() - 1));
    for (int i = 0; i < loopTypeSequences.size() - 1; i++) {
      newTypeSequence.add(loopTypeSequences.get(i));
    }
    for (int i = 0; i < loopTypeSequences.size(); i++) {
      cellLangtons.get(loopPositions.get(i)).setNextState(newTypeSequence.get(i));
    }
    loopTypeSequences = newTypeSequence;
  }

  private void applyRule() {
    if (test % 4 != 0 && test < 16) {
      ruleMoveForward();
    }
    if (test % 4 == 0 && test < 16) {
      ruleTurnBranch();
    }
    if (test == 16) {
      ruleSeperateLoops();
    } else {
      replicateGene();
    }
    test++;
  }

  private void ruleMoveForward() {
    branchEndPosition = branchEndPosition + branchIter;
    updateBranchStatesAfterRuleApplied();
  }

  private void updateBranchStatesAfterRuleApplied() {
    replicantPositions.add(branchEndPosition);
    replicantLoopTypeSequences.add(0);
    addEndCap();
  }

  private void addEndCap() {
    for (Cell x : cellLangtons.get(branchEndPosition).neighbors) {
      if (x.getType() == emptyCell) {
        x.setNextState(1);
      }
    }
  }

  private void ruleTurnBranch() {
    if (branchIter == 1) {
      branchEndPosition = branchEndPosition - widthOfGrid;
      branchIter = -widthOfGrid;
    } else if (branchIter == -1) {
      branchEndPosition = branchEndPosition + widthOfGrid;
      branchIter = widthOfGrid;
    } else if (branchIter == widthOfGrid) {
      branchEndPosition = branchEndPosition + 1;
      branchIter = 1;
    } else if (branchIter == -widthOfGrid) {
      branchEndPosition = branchEndPosition - 1;
      branchIter = -1;
    }
    updateBranchStatesAfterRuleApplied();
  }

  private void ruleSeperateLoops() {
    ruleMoveForward();
    replicateGene();
    cellLangtons.get(replicantPositions.get(0)).setNextState(1);
    for (int i = 1; i < replicantPositions.size() - (INNER_LOOP_SIDE_LENGTH * 4 - 1); i++) {
      cellLangtons.get(replicantPositions.get(i)).setNextState(0);
      cellLangtons.get(replicantPositions.get(i) + branchIter).setNextState(0);
      cellLangtons.get(replicantPositions.get(i) - branchIter).setNextState(0);
    }
    cellLangtons.get(
            replicantPositions.get(replicantPositions.size() - (INNER_LOOP_SIDE_LENGTH * 4 - 1)))
        .setNextState(1);
    this.isJunction = false;
    makeNewJunction();

  }

  private void makeNewJunction() {
    switch (junctionCornerPosition) {
      case "TopRight":
        nextJunctionInitiate(tJunctionPosition - INNER_LOOP_SIDE_LENGTH - 1 - branchIter);
        break;
      case "TopLeft":
        nextJunctionInitiate(
            tJunctionPosition + widthOfGrid * (INNER_LOOP_SIDE_LENGTH - 1) - branchIter);
        break;
      case "BottomRight":
        nextJunctionInitiate(
            tJunctionPosition - widthOfGrid * (INNER_LOOP_SIDE_LENGTH - 1) - branchIter);
        break;
      case "BottomLeft":
        nextJunctionInitiate(tJunctionPosition + INNER_LOOP_SIDE_LENGTH - 1 + branchIter);
        break;
    }
  }

  private void ruleCreateBridgePattern() {
    for (int i = 0; i < SPACE_BETWEEN_LOOPS + 3; i++) {
      ruleMoveForward();
      replicantLoopTypeSequences.set(replicantLoopTypeSequences.size() - 1, 4);
    }
  }

  public void nextJunctionInitiate(int branchStartPosition) {
    cellLangtons.get(branchStartPosition).setNextState(4);
    branchEndPosition = branchStartPosition;
    addEndCap();
    ruleCreateBridgePattern();

  }

  public void branchEndPosition() {
    switch (junctionCornerPosition) {
      case "TopRight":
        horizontalOrVerticalBranch(tJunctionPosition + 1, tJunctionPosition - widthOfGrid);
        break;
      case "TopLeft":
        horizontalOrVerticalBranch(tJunctionPosition - 1, tJunctionPosition - widthOfGrid);
        break;
      case "BottomRight":
        horizontalOrVerticalBranch(tJunctionPosition + 1, tJunctionPosition + widthOfGrid);
        break;
      case "BottomLeft":
        branchEndPosition = (findEndOfBranch());
        break;
    }
    branchEndPosition = (findEndOfBranch());
  }

  public String horizontalOrVerticalBranch(int horizontalIndex, int verticalIndex) {
    if (cellLangtons.get(horizontalIndex).getType() == geneBarrierCell) {
      branchIter = verticalIndex - tJunctionPosition;
      return "Vertical";

    } else {
      branchIter = horizontalIndex - tJunctionPosition;
      return "Horizontal";

    }
  }

  private int findEndOfBranch() {
    int i = branchIter;
    while (true) {
      if (cellLangtons.get(tJunctionPosition + i).getType() == geneBarrierCell) {
        return (tJunctionPosition + i - branchIter);
      } else {
        this.replicantPositions.add(tJunctionPosition + i);
        this.replicantLoopTypeSequences.add(cellLangtons.get(tJunctionPosition + i).getType());
      }
      i += branchIter;

    }

  }

  private void replicateGene() {
    junctionCellType = cellLangtons.get(tJunctionPosition).getType();
    replicantLoopTypeSequences.remove(replicantLoopTypeSequences.size() - 1);
    replicantLoopTypeSequences.add(0, junctionCellType);
    for (int i = 0; i < replicantPositions.size(); i++) {
      cellLangtons.get(replicantPositions.get(i)).setNextState(replicantLoopTypeSequences.get(i));
    }
  }

}



