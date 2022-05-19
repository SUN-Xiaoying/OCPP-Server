package com.xiao.csms.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiao.csms.connector.Connector;
import com.xiao.csms.connector.ConnectorService;
import com.xiao.csms.server.CentralSystem;
import com.xiao.csms.server.CentralSystemImpl;
import com.xiao.csms.transaction.Transaction;
import com.xiao.csms.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.ZonedDateTime;

@RestController
@RequestMapping(path="/api/")
public class APIController {

    @Autowired
    ConnectorService connectorService;
    @Autowired
    TransactionService transactionService;

    @GetMapping("heartbeat/{cid}")
    public ResponseEntity<Connector> getHeartbeat(@PathVariable int cid) throws IOException {
        Connector c = connectorService.getById(cid);
        if(c == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().body(c);
        }
    }

    @GetMapping("start")
    public ResponseEntity<Transaction> getStartCharging() throws IOException {
        Transaction t = transactionService.getLastTransaction();
        if(t==null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok().body(t);
        }
    }

    @GetMapping("stop")
    public ResponseEntity<Transaction> getStopCharging() throws IOException {
        Transaction t = transactionService.getLastTransaction();
        if(t==null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().body(t);
        }
    }

}
