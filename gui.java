import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class gui {

    private static JScrollPane scrollPane;
    private static boolean settingStart = true;

    private static void drawMaze(int[][] maze, JFrame frame) {
        JPanel mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int row = 0; row < maze.length; row++) {
                    for (int col = 0; col < maze[row].length; col++) {
                        if (maze[row][col] == -1) {
                            g.setColor(Color.BLACK);
                        } else if (maze[row][col] == -10) {
                            g.setColor(Color.RED);
                        } else if (maze[row][col] == 1) {
                            g.setColor(Color.GREEN);
                        } else if (maze[row][col] == -2) {
                            g.setColor(Color.BLUE);
                        } else {
                            g.setColor(Color.WHITE);
                        }
                        g.fillRect(col * 15, row * 15, 15, 15);
                        g.setColor(Color.GRAY);
                        g.drawRect(col * 15, row * 15, 15, 15);
                    }
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(maze[0].length * 15, maze.length * 15);
            }
        };

        scrollPane = new JScrollPane(mazePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0, 50, frame.getWidth() - 15, frame.getHeight() - 85);

        frame.add(scrollPane);
        frame.revalidate();
        frame.repaint();
    }

    private static void clearMaze(JFrame frame) {
        if (scrollPane != null) {
            frame.remove(scrollPane);
            scrollPane = null;
            frame.revalidate();
            frame.repaint();
        }
    }

    public static void design_button(JButton button, int x, int y, float which) {
        Font myfont = new Font("Arial", Font.PLAIN, 14);
        button.setBackground(Color.gray);
        button.setBounds((int) (x * which / 5), 0, x / 5, 50);
        button.setFont(myfont);
    }

    public static void mainFrame() {
        final int[][][] map = {{{1}}};
        int x = 1000;
        int y = 1000;

        JFrame frame = new JFrame("Maze-Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = frame.getContentPane();
        c.setBackground(Color.darkGray);

        JButton choosingButton = new JButton("Choose Maze");
        design_button(choosingButton, x, y, 0);
        frame.add(choosingButton);

        JButton generatingButton = new JButton("Shortest Route");
        design_button(generatingButton, x, y, 1);
        frame.add(generatingButton);

        JButton stepByStepButton = new JButton("Step by step Route");
        design_button(stepByStepButton, x, y, 2);
        frame.add(stepByStepButton);

        JButton ipsumButton = new JButton("Choose start and end");
        design_button(ipsumButton, x, y, 3);
        frame.add(ipsumButton);

        JButton saveButton = new JButton("Save as PNG");
        design_button(saveButton, x, y, 4);
        frame.add(saveButton);

        JScrollBar bar = new JScrollBar();
        bar.setVisible(true);
        frame.add(bar);
        frame.setSize(x, y);
        frame.setLayout(null);
        frame.setVisible(true);

        choosingButton.addActionListener(e -> {
            clearMaze(frame);
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filepath = selectedFile.getAbsolutePath();

                // Call the readMaze method
                fileHandling fileHandling = new fileHandling();
                map[0] = fileHandling.readMaze(filepath);
                drawMaze(map[0], frame);
                frame.repaint();
            }
        });

        generatingButton.addActionListener(e -> {
            clearMaze(frame);
            bfs bfs = new bfs();
            bfs.seed(map[0]);
            drawMaze(map[0], frame);
            frame.repaint();
        });

        stepByStepButton.addActionListener(e -> {
            clearMaze(frame);
            drawMaze(map[0], frame);
            rightHand solver = new rightHand();
            solver.solveMaze(map[0], (JPanel) scrollPane.getViewport().getView());
        });

        ipsumButton.addActionListener(e -> {
            clearMaze(frame);
            drawMaze(map[0], frame);

            JPanel mazePanel = (JPanel) scrollPane.getViewport().getView();
            mazePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int col = e.getX() / 15;
                    int row = e.getY() / 15;

                    if (row < map[0].length && col < map[0][0].length) {
                        if (settingStart) {
                            clearStartAndEnd(map[0], 1);
                            map[0][row][col] = 1; // Set start point
                        } else {
                            clearStartAndEnd(map[0], -2);
                            map[0][row][col] = -2; // Set end point
                        }

                        settingStart = !settingStart;
                        mazePanel.repaint();
                    }
                }
            });
        });

        saveButton.addActionListener(e -> saveMazeAsPNG((JPanel) scrollPane.getViewport().getView()));
    }

    private static void clearStartAndEnd(int[][] maze, int value) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == value) {
                    maze[row][col] = 0;
                }
            }
        }
    }

    private static void saveMazeAsPNG(JPanel mazePanel) {
        int width = mazePanel.getWidth();
        int height = mazePanel.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        mazePanel.printAll(g2d);
        g2d.dispose();

        try {
            ImageIO.write(image, "png", new File("maze.png"));
            JOptionPane.showMessageDialog(null, "Maze saved as maze.png");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save the maze image.");
        }
    }
}
