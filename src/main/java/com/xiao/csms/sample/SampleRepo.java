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
    List<Sample> findByTid(int tid);

    @Query("SELECT count(s)>0 FROM Sample s WHERE s.transactionId=?1")
    boolean ifExist(int tid);

    @Query("SELECT MIN(s.soc) FROM Sample s WHERE s.transactionId=?1")
    int getStartSoC(int tid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Sample s WHERE s.transactionId = ?1")
    void deleteByTid(int tid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Sample s WHERE s.id>0")
    void cleanAll();

    @Query("SELECT MAX(s.soc) FROM Sample s WHERE s.transactionId=?1 ORDER BY s.id DESC ")
    int getMaxSampleSoC(int tid);

}
