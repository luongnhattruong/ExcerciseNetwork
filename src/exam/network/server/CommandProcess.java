package exam.network.server;

import exam.network.common.Contants;

import java.io.*;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Created by john on 6/29/18.
 */
public class CommandProcess extends Thread{
    private Socket socket;

    public CommandProcess(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                clientMessage= clientMessage.trim().replaceAll("\\s+", " ");
                System.out.println("Client msg:" + clientMessage);
                if(clientMessage == null){
                    try {
                        Thread.sleep(500);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else if(clientMessage.startsWith("index")) {
                    String []args = clientMessage.split(" ");
                    if(args.length == 1){
                        processIndex(writer, "");
                    }else if(args.length == 2){
                        processIndex(writer, args[1]);
                    }else{
                        writer.println("Illegal Argument");
                    }
                }
                else if(clientMessage.startsWith("get")) {
                    String []args = clientMessage.split(" ");
                    if(args.length == 2){
                        getFile(writer, args[1], "");
                    }else if(args.length == 3){
                        getFile(writer, args[1], args[2]);
                    }else{
                        writer.println("Illegal Argument");
                    }
                }
                else if(clientMessage.equals("exit")) {
                    System.out.println("Client exit");
                    break;
                }else{
                    writer.println("unknow command");
                    break;
                }

            } while (!clientMessage.equals("exit") && !this.socket.isClosed());

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Couldn't close a socket.");
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println("Couldn't close input stream.");
            }
            if(writer != null) {
                writer.close();
            }

        }
    }


    private void processIndex(PrintWriter out, String pathString){
        try{
            if(pathString == null || pathString.trim() == ""){
                pathString = Contants.DEFAULT_DIRECTORY;
            }
            Path path = Paths.get(pathString);

            try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, "*.txt")) {
                Iterator<Path> iterator = ds.iterator();
                if(!iterator.hasNext()){
                    out.println("No file was found.");
                    return;
                }
                while (iterator.hasNext()) {
                    Path p = iterator.next();
                    out.println(p.getFileName());
                }
            }catch (Exception ex){
                out.println("Error");
            }

        }catch (Exception ex){
            out.println("Invalid directory");
        }
    }

    private void getFile(PrintWriter out, String filename, String pathString){
        try{
            if(pathString == null || pathString.trim() == ""){
                pathString = Contants.DEFAULT_DIRECTORY;
            }
            Path path = Paths.get(pathString);

            try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, filename)) {
                Iterator<Path> iterator = ds.iterator();
                if(!iterator.hasNext()){
                    out.println("File not found.");
                    return;
                }
                Path p = iterator.next();
                out.println("Ok");
                readFile(out, p);

            }catch (Exception ex){
                out.println("Error");
            }

        }catch (Exception ex){
            out.println("Invalid directory");
        }
    }

    private void readFile(PrintWriter out, Path path){
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
