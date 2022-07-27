# Nerdle Solver

A solver for https://nerdlegame.com/

This main module of this repo contains 4 packages: expressionsGenerator, game,
simpleCalculator and solver.

Additionally, the root package contains a file Main.kt that can be used to play
the game manually.

Disclaimer: This code is far from perfect. It was mostly meant as a quick hack to solve 
nerdle games.

## How to use
Unfortunately there is no support for input arguments. 

To change the game mode,
you have to change the game mode between MANUAL and ASSIST in the main file.

When playing in MANUAL mode, you have to manually set the solution in the main file.

## Packages

### simpleCalculator
The simpleCalculator package contains functionality to convert a simple 
expression from a string to a double. For example "8*5-37" should evaluate to 
3.0.

### expressionGenerator
The expressionGenerator package contains functionality to generate all legal
Nerdle expressions of length 8. It does so by checking all possible combinations
of the alphabet ("0123456789*/+-=") and writing valid expressions to a file.

The generator creates one thread per character in the alphabet and each thread
does work on its own subtree. The threads that get 0, *, /, +, - or = does no
work, as no expression can start with those characters.

### solver
The solver package contains functionality for solving a Nerdle game with no
additional knowledge.

It does so by relying on a list of possible expressions that is iteratively
filtered as the solver gets more knowledge.

Before the main loop starts, the solver tries two expressions to gain some
initial information. "12/3-4=0" and "9+8*7=65". This is not strictly necessary,
but it does give initial knowledge about every symbol in the alphabet.

The game loop looks like this:
1. If there are more attempts left, the solver suggests an expression.
2. The user puts the suggestion into a Nerdle game on https://nerdlegame.com/
3. The Nerdle game proceeds, with either
   1. the game ending. This means that the user has won or lost.
   2. the game continues. The user notes the feedback given by the game.
4. The feedback given by the game reveals characters that are
   1. in the correct position
   2. in the final expression, but in the wrong position
   3. not part of the final expression.
5. The user inputs the feedback into the solver in a single line in the following format
   1. If the character is in the solution and in the correct position (GREEN), write C
   2. If the character is in the solution and not in the correct position (PURPLE), write M
   3. If the character is not in the solution, write E
   4. For example, ECEMMCCC
6. The solver incorporates this new information and returns to step 1.

### game
The game packages contains functionality for playing the game where the solution
is known to the program.

The game loop looks like this:
1. The user is prompted to put in an answer in the console.
2. The game gives feedback of the form C (correct), M (misplaced) or E (excluded) as a sequence. 
3. If the user has more attempts left, then return to 1.



