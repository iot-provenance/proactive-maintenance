/*
 * package clusterers;
 * 
 * import java.awt.Color; import java.awt.Dimension; import java.awt.Graphics;
 * import java.awt.Point; import java.util.ArrayList; import java.util.Date;
 * import java.util.List;
 * 
 * import javax.swing.JFrame; import javax.swing.JPanel;
 * 
 * import org.math.plot.FrameView; import org.math.plot.Plot2DPanel; import
 * org.math.plot.plots.ScatterPlot;
 * 
 * import com.jujutsu.tsne.TSne; import com.jujutsu.tsne.TSneConfiguration;
 * //import com.jujutsu.tsne.TSneUtils; import com.jujutsu.utils.TSneUtils;
 * import com.jujutsu.tsne.FastTSne; import com.jujutsu.tsne.SimpleTSne; import
 * moa.cluster.Cluster;
 * 
 * public class tsne extends JPanel {
 * 
 * private static final long serialVersionUID = 1L; private static final int
 * WIDTH = 800; private static final int HEIGHT = 600; private static final int
 * POINT_SIZE = 5;
 * 
 * 
 * private List<Cluster> clusters; double[][] Y;
 * 
 * 
 * public tsne(List<Cluster> clusters) { this.clusters = clusters;
 * setPreferredSize(new Dimension(WIDTH, HEIGHT)); }
 * 
 * @Override public void paintComponent(Graphics g) { super.paintComponent(g);
 * 
 * // Compute t-SNE double[][] data = getClusterData(); TSneConfiguration config
 * = TSneUtils.buildConfig(data, 2, 50, 500, 0, true, 12.0, true); TSne tsne =
 * new SimpleTSne(); Y = tsne.tsne(config);
 * 
 * // Draw clusters int clusterIndex = 0; for (Cluster cluster : clusters) {
 * Point center = convertToScreenCoords(Y[clusterIndex]);
 * g.setColor(getColorForCluster(clusterIndex)); g.fillOval(center.x -
 * POINT_SIZE / 2, center.y - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
 * g.setColor(Color.BLACK); g.drawOval(center.x - POINT_SIZE / 2, center.y -
 * POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
 * g.drawString(Integer.toString(clusterIndex), center.x + POINT_SIZE / 2,
 * center.y + POINT_SIZE / 2); clusterIndex++; } }
 * 
 * private double[][] getClusterData() { List<double[]> data = new
 * ArrayList<>(); for (Cluster cluster : clusters) {
 * data.add(cluster.getCenter()); //data.add(cluster.getCenter());
 * 
 * } double[][] dataArray = new double[data.size()][]; data.toArray(dataArray);
 * return dataArray; }
 * 
 * 
 * 
 * 
 * private Point convertToScreenCoords(double[] point) { double x = (point[0]) *
 * WIDTH; double y = (point[1]) * HEIGHT; return new Point((int) x, (int) y); }
 * 
 * 
 * 
 * private Color getColorForCluster(int clusterIndex) { // Compute a color for
 * each cluster based on its index int numClusters = clusters.size(); float hue
 * = (float) clusterIndex / (float) numClusters; return Color.getHSBColor(hue,
 * 1f, 1f); }
 * 
 * public void run() { // JFrame frame = new
 * JFrame("MOA Cluster t-SNE Visualization"); //
 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // frame.add(this);
 * //frame.pack(); // frame.setVisible(true);
 * 
 * double[][] data = getClusterData(); TSneConfiguration config =
 * TSneUtils.buildConfig(data, 2, 50, 500, 10, true, 12.0, true); TSne tsne =
 * new SimpleTSne(); double [][] Y = tsne.tsne(config);
 * System.out.println("Finished TSNE: " + new Date());
 * //System.out.println("Result is = " + Y.length + " x " + Y[0].length +
 * " => \n" + MatrixOps.doubleArrayToString(Y));
 * System.out.println("Result is = " + Y.length + " x " + Y[0].length); //
 * saveFile(new File("Java-tsne-result.txt"), MatrixOps.doubleArrayToString(Y));
 * Plot2DPanel plot = new Plot2DPanel();
 * 
 * ScatterPlot setosaPlot = new ScatterPlot("setosa", Color.BLACK, Y);
 * plot.plotCanvas.setNotable(true); plot.plotCanvas.setNoteCoords(true);
 * plot.plotCanvas.addPlot(setosaPlot);
 * 
 * FrameView plotframe = new FrameView(plot);
 * plotframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 * plotframe.setVisible(true); } }
 */