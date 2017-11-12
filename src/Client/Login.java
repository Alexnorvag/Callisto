package Client;

import Server.MySQLAccess;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable{
    public Label lblLoginNotif, lblRegNotif;
    private MySQLAccess dataBase = new MySQLAccess();
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private JFXButton btnSignin, btnSignup, btnRegistration, btnLogin;
    @FXML private AnchorPane pnlSignin, pnlSignup;
    @FXML private MaterialDesignIconView btnClose;
    @FXML private JFXTextField tfLoginCountry, tfLoginPhone, tfUName, tfRegCountry, tfRegPhone;

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == btnSignin) {
            pnlSignin.toFront();
        } else if (actionEvent.getSource() == btnSignup) {
                pnlSignup.toFront();
        } else if (actionEvent.getSource() == btnRegistration) {
            dataBase.insertToDB(tfUName.getText(), tfRegCountry.getText(), tfRegPhone.getText());
            data.name = dataBase.getUsername();
            data.country = dataBase.getCountry();
            data.phone = dataBase.getPhone();
        } else if (actionEvent.getSource() == btnLogin) {
            dataBase.selectFromDB(tfLoginCountry.getText(), tfLoginPhone.getText());
            data.name = dataBase.getUsername();
            data.country = dataBase.getCountry();
            data.phone = dataBase.getPhone();
            createRoom(actionEvent);
        }
    }

    @FXML
    public void handleMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == btnClose) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }

    public void createRoom(ActionEvent actionEvent) throws IOException {

        Parent home_page_parent = FXMLLoader.load(getClass().getResource("chatroom.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        app_stage.hide();
        app_stage.setScene(home_page_scene);
        app_stage.show();

        //moving frame
        home_page_parent.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        home_page_parent.setOnMouseDragged(event -> {
            app_stage.setX(event.getScreenX() - xOffset);
            app_stage.setY(event.getScreenY() - yOffset);
        });
    }
}
