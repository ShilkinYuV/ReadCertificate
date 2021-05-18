package ru.shilkin;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import org.cryptacular.util.CertUtil;


import java.io.*;

import java.security.cert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ReadCertificate {
    Boolean bool = true;
    DAOCertificate daoCertificate;


    DAOCertificate readC(List <FileInputStream> fis, MenuItem menuItem, Alert alert) throws FileNotFoundException, CertificateException, SQLException {
        CertificateFactory fac = CertificateFactory.getInstance("X509");;
        for(FileInputStream f : fis){
            Ufk ufk = new Ufk();
            X509Certificate cert = (X509Certificate) fac.generateCertificate(f);
            Date befDate = null;
            Date aftDate = null;
            String[] certIngo = String.valueOf(cert).split("\n");
            String key = "";
            String stringDate = "";
            String stringBeforeDate = "";
            String stringAfterDate = "";


//            System.out.println(cert);
//            System.out.println(CertUtil.subjectNames(cert));


            String comm = String.valueOf(CertUtil.subjectNames(cert));
            String comment = "";

            DbConnect sverka = new DbConnect();
            ResultSet resSverka;
            resSverka = sverka.selTofk();
            while(resSverka.next()){
                if(comm.contains(resSverka.getString(4))){
                    comment = resSverka.getString(2) + " " + resSverka.getString(3);
                }
            }
            sverka.closeCon();


            Boolean boolDuble = true;

            for (int n = 0; n < certIngo.length; n++){
                if(certIngo[n].contains("ObjectId: 1.3.6.1.4.1.10244.4.3 Criticality=false")){
                    key = certIngo[n+3];
                }
                if(certIngo[n].contains("PrivateKeyUsage")){
                    stringDate = certIngo[n+1];

                }
            }
            stringBeforeDate = stringDate.substring(10,34);
            String stringBeforeDate1 = stringBeforeDate.substring(0,3);
            String stringBeforeDate2 = stringBeforeDate.substring(4,6);
            String stringBeforeDate3 = stringBeforeDate.substring(20);
            String finBeforeDate =stringBeforeDate3 + "-" + stringBeforeDate1 + "-" + stringBeforeDate2;


                stringAfterDate = stringDate.substring(44,68);
            String stringAfterDate1 = stringAfterDate.substring(0,3);
            String stringAfterDate2 = stringAfterDate.substring(4,6);
            String stringAfterDate3 = stringAfterDate.substring(20);
            String finAfterDate =stringAfterDate3 + "-" + stringAfterDate1 + "-" + stringAfterDate2;




            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                befDate = dateFormat.parse(repStrinDate(finBeforeDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                aftDate = dateFormat.parse(repStrinDate(finAfterDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            DbConnect dbConnect = new DbConnect();
//                String key = certIngo[59];
                String result = key.substring(58);
            ResultSet rs = dbConnect.paramSelect("SELECT \"numberKey\" FROM public.\"CertificateStore\"");
                while(rs.next()){
                    if(result.equals(rs.getString(1))){
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ALARM");
                        alert.setContentText("Сертификат " + result + " уже загружен");
                        alert.setHeaderText("Найдены дубликаты");
//                        Optional<ButtonType> option = alert.showAndWait();
//                        Platform.runLater((() -> alert.showAndWait()));
                        boolDuble = false;
                    }
                }
                dbConnect.closeCon();

            if(boolDuble){
                daoCertificate = new DAOCertificate(menuItem.getText(), result, befDate, aftDate, comment);
                dbConnect.insTable(daoCertificate);
            }
            bool = boolDuble;
        }
        return  daoCertificate;
    }


     String repStrinDate(String d){
         if(d.contains("Jan")){
             d = d.replace("Jan", "01");
         }else if(d.contains("Feb")){
             d = d.replace("Feb", "02");
         }else if(d.contains("Mar")){
             d = d.replace("Mar", "03");
         }else if(d.contains("Apr")){
             d = d.replace("Apr", "04");
         }else if(d.contains("May")){
             d = d.replace("May", "05");
         }else if(d.contains("Jun")){
             d = d.replace("Jun", "06");
         }else if(d.contains("Jul")){
             d = d.replace("Jul", "07");
         }else if(d.contains("Aug")){
             d = d.replace("Aug", "08");
         }else if(d.contains("Sep")){
             d = d.replace("Sep", "09");
         }else if(d.contains("Oct")){
             d = d.replace("Oct", "10");
         }else if(d.contains("Nov")){
             d = d.replace("Nov", "11");
         }else if(d.contains("Dec")){
             d = d.replace("Dec", "12");
         }
         return d;
     }

}



