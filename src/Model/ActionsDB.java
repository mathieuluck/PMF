package Model;

import java.sql.*;
import java.util.ArrayList;
import Model.Connect;

public class ActionsDB {

    public static void main(String[] args) {

        resetDB();
    }

    public static void insert (float temp, float humidite){
        try{
            PreparedStatement posted = Connect.getInstance().prepareStatement("INSERT INTO donnees (temperature, humidity, time) VALUES ('"+temp+"', '"+humidite+"',now())");
            //System.out.println(posted);
            posted.executeUpdate();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void resetDB(){
        try {
            Statement st = Connect.getInstance().createStatement();
            st.executeUpdate("DELETE FROM Donnees");
            System.out.println("Stats reset");
        }catch (SQLException e){
            e.printStackTrace();
        }


    }


    public static ArrayList<String> getLastVal(){
        try{
            PreparedStatement st = Connect.getInstance().prepareStatement("SELECT temperature, humidity, time FROM donnees ORDER BY id DESC LIMIT 1");
            ResultSet result = st.executeQuery();

            ArrayList<String> array = new ArrayList<String>();

            while(result.next()){
                //System.out.println(result.getString("temperature"));
                array.add(result.getString("temperature"));
                array.add(result.getString("humidity"));
                array.add(result.getString("time"));
            }

            return array;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getAllVal(){
        try{
            PreparedStatement st = Connect.getInstance().prepareStatement("SELECT temperature, humidity, time FROM donnees ORDER BY id");
            ResultSet result = st.executeQuery();

            ArrayList<String> array = new ArrayList<String>();

            while(result.next()){
                //System.out.println(result.getString("temperature"));
                array.add(result.getString("temperature"));
                array.add(result.getString("humidity"));
                array.add(result.getString("time"));
            }

            return array;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
