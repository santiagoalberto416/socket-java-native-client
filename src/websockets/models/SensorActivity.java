/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websockets.models;
import java.sql.CallableStatement;
import java.sql.Date;
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
public class SensorActivity {
    
    private static PreparedStatement command;
    private static ResultSet result;
    private CallableStatement procedure;
    
    private int id;
    private Location location;
    private Date date;

    public SensorActivity(Location location) {
        this.location = location;
    }
    
    
    public void add(){
        if(location!=null){
            String query = "INSERT INTO `sensoractivity`(`id`, `time`, `id_arduino`) VALUES (null, NOW() ,?)";
            try{
            command = MySqlConnection.getConnection().prepareStatement(query);
            command.setInt(1, location.getId());
            command.executeUpdate();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }
    

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
    
    
}
