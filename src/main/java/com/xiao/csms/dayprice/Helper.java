package com.xiao.csms.dayprice;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Helper {
    public static String getTodayDot(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        return ZonedDateTime.now().format(formatter);
    }

    public static String getTomorrowDot(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
        Date tmr = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
        return formatter.format(tmr);
    }

    public static ArrayList<Double> priceReader(String s){
        ArrayList<Double> result = new ArrayList<>(24);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(s.split(";")));

        for(String data : list){
            if(data.contains(",")){
                data = data.replaceAll(",","\\.");
                result.add(Double.parseDouble(data));
            }
        }
        return result;
    }
}
