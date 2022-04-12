package com.xiao.csms.helper;

import com.xiao.csms.dayprice.DayPrice;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helper {

    // Generate random five five digits
    public static int randomFiveDigits(){
        return 10000 + (int)(Math.random()*10000);
    }

    // Max(Outlet.Power) of KemPower Connector, kW
    private static final double maxPower=125000.0;

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

    // Root Mean Square
    public static double rms(double[] nums){
        double square = 0;
        double mean = 0;
        double root = 0;

        // Calculate square.
        for (double num : nums) {
            square += Math.pow(num, 2);
        }

        // Calculate Mean.
        mean = (square / (double) (nums.length));

        // Calculate Root.
        root = Math.sqrt(mean);
        return root;
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

//    public int[] priceRunner(String startTime){
//        ZonedDateTime start = ZonedDateTime.parse(startTime);
//        int hour = start.getHour();
//
//
//    }
}
