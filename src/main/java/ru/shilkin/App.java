package ru.shilkin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        File f = new File("C:\\Янтарь\\settingsdb.properties");
        if(f.exists() && !f.isDirectory()) {
            scene = new Scene(loadFXML("primary"), 1350, 667);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/roskazna.png")));
            stage.setTitle("UFK4800_OIS_AMBER_CHEKER_CERT");
            stage.setScene(scene);
//            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Platform.exit();
                    System.exit(0);
                }
            });
        }else{
            scene = new Scene(loadFXML("chekProperty"), 470, 520);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/roskazna.png")));
            stage.setTitle("System settings");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Platform.exit();
                    System.exit(0);
                }
            });
        }


    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}