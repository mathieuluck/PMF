package Controller;
import Model.ActionsDB;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

public class ArduinoController extends Application {

    MyRxTx myRxTx;
    Label textInfo, textIn;

    @Override
    public void start(Stage primaryStage) {
        myRxTx = new MyRxTx();
        myRxTx.initialize();
    }



    class MyRxTx implements SerialPortEventListener {

        SerialPort serialPort;
        /**
         * The port we're normally going to use.
         */
        private final String PORT_NAME = "COM4";
        private BufferedReader input;
        public OutputStream output;
        private static final int TIME_OUT = 2000;
        private static final int DATA_RATE = 9600;

        public void initialize() {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            //First, Find an instance of serial port as set in PORT_NAMES.
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId =
                        (CommPortIdentifier) portEnum.nextElement();

                if (currPortId.getName().equals(PORT_NAME)) {
                    portId = currPortId;
                    break;
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
                input = new BufferedReader(
                        new InputStreamReader(serialPort.getInputStream()));
                output = serialPort.getOutputStream();

                // add event listeners
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

        @Override
        public void serialEvent(SerialPortEvent spe) {
            if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    ActionsDB aDB = new ActionsDB();

                    String inputLine=input.readLine();
                    String[] parts = inputLine.split("\t");



                    String humidity = parts[0];
                    String temperature = parts[1];
                    String humidityout = parts[2];
                    String temperatureout = parts[3];

                    float hum = Float.parseFloat(humidity);
                    float temp = Float.parseFloat(temperature);
                    float humout = Float.parseFloat(humidityout);
                    float tempout = Float.parseFloat(temperatureout);

                    aDB.insert(temp, hum, tempout,humout);

                    System.out.println(inputLine);
                } catch (Exception e) {
                    System.err.println(e.toString());
                }
            }
        }

        /**
         * This should be called when you stop using the port. This will prevent
         * port locking on platforms like Linux.
         */
        public synchronized void close() {
            if (serialPort != null) {
                serialPort.removeEventListener();
                serialPort.close();
            }
        }
    }
}