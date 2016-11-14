package websockets;


public class RecordNotFoundException extends Exception {
    //attributes
    private String message;
    
    //getters and setters
    @Override
    public String getMessage(){
        return this.message;
    }
    public RecordNotFoundException(String className, String id)
    {
        String parts[] = className.split("\\.");
        this.message = parts[parts.length - 1] + " " + id + " Not Found";
    }
}
