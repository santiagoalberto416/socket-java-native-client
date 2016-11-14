/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websockets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author santiago
 */
public class Temperature {
    
    private int id;
    private double temperature;
    private static PreparedStatement command;
    private static ResultSet result;
    
    public Temperature(String data){
        try{
            JSONObject object = new JSONObject(data);
            temperature = object.getInt("temperatura");
            
        }catch(JSONException ex){
             System.out.println(ex);
        } 
    }
    
    public Boolean add(){
        if(temperature > 0){
        String query = "INSERT INTO `temperatura`(`medicion_temperatura`, humedad, `fecha`) VALUES (";
            query = query + this.temperature + ", ";
            query = query + "0.0" + ",";
            query = query + " NOW())";
            try{
            command = MySqlConnection.getConnection().prepareStatement(query);
            command.executeUpdate(); 
            
            }catch(SQLException ex){
                ex.printStackTrace();
            }
            return true;
        }else{
            return false;
        }
    }
    
    
}
