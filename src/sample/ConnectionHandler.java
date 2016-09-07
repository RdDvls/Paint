package sample;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by RdDvls on 9/2/16.
 */
public class ConnectionHandler implements Runnable{
    private Stroke stroke = null;
    private GraphicsContext gc = null;
    Socket connection;

    public ConnectionHandler(Socket clientSocket, GraphicsContext gc){
        this.connection = clientSocket;
        this.gc = gc;
    }
    public void run() {
        try{
            handleIncomingConnection(connection,gc);
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }

    private void handleIncomingConnection(Socket clientSocket,GraphicsContext gc) throws IOException{
        System.out.println("Connection established");
        System.out.println("clientSocket = " + clientSocket);
        System.out.println("Incoming connection from: " + clientSocket.getInetAddress().getHostAddress());
        BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter outputToClient = new PrintWriter(clientSocket.getOutputStream(), true);
        String inputLine;
        while ((inputLine = inputFromClient.readLine()) != null ){
//            System.out.println(" " + inputLine);
            Stroke jsonStroke = jsonRestore(inputLine);
//            System.out.println(jsonStroke.strokeX);
            gc.strokeOval(jsonStroke.strokeX,jsonStroke.strokeY,jsonStroke.strokeSize, jsonStroke.strokeSize);
            outputToClient.println("Drawing");
        }
    }
    public Stroke jsonRestore(String jsonString) {
        JsonParser strokeParser = new JsonParser();
        Stroke stroke = strokeParser.parse(jsonString, Stroke.class);

        return stroke;
    }

}
