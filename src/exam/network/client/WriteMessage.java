package exam.network.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by john on 6/30/18.
 */


public class WriteMessage extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private SocketClient client;
    public WriteMessage(Socket socket, SocketClient client) {
        this.socket = socket;
        this.client = client;
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));){
            String text;
            do {
                System.out.print("\nEnter your command: ");
                text = br.readLine();
                if(text != null){
                    writer.println(text);
                }

            } while (!text.equals("exit"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
            System.out.println("Close write to server");
            this.client.exit();
        }
    }
}
