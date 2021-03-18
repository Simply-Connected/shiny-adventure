package org.simply.connected.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.Function;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/app.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    private static final Function<Double, Double> FUN =
            (x) -> -3 * x * Math.sin(3 * x / 4) + Math.exp(-2 * x);

    public static void main(String[] args) {
        launch(args);
//        SteppedOptimizationMethod method = new FibonacciMethod(FUN, 1e-5, new Segment(0d, 2 * Math.PI));
//        System.out.println(method.getCurrSegment());
//        while (method.minimize()) {
//            System.out.println(method.getCurrSegment());
//        }

    }
}
