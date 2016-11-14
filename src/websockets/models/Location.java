/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websockets.models;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import websockets.MySqlConnection;
import websockets.RecordNotFoundException;
/**
 *
 * @author santiago
 */
public class Location {
    
    //objects
    private static PreparedStatement command;
    private static ResultSet result;
    private CallableStatement procedure;
    
    private int id;
    private double latitude;
    private double longitude;
    private String status;
    
    public Location(int id) throws RecordNotFoundException{
        try
        {  //query 
            String query = "SELECT `id`, `latitude`, `lenght`, `active`, `description` FROM `locations` WHERE `id` = ? ";
            //prepare statement
            command = MySqlConnection.getConnection().prepareStatement(query);
            //parameters
            command.setInt(1, id);
            //execute query
            result = command.executeQuery();
            //check if found data
            result.first();
            if (result.getRow() > 0) //found data
            {
                //read values, pass them to attributes
                this.id = result.getInt("id");
                this.latitude = result.getDouble("latitude");
                this.longitude = result.getDouble("lenght");
                this.status = result.getString("active");
            }
            else
                throw new RecordNotFoundException(this.getClass().getName(), String.valueOf(id));
        }
        catch(SQLException ex)
        {
            
        }
    }

    public Location(int id,double latitude, double longitude, String status) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitud() {
        return longitude;
    }

    public void setLongitud(double longitud) {
        this.longitude = longitud;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
