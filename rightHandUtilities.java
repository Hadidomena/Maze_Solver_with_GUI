import java.awt.*;

//Methods used by right hand algorithm
public class rightHandUtilities {

    // Values used to denote elements of maze
    private static final int WALL = -1;
    private static final int START = 1;

    // Checking if you are able to move
    static boolean canMove(Point point, int[][] maze) {
        return point.x >= 0 && point.x < maze.length && point.y >= 0 && point.y < maze[0].length && maze[point.x][point.y] != WALL;
    }

    // Finding cell marked as a start point
    static Point findStart(int[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == START) {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }
}
