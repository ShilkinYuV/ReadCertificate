package ru.shilkin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;

import javafx.fxml.FXMLLoader;

import static javafx.application.Application.launch;

public class ChekProperty {

    @FXML public TextField localhost;
    @FXML public TextField namedb;
    @FXML public TextField user;
    @FXML public PasswordField password;
    @FXML public TextField mailhost;
    @FXML public TextField mailport;
    @FXML public TextField mailtls;
    @FXML public TextField mail;
    @FXML public PasswordField mailpass;
    @FXML public Button save;
    private static Scene scene;


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void initialize(){
        save.setOnAction(actionEvent -> {
            if(localhost.getText() != "" && namedb.getText() != "" && user.getText() != "" && password.getText() != "" && mailhost.getText() != "" && mailport.getText() != "" && mailtls.getText() != "" && mail.getText() != "" && mailpass.getText() != ""){
                new File("C:\\Янтарь").mkdir();
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter("C:\\Янтарь\\settingsdb.properties", "UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                writer.println("localhost=" + localhost.getText());
                writer.println("namedb=" + namedb.getText());
                writer.println("user=" + user.getText());
                writer.println("password=" + password.getText());
                writer.println("mailhost=" + mailhost.getText());
                writer.println("mailport=" + mailport.getText());
                writer.println("mailtls=" + mailtls.getText());
                writer.println("mail=" + mail.getText());
                writer.println("mailpass=" + mailpass.getText());
                writer.close();
                PrimaryController primary = new PrimaryController();
                Window stageOld = save.getScene().getWindow();
                try {

                    Stage stage = new Stage();
                    scene = new Scene(loadFXML("primary"), 1350, 667);
                    stage.setScene(scene);
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/roskazna.png")));
                    stage.setTitle("UFK4800_OIS_AMBER_CHEKER_CERT");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stageOld.hide();
            }else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Внимание");
                alert.setContentText("Заполните все необходимые поля");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });
    }
}
