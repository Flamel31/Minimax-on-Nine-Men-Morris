# Minimax on Nine Men's Morris

Repository for the final project of the course MULTIAGENTS SYSTEMS - 90545 (year 2021/2022).

The purpose of the project was to develop an agent with the JADE Framework which plays the game “Nine Men’s Morris”.

## The game

The game is played on a board composed of twenty-four locations, usually called “*points*”, connected by lines which determine the directions each piece can take to
move. Each player has nine pieces, or “*men*”, coloured black and white. The purpose of the game is to form “*mills*”, three pieces of the same colour lined horizontally or
vertically, that allow the player to remove one of the opponent’s pieces. A player that has only two pieces left or that is unable to make a move lose the game.

The game starts within the “*placing phase*”, where each player takes turns placing their pieces on empty points. If one of the players form a mill while placing his pieces, he can remove one of their opponent’s pieces currently on the board (apart from pieces that are composing a mill).

After all pieces have been placed the game moves to the “*moving phase*”, where players take turns moving a piece to an adjacent point. A piece can only move to an
empty point and is not able to “*jump*” another piece. As before, forming a mill while in this phase of the games lead to the removal of one of the opponent’s pieces. When a player is reduced to three pieces, he has no longer limitation on the movement and can move his pieces on any empty point on the board.

## Game engine

All the classes, interfaces and enumerator regarding the game engine are collected under the package “model”. The class that incorporates all the core functionalities is
NineMensMorris, an object of that class represents a current match in progress. The board is recreated with a 7x7 matrix unique for each object. Instead, the coordinates of
all the points and their relationships of adjacency are stored inside static structures available for all classes (GameConst). This kind of approach helps mitigate the redundancy of information (and consequent waste of memory) that other data structure would create during the building of multiple instances.

The matrix that represents the board is composed by objects of the class GamePiece, which is an Enum that encodes all the possible state of a point: occupied by a *Black
Piece*, occupied by a *White Piece* or *Empty*. The “*turn*” variable, also of type GamePiece, is used to determine which player is supposed to act, where the “*phase*” variable instead establish what type of actions can be done by the player: *Placing Pieces*, *Moving Pieces*, *Removing Pieces* or *Game Ended*.

To perform an action from a phase it is sufficient to call one of the implemented methods, like: *movePiece*, *removePiece* or *addPiece*. Each of these methods return true
if the action was performed correctly, false otherwise (for example if it wasn’t a legal action in that phase).

Lastly, the class also implements the **Observer Java pattern** being the *observable* object which notifies all the *observer* whenever there is an important change in the game state, like: a turn change, a phase change or a game action (place/move/remove a piece).

## Minimax algorithm
### Game tree
<table>
  <tr>
    <td><img src="/img/MinimaxTree1.png" alt="Minimax tree"/></td>
    <td><img src="/img/MinimaxTree2.png" alt="Compressed minimax tree"/></td>
  </tr>
</table>

### Board state string format
Here an example:

 <table>
   <tr>
     <td>WWBBWWEWEBBEEBWEWEEBBWBE</td>
     <td>MB</td>
     <td>18</td>
   </tr>
 </table>
   
The first twenty-four characters are used to represent a point on the board (starting from the top left corner), the 3 possible values are: *W* (White Piece), *B* (Black Piece) and *E* (Empty). The next two characters are used to encode the current phase and the current active player. For the phase we have: *P* (Placing Pieces), *M* (Moving Pieces), *R* (Removing Pieces) and *E* (Game Ended). Finally, the last characters are used to keep track of how many pieces the players have put on the board (which is needed only during the placing phase).

## Evaluating the board
The key element of the minimax algorithm is being able to accurately evaluate the current game state, since all the decision that the agent will make are based on minimizing or maximizing this value.  Using my knowledge of the game and following some simple concepts I tried to design a basic algorithm to evaluate the board state:

1. If the game has ended, then we assign some fixed values: 0 to the losing player and 999 to the winning player.
2. A player with more pieces has more chances of winning.
3. If we are in the placing phase, we also need to consider the number of pieces that we can still place on the board and not only the ones that are currently on the board (since there could be ambiguous case where both players have the same number of pieces on the board but not the same number of pieces in hand).
4. We need to value more the pieces that are currently in a mill compared to pieces that are not (since they cannot be removed, and they can move back and forth to make a mill).
5. We need to consider the number of moves that a player has at a given state (since a player that is not able to make a move lose).

After assigning a weight to each of the information mentioned (pieces on the board, pieces in hand, pieces in a mill and number of moves) we sum them up and obtain a positive value that represent how good the board state is for a certain player. 

![Algorithm to evaluate the game board](/img/Algo_1.png)

Finally, to assign the value at each leaf node of the game tree, we evaluate the difference between the value of the board for the maximizing player and value of the board for the minimizing player. 

![Algorithm to evaluate the game board](/img/Algo_2.png)

## Alpha & Beta pruning 
The alpha & beta pruning is an improvement of the minimax algorithm that seeks to decrease the number of nodes that are evaluated. This can be done thanks to the use of two variables: 
- **Alpha**: which stores the minimum score of the maximizing player.
- **Beta**: which stores the maximum score of the minimizing player.
By keeping track of these values at each iteration, the algorithm can stop evaluating a branch of moves when at least one better solution has already been found.

# The Graphical User Interface
The Graphical User Interface has been developed using the Swing Java library, with the help of the Window Builder Tool provided by the Eclipse IDE.
<table>
  <tr>
    <td><img src="/img/GUI_1.png" alt="GUI: Main menu window"/></td>
    <td><img src="/img/GUI_2.png" alt="GUI: Game window"/></td>
  </tr>
</table>

Nicholas Gazzo, 4498892
