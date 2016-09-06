package sample;

import javafx.scene.canvas.GraphicsContext;
import jodd.json.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by RdDvls on 9/2/16.
 */
public class DisplayServer implements Runnable{
    GraphicsContext gc = null;
    public DisplayServer(GraphicsContext myGc) {
        this.gc = myGc;
    }

    @Override
    public void run() {
        startServer(gc);
    }

    public void startServer(GraphicsContext gc) {
    try {
        ServerSocket serverListener = new ServerSocket(8005);

        while(true) {
            Socket clientSocket = serverListener.accept();
            System.out.println("Incoming message from: " + clientSocket.getInetAddress().getHostAddress());
            ConnectionHandler handler = new ConnectionHandler(clientSocket,gc);
            Thread handlerThread = new Thread(handler);
            handlerThread.start();

        }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}