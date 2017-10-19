package Controller;

import Model.Connect;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.paint.Color;

import javafx.scene.chart.CategoryAxis;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

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
    boolean test = true;
    @FXML
    private Circle bouboule;
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
    private ArduinoController.MyRxTx arduino = new ArduinoController.MyRxTx();

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
            System.out.println(TextFieldDefine.getText());
            if(TextFieldDefine.getText().length() >=1 && TextFieldDefine.getText().length() <=2) {
                test = boule(temp, test);
                condensation(temp, tempout, hum);
            }
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
        try {
            int consigne = Integer.parseInt(TextFieldDefine.getText());
            if( consigne >= 0 && consigne <=35)
            {
                try {
                    arduino.output.write(consigne);
                    String info = "Consigne mise à jour à "+consigne;
                    javax.swing.JOptionPane.showMessageDialog(null, info);
                } catch (IOException ex) {

                    Logger.getLogger(
                            ArduinoController.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                String info = "Valeur invalide.\n Merci de rentrer une valeur comprise entre 0 et 35(°C). ";
                javax.swing.JOptionPane.showMessageDialog(null, info);
            }
        }
        catch (Exception e){
            String info = "Valeur invalide.\n Merci de rentrer une valeur comprise entre 0 et 35(°C). ";
            javax.swing.JOptionPane.showMessageDialog(null, info);
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

        Timeline tm = new Timeline((new KeyFrame(Duration.seconds(3), event -> {
            arduino.initialize();

            ArrayList<String> stat_array = aDB.getLastVal();
            System.out.println(stat_array);
            Float temp = Float.parseFloat(stat_array.get(0));
            Float hum = Float.parseFloat(stat_array.get(1));
            Float tempout = Float.parseFloat(stat_array.get(3));




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

    public boolean boule(Float temp, Boolean test) {       // Fonction pour toutes les alertes possibles et imagineables.

        try {
            if (Integer.parseInt(TextFieldDefine.getText()) <= temp) {
                test = true;
                bouboule.setFill(Color.RED);
                return test;
            }

            else if (Integer.parseInt(TextFieldDefine.getText()) >= temp) {
                if (test == true) {
                    String info = "Température de consigne atteinte !\n Extinction du Module Peltier.";
                    test = false;

                    javax.swing.JOptionPane.showMessageDialog(null, info);
                    bouboule.setFill(Color.GREEN);
                    return test;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return test;
    }

    public void condensation(float temp, float tempout, float hum)
    {

        double TempRose;
        double k = (17.27 * tempout)/(237.7 + tempout) + Math.log(hum/100);

        TempRose = (237.7 * k)/(17.27 - k);

        System.out.println(k);
        System.out.println(TempRose);

        if(temp < TempRose)
        {
            String info = "Attention risque de condensation, température interne inférieure au point de rosée";
            javax.swing.JOptionPane.showMessageDialog(null, info);
        }





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

    }

    private String getDate(String date){
        date = date.substring(11,date.length());
        date = date.substring(0,date.length()-2);
        return date;
    }

}

