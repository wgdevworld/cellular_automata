# cell society

## Team 13

## Ted Peterson, Jay Yoon, Woonggyu Jin

This project implements a cellular automata simulator.

### Timeline

* Start Date: January 31, 2023

* Finish Date: February 14, 2023

* Hours Spent: ~100

### Attributions

* Resources used for learning (including AI assistance)
  * https://en.wikipedia.org/wiki/Conway's_Game_of_Life#Examples_of_patterns
  * http://nifty.stanford.edu/2017/feinberg-falling-sand/
  * https://cs.gmu.edu/~eclab/projects/mason/publications/alife04ant.pdf
  * http://lslwww.epfl.ch/pages/embryonics/thesis/Chapter3.html 
  * https://en.wikipedia.org/wiki/Conway's_Game_of_Life
  * https://www.youtube.com/watch?v=FeDBcKbO29M
  * https://en.wikipedia.org/wiki/Forest-fire_model
  * https://en.wikipedia.org/wiki/Schelling's_model_of_segregation
  * https://en.wikipedia.org/wiki/Wa-Tor

* Resources used directly (including AI assistance)
  * ChatGPT was used throughout to learn about boilerplate JavaFX code.

### Running the Program

* Main class:
    * The `main` method in `SimulatorUI` is used to launch the program.

* Data files needed (located in /data):
    * XML files that include information about simulation
    * txt files that include initial grid layout
    * png files that represent graphics for buttons

* Interesting data files:
  * random initial layout can be put into Conway Game of Life

* Key/Mouse inputs:
    * Buttons handle different events like playing/pausing the simulation, speeding up/slowing down
      the simulation, and stepping through the frames.

### Notes/Assumptions

* Assumptions or Simplifications:
  * user provides all required configuration to start simulation
    * this includes simulation ID, cellShape, width, height, valid layout, cell types for simulation

* Known Bugs:
    * CellAnt Simulation not visualize pheromone levels or number of ants on the grid, but working
      logic
    * Cell Langton only demonstrates a single replication loop, will not complete more cycles 
    * SugarScape agent fails to die out on edge
    * Histogram chart view of the cell count does not update when you pause the simulation and step
      through the frames. The count is correct once the animation is resumed, though.


* Features implemented:
    * All 9 simulation models (partial bugs and limitations stated above)
    * Allowing different kinds of neighborhoods
    * Allowing different kinds of cell shapes (hexagonal grid version of Conway)
    * Allowing any aspect of the simulation to be styled
    * Error handling handles:
      * no configuration file given 
      * required value not given 
      * required tag not given 
      * invalid values for these tags given (ex. String value for width, non-existent simulation ID)
      * empty initial layout given 
      * invalid initial layout given (non text files, non-existent files)
      * initial layout referring to invalid cell states 
      * width and height do not match given layout
    * Simultaneously updating graph of cell types
    * Pause, resume, speed up simulation
    * Save current status of simulation to be later used as input file (pick off from latest state)
    * Language options (English and French) read from resources properties file


* Features unimplemented:
    * Allowing any different kind of grid edge types
    * Allow users to interact with any simulation values dynamically such that each change takes
      immediate effect on the current simulation

* Noteworthy Features:
    * Hexagonal grid version of Conway
    * Random initial layout (“random” written on <layout>)
    * Implementing encapsulation using data records, immutableCell, and enum classes

### Assignment Impressions

Our experience working on a cell society programming project was both challenging and rewarding. We
were tasked with designing a program that would simulate the behavior of cells in a given
environment, with the goal of studying how changes to the environment would affect the behavior of
the cells.Throughout the project, we encountered numerous obstacles and setbacks. There were times
when we struggled to come up with effective algorithms to simulate cell behavior, and times when our
code didn't run as expected. However, we persevered and continued to work together to find solutions
to the problems we encountered.As we worked on the project, we learned a lot about various
programming concepts, including object-oriented design, algorithm development, and large project
programming design. In the end, our hard work paid off. We were able to create a fully functional
cell society simulation program that accurately captured the behavior of cells in different
environments. Our program provided valuable insights into how changes to the environment could
impact cell behavior, and we were proud of what we had accomplished.Overall, the cell society
simulation programming project was a valuable learning experience for our team. We learned a lot
about the programming process, including the importance of collaboration, persistence, and
creativity in solving complex problems. We also gained a deeper appreciation for the power of
computer science in solving real-world problems, and we look forward to applying what we have
learned to future projects.


