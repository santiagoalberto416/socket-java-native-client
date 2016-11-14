/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websockets.webconnection;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import static websockets.Main.getLocationFromJSON;
import websockets.RecordNotFoundException;
import websockets.models.Location;
import websockets.models.SensorActivity;
import websockets.utils.Constants;
/**
 *
 * @author skirk
 */
public class SocketTest implements IOCallback {

    private static SocketIO socket;
    
    //objects
    static ServerSocket ss;
    static Socket s;
    static InputStreamReader isr;
    static BufferedReader br;
    static PrintStream ps;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
			new SocketTest();
                        activateArduinoListener();
                        System.out.print("si entra aqui");
		} catch (Exception e) {
			e.printStackTrace();
}
    }
    
     public static Location getLocationFromJSON(String data){
        try{
        JSONObject object = new JSONObject(data);
        int proximity = object.getInt("proximity");
        int idLocation = object.getInt("location");
        if(proximity > 0 && idLocation > 0){
            return new Location(idLocation);
        }else{
            return null;
        }
        }catch(JSONException ex){
            System.out.println(Constants.ARDUINO_MALFORMED_JSON);
            return null;
        }catch(RecordNotFoundException ex){
            System.out.println(Constants.ARDUINO_ID_NOT_FOUND);
            return null;
        }
    }
     
    private static void activateArduinoListener(){
        Thread socketThread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    System.out.println("Opening Socket....");//status
                    ss = new ServerSocket(8887);//socket port
                    System.out.println("Socket Opened....");
                    //keep reading
                    
                    while(true){
                        Calendar now = Calendar.getInstance();
                        String date = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
        
                        s = ss.accept();//read data
                        isr = new InputStreamReader(s.getInputStream());//read stream
                        br = new BufferedReader(isr);//read buffer
                        String data = br.readLine();//read data
                        //System.out.println(data + "\n");//show data
                        
                        //send acknowledge to client
                        if(data !=null){
                            if(!data.equals("null")){
                                Location location = getLocationFromJSON(data);
                                if(location!=null){
                                    System.out.println("***** location ******");
                                    System.out.print("latitude: "+ location.getLatitude());
                                    System.out.print(", longitud: "+location.getLongitud());
                                    System.out.print(", active: "+location.getStatus());
                                    System.out.println(date);
                                    SensorActivity sensor = new SensorActivity(location);
                                    sensor.add();
                                    
                                    if(socket!=null){
                                        socket.emit("removeAction", "{ \"locationid\":"+location.getId()+"}");
                                    }
                                    
                                }
                                ps = new PrintStream(s.getOutputStream());//stream writer
                                ps.println("Data Received...");
                            }
                        }
                    }
                }
                catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
            }
        });
        //start socket
        socketThread.start();
    }
    
    public SocketTest() throws Exception {
		socket = new SocketIO();
		socket.connect("http://localhost:8081/", this);
                /*
                aqui tengo que meter el ciclo del
                socket que esta escuchando
                */
		socket.emit("chat message", "hello from java");
                
	}

	@Override
	public void onMessage(JSONObject json, IOAcknowledge ack) {
		try {
                    System.out.println("Server said:" + json.toString(2));
		} catch (JSONException e) {
                    e.printStackTrace();
		}
	}

	@Override
	public void onMessage(String data, IOAcknowledge ack) {
		System.out.println("Server said: " + data);
	}

	@Override
	public void onError(SocketIOException socketIOException) {
		System.out.println("an Error occured");
		socketIOException.printStackTrace();
	}

	@Override
	public void onDisconnect() {
		System.out.println("Connection terminated.");
	}

	@Override
	public void onConnect() {
		System.out.println("Connection established");
	}

	@Override
	public void on(String event, IOAcknowledge ack, Object... args) {
		System.out.println("Server triggered event '" + event + "'");
        }
    
}
