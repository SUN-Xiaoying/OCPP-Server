package com.xiao.csms.sample;

import com.xiao.csms.exceptions.ResourceNotFoundException;
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
        int tid = r.getTransactionId();
        if(repo.ifExist(tid)){
            if(repo.getMaxSampleSoC(tid) == 100){
                return;
            }
        }

        if(meterValues.length > 0){
            SampledValue[] sampledValues = meterValues[0].getSampledValue();
            if(sampledValues.length == 6){
                Sample sample = new Sample();
                sample.setTransactionId(r.getTransactionId());
                sample.setSoc(Integer.parseInt(sampledValues[0].getValue()));
                sample.setTemperature(Integer.parseInt(sampledValues[1].getValue()));
                sample.setCurrent(Integer.parseInt(sampledValues[2].getValue()));
                sample.setVoltage(Integer.parseInt(sampledValues[3].getValue()));
                sample.setPower(Integer.parseInt(sampledValues[4].getValue()));
                sample.setEnergy(Integer.parseInt(sampledValues[5].getValue()));
                repo.save(sample);
            }
        }
    }

    // GET by transactionId
    public List<Sample> getByTid(int tid){
        return repo.findByTid(tid);
    }

    public int getThirdPower(int tid){
        List<Sample> samples = getByTid(tid);
        return samples.get(2).getPower();
    }

    // GET Start SoC
    public int getStartSoC(int tid){
        return repo.getStartSoC(tid);
    }

    // DELETE by transactionId
    public void deleteByTid(int tid){
        if(repo.ifExist(tid)){
            repo.deleteByTid(tid);
        }
    }

    // DELETE ALL
    public void cleanAll(){repo.cleanAll();}

    // Calculate current battery capacity
    public double estimateCapacity(int tid) {
        List<Sample> samples = repo.findByTid(tid);

        // TEST Linear Regression
//        double[] soc = new double[5];
//        double[] energy = new double[5];
//
//        for(int i=0; i<5;i++){
//            soc[i]=samples.get(i).getSoc();
//            energy[i]=samples.get(i).getEnergy();
//        }
//        LinearRegression guess = new LinearRegression(soc, energy);
//
//        return guess.predict(100.0);

        int rounds = 4;
        double sum = 0.0;
        double dSoC = 0.0;
        double dEnergy =0.0;

        for (int i=0; i<rounds; i++) {
            dSoC = samples.get(i+2).getSoc()-samples.get(i).getSoc();
            dSoC = dSoC==0.0 ? 0.23 : dSoC;
            dEnergy = samples.get(i+2).getEnergy()-samples.get(i).getEnergy();
            sum += dEnergy*100.0 / dSoC;
        }
        return  sum/rounds;
    }

    // Unit kWh
    public double estimateEnergy(int targetSoC, int tid){
        double capacity = estimateCapacity(tid);
        int startSoC = repo.getStartSoC(tid);
        return capacity*(targetSoC-startSoC)/100.0;
    }
}
