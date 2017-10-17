package Controller;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Model.ActionsDB;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Random;

public class Controller {

    @FXML
    public Button monBouton;

    @FXML
    public Label LabelTemp;

    @FXML
    public TextField TextFieldTemp;

    @FXML
    public TextField TextFieldHumidity;

    ArduinoController arduino = new ArduinoController();

    //exit
    public void leave(){
        System.exit(0);
    }

    public void start(){
        Timer timer = new Timer();
        ActionsDB aDB = new ActionsDB();

        TimerTask task = new TimerTask(){
            @Override
            public void run(){
//                int temp = random(1, 30);
                //arduino.initialize();

                //value aléatoire test
                float hum2 = (random(1, 30));
                float temp2 = (random(1, 30));

                aDB.insert(temp2, hum2);

                ArrayList<String> stat_array = aDB.get();
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
