package com.xiao.csms.client;

import com.xiao.csms.exceptions.ResourceNotFoundException;
import eu.chargetime.ocpp.model.core.MeterValuesRequest;
import eu.chargetime.ocpp.model.core.StartTransactionRequest;
import eu.chargetime.ocpp.model.core.StatusNotificationRequest;
import eu.chargetime.ocpp.model.core.StopTransactionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientService {
    @Autowired
    private ClientRepo repo;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // GET all clients
    public List<Client> getAllClients(){
        return repo.findAll();
    }

    // GET a client by connectorId
    public Client getByConnectorId(int connectorId){
        Optional<Client> result = repo.findByConnectorId(connectorId);
        if(result.isPresent()){
            return result.get();
        } throw new ResourceNotFoundException("Cannot find client by connectorId " + connectorId);
    }

    // DELETE a client by Id
    public void deleteClient(int id){
        Client result = this.repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
        repo.delete(result);
    }

    // DELETE ALL
    public void cleanAll(){ repo.cleanAll(); }

//    // SAVE meterValues
//    public void saveMeterValue(MeterValuesRequest reuqest){
//        reuqest.
//    }

    // SAVE startTransactionRequest
    public void saveStartTransaction(StartTransactionRequest request){
        Client c = new Client();
        c.setConnectorId(request.getConnectorId());
        if(request.getReservationId() != null){
            c.setReservationId(request.getReservationId());
        }

        c.setIdTag(request.getIdTag());
        c.setTimeStamp(String.valueOf(request.getTimestamp()));
        c.setMeterStart(request.getMeterStart());
        repo.save(c);

    }

    // SAVE stopTransactionRequest
    public void saveStopTransaction(StopTransactionRequest reuqest){
        repo.findByIdTag(reuqest.getIdTag()).map(target -> {
            target.setTimeStamp(String.valueOf(reuqest.getTimestamp()));
            target.setMeterStop(reuqest.getMeterStop());
            target.setConnectorId(0);
            target.setReservationId(0);
            repo.save(target);
            return target;
        });
    }

    // SAVE stopTransactionRequest
    public void saveStatusNotification(StatusNotificationRequest reuqest){
        int cid = reuqest.getConnectorId();
        if(repo.ifConnectorExists(cid)){
            repo.findByConnectorId(cid).map(target ->{
                target.setTimeStamp(String.valueOf(reuqest.getTimestamp()));
                repo.save(target);
                return target;
            });
        }
    }

}
