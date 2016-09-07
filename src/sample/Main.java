package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main extends Application {
    final double DEFAULT_SCENE_WIDTH = 800;
    final double DEFAULT_SCENE_HEIGHT = 600;
    double strokeSize = 2;
    boolean isDrawing = false;
    double X;
    double Y;
    boolean isSharing = false;
    Stroke currentStroke;
    String tempJsonString;

    GraphicsContext gc = null;


    public void setStrokeSize(double strokeSize) {
        this.strokeSize = strokeSize;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");

        GridPane grid = new GridPane();   //step 1
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        grid.setGridLinesVisible(false);

        // add buttons and canvas to the grid
        Text sceneTitle = new Text("Welcome to Paint application");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0); // text, column, row

        Button anotherButton = new Button ("Start Server");
        Button aButton = new Button("Connect");
        Button button = new Button("Sample paint button");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.TOP_LEFT);
        hbButton.getChildren().add(button);
        hbButton.getChildren().add(anotherButton);
        hbButton.getChildren().add(aButton);
//        hbButton.getChildren().add(anotherButton);
        grid.add(hbButton, 0, 1);
//
        anotherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                startSecondStage();
                DisplayServer myServer = new DisplayServer(gc);
                Thread aThread = new Thread(myServer);
                aThread.start();


            }
        });

        aButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                startClient();
//                isSharing = true;
                    isDrawing = true;
            }
        });

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("I can switch to another scene here ...");
//                primaryStage.setScene(loginScene);
                startSecondStage();
            }
        });

        // add canvas
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT-100);

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLUE);
        gc.setStroke(Color.BLACK);

// gc.setStroke(Color.color(, Math.random(), Math.random()));
        gc.setLineWidth(5);

        grid.setOnKeyPressed(new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent keyEvent) {
                System.out.println("Key pressed was: " + keyEvent.getCode().getName());

                if(keyEvent.getCode().getName() == "D"){
                    isDrawing = !isDrawing;
                    System.out.println(isDrawing);
                }else if(keyEvent.getCode().getName() == "Up" && strokeSize <= 30 ){
                    strokeSize = strokeSize + 1;
                }else if(keyEvent.getCode().getName() == "Down" && strokeSize >= 2){
                    strokeSize = strokeSize -1;
                }else if (keyEvent.getCode().getName() == "C"){
                    gc.clearRect(0,0,DEFAULT_SCENE_WIDTH,DEFAULT_SCENE_HEIGHT);
                }else if (keyEvent.getCode().getName() == "R"){
                    gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
                }
                System.out.println(keyEvent.getCode());
                System.out.println(keyEvent.getText());
            }
        });

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                if (isDrawing) {
                    if(e.isDragDetect()) {
                        gc.strokeOval(e.getX(), e.getY(), strokeSize, strokeSize);
                        X = e.getX();
                        Y = e.getY();
                        currentStroke = new Stroke(e.getX(), e.getY(), strokeSize);
                        tempJsonString = jsonStringGeneratorStroke(currentStroke);
                        startClient();
                    }
                }
            }
        });
        grid.add(canvas, 0, 2);

        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);  //step 2
        primaryStage.setScene(defaultScene); // also step 2
        primaryStage.show();
    }

    public void startSecondStage() {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Welcome to JavaFX");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(false);

        Text sceneTitle = new Text("Welcome to Paint application");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0);

        Button button = new Button("Sample paint button");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.TOP_LEFT);
        hbButton.getChildren().add(button);
        grid.add(hbButton, 0, 1);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("I can switch to another scene here ...");
            }
        });


        // add canvas
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT-100);

        grid.add(canvas,0,2);

        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        secondaryStage.setScene(defaultScene);
        System.out.println("About to show the second stage");

        secondaryStage.show();
    }

    public void startClient() {
        try {
            Socket clientSocket = new Socket("localhost", 8005);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println(tempJsonString);
            String serverResponse = in.readLine();
            clientSocket.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void setDrawing(boolean drawing) {
        isDrawing = drawing;
    }


    public static void main(String[] args) {
        launch(args);
    }


    public String jsonStringGeneratorStroke (Stroke currentStroke) {
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(currentStroke);
        return jsonString;
    }


}
