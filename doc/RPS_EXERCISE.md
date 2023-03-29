# Rock Paper Scissors Lab Discussion

#### Woonggyu Jin - wj61, Jay Yoon - jy320, Ted Peterson - tcp19

### High Level Design Goals

### CRC Card Classes

This class will manage the overall game functionality. It will be responsible for creating a new
game, resetting scores, updating the scores of the players, and managing the relationships between
the different choices.

| Game                                                    |              |
|---------------------------------------------------------|--------------|
| boolean public void resetScores()                       | Player       |
| public void updateScores(Player player, int score)      | Choice       |
| public void updateChoiceRelationships(Choice newChoice) | Relationship |
| void deliverTo (OrderLine, Customer)                    |              |

This class will represent a player in the game. It will manage the player's name, score, and choice
for the current round.

| Player                               |        |
|--------------------------------------|--------|
| public String getName()              | Game   |
| public int getScore()                | Choice |
| public void setChoice(Choice choice) |        |

This class will represent the different choices in the game (rock, paper, scissors). It will manage
the choice's name and its relationship to other choices.

| Choice                                                                |              |
|-----------------------------------------------------------------------|--------------|
| public String getName()                                               | Game         |
| public void setRelationship(Choice choice, Relationship relationship) | Player       |
| public void setChoice(Choice choice)                                  | Relationship |

This class will represent the relationships between the different choices. It will manage the type
of relationship (win, lose, draw) between two choices.

| Relationship                      |        |
|-----------------------------------|--------|
| public Choice getChoice1()        | Game   |
| public Choice getChoice2()        | Choice |
| public RelationshipType getType() |        |

This class will represent the different types of relationships between choices. It will manage the
name of the relationship type (win, lose, draw).

| RelationshipType        |              |
|-------------------------|--------------|
| public String getName() | Relationship |

### Use Cases

* A new game is started with five players, their scores are reset to 0.

 ```java
Game.createNewGame();
Game.resetScores();
 ```

* A player chooses his RPS "weapon" with which he wants to play for this round.

 ```java
Player.setChoice(Choice choice);
 ```

* Given three players' choices, one player wins the round, and their scores are updated.

 ```java
Game.updateScores(Player player, int score);
 ```

* A new choice is added to an existing game and its relationship to all the other choices is
  updated.

 ```java
Game.updateChoiceRelationships(Choice newChoice);
 ```

* A new game is added to the system, with its own relationships for its all its "weapons".

 ```java
Game.createNewGame()
    Game.updateChoiceRelationships(Choice newChoice);
 ```

