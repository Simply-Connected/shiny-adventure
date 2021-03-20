package org.simply.connected.application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;
import org.simply.connected.application.optimization.methods.*;
import org.simply.connected.application.optimization.methods.model.Data;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class Controller implements Initializable {

    @FXML
    public Text leftPositionText;
    @FXML
    public Text rightPositionText;
    @FXML
    public LineChart<Double, Double> lineGraph;
    @FXML
    public Button prevButton;
    @FXML
    public Button nextButton;
    @FXML
    public NumberAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public Text iterationCountText;
    @FXML
    public Text currentIterationText;
    @FXML
    public Text minXText;
    @FXML
    public Text minYText;

    private Double eps = 1e-4;
    private UnaryOperator<Double> function = (x) -> -3 * x * Math.sin(3 * x / 4) + Math.exp(-2 * x);
    private Data segment = new Data(0, 0, 2 * Math.PI);

    private List<Data> iterationData;
    private int iterationIndex;
    private boolean parabolic;
    private XYChart.Series<Double, Double> leftBorder;
    private XYChart.Series<Double, Double> rightBorder;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        ChartPanManager panner = new ChartPanManager(lineGraph);
        //while pressing the left mouse button, you can drag to navigate
        panner.setMouseFilter(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                mouseEvent.consume();
            }
        });
        panner.start();

        //holding the right mouse button will draw a rectangle to zoom to desired location TODO not working
        JFXChartUtil.setupZooming(lineGraph, mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY)
                mouseEvent.consume();
        });

        plotGraph();
    }

    @FXML
    public void choseDihotomy(final MouseEvent mouseEvent) {
        updateMethod(new DichotomyMethod(function, eps));
    }

    @FXML
    public void choseGoldenRation(final MouseEvent mouseEvent) {
        updateMethod(new GoldenRatioMethod(function, eps));
    }

    @FXML
    public void choseFibonacci(final MouseEvent mouseEvent) {
        updateMethod(new FibonacciMethod(function, eps));
    }

    @FXML
    public void choseParabolic(final MouseEvent mouseEvent) {
        updateMethod(new ParabolicMethod(function, eps));
        parabolic = true;
    }

    @FXML
    public void choseBrent(final MouseEvent mouseEvent) {
        updateMethod(new BrentsMethod(function, eps));
        parabolic = true;
    }

    @FXML
    public void prev(final MouseEvent mouseEvent) {
        iterationIndex--;
        drawSegment();
    }

    @FXML
    public void next(final MouseEvent mouseEvent) {
        iterationIndex++;
        drawSegment();
    }

    private void plotGraph() {
        lineGraph.getData().clear();
        XYChart.Series<Double, Double> series = new XYChart.Series<>();

        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (double x = segment.getLeft(); x < segment.getRight(); x += (segment.getRight() - segment.getLeft()) / 1000) {
            double yValue = function.apply(x);
            addPoint(series, x, yValue);
            minY = Double.min(minY, yValue);
            maxY = Double.max(maxY, yValue);
        }

        double xShift = (segment.getRight() - segment.getLeft()) / 15;
        setupAxis(xAxis, segment.getLeft() - xShift, segment.getRight() + xShift);
        double yShift = (maxY - minY) / 10;
        setupAxis(yAxis, minY - yShift, maxY + yShift);


        lineGraph.getData().add(series);
        XYChart.Series<Double, Double> series1 = new XYChart.Series<>();
        lineGraph.getData().add(series1);
        leftBorder = new XYChart.Series<>();
        rightBorder = new XYChart.Series<>();
        lineGraph.getData().add(leftBorder);
        lineGraph.getData().add(rightBorder);
    }

    private void addPoint(final XYChart.Series<Double, Double> series, final double x, final double y) {
        series.getData().add(new XYChart.Data<>(x, y));
    }

    private void setupAxis(NumberAxis axis, double left, double right) {
        axis.setLowerBound(left);
        axis.setUpperBound(right);
        axis.setTickUnit((axis.getUpperBound() - axis.getLowerBound()) / 15);
        axis.setMinorTickVisible(true);
        axis.setMinorTickLength(axis.getTickUnit() / 10); // TODO not working
    }

    private void updateMethod(OptimizationMethod method) {
        method.minimize(segment.getLeft(), segment.getRight());
        iterationData = method.getIterationData();
        iterationIndex = 0;
        iterationCountText.setText(String.valueOf(iterationData.size()));
        drawSegment();
    }

    private void drawSegment() {
        prevButton.setVisible(iterationIndex - 1 >= 0);
        nextButton.setVisible(iterationIndex + 1 < iterationData.size());
        double l = iterationData.get(iterationIndex).getLeft();
        double r = iterationData.get(iterationIndex).getRight();

        currentIterationText.setText(String.valueOf(iterationIndex + 1));
        leftPositionText.setText(String.valueOf(l));
        rightPositionText.setText(String.valueOf(r));
        minXText.setText(String.valueOf(iterationData.get(iterationIndex).getMin()));
        minYText.setText(String.valueOf(function.apply(iterationData.get(iterationIndex).getMin())));


        leftBorder.getData().clear();
        rightBorder.getData().clear();
        addPoint(leftBorder, l, yAxis.getLowerBound());
        addPoint(leftBorder, l, yAxis.getUpperBound());
        addPoint(rightBorder, r, yAxis.getLowerBound());
        addPoint(rightBorder, r, yAxis.getUpperBound());
    }

}
