package ru.shilkin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Cell;
import javafx.scene.control.TableView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableViewZap {
    ObservableList<DAOCertificate> ls= FXCollections.observableArrayList();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    public void zapTableView(TableView<DAOCertificate> tableView, ResultSet resultSet, ResultSet resultSetCount){
        int count = 0;
        while(true){
            try {
                if (!resultSetCount.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                count = resultSetCount.getInt(1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        List<Integer> list = new ArrayList<>();
        for(int schet = 1; schet <= count; schet++){
            list.add(schet);
        }

        int k = 0;
        try {
            while (resultSet.next()) {

                DAOCertificate daoCertificate = new DAOCertificate();

                daoCertificate.setFio(resultSet.getString(2));
                daoCertificate.setKeyNum(resultSet.getString(3).trim());
                daoCertificate.setBeforeDate(resultSet.getDate(4));
                daoCertificate.setAfterDate(resultSet.getDate(5));
                daoCertificate.setCommentary(resultSet.getString(6));
                ls.add(daoCertificate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(ls);
            }

}
