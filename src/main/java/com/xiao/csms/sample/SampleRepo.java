package com.xiao.csms.sample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SampleRepo extends JpaRepository<Sample, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Sample s WHERE s.id>0")
    public void cleanAll();
}
