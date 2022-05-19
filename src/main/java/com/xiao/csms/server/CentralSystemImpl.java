package com.xiao.csms.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiao.csms.connector.Connector;
import com.xiao.csms.connector.ConnectorService;
import com.xiao.csms.transaction.Transaction;
import com.xiao.csms.transaction.TransactionService;
import eu.chargetime.ocpp.model.core.AvailabilityType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Component
@Controller
public class CentralSystemImpl {
    private final CentralSystem centralSystem;

    @Autowired ConnectorService connectorService;
    @Autowired TransactionService transactionService;

    @PostConstruct
    public void startServer() throws Exception {
        centralSystem.start();
    }

    @GetMapping("/")
    public String getServerIndex(Model model) throws Exception {
        model.addAttribute("isClosed",centralSystem.isClosed());
        model.addAttribute("isConnected",centralSystem.conncted());
        model.addAttribute("currentSession", centralSystem.getCurrentSessionIndex());
        return "server/index";
    }

    @GetMapping("/availability/1")
    public String availabilityOperative(RedirectAttributes ra) throws Exception {
        centralSystem.sendChangeAvailabilityRequest(1,AvailabilityType.Operative);
        ra.addFlashAttribute("message", "ChangeAvailabilityRequest Sent!");
        return "redirect:/";
    }

    @GetMapping("/availability/0")
    public String availabilityInoperative(RedirectAttributes ra) throws Exception {
        centralSystem.sendChangeAvailabilityRequest(1,AvailabilityType.Inoperative);
        ra.addFlashAttribute("message", "ChangeAvailabilityRequest Sent!");
        return "redirect:/";
    }

    @GetMapping("/remoteStartTransaction")
    public String remoteStartTransaction(RedirectAttributes ra) throws Exception {
        centralSystem.sendRemoteStartTransactionRequest(1, "testId");
        ra.addFlashAttribute("message", "RemoteStartTransactionRequest Sent!");
        return "redirect:/";
    }

    @GetMapping("/remoteStopTransaction")
    public String remoteStopTransaction(RedirectAttributes ra) throws Exception {
        centralSystem.sendRemoteStopTransactionRequest();
        ra.addFlashAttribute("message", "RemoteStopTransactionRequest Sent!");
        return "redirect:/";
    }


    @GetMapping("/clearCache")
    public String clearCache(RedirectAttributes ra) throws Exception {
        centralSystem.sendClearCacheRequest();
        ra.addFlashAttribute("message", "ClearCacheRequest Sent!");
        return "redirect:/";
    }

    @GetMapping("/unlockConnector")
    public String unlockConnector(RedirectAttributes ra) throws Exception {
        centralSystem.sendUnlockConnectorRequest(1);
        ra.addFlashAttribute("message", "UnlockConnectorRequest Sent!");
        return "redirect:/";
    }

    // API
//    @GetMapping("/api/start")
//    public ResponseEntity<String> getStartCharging() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Transaction t = transactionService.getByTid(transactionService.getMaxTransactionId());
//        if(centralSystem.conncted() && t!=null){
//            return ResponseEntity.ok(objectMapper.writeValueAsString(t));
//        }else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/api/stop")
//    public ResponseEntity<String> getStopCharging() throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Transaction t = transactionService.getByTid(transactionService.getMaxTransactionId());
//        if(centralSystem.conncted()&& t!=null){
//            return ResponseEntity.ok(objectMapper.writeValueAsString(t));
//        }else{
//            return ResponseEntity.notFound().build();
//        }
//    }
}
