package com.xiao.csms.helper;

import com.xiao.csms.dayprice.DayPrice;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helper {
    private static DecimalFormat df = new DecimalFormat("0.000");
    // Generate random five five digits
    public static int randomFiveDigits(){
        return 10000 + (int)(Math.random()*10000);
    }
    public static String DotFormatter(ZonedDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        return dateTime.format(formatter);
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
    // Absolute Percentage Error
    public static String ape(String actual, String estimated){
        ZonedDateTime at = ZonedDateTime.parse(actual);
        ZonedDateTime et = ZonedDateTime.parse(estimated);
        int av = at.getHour()*60*60 + at.getMinute()*60 + at.getSecond();
        int ev = et.getHour()*60*60 + et.getMinute()*60 + et.getSecond();
        double result = Math.abs(av-ev)* 100.0 /(double)av;

        return df.format(result)+"%";
    }
    // Unit: SEK / KWh
    public static double[] getPriceList(List<DayPrice> dps){
        int size=dps.size();
        double[] prices = new double[size];
        for(int i=0; i<size;i++){
            prices[i]=dps.get(i).getPrice()/1000.0;
        }
        return prices;
    }

    public static int[] timeToArray(String s){
        String[] array = s.split(":");
        int size =array.length;
        int[] nums = new int[size];
        for(int i=0; i<size; i++){
            nums[i]=Integer.parseInt(array[i]);
        }
        return nums;
    }

    public static int getInterval(String start, String stop){
        int[] startInt = timeToArray(start);
        int[] stopInt = timeToArray(stop);
        return (int) Math.ceil(stopInt[0]-startInt[0]+((stopInt[1] - startInt[1])/60.0));
    }

    public double simpleMean(double[] nums){
        double sum=0.0;
        for(double num:nums){
            sum+=num;
        }
        return sum/nums.length;
    }

}
