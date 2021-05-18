package ru.shilkin;

import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class OpenCertificate {
    Stage stage;
    List<FileInputStream> openC() throws FileNotFoundException {
        final FileChooser fileChooser = new FileChooser();
        List<FileInputStream> lsFis = new ArrayList<>();
        List<File> files = fileChooser.showOpenMultipleDialog(stage);
        for(File file : files){
            FileInputStream fis = new FileInputStream(file);
            lsFis.add(fis);

        }
        return lsFis ;
    }


}
