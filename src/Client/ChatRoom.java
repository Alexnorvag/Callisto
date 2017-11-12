package Client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatRoom {
    public static Thread thread;
    public TextArea taWorkPls;
    public TextField tfWorkPls;
    public JFXTextArea taAllMessage;
    public JFXTextField tfMyMessage;
    //public JFXTextArea taAllMessage;
    Socket sock;
    DataOutputStream dos;
    DataInputStream dis;
    
    @FXML private MaterialDesignIconView btnCloseChat;
    @FXML private JFXButton btnInbox, btnSent, btnSend;
    @FXML private AnchorPane anMessage, anSent;

    public ChatRoom() {
        try {
            sock = new Socket("localhost", 8080);
            dos = new DataOutputStream(sock.getOutputStream());
            dis = new DataInputStream(sock.getInputStream());

            dos.writeUTF(data.name);
            /*
            * This Thread let the client recieve the message from the server for any time;
            */
            thread = new Thread(() -> {
                try {

                    JSONParser parser = new JSONParser();

                    while(true) {
                        String newMsgJson = dis.readUTF();

                        System.out.println("RE : " + newMsgJson);
                        Message newMsg = new Message();

                        Object obj = parser.parse(newMsgJson);
                        JSONObject msg = (JSONObject) obj;

                        newMsg.setName((String) msg.get("name"));
                        newMsg.setMessage((String) msg.get("message"));

                        taAllMessage.appendText(newMsg.getName() + " : " + newMsg.getMessage() + "\n");
                    }
                } catch (Exception E) {
                    E.printStackTrace();
                }

            });

            thread.setDaemon(true);
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == btnCloseChat) {
            //ChatRoom.thread.stop();
            System.exit(0);
        }
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnInbox) {
            anMessage.toFront();
        } else if (actionEvent.getSource() == btnSent) {
            anSent.toFront();
        }
    }

    public void onClickSend() {
        try {
            String msg = tfMyMessage.getText();

            //String json = "{" + " 'name' : '" + data.name + "', 'message' : '" + msg + "'" + "}";

            JSONObject js = new JSONObject();
            js.put("name", data.name);
            js.put("message", msg);

            String json = js.toJSONString();


            System.out.println(json);


            dos.writeUTF(json);
            tfMyMessage.setText("");
            tfMyMessage.requestFocus();

        } catch(IOException E) {
            E.printStackTrace();
        }
    }


    public void buttonPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode().toString().equals("ENTER"))
        {
            onClickSend();
        }
    }

    /*@Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }*/
}
