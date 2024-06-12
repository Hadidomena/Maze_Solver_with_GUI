import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
/*
Interface for rightHand algorithm, used in generating Step-by-Step solution for Maze
 */
public class rightHand {

    private static final int END = -2;

    // Directions: right, down, left, up
    private static final int[] dRow = {0, 1, 0, -1};
    private static final int[] dCol = {1, 0, -1, 0};


    public static void solveMaze(int[][] maze, JPanel mazePanel) {
        SwingWorker<List<Point>, Point> worker = new SwingWorker<>() {
            @Override
            protected List<Point> doInBackground() throws Exception {
                Point start = rightHandUtilities.findStart(maze);
                if (start == null) {
                    throw new IllegalArgumentException("Start point not found in the maze.");
                }

                List<Point> path = new ArrayList<>();
                int direction = 0; // Start moving right (East)

                while (maze[start.x][start.y] != END) {
                    path.add(new Point(start));
                    maze[start.x][start.y] = -10; // Mark as visited

                    // Check right-hand direction first
                    int rightDirection = (direction + 1) % 4;
                    Point rightPoint = new Point(start.x + dRow[rightDirection], start.y + dCol[rightDirection]);

                    if (rightHandUtilities.canMove(rightPoint, maze)) {
                        direction = rightDirection;
                        start.setLocation(rightPoint);
                    } else {
                        // Move forward if possible, otherwise turn left until we can move
                        Point nextPoint = new Point(start.x + dRow[direction], start.y + dCol[direction]);
                        while (!rightHandUtilities.canMove(nextPoint, maze)) {
                            direction = (direction + 3) % 4; // Turn left
                            nextPoint = new Point(start.x + dRow[direction], start.y + dCol[direction]);
                        }
                        start.setLocation(nextPoint);
                    }

                    publish(start); // Publish the new position
                    Thread.sleep(25); // Wait for a one fortieth of a second between moves
                }

                path.add(new Point(start)); // Add the last point
                return path;
            }

            @Override
            protected void process(List<Point> chunks) {
                mazePanel.repaint(); // Repaint maze panel for each step
            }

            @Override
            protected void done() {
                try {
                    get();
                    mazePanel.repaint(); // Final repaint after ending
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }
}


