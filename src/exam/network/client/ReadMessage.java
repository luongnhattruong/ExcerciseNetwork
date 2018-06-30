package exam.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by john on 6/30/18.
 */
public class ReadMessage extends Thread{

    private BufferedReader reader;
    private Socket socket;
    private boolean stop = false;
    public ReadMessage(Socket socket) {
        this.socket = socket;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public void Stop(){
        this.stop = true;
    }
    public void run() {
        while (!stop) {
            try {
                String response = reader.readLine();
                if(response != null){
                    System.out.println("\n" + response);
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
        try {
            this.reader.close();
            System.out.println("Close reading from server");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.socket.close();
            System.out.println("Close connection to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
