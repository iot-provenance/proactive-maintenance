package clusterers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import moa.cluster.Cluster;

public class ClusterVisualizer2 extends JPanel {
	

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int POINT_SIZE = 5;

    private List<Cluster> clusters;

    public ClusterVisualizer2(List<Cluster> clusters) {
        this.clusters = clusters;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Cluster cluster : clusters) {
            Point center = convertToScreenCoords(cluster.getCenter());
            g.setColor(Color.BLACK);
            g.drawOval(center.x - POINT_SIZE / 2, center.y - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
            g.drawString(Integer.toString(clusters.size()), center.x + POINT_SIZE / 2, center.y + POINT_SIZE / 2);
        }
    }

    private Point convertToScreenCoords(double[] point) {
        double x = (point[0] - 0) / (1 - 0) * WIDTH;
        double y = (point[1] - 0) / (1 - 0) * HEIGHT;
        return new Point((int) x, (int) y);
    }

    public void run() {
        JFrame frame = new JFrame("MOA Cluster Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }
}
