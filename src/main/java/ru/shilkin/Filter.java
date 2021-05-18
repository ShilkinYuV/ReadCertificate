package ru.shilkin;

import java.util.LinkedHashMap;
import java.util.Map;

public class Filter {

    public String paramList(String variableFio, String variableNumberKey, String variableBeforeDate, String variableAfterDate){
        String query = "SELECT * FROM " + Const.TABLE + " WHERE ";
        Map<String, String> paramsMap = new LinkedHashMap<>();

        paramsMap.put(Const.FIO,variableFio);
        paramsMap.put(Const.NUMBER_KEY, variableNumberKey);
        paramsMap.put(Const.BEFORE_DATE, variableBeforeDate);
        paramsMap.put(Const.AFTER_DATE,variableAfterDate);

        int i = 0;
        for(Map.Entry<String, String> item : paramsMap.entrySet()) {

            if (i>0 & !item.getValue().equals("") & !query.equals("SELECT * FROM " + Const.TABLE + " WHERE ")) {
                query += " AND ";
            }

            if (paramsMap.size() > 0 & !item.getValue().equals("") & (item.getKey().equals(Const.BEFORE_DATE) &
                    !item.getValue().equals("")) || (item.getKey().equals(Const.AFTER_DATE) &
                    !item.getValue().equals(""))){
                query += Const.dm + item.getKey() + Const.dm + " = " + "'" + item.getValue() + "'";
            }

                if (paramsMap.size() > 0 & !item.getValue().equals("") & !item.getKey().equals(Const.BEFORE_DATE) &
                        !item.getKey().equals(Const.AFTER_DATE)) {
                    query += Const.dm + item.getKey() + Const.dm + " like " + "'" + item.getValue() + "%'";
                }
            i++;
        }
        return query;
    }
}
