# Cell Society Design Plan

### Team 13: Jay Yoon, Woongyu Jin, Ted Peterson

## Overview

There are many 2D grid-based models that operate on a set of rules: Conway’s Game of Life, Spreading
of Fire, Schelling's model of segregation, Wa-Tor World model of predator-prey relationships, Water
Percolation, etc. Although they look very different, their generic structures can be reduced into
one model: Cellular Automata. In this project, we seek to generate a simple, user-friendly engine
that can run all these CA models on one single simulator.

Therefore, the primary goal of this project is scalability: to be able to run any CA model when
provided with a viable set of rules. Although we initially test the simulator with five CA models,
it is definitely open to extension. Thus, the project will center around using abstractions and
inheritance.

To this end, we plan to structure our project into three main functions: View, Model, Controller.
The View works with the direct user-end (what the user sees); the Model structures out the necessary
abstractions and other concrete subclasses; the Controller will act as the middleware, handling
events.

## User Interface

![User Interface 001](https://user-images.githubusercontent.com/90667275/215635059-ab3cf317-2262-40f1-aba3-cf929787943b.jpeg)

## Configuration File Format

[example configuration file formats can be found in /data directory: Example01.xml, Example02.xml]

### Attributes:

* simulation type: specifies type of simulation (ex. Conway, Fire, Predator-Prey)

### Elements:

* initial state: includes basic information and defines initial size and cell layout configuration
    * id: ID of configuration file
    * author: author information
    * description: short description of file
    * width: initial width of simulation grid
    * height: initial height of simulation grid
    * layout: initial cell layout configuration
* cell type: includes information about different cell types
    * id: identifies cell type ID
    * color: defines color of cell
* rule: includes information about rules (how to handle cell interactions)
    * cell state: current cell state
    * interactor cell state: interacting cell state
    * change condition: conditions for the current cell state to change

<img width="617" alt="스크린샷 2023-01-30 오후 8 29 27" src="https://user-images.githubusercontent.com/90667275/215636038-ac3c63b7-96ad-4ace-afb0-4b02476cd2a9.png">

## Design Overview

1. Cell.java: It represents a single cell in the grid and stores its current state (Type), next
   state (nextType), color, position, and other properties such as rectangle shape and row/column
   indices. It also maintains a list of relevant cells, which are the neighboring cells whose states
   are used to determine the next state of this cell.


2. Rule.java: It represents a rule for updating the state of a cell based on the states of its
   relevant cells. A rule specifies the current state of the cell, the state of its relevant cells,
   and the conditions under which the cell state should change. The conditions are specified as the
   number of relevant cells with the specified state and an equality operator (e.g. >= or <=). The
   rule also specifies the state the cell should change to and the probability of this change
   happening.


3. Simulator.java: This class stores the initial state of the grid, including the width and height
   of the grid, the number of cell states, and the color associated with each state.

<img width="817" alt="스크린샷 2023-01-30 오후 8 39 15" src="https://user-images.githubusercontent.com/90667275/215637379-7d3cbb35-853a-4cd6-9e75-ae37784158d7.png">

## Design Details

* The Simulator abstraction is to be extended into different Simulator subclasses that support
  different models. For instance, the width and height of the grid or the color of the cell in each
  state or grid outline may differ based on the automata model. Or if we are to have a simulation
  that is not in the form of a rectangle, but rather another shape, this can be supported by
  overriding the method that initiates the grids. The Rule abstraction is to be extended into
  multiple Rule
  subclasses that describe how each model differs in terms of logic. This includes how many/what
  states cells can have, or how far apart cells have to be in order to affect “this” cell. The Cell
  abstraction is to be extended into Cell subclasses that, among other things, have their own method
  of determining a cell’s next state.
* These abstractions would need the XML file that describes the model and any other configurations.
* Any other concrete classes that may be needed include the SimulatorUI class, which is responsible
  for displaying the animated grid and buttons, and the
  controller, which parses XML files, event handlers for buttons, and saving files in XML format to
  the system drive.
* Since our methods that update the state of a cell, such as applyRule(Cell cell),
  work on a cell collection that are private instance variables, the implementation of the data
  structure is hidden from the user. The same is true for the file format: how it is formatted is
  hidden from the user as we extract necessary components with the use of tags and store them as
  private instance variables. Likewise, the initiateGrid() method has no parameters that specify the
  specific layout of the grid as these are all to be private instance variables in each Simulator
  subclass.

## Use Cases

* Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of
  neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)
    * initial state calls public ArrayList relevantCells(Cell c, Rule r) which creates an arrayList
      of the relevant cells for Cell c and Rule r. This arrayList is added to Cell c.relevantCells (
      ArrayList<ArraList<Integer Positions>>) For each step, each inner array list is called for its
      relevant rule and checked to see if that rules activation conditions are met.


* Apply the rules to an edge cell: set the next state of a cell to live by counting its number of
  neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors
  missing)
    * Same workflow as a middle cell. The relevantCells method accounts for the edge.


* Move to the next generation: update all cells in a simulation from their current state to their
  next state and display the result graphically
    * During step 1 of the simulation if the cell state is changed, the cells nextType variable
      holds what state it will change into. In step 2 all the cells are changed to Type nextType and
      there colors are changed based on a call to whatColor( int Type). The grid is made up of
      individual cells, so once the cell Rectangle fill color is changed, the grid will update.


* Switch simulations: load a new simulation from a data file, replacing the current running
  simulation with the newly loaded one
    * If the new simulation has a rule that is not already defined, a new method must be made in the
      Rule class that defines relevant cells and change conditions. Otherwise a new simulation object can be created. 


* Set a simulation parameter: set the value of a parameter, probCatch, for a simulation Spreading of
  Fire, based on the value given in a data file
    * If the parameter is not defined, it must be added as an instance variable in the initial state
      class and the value set by parsing the input file. 
* Scale grid size and cell size based on how many cells are in the inputed simulation
    * The size of the grid will be constant. The size of cell will set in the simulation class via a method named setSizeCell((int) numberOfCells) and set instance variable cellSize. 
* Find relevant cells to check for inputed rules on each call of step1 of the loop. 
    * When a cell object is initialized, make a call to public HashSet<Integer> relevantPositions(Rule r,int cellPosition) which outputs a hashet of positions of relevant cells. 
* Set fill color of Rectangle cell object after cell type is changed
    * The type of the cell object will be a integer value. When the game is initiated a table of HashTable typeColorMap(Integer type, String (Hashcode) color). When a cells type is changed a call will be made to the cell method changeColor(Type) that will change setFill(typeColorMap.get(Type)). 
* Know when a rule is applied to a cell. 
  *The cell state and interacting cell state need to activate a rule will be instance variables of a rule object. If a cell is the neccary type to trigger a rule, the cells in the relavent positions will be checked for their type. If the type matched the value of rule.interactingCellState a count will be increased. After iterating though all relevent positions if the count meets the rule requirments, the rule will be actibated. 
* Rewind simulation
  * After during step1 of the simulation save the position index and type of each cell once all rules have been checked. After doing so for every cell save the two arrays. Once a key is pressed to activate rewind, have the animation call reverseStep() instead of step1() and step2() that sets values of cells based on blueprint of previous step.
* Add a rule to simulation. 
 * Call Simulation.addRule(cell type, interacting cell type, (String)proximity rules, optional probability, number to trigger). 




## Design Considerations

1. Having Rule as an abstract class

We spent time discussing whether Rule.java should be an abstract or concrete class. Option 1 was to
have Rule as an abstract class, and have the different rules be defined as a subclass. The
information about which model type would be read from input XML file. In other words, the rules for
the different models would somewhat be autogenerated (without having to specify specifics of rule in
input XML file.) Option 2 was to have specifics of the rule be specified in the input XML file.
Major advantage of this would be that it allows users to possibly "customize" their own CA models (
by setting set of rules). The current example XML configuration file follows this design, but this
might undergo change in the future.

2. Having Two set of Grid

Another consideration we had was whether to have two set of grids: one that represents current state
of cells, and another that represents the next grid (as directly updating current grid could affect
future state). One drawback would be that we would be increasing the number of data structures
needed. We concluded with another option, which is to add to Cell class one extra variable that
stores current and next state, and update one grid based on next state.

3. Having Cell as an abstract class

One other discussion was about having cell as an abstract class - mainly, about whether this was
needed. This seemed to depend on where we would have the specifics of the rules defined. If we were
to specify them in the Rule class (applyRule method, for example), this would remove the main
purpose of having cell subclasses for different CA models. On the other hand, if we had them on the
Cell class, we would have a method (like updateState()) that would differ based on which model we
were working on. For now, we plan to place the specifics of the models in Rule class, but this is
prone to change.

## Team Responsibilities

* Jay Yoon: mainly responsible for Controllers
    * start off with XMLInputController, then SimulationController
    * integrate SimulationUI class with relevant event handlers


* Woonggyu Jin: mainly responsible for Views
    * start off with SimulationUI that contains buttons to support basic functions
    * connect to Controller
    * continue on with additional windows like About Screen


* Ted Peterson: mainly responsible for Models
    * outline all necessary instance variables and methods for abstractions
    * create necessary concrete subclasses extended from abstractions
