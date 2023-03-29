# Cell Society Design Lab Discussion

#### Woonggyu Jin - wj61, Jay Yoon - jy320, Ted Peterson - tcp19

### Simulations

* Commonalities
    * Cell changes state according to a set of rules
    * simulation takes place on a grid of blocks
    * Two steps: first determines cell state next updates grid
    * Able to store grid state in XML format
    * takes configuration file as input

* Variations
    * different rules for how cell states changes in reaction to its environment
    * the number the cell states vary by CA model

### Discussion Questions

* How does a Cell know what rules to apply for its simulation?
    * The rules are given as an input XML file (takes input from user)

* How does a Cell know about its neighbors?
    * data structure would hold what cells are relevant to a specific cell as a key-value pair

* How can a Cell update itself without affecting its neighbors update?
    * we would have two versions of the grid (using the “original” grid to determine changes to be
      made, and another grid to hold the updated version)

* What behaviors does the Grid itself have?
    * initialize grid according to config file
    * update method to set updated version of grid to current grid

* How can a Grid update all the Cells it contains?
    * iterating over a data structure of cells

* What information about a simulation needs to be in the configuration file?
    * size of grid (number of columns and rows)
    * type of CA model
        * rules for what environment cells turn on/off
    * color scheme information (cell color, window background)
    * initial state(state of cells)

* How is configuration information used to set up a simulation?
    * reading from a stored XML file
    * feeding these information as parameters to initialize Simulator

* How is the graphical view of the simulation updated after all the cells have been updated?
    * Simulator class would update Grid variable
    * In Grid class, it would call update method (iterate through all cells and call setFill(
      cell.color))

### Alternate Designs

#### Design Idea #1

* Data Structure #1 and File Format #1
    * Grid could be a hashmap (key as position of cell, and value as a list of relevant cells)
    * File format: type element (int), author element (text), description (text), size element with
      row and col as attributes, initial state type (int), optional parameter values (double)

* Data Structure #2 and File Format #2
    * Grid same as above: could be a hashmap (key as position of cell, and value as a list of
      relevant
      cells)
    * File format same as above: type element (int), author element (text), description (text), size
      element with row and col as attributes, initial state type (int), optional parameter values (
      double)

#### Design Idea #2

* Data Structure #1 and File Format #1
    * When the simulation is initialized each cell object has arrayList instance variable containing
      every cell that can affect it.
    * File format: type element (int), author element (text), description (text), size element
      with
      row and col as attributes, initial state type (text file), optional parameters

* Data Structure #2 and File Format #2
    * Data structure same as above: When the simulation is initialized each cell object has
      arrayList
      instance variable containing every cell that can affect it.
    * File format same as above: type element (int), author element (text), description (text), size
      element with row and col as attributes, initial state type (text file), optional parameters

### High Level Design Goals

### CRC Card Classes

This class's purpose is to control the overall simulation and update the grid.

| Simulation |      |
|------------|------|
| start()    | Grid |
| stop()     |      |
| update()   |      |


This class represents the entire simulation grid.

| Grid       |      |
|------------|------|
| getCells() | Cell |
| update()   |      |

This class represents a single cell in the simulation grid.

| Cell           |      |
|----------------|------|
| getState()     | Grid |
| setState()     |      |
| getNeighbors() |      |



### Use Cases

* Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of
  neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)

* Apply the rules to an edge cell: set the next state of a cell to live by counting its number of
  neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors
  missing)

* Move to the next generation: update all cells in a simulation from their current state to their
  next state and display the result graphically

* Set a simulation parameter: set the value of a parameter, probCatch, for a simulation, Fire, based
  on the value given in a data file

* Switch simulations: load a new simulation from a data file, replacing the current running
  simulation with the newly loaded one
