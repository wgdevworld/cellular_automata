package cellsociety.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Purpose: The purpose of this class is to model the behavior of ant cells. The ants move on a grid
 * and search for food and the nest. The ants leave pheromones as they move so that they and other
 * ants can more easily find their way back to the nest or food. Assumptions: This code assumes that
 * the cells of the grid on which the ants are moving can have either food, nest, or path cells. The
 * cells can be in one of two states, either empty or occupied by an ant. Dependencies: This code
 * depends on the Cell class and the Neighborhood class. It also depends on the Grid class in which
 * this class is used.
 *
 * @author Ted Peterson
 */
public class CellAnt extends Cell {

  private ArrayList<Cell> neighbors;
  private static ArrayList<CellAnt> cellAnts = new ArrayList<>();
  private ArrayList<ArrayList<Object>> thisCellsAnts = new ArrayList<>();
  private int homePheromoneLevel = 0;
  private int foodPheromoneLevel = 0;
  private static final int foodPheromone_evaporationRate = 2;
  private static final int homePheromone_evaporationRate = 2;
  private static final int foodPheromoneMax = 10;
  private static final int homePheromoneMax = 10;
  private static final String[] Orientations = {"N", "W", "S", "E", "SE", "SW", "NW", "NE"};
  private static final int pheromoneSignificanceWhenChoosingLocationConstant = 1;
  private static final int maxAntsPerCell = 4;
  private static final double probabilityNestCellSpawnsAnt = 0.3;
  private static final int antLives = 100;
  private int numberOfAnts = 0;
  private int emptyCell = 0;
  private int foodCell = 1;
  private int nestCell = 2;
  private int barrierCell = 3;

  private ArrayList<Cell> possibleAntMoves;
  private ArrayList<Integer> antLocalPhermoneLevels;
  private Neighborhood n = new Neighborhood();
  //need to add visual representation of pheromones and ants;

  /*Purpose: Constructs a new instance of the CellAnt class with the specified type, color, position, and width.

  Assumptions: None.

  Parameters:

  type - an integer that represents the type of the cell.
  color - a string that represents the color of the cell.
  pos - an integer that represents the position of the cell.
  width - an integer that represents the width of the cell.
  Exceptions: None.

  Return Value: None.

   */
  public CellAnt(int type, String color, int pos, int width) {
    super(type, color, pos, width);
    cellAnts.add(this);

  }

  /*
  Purpose: Updates the state of the cell according to the rules of the Ants simulation.

   Assumptions: The cell's neighbors have been set.

   Parameters:

   grid - a Grid object that represents the grid in which the cell is located.
   width - an integer that represents the width of the grid.
   height - an integer that represents the height of the grid.
   Exceptions: None.

   Return Value: None.
   */
  @Override
  public void apply(Grid grid, int width, int height) {
    for (int i = 0; i < thisCellsAnts.size(); i++) {
      possibleAntMoves = new ArrayList<>();
      if (this.getType() != foodCell && this.getType() != nestCell) {
        forwardLocation((String) thisCellsAnts.get(i).get(0));
      }
      ifNoForwardLocations_SwitchToNeighbors();
      if (!possibleAntMoves.isEmpty()) {
        thisCellsAnts.get(i).set(2, (int) thisCellsAnts.get(i).get(2) - 1);
        Cell nextMoveCell = wereMove((Boolean) thisCellsAnts.get(i).get(1));
        if (nextMoveCell != null) {
          moveAnt(nextMoveCell, i);
        }
      }
    }
    if (this.getType() == nestCell) {
      spawnAnt();
    }
    this.homePheromoneLevel -= homePheromone_evaporationRate;
    this.foodPheromoneLevel -= foodPheromone_evaporationRate;
  }

  /**
   * Purpose: Sets the neighbors of the cell based on foragingAntsNeighbors method.
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
   **/
  @Override
  public void setNeighbors(Grid grid, int width, int height) {
    neighbors = n.foragingAntsNeighbors(grid, width, height, this);

  }

  /**
   * Purpose: Transitions the color of the cell to the specified color or to ant color if contains
   * ants.
   * <p>
   * Assumptions: None.
   * <p>
   * Parameters:
   * <p>
   * color - a string that represents the new color of the cell. Exceptions: None.
   * <p>
   * Return Value: None.
   * <p>
   * Note: This method overrides the transitionColor method in the Cell class.
   **/
  @Override
  public void transitionColor(String color) {
    super.transitionColor(color);
    if (this.numberOfAnts > 0) {
      this.transitionColor("000000");
    }
  }

  private void forwardLocation(String orientation) {
    if (orientation.equals("N")) {
      addForwardLocations(5, 0, 4);
    }
    if (orientation.equals("NE")) {
      addForwardLocations(1, 5, 0);
    }
    if (orientation.equals("E")) {
      addForwardLocations(6, 1, 5);
    }
    if (orientation.equals("SE")) {
      addForwardLocations(2, 6, 1);
    }
    if (orientation.equals("S")) {
      addForwardLocations(7, 2, 6);
    }
    if (orientation.equals("SW")) {
      addForwardLocations(3, 7, 2);
    }
    if (orientation.equals("W")) {
      addForwardLocations(4, 3, 7);
    }
    if (orientation.equals("NW")) {
      addForwardLocations(0, 4, 3);
    }
    removeImpossibleAntMoves();
  }

  private void addForwardLocations(int neighborhoodForwardLeftIndex, int neighborhoodForwardIndex,
      int neighborhoodForwardRightIndex) {
    possibleAntMoves.add(neighbors.get(neighborhoodForwardLeftIndex));
    possibleAntMoves.add(neighbors.get(neighborhoodForwardIndex));
    possibleAntMoves.add(neighbors.get(neighborhoodForwardRightIndex));
  }

  private Cell wereMove(Boolean hasFood) {

    antLocalPhermoneLevels = localPhermones(hasFood);
    Random random = new Random();
    int n = 0;
    for (int x : antLocalPhermoneLevels) {
      n += pheromoneSignificanceWhenChoosingLocationConstant + x;
    }
    double[] probabilities = new double[possibleAntMoves.size()];
    for (int i = 0; i < antLocalPhermoneLevels.size(); i++) {
      probabilities[i] =
          (pheromoneSignificanceWhenChoosingLocationConstant + antLocalPhermoneLevels.get(i))
              / (double) n;
    }
    double r = random.nextDouble();
    double sum = 0;
    for (int i = 0; i < probabilities.length; i++) {
      sum += probabilities[i];
      if (r <= sum) {
        return possibleAntMoves.get(i);
      }
    }

    return possibleAntMoves.get(possibleAntMoves.size() - 1);


  }

  private ArrayList<Integer> localPhermones(Boolean hasFood) {
    ArrayList<Integer> localPhermoneLevels = new ArrayList<>();
    if (hasFood) {
      for (Cell x : possibleAntMoves) {
        if (x != null) {
          localPhermoneLevels.add(cellAnts.get(x.getPosition()).homePheromoneLevel);
        }
      }
    } else {
      for (Cell x : possibleAntMoves) {
        if (x != null) {
          localPhermoneLevels.add(cellAnts.get(x.getPosition()).foodPheromoneLevel);
        }
      }
    }
    return localPhermoneLevels;
  }

  private void ifNoForwardLocations_SwitchToNeighbors() {
    if (possibleAntMoves.isEmpty()) {
      for (Cell x : neighbors) {
        if (x != null) {
          possibleAntMoves.add(x);
        }
      }
    }
    removeImpossibleAntMoves();
  }

  private void removeImpossibleAntMoves() {
    ArrayList<Cell> notImpossibleAntMoves = new ArrayList<>();
    for (Cell x : possibleAntMoves) {
      //type3=obstruction
      if (x != null && x.getType() != barrierCell
          && cellAnts.get(this.getPosition()).numberOfAnts != maxAntsPerCell) {
        notImpossibleAntMoves.add(x);
      }
      possibleAntMoves = notImpossibleAntMoves;
    }
  }

  private void moveAnt(Cell whereMoveCell, int whatAnt) {
    dropPheromone((boolean) thisCellsAnts.get(whatAnt).get(1));
    changeOrentation(whereMoveCell, whatAnt);
    if (whereMoveCell.getType() == foodCell) {
      thisCellsAnts.get(whatAnt).set(1, true);
    }
    if (whereMoveCell.getType() == nestCell) {
      thisCellsAnts.get(whatAnt).set(1, false);
    }
    //figure out visualRepresentation
    if ((int) cellAnts.get(this.getPosition()).thisCellsAnts.get(whatAnt).get(2) != 0) {
      cellAnts.get(whereMoveCell.getPosition()).thisCellsAnts.add(thisCellsAnts.get(whatAnt));
      cellAnts.get(whereMoveCell.getPosition()).numberOfAnts++;
    }
    cellAnts.get(this.getPosition()).thisCellsAnts.remove(thisCellsAnts.get(whatAnt));
    this.numberOfAnts--;
  }

  private void dropPheromone(Boolean hasFood) {
    if (this.getType() == foodCell) {
      this.foodPheromoneLevel += (foodPheromoneMax - this.foodPheromoneLevel);
    } else if (this.getType() == nestCell) {
      this.homePheromoneLevel += (homePheromoneMax - this.homePheromoneLevel);
    } else if (hasFood) {
      int homeHormoneDifferential =
          Collections.max(antLocalPhermoneLevels) - 2 - this.homePheromoneLevel;
      if (homeHormoneDifferential > 0) {
        this.homePheromoneLevel += homeHormoneDifferential;
      }
    } else {
      int foodHormoneDifferential =
          Collections.max(antLocalPhermoneLevels) - 2 - this.foodPheromoneLevel;
      if (foodHormoneDifferential > 0) {
        this.foodPheromoneLevel += foodHormoneDifferential;
      }
    }
  }

  private void changeOrentation(Cell whereMoveCell, int whatAnt) {
    int orientationChange = neighbors.indexOf(whereMoveCell);
    if (orientationChange == 0) {
      thisCellsAnts.get(whatAnt).set(0, "N");
    } else if (orientationChange == 1) {
      thisCellsAnts.get(whatAnt).set(0, "E");
    } else if (orientationChange == 2) {
      thisCellsAnts.get(whatAnt).set(0, "S");
    } else if (orientationChange == 3) {
      thisCellsAnts.get(whatAnt).set(0, "W");
    } else if (orientationChange == 4) {
      thisCellsAnts.get(whatAnt).set(0, "NW");
    } else if (orientationChange == 5) {
      thisCellsAnts.get(whatAnt).set(0, "NE");
    } else if (orientationChange == 6) {
      thisCellsAnts.get(whatAnt).set(0, "SE");
    } else if (orientationChange == 7) {
      thisCellsAnts.get(whatAnt).set(0, "SW");
    }
  }

  private void spawnAnt() {
    Random random = new Random();
    double r = random.nextDouble();
    if (r <= probabilityNestCellSpawnsAnt) {
      ArrayList<Object> newAnt = new ArrayList<>();
      newAnt.add(Orientations[(int) (Math.random() * 8)]);
      newAnt.add(false);
      newAnt.add(antLives);
      this.thisCellsAnts.add(newAnt);
      this.numberOfAnts++;
    }
  }
}
