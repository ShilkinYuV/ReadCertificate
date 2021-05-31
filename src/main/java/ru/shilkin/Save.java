package ru.shilkin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Save {
    Date date = new Date();
    Settings settingss = new Settings();
    String period;
    DAOCertificate daoCertificate = new DAOCertificate();
    ObservableList<DAOCertificate> ls= FXCollections.observableArrayList();
    void saveTable(TableView<DAOCertificate> tableView, ImageView save) {
        if (settingss.sPeriodCert == null) {
            period = settingss.sPeriodCert = "30";
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Employees sheet");
        sheet.setColumnWidth(0, (int) (5 * 1.14388) * 256);
        sheet.setColumnWidth(1, (int) (25 * 1.14388) * 256);
        sheet.setColumnWidth(2, (int) (30 * 1.14388) * 256);
        sheet.setColumnWidth(3, (int) (30 * 1.14388) * 256);
        sheet.setColumnWidth(4, (int) (30 * 1.14388) * 256);
        sheet.setColumnWidth(5, (int) (90 * 1.14388) * 256);
        int rownum = 0;
        Cell cell;
        Row row;
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFCellStyle styleRead = workbook.createCellStyle();
        style.setFont(font);
        styleRead.setFillForegroundColor(IndexedColors.RED.getIndex());
        styleRead.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // создаем подписи к столбцам (это будет первая строчка в листе Excel файла)
        row = sheet.createRow(rownum);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("№");
        cell.setCellStyle(style);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Владелец");
        cell.setCellStyle(style);
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Номер ключа");
        cell.setCellStyle(style);
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Действует с");
        cell.setCellStyle(style);
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Действует по");
        cell.setCellStyle(style);
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("ТОФК");
        cell.setCellStyle(style);

        int k = 1;
        for (DAOCertificate daoCertificate : tableView.getItems()){
        rownum++;
        row = sheet.createRow(rownum);
        cell = row.createCell(0, CellType.NUMERIC);
        cell.setCellValue(k++);

        long realPeriod = (daoCertificate.getAfterDate().getTime() - date.getTime()) / (1000 * 60 * 60 * 24);
        if(realPeriod <= Long.valueOf(period)){
            cell.setCellStyle(styleRead);
        }

        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue(daoCertificate.getFio());

        if(realPeriod <= Long.valueOf(period)){
            cell.setCellStyle(styleRead);
        }

        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue(daoCertificate.getKeyNum());

            if(realPeriod <= Long.valueOf(period)){
                cell.setCellStyle(styleRead);
            }

        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue(String.valueOf(daoCertificate.getBeforeDate()));

            if(realPeriod <= Long.valueOf(period)){
                cell.setCellStyle(styleRead);
            }

        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue(String.valueOf(daoCertificate.getAfterDate()));

            if(realPeriod <= Long.valueOf(period)){
                cell.setCellStyle(styleRead);
            }

        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue(String.valueOf(daoCertificate.getCommentary()));

            if(realPeriod <= Long.valueOf(period)){
                cell.setCellStyle(styleRead);
            }

        }

        // записываем созданный в памяти Excel документ в файл
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить как");
        fileChooser.setInitialFileName("Дата действия сертфикатов");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xls"));
        File file = fileChooser.showSaveDialog(save.getScene().getWindow());
        if (file != null) {

            try (FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath())) {
                workbook.write(outputStream);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
