package ru.shilkin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.poi.ss.formula.functions.T;

public class PrimaryController {

    @FXML
    public TableView<DAOCertificate> table;
    @FXML public TextField fio;
    @FXML public TextField numberKey;
    @FXML public Button find;
    @FXML public ImageView sbros;
    @FXML public DatePicker beforeDate;
    @FXML public DatePicker afterDate;
    @FXML public MenuItem shilkin;
    @FXML public MenuItem golokov;
    @FXML public MenuItem lobanova;
    @FXML public MenuItem bagninova;
    @FXML public Menu load;
    @FXML public MenuItem del;
    @FXML public MenuItem runCheck;
    @FXML public MenuItem stopCheck;
    @FXML public TextArea vivod;
    @FXML public MenuItem mailTo;
    @FXML public ImageView mailIcon;
    @FXML public ImageView runIcon;
    @FXML public ImageView savee;
    @FXML public MenuItem editCom;
    @FXML public Button editOk;
    @FXML public TextField editPole;
    @FXML public MenuItem about;
    @FXML public MenuItem settings;
    @FXML public ProgressIndicator progress;
    @FXML public AnchorPane ancor;
    @FXML public Label countRow;
    @FXML public MenuItem beloshapko;
    @FXML public MenuItem dagaev;

    Settings settingss = new Settings();
    Alert alert = new Alert(Alert.AlertType.WARNING);
    Alert alertDubl = new Alert(Alert.AlertType.ERROR);



    DAOCertificate daoCertificate;
    DbConnect dbConnect = new DbConnect();
    OpenCertificate openCertificate = new OpenCertificate();
    ReadCertificate readCertificate = new ReadCertificate();
    TableViewZap tableViewZap = new TableViewZap();
    Filter filter = new Filter();
    Save save = new Save();
//    TableColumn<DAOCertificate,String> tId = new TableColumn<DAOCertificate,String>("№");
    TableColumn<DAOCertificate,String> tFio = new TableColumn<DAOCertificate,String>("Владелец");
    TableColumn<DAOCertificate,String> tNumberKey = new TableColumn<DAOCertificate,String>("Номер ключа");
    TableColumn<DAOCertificate,String> tBeforeDate = new TableColumn<DAOCertificate, String>("Действует с");
    TableColumn<DAOCertificate,String> tAfterDate = new TableColumn<DAOCertificate,String>("Действует по");
    TableColumn<DAOCertificate,String> tCommentary = new TableColumn<DAOCertificate,String>("ТОФК");
    Boolean toMail = false;
    Boolean checked = true;

    List<FileInputStream> lFisF = new ArrayList<>();
    String selectContentTable;
    String mailC1 = "";
    String mailC2 = "";
    String mailC3 = "";
    String mailP1 = "";
    String mailP2 = "";
    String mailP3 = "";
    String fioC1 = "";
    String fioC2 = "";
    String fioC3 = "";
    String fioP1 = "";
    String fioP2 = "";
    String fioP3 = "";


    @FXML
    public void initialize() {

        setControl1();
        setControl2();
        setControl3();
        setProcessing1();
        setProcessing2();
        setProcessing3();

        progress.setVisible(false);
        progress.setStyle(" -fx-progress-color: red;");
        table.setPlaceholder(new Label(""));

        settings.setOnAction(event -> {
            openNewScene("settings.fxml");
            setControl1();
            setControl2();
            setControl3();
            setProcessing1();
            setProcessing2();
            setProcessing3();
        });

        about.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFO");
            alert.setContentText("Болдаков АИ: Руководитель проекта \n" + "Кривошеина МА: Заместитель руководителя проекта \n" + "Шилкин ЮВ: КА Основной (Автор ПО) \n" + "Голоков ИК: ЗК Основной\n"  + "Лобанова ИМ: ЗК Резервный \n" + "Чулкин ДС: КА Резервный\n");
            alert.setHeaderText("team Amber OIS UFK MO:");
            Optional<ButtonType> option = alert.showAndWait();

        });

        editCom.setOnAction(actionEvent -> {

                    editPole.setVisible(true);
                    editOk.setVisible(true);
                    textSetField(dbConnect.selectCommit(selectContentTable));
            Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));

        });


        editOk.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Изменение комментария");
            alert.setContentText("Изменить комментарий для ключа " + selectContentTable + "?");
            alert.setHeaderText(null);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null || option.get() == ButtonType.CANCEL) {
                editPole.setVisible(false);
                editOk.setVisible(false);
            } else if (option.get() == ButtonType.OK) {
                table.getItems().clear();
                dbConnect.updateCommit(editPole.getText(), selectContentTable);
                editPole.setVisible(false);
                editOk.setVisible(false);
                tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
            }
        });

        savee.setOnMouseClicked(event -> {
           save.saveTable(table, savee);

        });

        mailTo.setOnAction(actionEvent -> {
            toMail = true;
            mailIcon.setVisible(true);
        });

        find.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        find.fire();
                    }
                }
        );

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
        // Converter
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {


            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        beforeDate.setConverter(converter);
        afterDate.setConverter(converter);
        beforeDate.setPromptText("гггг-мм-дд");
        afterDate.setPromptText("гггг-мм-дд");

        widthStTablr();

        runCheck.setOnAction(actionEvent -> {
            checked = true;
            new Thread(() -> {
                try {
                    cicle();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            runIcon.setVisible(true);
            if (settingss.sIntervalProv == null){
                settingss.sIntervalProv = "24";;
            }
            if (settingss.sPeriodCert == null){
                settingss.sPeriodCert = "30";
            }

        });

        stopCheck.setOnAction(actionEvent -> {
            checked = false;
            toMail = false;
            mailIcon.setVisible(false);
            runIcon.setVisible(false);
        });

        //Получаем значение номера заявки из TableView
        TableView.TableViewSelectionModel<DAOCertificate> selectionModel = table.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(new ChangeListener<DAOCertificate>(){

            public void changed(ObservableValue<? extends DAOCertificate> val, DAOCertificate oldVal, DAOCertificate newVal){
                if(newVal != null) selectContentTable = newVal.getKeyNum();
            }
        });


        del.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление записи");
            alert.setContentText("Удалить ключ " + selectContentTable + "?");
            alert.setHeaderText(null);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null || option.get() == ButtonType.CANCEL) {
            } else if (option.get() == ButtonType.OK) {
                dbConnect.delCert(selectContentTable);
                table.getItems().clear();
                tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
            }
        });

        shilkin.setOnAction(actionEvent -> {

//            try {
//                try {
//                    readCertificate.readC(openCertificate.openC(), shilkin);
//                } catch (CertificateException e) {
//                    e.printStackTrace();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            table.getItems().clear();
//            tableViewZap.zapTableView(table, dbConnect.selEct());


            try {
                lFisF = openCertificate.openC();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Thread threadRead = new Thread(() ->{
                progress.setVisible(true);
                try {
                    readCertificate.readC(lFisF, shilkin, alertDubl);
                    if (!readCertificate.bool){
                        Platform.runLater((() -> alertDubl.showAndWait()));
                    }
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(),dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                    progress.setVisible(false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            threadRead.start();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                }
            });
        });

        golokov.setOnAction(actionEvent -> {
//            try {
//                try {
//                    readCertificate.readC(openCertificate.openC(), golokov);
//                } catch (CertificateException e) {
//                    e.printStackTrace();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            table.getItems().clear();
//            tableViewZap.zapTableView(table, dbConnect.selEct());


            try {
                lFisF = openCertificate.openC();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Thread threadRead = new Thread(() ->{
                progress.setVisible(true);
                try {
                    readCertificate.readC(lFisF,golokov, alertDubl);
                    if (!readCertificate.bool){
                        Platform.runLater((() -> alertDubl.showAndWait()));
                    }
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                    progress.setVisible(false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            threadRead.start();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                }
            });
        });

        bagninova.setOnAction(actionEvent -> {
//                try {
//                    try {
//                        readCertificate.readC(openCertificate.openC(), bagninova);
//                    } catch (CertificateException e) {
//                        e.printStackTrace();
//                    } catch (SQLException throwables) {
//                        throwables.printStackTrace();
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

            try {
                lFisF = openCertificate.openC();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Thread threadRead = new Thread(() ->{
                progress.setVisible(true);
                try {
                    readCertificate.readC(lFisF,bagninova, alertDubl);
                    if (!readCertificate.bool){
                        Platform.runLater((() -> alertDubl.showAndWait()));
                    }
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));

                    progress.setVisible(false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            threadRead.start();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                }
            });

        });

        beloshapko.setOnAction(actionEvent -> {
//            try {
//                try {
//                    readCertificate.readC(openCertificate.openC(), lobanova);
//                } catch (CertificateException e) {
//                    e.printStackTrace();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            table.getItems().clear();
//            tableViewZap.zapTableView(table, dbConnect.selEct());


            try {
                lFisF = openCertificate.openC();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Thread threadRead = new Thread(() ->{
                progress.setVisible(true);
                try {
                    readCertificate.readC(lFisF,beloshapko, alertDubl);
                    if (!readCertificate.bool){
                        Platform.runLater((() -> alertDubl.showAndWait()));
                    }
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                    progress.setVisible(false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            threadRead.start();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                }
            });
        });

        lobanova.setOnAction(actionEvent -> {
//            try {
//                try {
//                    readCertificate.readC(openCertificate.openC(), lobanova);
//                } catch (CertificateException e) {
//                    e.printStackTrace();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            table.getItems().clear();
//            tableViewZap.zapTableView(table, dbConnect.selEct());


            try {
                lFisF = openCertificate.openC();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Thread threadRead = new Thread(() ->{
                progress.setVisible(true);
                try {
                    readCertificate.readC(lFisF,lobanova, alertDubl);
                    if (!readCertificate.bool){
                        Platform.runLater((() -> alertDubl.showAndWait()));
                    }
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                    progress.setVisible(false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            threadRead.start();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                }
            });
        });

        dagaev.setOnAction(actionEvent -> {
//            try {
//                try {
//                    readCertificate.readC(openCertificate.openC(), lobanova);
//                } catch (CertificateException e) {
//                    e.printStackTrace();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            table.getItems().clear();
//            tableViewZap.zapTableView(table, dbConnect.selEct());


            try {
                lFisF = openCertificate.openC();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Thread threadRead = new Thread(() ->{
                progress.setVisible(true);
                try {
                    readCertificate.readC(lFisF,dagaev, alertDubl);
                    if (!readCertificate.bool){
                        Platform.runLater((() -> alertDubl.showAndWait()));
                    }
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                    progress.setVisible(false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            threadRead.start();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    table.getItems().clear();
                    tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                    Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                }
            });
        });


        find.setOnAction(event -> {
            String variableFio = fio.getText().trim();
            String variableNumberKey = numberKey.getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
            String variableBeforeDate;
            String variableAfterDate;
            if(beforeDate.getValue()==null){
                variableBeforeDate = "";
            }else{
                variableBeforeDate = (beforeDate.getValue()).format(formatter);
            }

            if(afterDate.getValue()==null){
                variableAfterDate = "";
            }else{
                variableAfterDate = (afterDate.getValue()).format(formatter);
            }

            if(variableFio.equals("") & variableNumberKey.equals("") & variableBeforeDate.equals("") & variableAfterDate.equals("")){
                table.getItems().clear();
                DbConnect dbConnect = new DbConnect();
                tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
                Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
                for ( DAOCertificate daoCertificate : table.getItems() ) {
                }
            } else {
                table.getItems().clear();
                findUs(filter.paramList(
                        variableFio,
                        variableNumberKey,
                        variableBeforeDate,
                        variableAfterDate
                ));
            }
        });

        sbros.setOnMouseClicked(event -> {
            fio.setText("");
            numberKey.setText("");
            beforeDate.getEditor().clear();
            beforeDate.setValue(null);
            afterDate.getEditor().clear();
            afterDate.setValue(null);
        });
}

    private void findUs(String textp){

        DbConnect dbConnect = new DbConnect();
        DAOCertificate daoCertificate = new DAOCertificate();
        ResultSet slRes = dbConnect.paramSelect(textp);
        tableViewZap.zapTableView(table,slRes, dbConnect.selEctCount());
        Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
    }

    void cicle () throws InterruptedException {
        while(checked){
            dbConnect.threadSelect(vivod, toMail, settingss.sPeriodCert, mailC1, mailC2, mailC3, mailP1, mailP2, mailP3, fioC1, fioC2, fioC3, fioP1, fioP2, fioP3, alert);
            DbConnect dbWarninchek = new DbConnect();
            if(dbWarninchek.checkWarning){
                Platform.runLater((() -> alert.showAndWait()));
            }
            Thread.sleep(60000 * 60 * Integer.valueOf(settingss.sIntervalProv));
        }
    }

    void widthStTablr(){
//        tId.setCellValueFactory(new PropertyValueFactory<DAOCertificate, String>("id"));
//        tId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Number>(
//                table.getItems().indexOf(cellData.getValue()) + 1));
//        tId.setPrefWidth(45.0);
        tFio.setCellValueFactory(new PropertyValueFactory<DAOCertificate, String>("fio"));
        tFio.setSortType(TableColumn.SortType.ASCENDING);
        tFio.setPrefWidth(156.0);
        tNumberKey.setCellValueFactory(new PropertyValueFactory<DAOCertificate, String>("keyNum"));
        tNumberKey.setPrefWidth(185.0);
        tBeforeDate.setCellValueFactory(new PropertyValueFactory<DAOCertificate, String>("beforeDate"));
        tBeforeDate.setPrefWidth(170.0);
        tAfterDate.setCellValueFactory(new PropertyValueFactory<DAOCertificate, String>("afterDate"));
        tAfterDate.setPrefWidth(170.0);
        tCommentary.setCellValueFactory(new PropertyValueFactory<DAOCertificate, String>("commentary"));
        tCommentary.setSortType(TableColumn.SortType.ASCENDING);
        tCommentary.setPrefWidth(345);
        table.getColumns().addAll(tFio, tNumberKey, tBeforeDate, tAfterDate, tCommentary);
        tableViewZap.zapTableView(table, dbConnect.selEct(), dbConnect.selEctCount());
        Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
        table.getSortOrder().addAll(tCommentary, tFio);
    }

    public void textSetField(ResultSet rs){
        try {
            while (rs.next()) {
                editPole.setText(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openNewScene(String window){

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Настройки");
        stage.showAndWait();
    }

    void setControl1(){
        dbConnect = new DbConnect();
        ResultSet rsC1 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'control1';");
        Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
        try{
            while(rsC1.next()){
                shilkin.setText(rsC1.getString(2));
                mailC1 = rsC1.getString(3);
                fioC1 = rsC1.getString(2);
            }

        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();
    }

    void setControl2(){
        dbConnect = new DbConnect();
        ResultSet rsC2 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'control2';");
        Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
        try{
            while(rsC2.next()){
                bagninova.setText(rsC2.getString(2));
                mailC2 = rsC2.getString(3);
                fioC2 = rsC2.getString(2);
            }

        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();
    }

    void setControl3(){
        dbConnect = new DbConnect();
        ResultSet rsC3 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'control3';");
        Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
        try{
            while(rsC3.next()){
                beloshapko.setText(rsC3.getString(2));
                mailC3 = rsC3.getString(3);
                fioC3 = rsC3.getString(2);
            }

        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();
    }

    void setProcessing1(){
        dbConnect = new DbConnect();
        ResultSet rsP1 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'processing1';");
        Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
        try{
            while(rsP1.next()){
                golokov.setText(rsP1.getString(2));
                mailP1 = rsP1.getString(3);
                fioP1 = rsP1.getString(2);
            }

        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();
    }

    void setProcessing2(){
        dbConnect = new DbConnect();
        ResultSet rsP2 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'processing2';");
        Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
        try{
            while(rsP2.next()){
                lobanova.setText(rsP2.getString(2));
                mailP2 = rsP2.getString(3);
                fioP2 = rsP2.getString(2);
            }

        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();
    }

    void setProcessing3(){
        dbConnect = new DbConnect();
        ResultSet rsP3 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'processing3';");
        Platform.runLater((() -> countRow.setText("Количество строк: " + String.valueOf(table.getItems().size()))));
        try{
            while(rsP3.next()){
                dagaev.setText(rsP3.getString(2));
                mailP3 = rsP3.getString(3);
                fioP3 = rsP3.getString(2);
            }

        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();
    }


}
