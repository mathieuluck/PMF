package Controller;

import javafx.fxml.FXML;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Random;

public class GraphiqueController {


    @FXML
    private LineChart<?, ?> LineChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;


    public void stats(){
    XYChart.Series series = new XYChart.Series<>();
    series.getData().add(new XYChart.Data("1", 23));

LineChart.getData().addAll(series);

}


}
