package ru.shilkin;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class DbConnect {
    Settings settings = new Settings();
    Connection dbConnection;
    Statement st;
    String alMess;
    static boolean checkWarning = false;

    //    PreparedStatement prst;
    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String connectionString = "jdbc:postgresql://localhost:5432/postgres";
        dbConnection = DriverManager.getConnection(connectionString,
                "postgres", "P@ssw0rd");
        return dbConnection;
    }

    public ResultSet selTofk(){
        ResultSet rs = null;
        try{
            st = getDbConnection().createStatement();
            rs = st.executeQuery("SELECT * FROM public.\"Tofks\";");
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet selEct(){
        ResultSet rs = null;
        try{
            st = getDbConnection().createStatement();
            rs = st.executeQuery("SELECT * FROM public.\"CertificateStore\";");
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet selEctCount(){
        ResultSet rs = null;
        try{
            st = getDbConnection().createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM public.\"CertificateStore\";");
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet selectCommit(String selectkey){
        ResultSet resSet = null;
        String select = "SELECT commentary FROM public.\"CertificateStore\" WHERE \"numberKey\" = ?;";
        try {
            PreparedStatement prST = getDbConnection().prepareStatement(select);
            prST.setString(1, selectkey);
            resSet = prST.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resSet;
    }

    public ResultSet paramSelect(String select){
        ResultSet res = null;
        try{
            PreparedStatement prSel = getDbConnection().prepareStatement(select);
            res = prSel.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return res;
    }

    public void insTable(DAOCertificate daoCertificate){
        String insert = "INSERT INTO public.\"CertificateStore\"(\n" +
                "\tfio, \"numberKey\", \"beforeDate\", \"afterDate\", \"commentary\")\n" +
                "\tVALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement prST = getDbConnection().prepareStatement(insert);
            prST.setString(1, daoCertificate.getFio());
            prST.setString(2, daoCertificate.getKeyNum());
            prST.setTimestamp(3, new Timestamp(daoCertificate.getBeforeDate().getTime()));
            prST.setTimestamp(4, new Timestamp(daoCertificate.getAfterDate().getTime()));
            prST.setString(5,daoCertificate.getCommentary());

            prST.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public String threadSelect(TextArea vivod, Boolean toMail, String sPeriodCert,
                               String mailC1, String mailC2, String mailP1, String mailP2, String fioC1, String fioC2, String fioP1, String fioP2, Alert alert ){
        ResultSet rs = null;
        String res = "";
        String messageShilkin = "";
        String messageGolokov = "";
        String messageBagninova = "";
        String messageLobanova = "";

        String mailShilkin = "";
        String mailGolokov = "";
        String mailBagninova = "";
        String mailLobanova = "";


        try{

            int numbDay = Integer.valueOf(sPeriodCert);
            String select = "SELECT fio, \"numberKey\", \"afterDate\" FROM public.\"CertificateStore\"  WHERE now()::date + (? * interval '1' DAY) >= \"afterDate\";";
            PreparedStatement prST = getDbConnection().prepareStatement(select);
            prST.setInt(1, numbDay);
            rs = prST.executeQuery();
//                    st = getDbConnection().createStatement();
//            rs = st.executeQuery("SELECT fio, \"numberKey\", \"afterDate\"\n" +
//                    "\tFROM public.\"CertificateStore\" WHERE now()::date + interval '30' DAY >= \"afterDate\"");
            vivod.clear();
            while(rs.next()){
                    String message = "Сертификат " + rs.getString(1) + " " + rs.getString(2) + " Истекает " + rs.getString(3) + " \n";
                    if(message.contains(fioC1)){
//                        System.out.println(message);
                        vivod.appendText(message);
                        messageShilkin = message;
                    }
                if(message.contains(fioP1)){
//                    System.out.println(message);
                    vivod.appendText(message);
                    messageGolokov = message;
                }
                if(message.contains(fioC2)){
//                    System.out.println(message);
                    vivod.appendText(message);
                    messageBagninova = message;
                }
                if(message.contains(fioP2)){
//                    System.out.println(message);
                    vivod.appendText(message);
                    messageLobanova = message;
                }

                mailShilkin = mailShilkin + messageShilkin;
                messageShilkin = "";
                mailGolokov = mailGolokov + messageGolokov;
                messageGolokov = "";
                mailBagninova = mailBagninova + messageBagninova;
                messageBagninova = "";
                mailLobanova = mailLobanova + messageLobanova;
                messageLobanova = "";
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        if(mailShilkin != ""|mailGolokov != ""|mailBagninova != ""|mailLobanova != ""){
            checkWarning = true;
            alMess = mailShilkin + "\n" + mailGolokov + "\n" + mailBagninova + "\n" + mailLobanova + "\n";
                    alert.setTitle("Внимание!!!");
        alert.setContentText(alMess);
        alert.setHeaderText("ИСТЕКАЮТ СЕРТИФИКАТЫ");
        }else {
            checkWarning = false;
        }




        if(mailShilkin != "" & toMail & !mailC1.equals("")){
            Sender sender = new Sender("4800amber@gmail.com", "gdychsajxuskakrx");
            sender.send("Уведомление Янтарь", mailShilkin, "4800amber@gmail.com" , mailC1);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(mailGolokov != "" & toMail & !mailP1.equals("")){
            Sender sender = new Sender("4800amber@gmail.com", "gdychsajxuskakrx");
            sender.send("Уведомление Янтарь", mailGolokov, "4800amber@gmail.com", mailP1);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(mailBagninova != "" & toMail & !mailC2.equals("")){
            Sender sender = new Sender("4800amber@gmail.com", "gdychsajxuskakrx");
            sender.send("Уведомление Янтарь", mailBagninova, "4800amber@gmail.com", mailC2);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(mailLobanova != "" & toMail & !mailP2.equals("")){
            Sender sender = new Sender("4800amber@gmail.com", "gdychsajxuskakrx");
            sender.send("Уведомление Янтарь", mailLobanova, "4800amber@gmail.com", mailP2);
        }


        return res;
    }

    public void delCert(String number){
        String insert = "DELETE FROM public.\"CertificateStore\" WHERE \"numberKey\" = ?";
        try {
            PreparedStatement prST = getDbConnection().prepareStatement(insert);
            prST.setString(1, number);
            prST.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void updateCommit(String commentary, String numberKey){
        String update = "UPDATE public.\"CertificateStore\" SET commentary= ? WHERE \"numberKey\"=?;";
        try {
            PreparedStatement prST = getDbConnection().prepareStatement(update);
            prST.setString(1, commentary);
            prST.setString(2, numberKey);
            prST.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }


   void closeCon(){
        try {
            dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void updAdmin(String fio, String mail, String role){
        String insert = "UPDATE public.\"AdminYantar\" SET fio=?, mail=? WHERE role=?;";
        try {
            PreparedStatement prST = getDbConnection().prepareStatement(insert);
            prST.setString(1, fio);
            prST.setString(2, mail);
            prST.setString(3, role);
            prST.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void insAdmin(String fio, String mail, String role){
        String insert = "INSERT INTO public.\"AdminYantar\"(fio, mail, role) VALUES (?, ?, ?);";
        try {
            PreparedStatement prST = getDbConnection().prepareStatement(insert);
            prST.setString(1, fio);
            prST.setString(2, mail);
            prST.setString(3, role);
            prST.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}