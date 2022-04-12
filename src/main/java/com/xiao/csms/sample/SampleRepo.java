package com.xiao.csms.sample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SampleRepo extends JpaRepository<Sample, Integer> {

    @Query("SELECT s FROM Sample s WHERE s.transactionId = ?1")
    public List<Sample> findByTid(int tid);

    @Query("SELECT count(s)>0 FROM Sample s WHERE s.transactionId=?1")
    public boolean ifExist(int tid);

    @Query("SELECT MIN(s.soc) FROM Sample s WHERE s.transactionId=?1")
    public int getStartSoC(int tid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Sample s WHERE s.transactionId = ?1")
    public void deleteByTid(int tid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Sample s WHERE s.id>0")
    public void cleanAll();

    @Query("SELECT count(s)>2 FROM Sample s WHERE s.transactionId=?1")
    public boolean isEnough(int tid);

//    @Query("SELECT s FROM Sample s WHERE s.id ")
//    public List<Sample> findTopThreeByTid(int tid);
}
