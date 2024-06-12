import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class fileHandling {

    public static void binToTxt(String binaryFilename, String textFilename) {
        try (DataInputStream binary = new DataInputStream(new FileInputStream(binaryFilename));
             BufferedWriter text = new BufferedWriter(new FileWriter(textFilename))) {

            int currRow = 1;
            int currCol = 1;

            // Read the header information from the binary file
            int fileId = Integer.reverseBytes(binary.readInt());
            byte escape = binary.readByte();
            int columns = Short.reverseBytes(binary.readShort()) & 0xFFFF;
            int lines = Short.reverseBytes(binary.readShort()) & 0xFFFF;
            int entryX = Short.reverseBytes(binary.readShort()) & 0xFFFF;
            int entryY = Short.reverseBytes(binary.readShort()) & 0xFFFF;
            int exitX = Short.reverseBytes(binary.readShort()) & 0xFFFF;
            int exitY = Short.reverseBytes(binary.readShort()) & 0xFFFF;

            binary.skipBytes(12); // Skip 12 bytes as in the original C code

            int counter = Integer.reverseBytes(binary.readInt());
            int solutionOffset = Integer.reverseBytes(binary.readInt());
            byte separator = binary.readByte();
            byte wall = binary.readByte();
            byte path = binary.readByte();

            while (counter-- > 0) {
                binary.skipBytes(1);
                byte value = binary.readByte();
                int count = Byte.toUnsignedInt(binary.readByte());

                for (int x = 0; x < count + 1; x++) {
                    if (currRow == entryY && currCol == entryX) {
                        text.write('P');
                    } else if (currRow == exitY && currCol == exitX) {
                        text.write('K');
                    } else if (value == wall) {
                        text.write('X');
                    } else if (value == path) {
                        text.write(' ');
                    }

                    currCol++;
                    if (currCol > columns) {
                        text.write("\n");
                        currCol = 1;
                        currRow++;
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }

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

    public static int[][] readMaze (String filepath) {
        try {
            if (filepath.split("\\.")[1].equals("txt") ) {
                return readMapFromFile(filepath);
            } else if (filepath.split("\\.")[1].equals("bin")) {
                binToTxt(filepath, "temporary_maze.txt");
                int[][] output = readMapFromFile("temporary_maze.txt");
                Path path = Paths.get("temporary_maze.txt");
                Files.delete(path);
                return output;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[0][0];
    }

}
