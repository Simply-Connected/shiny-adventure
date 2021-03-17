package org.simply.connected.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.simply.connected.application.model.Segment;
import org.simply.connected.application.optimization.methods.*;

import java.rmi.MarshalException;
import java.util.function.Function;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    private static final Function<Double, Double> FUN =
            (x) -> -3 * x * Math.sin(3 * x / 4) + Math.exp(-2 * x);

    public static void main(String[] args) {
        //launch(args);
        SteppedOptimizationMethod method = new ParabolicMethod(FUN, 1e-5, new Segment(0d, 2 * Math.PI));
        System.out.println(method.getCurrSegment());
        while (method.minimize()) {
            System.out.println(method.getCurrSegment());
        }

    }
}
