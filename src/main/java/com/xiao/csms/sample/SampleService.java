package com.xiao.csms.sample;

import eu.chargetime.ocpp.model.core.MeterValue;
import eu.chargetime.ocpp.model.core.MeterValuesRequest;
import eu.chargetime.ocpp.model.core.SampledValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SampleService {
    @Autowired private SampleRepo repo;

    // INPUT SampledValues
    public void create(MeterValuesRequest r){
        MeterValue[] meterValues = r.getMeterValue();
        if(meterValues.length > 0){
            SampledValue[] sampledValues = meterValues[0].getSampledValue();
            if(sampledValues.length == 6){
                Sample sample = new Sample();
                sample.setTransactionId(r.getTransactionId());
                sample.setSoc(Integer.parseInt(sampledValues[0].getValue()));
                sample.setTemperature(sampledValues[1].getValue());
                sample.setCurrent(sampledValues[2].getValue());
                sample.setVoltage(sampledValues[3].getValue());
                sample.setPower(sampledValues[4].getValue());
                sample.setEnergy(sampledValues[5].getValue());
                repo.save(sample);
            }
        }
    }

    // GET by transactionId
    public List<Sample> getByTid(int tid){
        return repo.findByTid(tid);
    }

    // DELETE by transactionId
    public void deleteByTid(int tid){
        repo.cleanByTid(tid);
    }


    // DELETE ALL
    public void cleanAll(){repo.cleanAll();}
}
