package com.xiao.csms.dayprice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DayPriceRepo extends JpaRepository<DayPrice, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM DayPrice dp WHERE dp.id>0")
    public void cleanAll();

    @Transactional
    @Query("SELECT count(dp)>0 FROM DayPrice dp WHERE dp.date = ?1")
    public boolean ifExist(String date);
}
