package cellsociety.View;

import cellsociety.Controller.SimulationController;
import java.util.ArrayDeque;
import java.util.Deque;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Purpose:
 * <p> Define parameters for Chart view of the simulation
 * Assumptions:
 * <p> None
 * Dependencies:
 * <p> Simluation controller
 * @author Woonggyu Jin
 */

public class ChartView {

  private double LINE_WIDTH = 2;
  private double PIXELS_PER_UNIT = 50.0;
  private double MAX_ITEMS = SimulationUI.CHART_WIDTH / PIXELS_PER_UNIT;
  private double oldX = -1;
  private double oldY = -1;

  private GraphicsContext g;
  private Color color;
  private SimulationController simController;
  private int cellType;
  private Deque<Double> buffer = new ArrayDeque<>();

  public ChartView(GraphicsContext g, Color color, SimulationController simController,
      int cellType) {
    this.g = g;
    this.color = color;
    this.simController = simController;
    this.cellType = cellType;
  }

  /**
   * Purpose:
   * <p> Update the animated histogram of live cell count.
   * Assumptions:
   * <p> None
   * Dependencies:
   * <p> None
   * @author Woonggyu Jin
   */

  public void update() {
    double yValue = SimulationUI.CHART_HEIGHT;
    if (simController.getCellCount().get(cellType) != null) {
      yValue =
          (1 - simController.getCellCount().get(cellType) / (double) simController.getTotalCellCount())
              * SimulationUI.CHART_HEIGHT;
    }
    buffer.addLast(yValue);

    if (buffer.size() > MAX_ITEMS) {
      buffer.removeFirst();
    }

    g.setStroke(color);
    g.setLineWidth(LINE_WIDTH);
    buffer.forEach(y -> {
      if (oldY > -1) {
        g.strokeLine(oldX * PIXELS_PER_UNIT, oldY, (oldX + 1) * PIXELS_PER_UNIT, y);
      }
      oldX += 1;
      oldY = y;
    });
    oldX = -1;
    oldY = -1;
  }
}