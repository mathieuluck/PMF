package Model;

import java.util.ArrayList;

public class Model extends ActionsDB {


    public void setAll(Float hum, Float temp, Float tempout){
        insert(temp, hum, tempout);
    }


    public Float getHum() {

        ArrayList<String> stat_array = getLastVal();

        Float humIn = Float.parseFloat(stat_array.get(0));

        return humIn;
    }


    public Float getTempIn() {

        ArrayList<String> stat_array = getLastVal();

        Float tempIn = Float.parseFloat(stat_array.get(1));

        return tempIn;
    }


    public Float getTempOut() {

        ArrayList<String> stat_array = getLastVal();

        Float tempOut = Float.parseFloat(stat_array.get(3));
        return tempOut;
    }

    public String getDate() {

        ArrayList<String> stat_array = getLastVal();

        String date = stat_array.get(2);

        return date;
    }

}
