package com.xiao.csms.dayprice;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DayPriceService{
    private static final Logger logger = LoggerFactory.getLogger(DayPriceService.class);

    @Autowired
    private DayPriceRepo repo;

    private static final int MAX_HOUR = 24;
    private static final String todayDot = Helper.getTodayDot();
    private static final String tomorrowDot = Helper.getTomorrowDot();

    public String findDayPrice(String date){
        try {
            URL url = new URL("ftp://studentnordic:nordic_2020@ftp.nordpoolgroup.com/Elspot/Elspot_prices/Sweden/SE3_Stockholm/stosek22.sdv");
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                if(inputLine.contains(date)){
                    Helper.priceReader(inputLine);
                    return inputLine;
                }
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public void saveDayPrice(String date){
        String str = findDayPrice(date);
        ArrayList<Double> result = Helper.priceReader(str);

        if(result.size() < 2){
            logger.warn("The Price of " + date +" is not updated");
            return ;
        }

        if(!repo.ifExist(date)){
            for(int i=0; i<MAX_HOUR; i++){
                DayPrice dp = new DayPrice();
                dp.setDate(date);
                dp.setHour(i+1);
                dp.setPrice(result.get(i));
                repo.save(dp);
            }

            logger.info("The price of "+ date + " has updated.");
        }
    }

    // save the price of today
    public void saveToday(){
        saveDayPrice(todayDot);
        saveDayPrice(tomorrowDot);
    }

    // get all prices
    public List<DayPrice> getAll(){
        return repo.findAll();
    }

    // clean the table
    public void cleanCache(){
        repo.cleanAll();
    }
}