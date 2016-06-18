# AStar-Visualizer
A simple program that demonstrates AStar path planning through visuals

After compilation, run in the format:
-java AStar [heuristic] < [filename]

Where [heuristic] is a number 0, 1, or 2, such that
- 0 = no heuristic, also known as Dijkstras algorithm
- 1 = Euclidean distance
- 2 = Octile distance with tie breaking priority given to the higher g value
