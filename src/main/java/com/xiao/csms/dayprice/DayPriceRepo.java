package com.xiao.csms.dayprice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Repository
public interface DayPriceRepo extends JpaRepository<DayPrice, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM DayPrice dp WHERE dp.id>0")
    void cleanAll();

    @Transactional
    @Query("SELECT count(dp)>0 FROM DayPrice dp WHERE dp.date = ?1")
    boolean ifExist(String dotDate);

    @Query("SELECT dp.price FROM DayPrice dp WHERE dp.date=?1 AND dp.hour=?2")
    double getHourlyPrice(String day, int hour);

    @Query("SELECT dp.id FROM DayPrice dp WHERE dp.date=?1 AND dp.hour=?2")
    int getCurrentId(String day, int hour);

    @Query("SELECT dp FROM DayPrice dp WHERE dp.id>=?1 AND dp.id<=?1+?2" )
    List<DayPrice> getAllBetweenInterval(int id, int interval);

}
