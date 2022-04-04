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

    @Transactional
    @Modifying
    @Query("DELETE FROM Sample s WHERE s.transactionId = ?1")
    public void cleanByTid(int tid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Sample s WHERE s.id>0")
    public void cleanAll();
}
