package Controller;

import javafx.fxml.FXML;

import javafx.scene.chart.CategoryAxis;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import Model.ActionsDB;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Random;

public class Controller {

    @FXML
    public MenuItem resetDB;

    @FXML
    public MenuItem closebtn;

    @FXML
    public Label LabelTemp;

    @FXML
    public TextField TextFieldTemp;

    @FXML
    public TextField TextFieldHumidity;

    @FXML
    private LineChart<?, ?> LineChartTemp;


    @FXML
    private LineChart<?, ?> LineChartHum;

    @FXML
    private CategoryAxis xTemp;

    @FXML
    private NumberAxis yTemp;

    @FXML
    private CategoryAxis xHum;

    @FXML
    private NumberAxis yHum;

    ArduinoController arduino = new ArduinoController();


    public XYChart.Series stats(){
        XYChart.Series series = new XYChart.Series<>();

        return series;
    }

    public XYChart.Series stats2(){
        XYChart.Series series = new XYChart.Series<>();
        return series;
    }

    public void updateTemp(XYChart.Series series, Float temp, String time){
        try {
            series.getData().add(new XYChart.Data(time, temp));
            LineChartTemp.getData().add(series);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void updateHum(XYChart.Series series, Float hum, String time){
        try {
            //Float hum = Float.parseFloat(stat_array.get(1));
            series.getData().add(new XYChart.Data(time, hum));
            LineChartHum.getData().add(series);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void dropDB(){
        ActionsDB aDB = new ActionsDB();
        aDB.resetDB();
    }

    //exit
    public void leave(){
        System.exit(0);
    }

    public void start(){

        Timer timer = new Timer();
        ActionsDB aDB = new ActionsDB();

        XYChart.Series st = stats();
        XYChart.Series st2 = stats2();
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
//                int temp = random(1, 30);
                arduino.initialize();

                //value aléatoire test
//                float hum2 = (random(1, 30));
//                float temp2 = (random(1, 30));

//                aDB.insert(temp2, hum2);

                ArrayList<String> stat_array = aDB.getLastVal();
                Float temp = Float.parseFloat(stat_array.get(0));
                Float hum = Float.parseFloat(stat_array.get(1));
                String time = stat_array.get(2);
                updateTemp(st, temp, getDate(time));
                updateHum(st2, hum, getDate(time));

//                String time = stat_array.get(2);
//                series.getData().add(new XYChart.Data(time, hum));
//                LineChart.getData().addAll(series);



//                si la température n'est pas null
                if(temp != null && hum != null){

                    //afficher la température
                    TextFieldTemp.setText(Float.toString(temp)+ "°C");
                    TextFieldHumidity.setText(Float.toString(hum)+ "%");
//                    //stop the timer
//                    timer.cancel();
//                    timer.purge();
//                    javax.swing.JOptionPane.showMessageDialog(null,msg + "  température : " + temp);
                }

            }
        };

        timer.scheduleAtFixedRate(task, 0,3000);

    }


    private int random(int min, int max) {
        Random rand = new Random();
        int randomInt = rand.nextInt((max - min ) + 1 ) + min;

        return randomInt;
    }

    private String getDate(String date){
        date = date.substring(11,date.length());
        date = date.substring(0,date.length()-2);
        return date;
    }

}
