import java.util.Arrays;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;

public class bfs {

    public static void seed(int[][] maze) {
        int[] start = bfsUtilities.find_start(maze);
        int[] end = bfsUtilities.find_end(maze);
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

            pool.invoke(new bfsUtilities.ParallelTask(maze, currentLayer, queue, distance));
        }

        pool.shutdown();
        bfsUtilities.backtrack(maze, end);
    }
}