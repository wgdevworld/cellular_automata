# Cell Society Design Final
### TEAM NUMBER 13
### Jay Yoon, Woonggyu Jin, Ted Peterson


## Team Roles and Responsibilities

 * Woonggyu Jin
   * Responsible for Views (frontend)
   * Debugging issues with controller

 * Jay Yoon
   * Responsible for Controller and Model development (backend)
   * Debugging models and creating testing files

 * Ted Peterson
   * Responsible for Model development (backend)
   * Designing and implementing API


## Design goals

#### What Features are Easy to Add
* Our team aimed to make it easier for a client to add new types of simulations and new cell shapes
* Cell shapes can theoretically be any type of polygon as long as they are able to "draw out" the cells by defining the coordinates of each vertex
* Can easily extend rulesets for simulations by extending cell abstract class (simply overriding two methods: setNeighbors and apply)
* Infinite expanding grid: unlike arrays whose size need to be declared when initialized, ArrayList is flexible in extension, thus easy to add new cells



## High-level Design
* High-level initializing simulation logic: SimulationController.java
    * Simulation Controller initiates Grid, adding appropriate Cells (determined by input XML configuration simulation id) to the grid
    * It also initiates the collection of polygons, which are visual representations of the cell models (detached from any simulation logic, with sole purpose to be displayed)
    * It creates a timeline, calling step() method every frame, which in turn calls apply() method for every cell placed in grid (updating next state of cell)


* High-level Simulation UI logic: SimuationUI.java
    * Uses SimulationController class to handle button triggered events (such as resume, pause, save, speed-up, down-down functions)
    * Uses SimulationController initiateSimulation() method to receive Collection of Polygons (cell GUI) to be displayed on simulation window
    * SimulationUI uses AboutData record class to display general simulation information


* High-level Simulation loop logic: SimulationController.java
  * In a cellular automata simulation, the high-level simulation loop is the main process that updates the state of all cells in the grid based on the rules of the simulation.
  * The loop iterates through every cell held in the grid.cells collection, and for each cell, it calls the apply method defined in the Cell.java class. The apply method checks the current state of the cell and its neighbors, and applies the relevant rule method to determine the cell's next state.
  * If a rule condition is met, such as a certain number of neighboring cells being in a certain state, the rule method sets the nextType of the affected cells to their next state. This means that the cells' current state remains unchanged during the current iteration of the simulation loop, but their next state is updated based on the rule. Once all cells have been iterated through, and their next states have been determined, the simulation loop updates the state of all cells in the grid simultaneously.
  * This is achieved by setting the currentType of each cell to its nextType. By updating all cells simultaneously, the simulation ensures that each cell is updated based on the same time step, and there is no bias or lag between cells.

#### Core Classes
* Cell.java
  * The Cell.java class defines a cell object with global instance variables that define common cell attributes. This class provides getter methods that can be called by SimulationUI to define the color and position of the cell.
  * The Cell object represents an individual unit of the grid that can change its state over time based on certain rules. The global instance variables of the cell object include properties such as its current state, its next state, its location in the grid, and its color.
  * The getter methods allow other classes, such as SimulationUI, to access these attributes and display them in a visual representation of the simulation.


* Neighbors.java
  * The Neighbors.java class defines common cell neighborhood types and accounts for edge cases. In a cellular automata simulation, a cell's neighborhood is defined as the set of neighboring cells that directly interact with the cell in question.
  * The neighbors can be defined in different ways, such as a Moore neighborhood or a von Neumann neighborhood, which determine the shape and size of the neighborhood.
  * This class provides methods for calculating the neighbors of a cell based on its position in the grid and the chosen neighborhood type. It also accounts for edge cases, where cells on the boundary of the grid may have fewer neighbors than those in the center.


* Grid.java
  * The Grid.java class compiles the grid as an array of cells and provides helper methods for manipulating the grid. In a cellular automata simulation, the grid is the space where the cell objects are placed and interact with each other.
  * Our grid is essentially an ArrayList of Cell objects, where each cell has a position defined by its row and column coordinates. This class creates the grid by initializing an array of cell objects and populating it with the appropriate attributes.
  * It also provides helper methods for accessing and modifying the cells in the grid, such as getting a specific cell object by its position, setting the state of a cell, and updating the state of all cells in the grid simultaneously. These methods are used to implement the rules of the simulation and simulate the passage of time.


## Assumptions that Affect the Design
* Cell Homogeneity: The assumption that the cells in the system are identical in size, shape, and behavior.
* Discreteness: The assumption that the system is made up of a finite number of discrete cells, with each cell representing a specific location in space.
* Local interactions: The assumption that the behavior of each cell is determined only by the behavior of its immediate neighbors.
* Fixed topology: The assumption that the connectivity between cells is fixed and does not change over time.
* Synchronous updating: The assumption that all cells in the system are updated at the same time, in discrete time steps.
* Deterministic rules: The assumption that the behavior of each cell is determined by a fixed set of rules, which do not change over time.
* Finite state: The assumption that each cell can take on a finite number of states, with the behavior of the cell determined by its current state and the states of its neighbors.


#### Features Affected by Assumptions
* The assumption of synchronous updating requires that the nextState of Cell be calculated first, before any of the actual change of cell state takes place
* Possible cell states are all defined at the beginning of simulation in XML file and currently does not support addition of new states in the middle of simulation
* Each cell has a uniform size, with the same rules applied on every cell (therefore, one simulation id is taken from user)


## Significant differences from Original Plan
* Addition of Grid class
  * Originally, there was no separate Grid class.
  * Because each cell had their own apply() method and kept track of required cell lists (ex. emptyCells) as a static variable, we did not feel the necessity of creating a new Grid class.
  * However, this resulted in use of many static methods
  * We decided to create a new Grid class, that would return these empty cells and take care of the cell ArrayList.
  * This also returned cells that were converted into ImmutableCell type before returning to ensure immutability.


* Addition of Neighborhood class
  * Originally, there was no separate Neighborhood class
  * This was because each neighboring cells were calculated in specific cell setNeighbors() method.
  * However, this resulted in some duplicate code
  * Creation of Neighborhood class led to removal of duplicate code
  * Standard Moore and von Neumann neighborhoods could be extracted to separate methods in Neighborhood class


## New Features

#### Other Features Not Yet Done
* Interacting with the simulation parameters in real time
* Infinite grid: have grid format dynamically scale to perpetual cell replication.
* Allow for different types of neighborhoods to be selected based on cell shapes with differing edge types

#### How to Implement
* To interact with simulation parameters real time, would need to create a subclass of the Button class with event handlers defined in the Controller class. The event handler would simply adjust the parameters of the cells loaded in and reload the grid onto the GUI.
* To support the infinite grid option, update the grid when the cell is at the edge. This can be done by updating width and height of the Grid class, with new Cells added to the ArrayList of Cells. Because ArrayList can easily add and remove new objects, this can be implemented rather easily.
* To create a new simulation that is the base case grid layout, extend the cell.java class. If the simulation requires neighborhoods not defined in neighborhood.java, it must be extended as well with a class holding your neighbor logic. Your extended cell class should define any rules for cells given their neighborhood. Create a new xml file based on the template that defines grid features,cell types, and the path to your initialLayout.txt file. Finally create the txt file that holds the initial layout of your simulation. 

