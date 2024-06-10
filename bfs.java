import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class bfs {

    private static int[] find_start(int[][] maze) {

        for ( int row = 0; row < maze.length; row++ ) {

            for ( int col = 0; col < maze[0].length; col++ ) {

                if ( maze[row][col] == 1 ) {
                    return new int[]{row, col};
                }
            }
        }

        return new int[]{1,0};
    }

    private static int[] find_end(int[][] maze) {

        for ( int row = 0; row < maze.length; row++ ) {

            for ( int col = 0; col < maze[0].length; col++ ) {

                if ( maze[row][col] == -2 ) {
                    return new int[]{row, col};
                }
            }
        }

        return new int[]{maze.length - 1, maze[0].length};
    }

    private static int[][] get_bordering(int[][] maze, int row, int col) {
        int[][] result = new int[4][3];

        if ( row > 0 ) {
            result[0][0] = maze[row - 1][col];
        } else {
            result[0][0] = -1;
        }
        result[0][1] = row - 1;
        result[0][2] = col;

        if ( col > 0 ) {
            result[1][0] = maze[row][col - 1];
        } else {
            result[1][0] = -1;
        }
        result[1][1] = row;
        result[1][2] = col - 1;

        if ( row < maze.length - 1 ) {
            result[2][0] = maze[row + 1][col];
        } else {
            result[2][0] = -1;
        }
        result[2][1] = row + 1;
        result[2][2] = col;

        if ( col < maze[0].length - 1 ) {
            result[3][0] = maze[row][col + 1];
        } else {
            result[3][0] = -1;
        }
        result[3][1] = row;
        result[3][2] = col + 1;

        return result;
    }

    public static void seed(int[][] maze) {
        int[] start = find_start(maze);
        int[] end = find_end(maze);
        maze[start[0]][start[1]] = 1;
        maze[end[0]][end[1]] = -2;

        ConcurrentLinkedQueue<int[]> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        Map<String, Integer> distance = new ConcurrentHashMap<>();
        distance.put(Arrays.toString(start), 1);

        ForkJoinPool pool = new ForkJoinPool();
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<int[]> currentLayer = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                currentLayer.add(queue.poll());
            }

            pool.invoke(new ParallelTask(maze, currentLayer, queue, distance));
        }

        pool.shutdown();
        backtrack(maze, end);
    }

    public static void backtrack( int[][] maze, int[] end ) {
        int[] current = end;
        int value_of_field = maze[current[0]][current[1]];
        int[][] bordering_current;
        while ( true ) {
            value_of_field = maze[current[0]][current[1]];
            if (value_of_field <= 0) {
                return;
            }
            maze[current[0]][current[1]] = -10;
            bordering_current = get_bordering( maze, current[0], current[1] );

            for (int[] cell : bordering_current) {
                if (cell[0] == value_of_field - 1) {
                    current[0] = cell[1];
                    current[1] = cell[2];
                }
            }

        }
    }

    static class ParallelTask extends RecursiveAction {
        private int[][] maze;
        private List<int[]> currentLayer;
        private Queue<int[]> queue;
        private Map<String, Integer> distance;

        ParallelTask(int[][] maze, List<int[]> currentLayer, Queue<int[]> queue, Map<String, Integer> distance) {
            this.maze = maze;
            this.currentLayer = currentLayer;
            this.queue = queue;
            this.distance = distance;
        }

        @Override
        protected void compute() {
            List<ParallelTask> subtasks = new ArrayList<>();

            for (int[] current : currentLayer) {
                int value_of_field = distance.get(Arrays.toString(current));
                int[][] bordering_current = get_bordering(maze, current[0], current[1]);

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

            for (ParallelTask subtask : subtasks) {
                subtask.fork();
            }

            for (ParallelTask subtask : subtasks) {
                subtask.join();
            }
        }
    }
}
