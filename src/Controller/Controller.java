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
    private javafx.scene.chart.LineChart<?, ?> LineChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    ArduinoController arduino = new ArduinoController();


    public void stats(){
        XYChart.Series series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data("1", 23));
        series.getData().add(new XYChart.Data("2", 40));
        LineChart.getData().addAll(series);

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

stats();
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

}
