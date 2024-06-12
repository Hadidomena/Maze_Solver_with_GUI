import java.util.Arrays;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;

// Implementation of Parallel BFS
public class bfs {

    public static void seed(int[][] maze) {
        int[] start = bfsUtilities.findStart(maze);
        int[] end = bfsUtilities.findEnd(maze);
        maze[start[0]][start[1]] = 1;
        maze[end[0]][end[1]] = -2;

        // Beginning of the Queue, Concurrent one is used to make algorithm faster
        ConcurrentLinkedQueue<int[]> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        Map<String, Integer> distance = new ConcurrentHashMap<>();
        distance.put(Arrays.toString(start), 1);

        // Loop will work for as long as queue is not empty or exit is not found
        ForkJoinPool pool = new ForkJoinPool();
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<int[]> currentLayer = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                currentLayer.add(queue.poll());
            }

            pool.invoke(new bfsUtilities.ParallelTask(maze, currentLayer, queue, distance));
        }

        // after finding exit backtrack method is invoked
        pool.shutdown();
        bfsUtilities.backtrack(maze, end);
    }
}