package com.xiao.csms.transaction;

import com.xiao.csms.exceptions.ResourceNotFoundException;
import eu.chargetime.ocpp.model.core.MeterValuesRequest;
import eu.chargetime.ocpp.model.core.StartTransactionRequest;
import eu.chargetime.ocpp.model.core.StopTransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired private TransactionRepo repo;

    // GET all transactions
    public List<Transaction> getAll(){ return repo.findAll();};

    // GET transaction by transactionId
    public Transaction getById(int tid){
        Optional<Transaction> result = repo.getByTransactionId(tid);
        if(result.isPresent()){
            return result.get();
        } throw new ResourceNotFoundException("Cannot find transaction by transactionId " + tid);
    }

    // SAVE StartTransaction
    public void saveStartRequest(StartTransactionRequest request, int tid){
        Transaction t = new Transaction();
        t.setConnectorId(request.getConnectorId());
        t.setIdTag(request.getIdTag());
        t.setMeterStart(Long.valueOf(request.getMeterStart()));
        t.setStartTime(String.valueOf(request.getTimestamp()));
        t.setTransactionId(tid);
        repo.save(t);
    }

//    // SAVE MeterValuesRequest
//    public void saveMeterValuesRequest(MeterValuesRequest r){
//        repo.getByConnectorId(r.getConnectorId()).map(target -> {
//            target.setTransactionId(r.getTransactionId());
//            repo.save(target);
//            return target;
//        });
//    }

    // GET last transaction
    public int getMaxTransactionId(){
        int id = repo.getMaxId();
        return repo.getById(id).getTransactionId();
    }

    // SAVE StopTransaction
    public void saveStopRequest(StopTransactionRequest r){
        repo.getByTransactionId(r.getTransactionId()).map(target -> {
            target.setMeterStop(Long.valueOf(r.getMeterStop()));
            target.setStopTime(String.valueOf(r.getTimestamp()));
            repo.save(target);
            return target;
        });
    }

    // STOP
    public void stop(StopTransactionRequest r){
        repo.getByTransactionId(r.getTransactionId()).map(target -> {
            target.setMeterStop(Long.valueOf(r.getMeterStop()));
            target.setStopTime(String.valueOf(r.getTimestamp()));
            repo.save(target);
            return target;
        });
    }


    // DELETE ALL
    public void cleanAll(){repo.cleanAll();}
}