package org.simply.connected.application.optimization.methods.front.multivariate;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;
import org.simply.connected.application.optimization.methods.multivariate.ConjugateGradientMethod;
import org.simply.connected.application.optimization.methods.multivariate.GradientDescentMethod;
import org.simply.connected.application.optimization.methods.multivariate.MultivariateOptimizationMethod;
import org.simply.connected.application.optimization.methods.multivariate.SteepestDescentMethod;
import org.simply.connected.application.optimization.methods.multivariate.math.BaseMatrix;
import org.simply.connected.application.optimization.methods.multivariate.math.DiagonalMatrix;
import org.simply.connected.application.optimization.methods.multivariate.math.QuadraticFunction;
import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.multivariate.model.MultivariateData;

import static org.simply.connected.application.optimization.methods.multivariate.math.Math.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller implements Initializable {

    @FXML
    public LineChart<Number, Number> lineGraph;
    @FXML
    public Button prevButton;
    @FXML
    public Button nextButton;
    @FXML
    public Text iterationCountText;
    @FXML
    public Text currentIterationText;
    @FXML
    public Text minXText;
    @FXML
    public Text minYText;
    @FXML
    public Button gradientButton;
    @FXML
    public Button steepestButton;
    @FXML
    public Button conjugateButton;
    @FXML
    public ComboBox<String> functionBox;
    @FXML
    public ComboBox<Integer> epsBox;
    @FXML
    public TextField initialXText;
    @FXML
    public TextField initialYText;
    @FXML
    public ToggleButton levelLinesButton;
    @FXML
    public NumberAxis xAxis;
    @FXML
    public NumberAxis yAxis;


    private static final String DEFAULT_BUTTON_BACKGROUND = "derive(#353434,20%)";
    private static final String SELECTED_BUTTON_BACKGROUND = "gray";
    private static final String STEP_COLOR = "-fx-stroke: #f1f10f";
    private static final String LEVEL_COLOR = "-fx-stroke: #58BDFF8E";
    private static final List<Integer> epsilons = Stream.iterate(1, it -> it + 1)
            .limit(6).collect(Collectors.toList());
    private static final List<QuadraticFunction> functions = List.of(
            new QuadraticFunction(
                    new BaseMatrix(
                            new Vector(128d, 126),
                            new Vector(126d, 128)
                    ),
                    new Vector(-10d, 30),
                    13
            ),
            new QuadraticFunction(
                    new DiagonalMatrix(1d, 10d),
                    new Vector(5d, 15d),
                    2
            ),
            new QuadraticFunction (
                    new DiagonalMatrix(4d, 4d),
                    new Vector(-4d, -4d),
                    1000
            )
    );
    private static final List<Vector> centers = List.of(
            new Vector(1265d/127, -1275d/127 ),
            new Vector(-5d, -1.5),
            new Vector(1d, 1)
    );
    private static final List<String> funAsString = List.of(
            "f = 64x^2 + 126xy + 64y^2 - 10x + 30y + 13",
            "f = 0.5x^2 + 5y^2 + 5x + 15y + 2",
            "f = 2x^2 + 2y^2 - 4x - 4y + 1000"
    );



    private final List<XYChart.Series<Number, Number>> levelLines = new ArrayList<>();
    private final ArrayList<XYChart.Series<Number, Number>> stepSeries = new ArrayList<>();

    private List<MultivariateData> iterationData;
    private int iterationIndex;

    private QuadraticFunction function;
    private Vector center;
    private double methodEps;
    private double initX = 0;
    private double initY = 0;

    private boolean levelLinesVisible = true;
    private List<Button> methodButtons;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        final ChartPanManager panner = new ChartPanManager(lineGraph);
        //while pressing the left mouse button, you can drag to navigate
        panner.setMouseFilter(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                mouseEvent.consume();
            }
        });
        panner.start();

        //holding the right mouse button will draw a rectangle to zoom to desired location
        JFXChartUtil.setupZooming(lineGraph, mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY)
                mouseEvent.consume();
        });

        methodButtons = List.of(
                gradientButton,
                steepestButton,
                conjugateButton
        );

        initialYText.setText(String.valueOf(initX));
        initialXText.setText(String.valueOf(initY));


        functionBox.getItems().addAll(funAsString);
        functionBox.setValue(funAsString.get(1));
        chooseFunction();


        epsBox.getItems().addAll(epsilons);
        epsBox.setValue(4);
        chooseEps();
    }

    @FXML
    public void choseGradient() {
        changeMethodFocus(gradientButton);
        updateMethod(new GradientDescentMethod(function, methodEps));
    }

    @FXML
    public void choseSteepestButton() {
        changeMethodFocus(steepestButton);
        updateMethod(new SteepestDescentMethod(function, methodEps));
    }

    @FXML
    public void choseConjugate() {
        changeMethodFocus(conjugateButton);
        updateMethod(new ConjugateGradientMethod(function, methodEps));
    }

    @FXML
    public void prev() {
        stepSeries.remove(iterationIndex);
        iterationIndex--;
        redraw();
    }

    @FXML
    public void next() {
        iterationIndex++;
        addStep();
        redraw();
    }

    @FXML
    public void toggleLevelLines() {
        levelLinesVisible ^= true;
        setLevelLinesVisible();
    }

    private void setLevelLinesVisible() {
        levelLines.forEach(it -> it.getNode().setVisible(levelLinesVisible));
    }

    @FXML
    public void chooseFunction() {
        String str = functionBox.getValue();
        int i = funAsString.indexOf(str);
        function = functions.get(i);
        center = centers.get(i);
        clearAll();
        plotGraph();
    }

    @FXML
    public void chooseEps() {
        int epsPower = epsBox.getValue();
        methodEps = Math.pow(10, -epsPower);
        clearAll();
        plotGraph();
    }

    @FXML
    public void enterInitX() {
        if (!initialXText.getText().matches("-?\\d+.?\\d*")) {
            initialXText.setText("");
            return;
        }
        initX = Double.parseDouble(initialXText.getText());
        clearAll();
        plotGraph();
    }

    @FXML
    public void enterInitY() {
        if (!initialYText.getText().matches("-?\\d+.?\\d*")) {
            initialYText.setText("");
            return;
        }
        initY = Double.parseDouble(initialYText.getText());
        clearAll();
        plotGraph();
    }

    private void plotGraph() {
        levelLines.clear();
        double shift = norm(sum(center, negate(new Vector(initX, initY))));

        double minX = center.get(0);
        double maxX = center.get(0);
        double minY = center.get(1);
        double maxY = center.get(1);

        for (double rad = 0.1; rad < shift; rad += shift / 20) {
            double h = function.apply(sum(center, new Vector(0d, rad)));
            double gradesPerStep = 0.5;
            double angle = 0;
            XYChart.Series<Number, Number> level = new XYChart.Series<>();
            for (int i = 0; i <= 360 / gradesPerStep; i++) {
                Vector normal = new Vector(Math.cos(angle), Math.sin(angle));
                double a = 1 / 2d * dotProduct(product(function.getA(), normal), normal);
                double b = dotProduct(function.getB(), normal) + dotProduct(product(function.getA(), center), normal);
                double c = function.getC() - h
                        + dotProduct(center, function.getB())
                        + 1 / 2d * dotProduct(center, product(function.getA(), center));
                double r = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
                Vector newPoint = sum(product(r, normal), center);
                addPoint(level, newPoint.get(0), newPoint.get(1));
                angle += Math.PI * 2 / 360 * gradesPerStep;

                minX = Math.min(minX, newPoint.get(0));
                maxX = Math.max(maxX, newPoint.get(0));
                minY = Math.min(minY, newPoint.get(1));
                maxY = Math.max(maxY, newPoint.get(1));
            }
            levelLines.add(level);
        }
        redrawLevelLines();
        double rad = Math.max(maxX - minX, maxY - minY) / 2;
        rad *= 1.1;
        double centerX = (maxX + minX) / 2;
        double centerY = (maxY + minY) / 2;
        setupAxis(xAxis, centerX - rad, centerX + rad);
        setupAxis(yAxis, centerY - rad, centerY + rad);
    }

    private void setupAxis(NumberAxis axis, double left, double right) {
        axis.setLowerBound(left);
        axis.setUpperBound(right);
        axis.setTickUnit((right - left) / 22);
    }

    private void updateMethod(MultivariateOptimizationMethod method) {
        method.minimize(new Vector(initX, initY));
        clearStepButtons();
        iterationData = method.getIterationData();
        iterationIndex = 0;
        iterationCountText.setText(String.valueOf(iterationData.size() - 1));
        lineGraph.getData().clear();
        stepSeries.clear();
        addStep();
        redraw();
    }

    private void updateStepButtons() {
        prevButton.setVisible(iterationIndex - 1 >= 0);
        nextButton.setVisible(iterationIndex + 2 < iterationData.size());
        currentIterationText.setText(String.valueOf(iterationIndex + 1));
        MultivariateData currentData = iterationData.get(iterationIndex + 1);
        minXText.setText(currentData.getX().toString());
        minYText.setText(String.valueOf(function.apply(currentData.getX())));
    }

    private void clearStepButtons() {
        prevButton.setVisible(false);
        nextButton.setVisible(false);
        currentIterationText.setText("");
        minXText.setText("");
        minYText.setText("");
    }

    private void addStep() {
        XYChart.Series<Number, Number> step = new XYChart.Series<>();
        Vector x = iterationData.get(iterationIndex).getX();
        Vector y = iterationData.get(iterationIndex + 1).getX();

        addPoint(step, x.get(0), x.get(1));
        addPoint(step, y.get(0), y.get(1));

        stepSeries.add(step);
    }


    private void redraw() {
        updateStepButtons();

        lineGraph.getData().clear();

        redrawLevelLines();
        redrawSteps();
    }

    private void redrawLevelLines() {
        lineGraph.getData().addAll(levelLines);
        setLevelLinesVisible();
        levelLines.forEach(step -> step.getNode().setStyle(LEVEL_COLOR));
    }

    private void redrawSteps() {
        lineGraph.getData().addAll(stepSeries);
        stepSeries.forEach(step -> step.getNode().setStyle(STEP_COLOR));
    }

    private void clearAll() {
        levelLines.clear();
        stepSeries.clear();
        lineGraph.getData().clear();
        clearStepButtons();
        changeMethodFocus(null);
    }

    private void changeMethodFocus(Button selected) {
        methodButtons.forEach(it -> it.setStyle(String.format("-fx-background-color: %s", DEFAULT_BUTTON_BACKGROUND)));
        if (selected != null) {
            selected.setStyle(String.format("-fx-background-color: %s", SELECTED_BUTTON_BACKGROUND));
        }
    }

    private void addPoint(final XYChart.Series<Number, Number> series, final double x, final double y) {
        series.getData().add(new XYChart.Data<>(x, y));
    }

    private void addCircle(final XYChart.Series<Number, Number> series, final double x, final double y) {
        XYChart.Data<Number, Number> circleData = new XYChart.Data<>(x, y);
        circleData.setNode(new Circle(5, Color.RED));
        series.getData().add(circleData);
    }
}
