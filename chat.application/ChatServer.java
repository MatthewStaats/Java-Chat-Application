package chat.application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChatServer extends Application {
   
    private DataInputStream input;
    private DataOutputStream output;
    private ServerSocket ss;
    private Socket socket;
    
    TextField outputMessage = new TextField();
    TextArea inputMessage = new TextArea();
    Button sendBtn = new Button("Send");
    Button exitBtn = new Button("Exit");
    VBox vBox = new VBox();
     
    @Override
    public void start(Stage stage) {
        
        // start serversocket and link streams
        ServerBoot();
        
        // make gui chat window pretty
        Style();
       
        // get vbox children 
        VBox();
        
        // write to the DataOutputStream to send to Client side
        sendBtn.setOnAction(e -> {
             Send();  
        });
                   
        Scene scene = new Scene(vBox, 450, 250);
        stage.setTitle("Chat Server Side");
        stage.setScene(scene);
        stage.show();
        
    }
       
    // start serversocket and data streams
    public void ServerBoot() {
          new Thread( () -> {
            try {
                ss = new ServerSocket(7777);
                socket = ss.accept();
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                System.out.println("Server is now connected");
                    
                while(true) {
                    String string = input.readUTF();
                    output.flush();
                        
                    Platform.runLater(() -> {
                        inputMessage.appendText("Client: " + string + "\n");
                    });
                }
            } catch(IOException ex) {
                    System.out.println(ex);
                }
            }).start();
    }
    
    // method to dataoutput stream to client
    public void Send(){
          Platform.runLater(() -> {
                    try {
                        output.writeUTF(outputMessage.getText());
                        inputMessage.appendText("Server: " + outputMessage.getText() + "\n");
                        outputMessage.setText("");
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }); 
    }
    
    public void VBox() {
        vBox.getChildren().addAll(inputMessage, outputMessage, sendBtn);
        vBox.setStyle("-fx-border-color: black;");
        vBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    
    public void Style() {
        inputMessage.setStyle("-fx-font-size: 13;");
        outputMessage.setStyle("-fx-font-size: 13;");
        inputMessage.setEditable(false);
    }
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }  
}