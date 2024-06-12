import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class mazePanel {

    public static JScrollPane drawMaze(int[][] maze, JFrame frame) {
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

        JScrollPane scrollPane = new JScrollPane(mazePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0, 50, frame.getWidth() - 15, frame.getHeight() - 85);

        frame.add(scrollPane);
        frame.revalidate();
        frame.repaint();
        return scrollPane;
    }

    public static void clearMaze(JFrame frame, JScrollPane scrollPane) {
        if (scrollPane != null) {
            frame.remove(scrollPane);
            frame.revalidate();
            frame.repaint();
        }
    }

    public static void clearStartAndEnd(int[][] maze, int value) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == value) {
                    maze[row][col] = 0;
                }
            }
        }
    }

    public static void saveAsPNG(JPanel mazePanel) {
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
