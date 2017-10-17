package Model;

import java.lang.reflect.Executable;
import java.sql.*;
import java.util.ArrayList;


public class Connect {

    private static Connection conn = null;


    private String url = "jdbc:mysql://localhost:3306/pmf";
    private String user = "root";
    private String passwd = "toor";

    private Connect(){
        try{
            if (conn == null){
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, passwd);
            }
        }catch(SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Méthode qui va nous retourner notre instance et la créer si elle n'existe pas
    public static Connection getInstance(){
        if(conn == null){
            new Connect();
            System.out.println("Connected");
        }


        return conn;
    }
}
