import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
/*
Implementation of rightHand algorithm, used in generating Step by Step solution for Maze
 */
public class rightHand {

    // Values used to denote elements of maze
    private static final int WALL = -1;
    private static final int PATH = 0;
    private static final int START = 1;
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
                Point current = start;
                int direction = 0; // Start moving right (East)

                while (maze[current.x][current.y] != END) {
                    path.add(new Point(current));
                    maze[current.x][current.y] = -10; // Mark as visited

                    // Check right-hand direction first
                    int rightDirection = (direction + 1) % 4;
                    Point rightPoint = new Point(current.x + dRow[rightDirection], current.y + dCol[rightDirection]);

                    if (rightHandUtilities.canMove(rightPoint, maze)) {
                        direction = rightDirection;
                        current.setLocation(rightPoint);
                    } else {
                        // Move forward if possible, otherwise turn left until we can move
                        Point nextPoint = new Point(current.x + dRow[direction], current.y + dCol[direction]);
                        while (!rightHandUtilities.canMove(nextPoint, maze)) {
                            direction = (direction + 3) % 4; // Turn left
                            nextPoint = new Point(current.x + dRow[direction], current.y + dCol[direction]);
                        }
                        current.setLocation(nextPoint);
                    }

                    publish(current); // Publish the new position
                    Thread.sleep(50); // Wait for a one twentieth of a second between moves
                }

                path.add(new Point(current)); // Add the last point
                return path;
            }

            @Override
            protected void process(List<Point> chunks) {
                mazePanel.repaint(); // Repaint maze panel for each step
            }

            @Override
            protected void done() {
                try {
                    List<Point> path = get();
                    mazePanel.repaint(); // Final repaint after done
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }
}


