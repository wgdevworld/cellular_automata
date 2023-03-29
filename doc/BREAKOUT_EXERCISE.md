# Breakout Abstractions Lab Discussion
#### Woonggyu Jin - wj61, Jay Yoon - jy320, Ted Peterson - tcp19

### Block

This superclass's purpose as an abstraction:
```java
public class Block {
public void draw() {
// The purpose of this class is to provide an abstraction for a block in a game or level. It could
//contain information such as the block's position, size, and appearance.
}
}

```

#### Subclasses

This subclass's high-level behavioral differences from the superclass:
```java
public class BreakableBlock extends Block {
public void break() {
// This subclass differs from the superclass by adding the ability for the block to be broken or
//destroyed. It could have additional methods or variables related to the breaking behavior.
  }
}
```

#### Affect on Game/Level class


The addition of a BreakableBlock subclass would affect the Game/Level class by introducing the
possibility for the player to interact and break blocks. This could affect gameplay mechanics,
scoring, and level design.

### Power-up

This superclass's purpose as an abstraction:
```java
public class PowerUp {
public void activate() {
// The purpose of this class is to provide an abstraction for a power-up in a game. It could contain
//information such as the power-up's effect and duration.
}
}
```

#### Subclasses

This subclass's high-level behavioral differences from the superclass:
```java
public class ShieldPowerUp extends PowerUp {
public void deactivate() {
// This subclass differs from the superclass by adding the ability for the power-up to have a start
//and end time, and providing the player with a shield. It could have additional methods or variables
//related to the shield behavior.
}
}
```

#### Affect on Game/Level class

The addition of a ShieldPowerUp subclass would affect the Game/Level class by introducing the
possibility for the player to have a shield and be protected from damage. This could affect gameplay
mechanics, scoring, and level design.

### Level

This superclass's purpose as an abstraction:
```java
public class Level {
public void load() {
// The purpose of this class is to provide an abstraction for a level in a game. It could contain
//information such as the level's layout, difficulty, and objectives.
}
}
```

#### Subclasses

This subclass's high-level behavioral differences from the superclass:
```java
public class BossLevel extends Level {
public void defeatBoss() {
//// This subclass differs from the superclass by adding a boss to the level that the player must
//defeat. It could have additional methods or variables related to the boss behavior and defeat
//conditions.
}
}
```

#### Affect on Game class

The addition of a BossLevel subclass would affect the Game class by introducing the possibility for
the player to face a boss and defeat it, adding a new level of difficulty and gameplay mechanics.