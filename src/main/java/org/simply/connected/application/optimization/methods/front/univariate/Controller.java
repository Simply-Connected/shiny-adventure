package org.simply.connected.application.optimization.methods.front.univariate;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.simply.connected.application.optimization.methods.univariate.*;
import org.simply.connected.application.optimization.methods.univariate.model.BrentsData;
import org.simply.connected.application.optimization.methods.univariate.model.Data;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BinaryOperator;
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
    private static final Double eps = 1e-4;
    private static final UnaryOperator<Double> function = (x) -> -3 * x * Math.sin(3 * x / 4) + Math.exp(-2 * x);
    private static final Data segment = new Data(0, 0, 2 * Math.PI);
    private static final BinaryOperator<Double> DRAW_EPS = (a, b) -> Math.max((b - a) / 1000, 1e-8);
    private static final String DEFAULT_BUTTON_BACKGROUND = "derive(#353434,20%)";
    private static final String SELECTED_BUTTON_BACKGROUND = "gray";
    private final XYChart.Series<Double, Double> functionSeries = new XYChart.Series<>();
    private final XYChart.Series<Double, Double> parabolaSeries = new XYChart.Series<>();
    private final XYChart.Series<Double, Double> leftBorder = new XYChart.Series<>();
    private final XYChart.Series<Double, Double> rightBorder = new XYChart.Series<>();
    private final XYChart.Series<Double, Double> minPoint = new XYChart.Series<>();

    private Data currentSegment = new Data(0, 0, 2 * Math.PI);
    private List<Data> iterationData;
    private int iterationIndex;
    private boolean parabolic;

    @FXML
    public Button dichotomyButton;

    @FXML
    public Button goldenRationButton;

    @FXML
    public Button fibonacciButton;

    @FXML
    public Button parabolicButton;
    @FXML
    public Button brentButton;

    private List<Button> methodButtons;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        methodButtons = List.of(
                dichotomyButton,
                goldenRationButton,
                fibonacciButton,
                parabolicButton,
                brentButton
        );

        lineGraph.getData().add(parabolaSeries);
        lineGraph.getData().add(functionSeries);
        lineGraph.getData().add(leftBorder);
        lineGraph.getData().add(rightBorder);
        lineGraph.getData().add(minPoint);

        plotGraph();
    }

    @FXML
    public void choseDichotomy() {
        changeMethodFocus(dichotomyButton);
        parabolic = false;
        updateMethod(new DichotomyMethod(function, eps));
    }

    @FXML
    public void choseGoldenRation() {
        changeMethodFocus(goldenRationButton);
        parabolic = false;
        updateMethod(new GoldenRatioMethod(function, eps));
    }

    @FXML
    public void choseFibonacci() {
        changeMethodFocus(fibonacciButton);
        parabolic = false;
        updateMethod(new FibonacciMethod(function, eps));
    }

    @FXML
    public void choseParabolic() {
        changeMethodFocus(parabolicButton);
        parabolic = true;
        updateMethod(new ParabolicMethod(function, eps));
    }

    @FXML
    public void choseBrent() {
        changeMethodFocus(brentButton);
        parabolic = true;
        updateMethod(new BrentsMethod(function, eps));
    }

    @FXML
    public void prev() {
        iterationIndex--;
        drawSegment();
    }

    @FXML
    public void next() {
        iterationIndex++;
        drawSegment();
    }

    private void plotGraph() {
        lineGraph.getData().forEach(it -> it.getData().clear());

        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;


        double left = currentSegment.getLeft() - (currentSegment.getRight() - currentSegment.getLeft()) / 20;
        double right = currentSegment.getRight() + (currentSegment.getRight() - currentSegment.getLeft()) / 20;

        for (double x = left;
             x < right;
             x += DRAW_EPS.apply(left, right)) {
            double yValue = function.apply(x);
            addPoint(functionSeries, x, yValue);
            minY = Double.min(minY, yValue);
            maxY = Double.max(maxY, yValue);
        }

        double yShift = (maxY - minY) / 15;

        setupAxis(xAxis, left, right);
        setupAxis(yAxis, minY - yShift, maxY + yShift);

    }

    private void addPoint(final XYChart.Series<Double, Double> series, final double x, final double y) {
        series.getData().add(new XYChart.Data<>(x, y));
    }

    private void addCircle(final XYChart.Series<Double, Double> series, final double x, final double y) {
        XYChart.Data<Double, Double> circleData = new XYChart.Data<>(x, y);
        circleData.setNode(new Circle(5, Color.RED));
        series.getData().add(circleData);
    }

    private void setupAxis(NumberAxis axis, double left, double right) {
        axis.setLowerBound(left);
        axis.setUpperBound(right);
        axis.setTickUnit((right - left) / 22);
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
        Data currentData = iterationData.get(iterationIndex);

        currentSegment = currentData;

        plotGraph();

        double l = currentData.getLeft();
        double r = currentData.getRight();

        currentIterationText.setText(String.valueOf(iterationIndex + 1));
        leftPositionText.setText(String.valueOf(l));
        rightPositionText.setText(String.valueOf(r));
        minXText.setText(String.valueOf(currentData.getMin()));
        minYText.setText(String.valueOf(function.apply(currentData.getMin())));


        addPoint(leftBorder, l, yAxis.getLowerBound());
        addPoint(leftBorder, l, yAxis.getUpperBound());
        addPoint(rightBorder, r, yAxis.getLowerBound());
        addPoint(rightBorder, r, yAxis.getUpperBound());
        addCircle(minPoint, currentData.getMin(), function.apply(currentData.getMin()));


        addPoint(parabolaSeries, Long.MIN_VALUE, Long.MIN_VALUE);
        if (parabolic) {
            if (currentData instanceof BrentsData) {
                if (((BrentsData) currentData).isParabolicIteration()) {
                    drawParabola(currentData);
                }
            } else {
                drawParabola(currentData);
            }
        }
    }


    private void drawParabola(Data currentData) {
        parabolaSeries.getData().clear();
        UnaryOperator<Double> parabola = ParabolicMethod.getParabolaWithin(
                currentData.getLeft(), currentData.getMin(), currentData.getRight(), function
        );
        for (double x = currentSegment.getLeft();
             x < currentSegment.getRight();
             x += DRAW_EPS.apply(currentSegment.getLeft(), currentSegment.getRight())) {
            double yValue = parabola.apply(x);
            addPoint(parabolaSeries, x, yValue);
        }
    }

    private void changeMethodFocus(Button selected) {
        methodButtons.forEach(it -> it.setStyle(String.format("-fx-background-color: %s", DEFAULT_BUTTON_BACKGROUND)));
        selected.setStyle(String.format("-fx-background-color: %s", SELECTED_BUTTON_BACKGROUND));
    }
}
