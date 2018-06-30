package exam.network;

/**
 * Created by john on 6/29/18.
 */

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class SocketClient {
    private ReadMessage read;
    private WriteMessage write;
    private Socket socket;
    public SocketClient(String hostname, int port) {
        try {
            this.socket = new Socket(hostname, port);
            System.out.println("Connected to the server");
            this.read = new ReadMessage(socket);
            this.write = new WriteMessage(socket, this);
        }catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }
    public void execute() {
        this.read.start();
        this.write.start();
    }
    public void exit(){
        this.read.Stop();

    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
        InetAddress host = InetAddress.getLocalHost();
        SocketClient socketClient = new SocketClient(host.getHostName(), Contants.PORT);
        socketClient.execute();
    }
}
