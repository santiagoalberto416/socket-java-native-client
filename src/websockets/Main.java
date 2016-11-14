//socket abre puerto y recibe información, existe otro que envía los datos.
//id objeto arduino, fecha y hora (timestamp) y medición
//Socket quedará corriendo, el socket recibirá datos, y la aplicación recibirá datos.
//SERVER

package websockets;
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONException;
import org.json.JSONObject;
import websockets.models.Location;
import websockets.utils.Constants;
import websockets.models.SensorActivity;
// esta es la clase que esta escuchando
public class Main {
    //objects
    static ServerSocket ss;
    static Socket s;
    static InputStreamReader isr;
    static BufferedReader br;
    static PrintStream ps;
    
    public static void main(String[] args){
        //thread
        
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
    
}
