import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
        int[] start = find_start( maze );
        int[] end = find_end( maze );
        int[] current;
        int[][] bordering_current;
        int value_of_field;
        maze[ start[0] ][ start[1] ] = 1;
        maze[ end[0] ][ end[1] ] = -2;
        Queue queue = new LinkedList<>();

        queue.add( start );
        queue.add( 1 );

        while ( !queue.isEmpty() ) {

            current = (int[]) queue.remove( );
            value_of_field = (int) queue.remove();
            maze[current[0]][current[1]] = value_of_field;
            bordering_current = get_bordering( maze, current[0], current[1] );

            for (int[] cell : bordering_current) {
                if (cell[0] == 0) {
                    queue.add(new int[]{cell[1], cell[2]});
                    queue.add(value_of_field + 1);
                } else if (cell[0] == -2) {
                    maze[cell[1]][cell[2]] = value_of_field + 1;
                    backtrack(maze, end);
                    return;
                }
            }
        }
    }

    public static void backtrack( int[][] maze, int[] end ) {
        int[] current = end;
        int value_of_field = maze[current[0]][current[1]];
        int[][] bordering_current;
        while ( true ) {
            System.out.println("Current value: " + current[0] + ", " + current[1] + ", " + value_of_field);
            value_of_field = maze[current[0]][current[1]];
            if (value_of_field <= 0) {
                return;
            }
            maze[current[0]][current[1]] = -10;
            bordering_current = get_bordering( maze, current[0], current[1] );

            for (int[] cell : bordering_current) {
                System.out.println("Current cell: " + cell[0] + cell[1] + cell[2]);
                if (cell[0] == value_of_field - 1) {
                    current[0] = cell[1];
                    current[1] = cell[2];
                }
            }

        }
    }
    public static void main(String[] args) {

        int[][] maze = {{-1,-1,-1,-1,-1}, {1,0,0,0,-1}, {-1,0,-1,0,-1},{-1,0,0,0,-1}, {-1,-1,-1,-2,-1}};

        for (int[] line: maze) {
            System.out.println(Arrays.toString(line));
        }

        seed(maze);

        for (int[] line: maze) {
            System.out.println(Arrays.toString(line));
        }
    }
}
