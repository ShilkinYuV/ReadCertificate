package ru.shilkin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Settings {
    static String sPeriodCert = "30";
    static String sIntervalProv = "24";


    @FXML public Button ok;
    @FXML public TextField periodCert;
    @FXML public TextField intervalProv;
    @FXML public TextField con1;
    @FXML public TextField con2;
    @FXML public TextField proc1;
    @FXML public TextField proc2;
    @FXML public TextField mcon1;
    @FXML public TextField mcon2;
    @FXML public TextField mproc1;
    @FXML public TextField mproc2;
    @FXML public CheckBox checkEdit;
    @FXML public Button changeC1;
    @FXML public Button changeC2;
    @FXML public Button changeP1;
    @FXML public Button changeP2;
    @FXML public Button createcon1;
    @FXML public Button createcon2;
    @FXML public Button createproc1;
    @FXML public Button createproc2;
    @FXML public TextField con3;
    @FXML public TextField mcon3;
    @FXML public TextField proc3;
    @FXML public TextField mproc3;
    @FXML public Button changeC3;
    @FXML public Button changeP3;
    @FXML public Button createcon3;
    @FXML public Button createproc3;
    DbConnect dbConnect;
    public void initialize() {
        dbConnect = new DbConnect();
        ResultSet rsC1 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'control1';");
            try{
                if(!rsC1.isBeforeFirst()){
                    checkEdit.setVisible(false);
                    createcon1.setVisible(true);
                    con1.setEditable(true);
                    mcon1.setEditable(true);
                }else {
                    createcon1.setVisible(false);
                    con1.setEditable(false);
                    mcon1.setEditable(false);
                }
            }catch(SQLException throwables) {
                throwables.printStackTrace();
            }
        dbConnect.closeCon();

        dbConnect = new DbConnect();
        ResultSet rsC2 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'control2';");
        try{
            if(!rsC2.isBeforeFirst()){
                checkEdit.setVisible(false);
                createcon2.setVisible(true);
                con2.setEditable(true);
                mcon2.setEditable(true);
            }else {
                createcon2.setVisible(false);
                con2.setEditable(false);
                mcon2.setEditable(false);
            }
        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();

        dbConnect = new DbConnect();
        ResultSet rsC3 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'control3';");
        try{
            if(!rsC3.isBeforeFirst()){
                checkEdit.setVisible(false);
                createcon3.setVisible(true);
                con3.setEditable(true);
                mcon3.setEditable(true);
            }else {
                createcon3.setVisible(false);
                con3.setEditable(false);
                mcon3.setEditable(false);
            }
        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();

        dbConnect = new DbConnect();
        ResultSet rsP1 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'processing1';");
        try{
            if(!rsP1.isBeforeFirst()){
                checkEdit.setVisible(false);
                createproc1.setVisible(true);
                proc1.setEditable(true);
                mproc1.setEditable(true);
            }else {
                createproc1.setVisible(false);
                proc1.setEditable(false);
                mproc1.setEditable(false);
            }
        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();

        dbConnect = new DbConnect();
        ResultSet rsP2 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'processing2';");
        try{
            if(!rsP2.isBeforeFirst()){
                checkEdit.setVisible(false);
                createproc2.setVisible(true);
                proc2.setEditable(true);
                mproc2.setEditable(true);
            }else {
                createproc2.setVisible(false);
                proc2.setEditable(false);
                mproc2.setEditable(false);
            }
        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();

        dbConnect = new DbConnect();
        ResultSet rsP3 = dbConnect.paramSelect("SELECT * FROM public.\"AdminYantar\" WHERE role = 'processing3';");
        try{
            if(!rsP3.isBeforeFirst()){
                checkEdit.setVisible(false);
                createproc3.setVisible(true);
                proc3.setEditable(true);
                mproc3.setEditable(true);
            }else {
                createproc3.setVisible(false);
                proc3.setEditable(false);
                mproc3.setEditable(false);
            }
        }catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        dbConnect.closeCon();


        periodCert.setText(sPeriodCert);
        intervalProv.setText(sIntervalProv);
        ok.setOnAction(actionEvent -> {
            sPeriodCert =periodCert.getText();
            sIntervalProv = intervalProv.getText();
            ok.getScene().getWindow().hide();
        });

        try {
            setField();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        checkEdit.setOnAction(actionEvent -> {
            if(checkEdit.isSelected()){
                con1.setEditable(true);
                con2.setEditable(true);
                con3.setEditable(true);
                proc1.setEditable(true);
                proc2.setEditable(true);
                proc3.setEditable(true);
                mcon1.setEditable(true);
                mcon2.setEditable(true);
                mcon3.setEditable(true);
                mproc1.setEditable(true);
                mproc2.setEditable(true);
                mproc3.setEditable(true);
                changeC1.setVisible(true);
                changeC2.setVisible(true);
                changeC3.setVisible(true);
                changeP1.setVisible(true);
                changeP2.setVisible(true);
                changeP3.setVisible(true);
            }else {
                con1.setEditable(false);
                con2.setEditable(false);
                con3.setEditable(false);
                proc1.setEditable(false);
                proc2.setEditable(false);
                proc3.setEditable(false);
                mcon1.setEditable(false);
                mcon2.setEditable(false);
                mcon3.setEditable(false);
                mproc1.setEditable(false);
                mproc2.setEditable(false);
                mproc3.setEditable(false);
                changeC1.setVisible(false);
                changeC2.setVisible(false);
                changeC3.setVisible(false);
                changeP1.setVisible(false);
                changeP2.setVisible(false);
                changeP3.setVisible(false);
            }
        });

        changeC1.setOnAction(actionEvent -> {
            dbConnect.updAdmin(con1.getText().trim(),mcon1.getText().trim(), "control1");
            changeC1.setVisible(false);
            con1.setEditable(false);
            mcon1.setEditable(false);
        });

        changeC2.setOnAction(actionEvent -> {
            dbConnect.updAdmin(con2.getText().trim(),mcon2.getText().trim(), "control2");
            changeC2.setVisible(false);
            con2.setEditable(false);
            mcon3.setEditable(false);
        });

        changeC3.setOnAction(actionEvent -> {
            dbConnect.updAdmin(con3.getText().trim(),mcon3.getText().trim(), "control3");
            changeC3.setVisible(false);
            con3.setEditable(false);
            mcon3.setEditable(false);
        });

        changeP1.setOnAction(actionEvent -> {
            dbConnect.updAdmin(proc1.getText().trim(),mproc1.getText().trim(), "processing1");
            changeP1.setVisible(false);
            proc1.setEditable(false);
            mproc1.setEditable(false);
        });

        changeP2.setOnAction(actionEvent -> {
            dbConnect.updAdmin(proc2.getText().trim(),mproc2.getText().trim(), "processing2");
            changeP2.setVisible(false);
            proc2.setEditable(false);
            mproc2.setEditable(false);
        });

        changeP3.setOnAction(actionEvent -> {
            dbConnect.updAdmin(proc3.getText().trim(),mproc3.getText().trim(), "processing3");
            changeP3.setVisible(false);
            proc3.setEditable(false);
            mproc3.setEditable(false);
        });

        createcon1.setOnAction(actionEvent -> {
            dbConnect.insAdmin(con1.getText().trim(),mcon1.getText().trim(), "control1");
            createcon1.setVisible(false);
            con1.setEditable(false);
            mcon1.setEditable(false);
        });

        createcon2.setOnAction(actionEvent -> {
            dbConnect.insAdmin(con2.getText().trim(),mcon2.getText().trim(), "control2");
            createcon2.setVisible(false);
            con2.setEditable(false);
            mcon2.setEditable(false);
        });

        createcon3.setOnAction(actionEvent -> {
            dbConnect.insAdmin(con3.getText().trim(),mcon3.getText().trim(), "control3");
            createcon3.setVisible(false);
            con3.setEditable(false);
            mcon3.setEditable(false);
        });

        createproc1.setOnAction(actionEvent -> {
            dbConnect.insAdmin(proc1.getText().trim(),mproc1.getText().trim(), "processing1");
            createproc1.setVisible(false);
            proc1.setEditable(false);
            mproc1.setEditable(false);
        });

        createproc2.setOnAction(actionEvent -> {
            dbConnect.insAdmin(proc2.getText().trim(),mproc2.getText().trim(), "processing2");
            createproc2.setVisible(false);
            proc2.setEditable(false);
            mproc2.setEditable(false);
        });

        createproc3.setOnAction(actionEvent -> {
            dbConnect.insAdmin(proc3.getText().trim(),mproc3.getText().trim(), "processing3");
            createproc3.setVisible(false);
            proc3.setEditable(false);
            mproc3.setEditable(false);
        });
    }

    void setField() throws SQLException {
        dbConnect = new DbConnect();
        ResultSet rs = dbConnect.paramSelect("SELECT fio, mail, role FROM public.\"AdminYantar\";");
        while(rs.next()){
            if(rs.getString(3).equals("control1")){
                con1.setText(rs.getString(1));
                mcon1.setText(rs.getString(2));
            }
            if(rs.getString(3).equals("control2")){
                con2.setText(rs.getString(1));
                mcon2.setText(rs.getString(2));
            }
            if(rs.getString(3).equals("control3")){
                con3.setText(rs.getString(1));
                mcon3.setText(rs.getString(2));
            }

            if(rs.getString(3).equals("processing1")){
                proc1.setText(rs.getString(1));
                mproc1.setText(rs.getString(2));
            }
            if(rs.getString(3).equals("processing2")){
                proc2.setText(rs.getString(1));
                mproc2.setText(rs.getString(2));
            }
            if(rs.getString(3).equals("processing3")){
                proc3.setText(rs.getString(1));
                mproc3.setText(rs.getString(2));
            }
        }
        dbConnect.closeCon();
    }
}
