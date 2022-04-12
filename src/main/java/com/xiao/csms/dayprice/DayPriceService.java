package com.xiao.csms.dayprice;

import com.xiao.csms.helper.Helper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DayPriceService{
    private static final Logger logger = LoggerFactory.getLogger(DayPriceService.class);

    @Autowired
    private DayPriceRepo repo;

    private static final int MAX_HOUR = 24;

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
                dp.setHour(i);
                dp.setPrice(result.get(i));
                repo.save(dp);
            }

            logger.info("The price of "+ date + " has updated.");
        }
    }

    // SAVE the price of today and tomorrow
    public void saveToday(){
        saveDayPrice(Helper.DotFormatter(ZonedDateTime.now()));
        saveDayPrice(Helper.DotFormatter(ZonedDateTime.now().plusDays(1)));
    }

    // GET all prices
    public List<DayPrice> getAll(){
        return repo.findAll();
    }

    // CLEAN all cache
    public void cleanCache(){
        repo.cleanAll();
    }

    // GET prices in next N hours
    public List<DayPrice> getPricesInNextHours(String date, int interval){
        ZonedDateTime current = ZonedDateTime.parse(date);
        String currentDay = Helper.DotFormatter(current);
        int currentHour = current.getHour();
        return repo.getAllBetweenInterval(repo.getCurrentId(currentDay,currentHour), interval);
    }

    // GET date.hour Price
    public double getHourlyPrice(String date, int hour){
        ZonedDateTime current = ZonedDateTime.parse(date);
        String targetDay = Helper.DotFormatter(current);
        return repo.getHourlyPrice(targetDay, hour);
    }

}