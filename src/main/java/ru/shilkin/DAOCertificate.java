package ru.shilkin;

import java.util.Date;
import java.util.List;

public class DAOCertificate {
    private String fio;

    public DAOCertificate(String fio, String keyNum, Date beforeDate, Date afterDate, String commentary) {
        this.fio = fio;
        this.keyNum = keyNum;
        this.commentary = commentary;
        this.beforeDate = beforeDate;
        this.afterDate = afterDate;
    }

    private String keyNum;


    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    private String commentary;

    public DAOCertificate(String fio, String keyNum, Date afterDate) {
        this.fio = fio;
        this.keyNum = keyNum;
        this.afterDate = afterDate;
    }

    private int id;

    public DAOCertificate(int id, String fio, String keyNum, Date beforeDate, Date afterDate) {
        this.fio = fio;
        this.keyNum = keyNum;
        this.id = id;
        this.beforeDate = beforeDate;
        this.afterDate = afterDate;
    }

    public DAOCertificate(String fio, String keyNum, Date beforeDate, Date afterDate) {
        this.fio = fio;
        this.keyNum = keyNum;
        this.beforeDate = beforeDate;
        this.afterDate = afterDate;
    }

    private Date beforeDate;

    public DAOCertificate() {
    }

    private Date afterDate;


    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Date getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(Date beforeDate) {
        this.beforeDate = beforeDate;
    }

    public Date getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(Date afterDate) {
        this.afterDate = afterDate;
    }



    public String getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(String keyNum) {
        this.keyNum = keyNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
