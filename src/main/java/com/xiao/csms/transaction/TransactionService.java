package com.xiao.csms.transaction;

import com.xiao.csms.exceptions.ResourceNotFoundException;
import com.xiao.csms.sample.SampleService;
import eu.chargetime.ocpp.model.core.StartTransactionRequest;
import eu.chargetime.ocpp.model.core.StopTransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired private TransactionRepo repo;
    @Autowired private SampleService sampleService;

    // GET all transactions
    public List<Transaction> getAll(){ return repo.findAll();};

    public boolean ifExists(int tid){
        return repo.ifExists(tid);
    }
    // GET transaction by transactionId
    public Transaction getByTid(int tid){
        Optional<Transaction> result = repo.getByTransactionId(tid);
        if(result.isPresent()){
            return result.get();
        } throw new ResourceNotFoundException("Cannot find transaction by transactionId " + tid);
    }

    // SAVE StartTransaction
    public void saveStartRequest(StartTransactionRequest request, int tid){
        Transaction t = new Transaction();
        t.setConnectorId(request.getConnectorId());
        t.setStartTime(String.valueOf(request.getTimestamp()));
        t.setTransactionId(tid);
        repo.save(t);
    }

    // GET last transaction
    public int getMaxTransactionId(){
        int id = repo.getMaxId();
        return repo.getById(id).getTransactionId();
    }

    public Transaction getLastTransaction(){
        return repo.getById(repo.getMaxId());
    }

    public int getLastConnector(){
        return repo.getById(repo.getMaxId()).getConnectorId();
    }
    // SAVE StopRequest
    public void saveStopRequest(StopTransactionRequest r){
        repo.getByTransactionId(r.getTransactionId()).map(target -> {
            target.setStopTime(String.valueOf(r.getTimestamp()));
            repo.save(target);
            return target;
        });
    }

    public void setStopTime(int tid, String stopTime){
        repo.getByTransactionId(tid).map(target -> {
            target.setStopTime(stopTime);
            repo.save(target);
            return target;
        });
    }

    //DELETE by transactionID
    public void deleteByTransactionId(int tid){
        sampleService.deleteByTid(tid);
        if(repo.ifExists(tid)){
            repo.deleteByTid(tid);
        }
    }

    // DELETE ALL
    public void cleanAll(){repo.cleanAll();}



}
