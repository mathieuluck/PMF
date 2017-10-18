package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import Model.ActionsDB;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Model.Connect;


public class ArduinoController implements SerialPortEventListener {

    @FXML
    public Button monBouton;

    @FXML
    public Label monLabel;

    @FXML
    public TextField monTextField2;
    @FXML
    SerialPort serialPort;
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = {
            "COM4", // Windows
    };
    public String inputLine;





    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */
    private BufferedReader input;
    /** The output stream to the port */
    private OutputStream output;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;

    public void initialize() {
        // the next line is for Raspberry Pi and
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams


            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


    /**
     * This should be called when you stop using the port.
     * This will prevent port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    public synchronized void send(int consigne) {

        System.out.println(consigne);
        /*try {
            output.write(consigne);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                ActionsDB aDB = new ActionsDB();

                String inputLine=input.readLine();
//                System.out.println(inputLine);
                String[] parts = inputLine.split("\t");



                String humidity = parts[0];
                String temperature = parts[1];
                String humidityout = parts[2];
                String temperatureout = parts[3];

                float hum = Float.parseFloat(humidity);
                float temp = Float.parseFloat(temperature);
                float humout = Float.parseFloat(humidityout);
                float tempout = Float.parseFloat(temperatureout);



//                float hum = (random(1, 30));
//                float temp = (random(1, 30));

                aDB.insert(temp, hum, tempout,humout);
//                System.out.println("Humidité mesuré: " + humidity);
//                System.out.println("Température mesuré: " + temperature);


            } catch (Exception e) {
               System.err.println(e.toString());
            }
        }


        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    //test nb random

    private int random(int min, int max) {
        Random rand = new Random();
        int randomInt = rand.nextInt((max - min ) + 1 ) + min;

        return randomInt;
    }

}