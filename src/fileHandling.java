import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class fileHandling {

    public static int[][] readMapFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        // First, count the number of lines to determine the size of the array
        int rows = 0;
        int cols = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            rows++;
            if (line.length() > cols) {
                cols = line.length();
            }
        }

        // Initialize the array
        int[][] map = new int[rows][cols];

        // Reset the reader to the beginning of the file
        reader.close();
        reader = new BufferedReader(new FileReader(filePath));

        // Fill the array with the appropriate values
        int row = 0;
        while ((line = reader.readLine()) != null) {
            for (int col = 0; col < line.length(); col++) {
                char ch = line.charAt(col);
                switch (ch) {
                    case 'X':
                        map[row][col] = -1;
                        break;
                    case 'P':
                        map[row][col] = 1;
                        break;
                    case 'K':
                        map[row][col] = -2;
                        break;
                    default:
                        map[row][col] = 0;
                        break;
                }
            }
            row++;
        }

        reader.close();
        return map;
    }

    public static void main(String[] args) {
        try {
            int[][] map = readMapFromFile("path_to_your_file.txt");
            // Print the map for verification
            for (int[] row : map) {
                for (int cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
