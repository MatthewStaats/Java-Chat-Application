package chat.application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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


public class ChatClient extends Application {
    
   private DataOutputStream output;
   private DataInputStream input;
   
   TextArea inputMessage = new TextArea();
   TextField outputMessage = new TextField();
   Button sendBtn = new Button("Send");
   VBox vBox = new VBox();
    
  @Override
  
    public void start(Stage stage) {
        
        // connect to socket and link streams
        ServerConn();
        
        // gui improvements
        Style();
        
        // get vbox children
        VBox();
        
        // write textfield text to DataOutputStream to send to Server side
        sendBtn.setOnAction(e -> {
            Send();
        });
            
        
        Scene scene = new Scene(vBox, 450, 250);
        stage.setTitle("Chat Client Side");
        stage.setScene(scene);
        stage.show();
        
    }  
    
    public void ServerConn() {
        try {
            Socket socket = new Socket("localhost", 7777);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            System.out.println("Client is now Connected");
                
            new Thread(() -> {
                try {
                    while(true) {
                        String incoming = input.readUTF();
                        Platform.runLater(() -> {
                            inputMessage.appendText("Server: " + incoming + "\n");
                        });
                    }
                } catch (IOException e) {
                        System.out.println(e);
                  }
            }).start();
            
            } catch (IOException ex) {
                System.out.println(ex);
            }
    }
    
    public void Send() {
        try {
            String message = outputMessage.getText();
            output.writeUTF(message);
            output.flush();
            outputMessage.setText("");
            inputMessage.appendText("Client: " + message + "\n");
            
            } catch (IOException ex) {
                  System.out.println(ex);
              }
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