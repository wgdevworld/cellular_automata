# Refactoring Lab Discussion
#### Jay Yoon, Woonggyu Jin, Ted Peterson
#### TEAM 13


## Principles

### Current Abstractions

#### Abstraction #1: Cell
* Open/Close: To implement a new simulation, no revisions to any existing files need to be made, as creating a new Cell subclass file would suffice (overriding setNeighbors and apply to reflect the new set of rules)

* Liskov Substitution: Current operations on Cell class only has the most "general" operations, like setting neighboring cells and applying rule.


### New Abstractions

#### Abstraction #1: Grid
* Open/Close: To implement new grid, addition of new type of Grid would be needed. Grid will hold the arrayList of cells and getter methods.

* Liskov Substitution: operations on Grid would only include the most general actions of the grid, including returning specific cell in the grid by position.

* Whether to have Grid as an abstraction, or have one grid concrete class would need more discussion.

#### Abstraction #2: Neighbor
* One other possibility in consideration is having a Neighbor class, that would take care of the different neighbor types needed in different simulations to handle more various types.
* Open/Close: Too add new neighboring type (other than Moore), adding a new file would need to suffice. Therefore, no specific references would be made using Neighbor type.

* Liskov Substitution: We would be able to replace any call to Neighbor type with a subclass of Neighbor. This would require that the Neighbor class only includes the most general actions, like setNeighbors.


## Issues in Current Code

### Cell
* Subclasses of cell have their own specific instance variables to be able to implement their own set of rules
* Some also have static variables to keep track of emptyCells, and this can be removed by creating a Grid class

### Predation Cell
* applying rule to predationCell currently requires downcasting Cell to Predation cell
* updating to not passing the direct cell list could solve this problem

### SimulationUI
* switching models in SimulationUI currently results in some error
* this could be solved by creating separate SimulationUI classes or updating logic to switch roots groups.


## Refactoring Plan

* What are the code's biggest issues?

We would need a new Grid class to handle the overarching cells (keeping track of which cells are empty, grid shapes, etc.)

* Which issues are easy to fix and which are hard?

Some easy fixes include checking variable scope (some variables global), reconsidering static methods.
Harder fixes would include adding a new Grid class and make new methods accordingly.

* What are good ways to implement the changes "in place"?

This would relate to the OC principle. If we have the appropriate abstractions, it would be easier to implement new changes, as we would not need to keep making changes; we would have to add new files.
This also includes accepting parameters in the most "general" form possible.



## Refactoring Work

* Creating Grid Class
  * this would remove the need for most static variables present in Cell subclasses
  * this would also allow us to follow the encapsulation principle (without exposing cell structure in other classes)


* Updating Cell Subclasses
  * updating cell subclass-specific instance variables would allow us to follow Liskov substitution principle, as current Cell abstraction has only the most general cell variables


* Removing imports from .javafx (color, shape) from backend
  * this would require to pass color and shape as Strings (hex and shape form) to frontend
  * the frontend would receive shape, color, position returns and display accordingly
