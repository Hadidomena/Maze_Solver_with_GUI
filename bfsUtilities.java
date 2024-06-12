import java.util.*;
import java.util.concurrent.RecursiveAction;

public class bfsUtilities {

    // Method used for finding start
    static int[] findStart (int[][] maze) {
        for ( int row = 0; row < maze.length; row++ ) {
            for ( int col = 0; col < maze[0].length; col++ ) {
                if ( maze[row][col] == 1 ) {
                    return new int[]{row, col};
                }
            }
        }
        return new int[]{1,0};
    }

    // Method used for finding exit/end of maze
    static int[] findEnd (int[][] maze) {
        for ( int row = 0; row < maze.length; row++ ) {
            for ( int col = 0; col < maze[0].length; col++ ) {
                if ( maze[row][col] == -2 ) {
                    return new int[]{row, col};
                }
            }
        }
        return new int[]{maze.length - 1, maze[0].length};
    }

    // Method used to get cells bordering current cell
    static int[][] get_bordering(int[][] maze, int row, int col) {
        int[][] result = new int[4][3];
        int[][] offsets = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}}; // row_offset, col_offset

        for (int i = 0; i < 4; i++) {
            int newRow = row + offsets[i][0];
            int newCol = col + offsets[i][1];

            if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length) {
                result[i][0] = maze[newRow][newCol];
            } else {
                result[i][0] = -1;
            }

            result[i][1] = newRow;
            result[i][2] = newCol;
        }

        return result;
    }

    // Implementation of class used to make bfs algorithm run Parallel
    static class ParallelTask extends RecursiveAction {
        private final int[][] maze;
        private final List<int[]> currentLayer;
        private final Queue<int[]> queue;
        private final Map<String, Integer> distance;

        ParallelTask(int[][] maze, List<int[]> currentLayer, Queue<int[]> queue, Map<String, Integer> distance) {
            this.maze = maze;
            this.currentLayer = currentLayer;
            this.queue = queue;
            this.distance = distance;
        }

        @Override
        protected void compute() {

            for (int[] current : currentLayer) {
                int value_of_field = distance.get(Arrays.toString(current));
                int[][] bordering_current = bfsUtilities.get_bordering(maze, current[0], current[1]);

                for (int[] cell : bordering_current) {
                    if (cell[0] == 0) {
                        int[] next = new int[]{cell[1], cell[2]};
                        synchronized (queue) {
                            if (!distance.containsKey(Arrays.toString(next))) {
                                queue.add(next);
                                distance.put(Arrays.toString(next), value_of_field + 1);
                                maze[next[0]][next[1]] = value_of_field + 1;
                            }
                        }
                    } else if (cell[0] == -2) {
                        maze[cell[1]][cell[2]] = value_of_field + 1;
                        return;
                    }
                }
            }

        }
    }

    // Method invoked after algorithm has found exit from the maze to create route
    public static void backtrack( int[][] maze, int[] end ) {
        int value_of_field;
        int[][] bordering_current;
        while ( true ) {
            value_of_field = maze[end[0]][end[1]];
            if (value_of_field <= 0) { // when we reach entrance method stops
                return;
            }
            maze[end[0]][end[1]] = -10;
            bordering_current = bfsUtilities.get_bordering( maze, end[0], end[1] ); //getting cells bordering current one

            for (int[] cell : bordering_current) {
                if (cell[0] == value_of_field - 1) { // while backtracking it moves from the current cell to the cell with the value smaller by one
                    end[0] = cell[1];
                    end[1] = cell[2];
                }
            }

        }
    }
}
