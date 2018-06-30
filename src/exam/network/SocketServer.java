package exam.network;

/**
 * Created by john on 6/29/18.
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class SocketServer{

    private int port;
    public SocketServer(int port) {
        this.port = port;
    }
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {

            System.out.println("Chat Server is listening on port " + this.port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client socket connected");
                CommandProcess process = new CommandProcess(socket);
                process.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public static void main(String args[]) throws IOException{
        SocketServer server = new SocketServer(Contants.PORT);
        server.execute();
    }

}