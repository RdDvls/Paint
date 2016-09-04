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
    public ConnectionHandler(Socket clientSocket){
        this.connection = clientSocket;
    }
    public void run() {
        try{
            handleIncomingConnection(connection);
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }

    private void handleIncomingConnection(Socket clientSocket) throws IOException{
        System.out.println("Connection established");
        System.out.println("clientSocket = " + clientSocket);
        System.out.println("Incoming connection from: " + clientSocket.getInetAddress().getHostAddress());
        BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter outputToClient = new PrintWriter(clientSocket.getOutputStream(), true);
        Main myMain = new Main();
        myMain.startSecondStage();
        Platform.runLater(new RunnableGC(gc, stroke));



//        String inputLine;
//        while ((inputLine = inputFromClient.readLine()) != null ){
//            System.out.println(" " + inputLine);
//            outputToClient.println("This is irrelevant");
//        }
    }
    public Stroke jsonRestore(String jsonTD) {
        JsonParser toDoItemParser = new JsonParser();
        Stroke item = toDoItemParser.parse(jsonTD, Stroke.class);

        return item;
    }
    public String jsonStringGeneratorGC(Stroke currentStroke) {
        System.out.println(currentStroke.strokeX);
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(currentStroke);
        return jsonString;
    }

}
