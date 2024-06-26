import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class gui {

    private static JScrollPane scrollPane;
    private static boolean settingStart = true;
    private static final int size_of_cell = 15;

    // Frame of the app
    public static void mainFrame() {
        final int[][][] map = {{{1}}};
        int x = 1000;
        int y = 1000;

        JFrame frame = new JFrame("Maze-Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = frame.getContentPane();
        c.setBackground(Color.darkGray);

        JButton choosingButton = new JButton("Choose Maze");
        designButton(choosingButton, x, 0);
        frame.add(choosingButton);

        JButton generatingButton = new JButton("Shortest Route");
        designButton(generatingButton, x, 1);
        frame.add(generatingButton);

        JButton stepByStepButton = new JButton("Step by step Route");
        designButton(stepByStepButton, x, 2);
        frame.add(stepByStepButton);

        JButton startAndEndButton = new JButton("Choose start and end");
        designButton(startAndEndButton, x, 3);
        frame.add(startAndEndButton);

        JButton saveButton = new JButton("Save as PNG");
        designButton(saveButton, x, 4);
        frame.add(saveButton);

        frame.setSize(x, y);
        frame.setLayout(null);
        frame.setVisible(true);

        choosingButton.addActionListener(e -> {
            mazePanel.clearMaze(frame, scrollPane);
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filepath = selectedFile.getAbsolutePath();

                int[][] maze = fileHandling.readMaze(filepath);
                map[0] = maze;
                scrollPane = mazePanel.drawMaze(maze, frame);
                frame.repaint();
            }
        });

        generatingButton.addActionListener(e -> {
            mazePanel.clearMaze(frame, scrollPane);
            bfs.seed(map[0]);
            scrollPane = mazePanel.drawMaze(map[0], frame);
            frame.repaint();
        });

        stepByStepButton.addActionListener(e -> {
            mazePanel.clearMaze(frame, scrollPane);
            scrollPane = mazePanel.drawMaze(map[0], frame);
            rightHand.solveMaze(map[0], (JPanel) scrollPane.getViewport().getView());
        });

        // After clicking this button you may change the start and end point, previous ones will become blank spaces
        startAndEndButton.addActionListener(e -> {
            mazePanel.clearMaze(frame, scrollPane);
            scrollPane = mazePanel.drawMaze(map[0], frame);

            JPanel MazePanel = (JPanel) scrollPane.getViewport().getView();
            MazePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int col = e.getX() / size_of_cell;
                    int row = e.getY() / size_of_cell;

                    if (row < map[0].length && col < map[0][0].length) {
                        if (settingStart) {
                            mazePanel.clearStartAndEnd(map[0], 1);
                            map[0][row][col] = 1; // Set start point
                        } else {
                            mazePanel.clearStartAndEnd(map[0], -2);
                            map[0][row][col] = -2; // Set end point
                        }

                        settingStart = !settingStart;
                        MazePanel.repaint();
                    }
                }
            });
        });

        saveButton.addActionListener(e -> mazePanel.saveAsPNG((JPanel) scrollPane.getViewport().getView()));
    }

    // Method used to simplify creating buttons
    public static void designButton(JButton button, int x, float which) {
        Font myfont = new Font("Arial", Font.PLAIN, 14);
        button.setBackground(Color.gray);
        button.setBounds((int) (x * which / 5), 0, x / 5, 50);
        button.setFont(myfont);
    }
}
