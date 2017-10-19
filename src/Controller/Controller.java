package Controller;

import Model.Connect;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;

import javafx.scene.paint.Color;

import javafx.scene.chart.CategoryAxis;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import Model.ActionsDB;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller extends ArduinoController {

    @FXML
    private Circle bouboule;

    @FXML
    public MenuItem menutest;

    @FXML
    public MenuItem resetDB;

    @FXML
    public MenuItem closebtn;

    @FXML
    public Label LabelTemp;

    @FXML
    public TextField TextFieldTemp;

    @FXML
    public Label LabelTempOut;

    @FXML
    public TextField TextFieldTempOut;

    @FXML
    public TextField TextFieldHumidity;

    @FXML
    public TextField TextFieldDefine;

    @FXML
    private LineChart<?, ?> LineChartTemp;


    @FXML
    private LineChart<?, ?> LineChartHum;

    @FXML
    private CategoryAxis xTemp = new CategoryAxis();

    @FXML
    private NumberAxis yTemp  = new NumberAxis();

    @FXML
    private CategoryAxis xHum = new CategoryAxis();

    @FXML
    private NumberAxis yHum  = new NumberAxis();

    private ArduinoController arduino = new ArduinoController();


    public void initialize() {

        start();
        ActionsDB aDB = new ActionsDB();
        XYChart.Series st = stats();

        XYChart.Series st2 = stats2();

        XYChart.Series st3 = stats3();
        st.setName("Température IN");
        st2.setName("Taux d'humidité");
        st3.setName("Température OUT");
        Timeline tm = new Timeline((new KeyFrame(Duration.seconds(3), event -> {

                    ArrayList<String> stat_array = aDB.getLastVal();

                    Float temp = Float.parseFloat(stat_array.get(0));
                    Float tempout = Float.parseFloat(stat_array.get(3));
                    Float hum = Float.parseFloat(stat_array.get(1));
                    String time = stat_array.get(2);
                    updateTemp(st, temp, getDate(time));
                    updateTempOut(st3, tempout, getDate(time));
                    updateHum(st2, hum, getDate(time));
//                    sAlertMsg();

        })));
        tm.setCycleCount(Animation.INDEFINITE);
        tm.play();
        LineChartTemp.getData().addAll(st);
        LineChartHum.getData().addAll(st2);
        LineChartTemp.getData().addAll(st3);
    }

    public XYChart.Series stats(){
        XYChart.Series series = new XYChart.Series<>();
     return series;
    }

    public XYChart.Series stats2(){
        XYChart.Series series = new XYChart.Series<>();
        return series;
    }

    public XYChart.Series stats3(){
        XYChart.Series series = new XYChart.Series<>();
        return series;
    }

    public void recupConsigne(){
        int consigne = Integer.parseInt(TextFieldDefine.getText());
        try {
            myRxTx.output.write(consigne);
        } catch (IOException ex) {
            Logger.getLogger(
                    ArduinoController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public void updateTemp(XYChart.Series series, Float temp,  String time){
        try {
            series.getData().add(new XYChart.Data(time, temp));

            xTemp.setAnimated(false);
            yTemp.setLabel("Degré (°C)");
            xTemp.setLabel("Temps");


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void updateTempOut(XYChart.Series series,  Float tempout, String time){
        try {

            series.getData().add(new XYChart.Data(time, tempout));




        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public void updateHum(XYChart.Series series, Float hum, String time){
        try {
            //Float hum = Float.parseFloat(stat_array.get(1));
            series.getData().add(new XYChart.Data(time, hum));

            xHum.setAnimated(false);
            yHum.setLabel("% humidité");
            xHum.setLabel("Temps");




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


        ActionsDB aDB = new ActionsDB();

//        XYChart.Series st = stats();
//        XYChart.Series st2 = stats2();
        Timeline tm = new Timeline((new KeyFrame(Duration.seconds(3), event -> {
            //arduino.initialize();


            ArrayList<String> stat_array = aDB.getLastVal();
            System.out.println(stat_array);
            Float temp = Float.parseFloat(stat_array.get(0));
            Float hum = Float.parseFloat(stat_array.get(1));
            Float tempout = Float.parseFloat(stat_array.get(3));
            //Float humout = Float.parseFloat(stat_array.get(4));

//                si la température n'est pas null
            if(temp != null && hum != null){

                //afficher la température
                TextFieldTemp.setText(Float.toString(temp)+ "°C");
                TextFieldTempOut.setText(Float.toString(tempout)+ "°C");
                TextFieldHumidity.setText(Float.toString(hum)+ "%");
            }
        })));
        tm.setCycleCount(Animation.INDEFINITE);
        tm.play();

        }

//
//    public void AlertMsg() {       // Fonction pour toutes les alertes possibles et imagineables.
//        ActionsDB aDB = new ActionsDB();
//        ArrayList<String> stat_array = aDB.getLastVal();
//        Float temp = Float.parseFloat(stat_array.get(0));
//        Float hum = Float.parseFloat(stat_array.get(1));
//
//        if (Integer.parseInt(TextFieldDefine.getText()) <= Integer.parseInt(Float.toString(temp))) {
//            bouboule.setFill(Color.RED);
//        }
//     //   else if (Integer.parseInt(Float.toString(temp)) -= 5 <= Integer.parseInt(TextFieldDefine.getText()) &&  Integer.parseInt(Float.toString(temp)) += 5+ Integer.parseInt(TextFieldDefine.getText()))
//       // {
//
//        //}
//        else if (Integer.parseInt(TextFieldDefine.getText()) >= Integer.parseInt(Float.toString(temp))) {
//            bouboule.setFill(Color.RED);
//        }
//
//        if (temp <= 6 && hum >= 50) //AlerteCondensation
//        {
//
//            String info = "OLALA! Condensation arrive!!! \n" + Float.toString(temp) + " et " + Float.toString(hum);
//            javax.swing.JOptionPane.showMessageDialog(null, info);
//
//        } else if (TextFieldDefine.getText() != Float.toString(temp)) {
//            String info = "Température de consigne et mesurée trop différente. \n Vérifiez que rien n'obstrue le module de refroidissement ou que la porte du frigo est bien fermée.";
//            javax.swing.JOptionPane.showMessageDialog(null, info);
//        }
//    }


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
