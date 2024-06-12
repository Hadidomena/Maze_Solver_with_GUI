The goal of the program created for the project, written in Java using the Swing library, which was used for the graphical interface, 
was to find a path through a maze with a maximum size of 1024x1024 cells, counted along paths that can be navigated. The result of the program's operation should be a graphical representation of the sought path.
Two algorithms are implemented into program to allow for comparision of their output and to enable user to see creation of the route if using Right Hand algorithm.

Shortest Route created with BFS algorithm
<p align="center">
<img src="https://github.com/Hadidomena/Maze_Solver_with_GUI/assets/106683153/33a2060c-5e91-40d3-8fb7-e9796baeaf02" />
</p>
Route created with Right Hand algorithm
<p align="center">
<img src="https://github.com/Hadidomena/Maze_Solver_with_GUI/assets/106683153/b35a1d9d-cdb9-4d26-adde-ad58e34b1c6e" />
</p>

Maze this program is created to solve are written as follows if they are .txt files.
•P -- the start of the maze (entry)
•K -- the end of the maze (exit)
•X -- wall
•Space -- a place where it is possible to move.

A sample .txt file looks as follows:
<p align="center">
<img src="https://github.com/Hadidomena/Maze_Solver_with_GUI/assets/106683153/dd28e9ab-34b4-4ca8-aa5a-c756c6c5f96c" />
</p>

The file can also be in .bin format. Such a file is described as follows:
<p align="center">
<img src="https://github.com/Hadidomena/Maze_Solver_with_GUI/assets/106683153/754f0ba5-4279-47a8-bef8-78199ccc5f87" />
</p>
