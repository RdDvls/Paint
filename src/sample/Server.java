package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by RdDvls on 9/2/16.
 */
public class Server {

    public static void main(String[] args) {
    try {
        ServerSocket serverListener = new ServerSocket(8005);

        while(true) {
            Socket clientSocket = serverListener.accept();
            System.out.println("Incoming message from: " + clientSocket.getInetAddress().getHostAddress());
            ConnectionHandler handler = new ConnectionHandler(clientSocket);
            Thread handlerThread = new Thread(handler);
            handlerThread.start();

        }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }



}