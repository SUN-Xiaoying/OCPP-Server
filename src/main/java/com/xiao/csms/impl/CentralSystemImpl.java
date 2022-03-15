package com.xiao.csms.impl;

import com.xiao.csms.server.CentralSystem;
import eu.chargetime.ocpp.model.core.AvailabilityType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;

@Slf4j
@AllArgsConstructor
@Component
@Controller
public class CentralSystemImpl {
    private final CentralSystem centralSystem;

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

    @GetMapping("/changeAvailability")
    public String changeAvailability(RedirectAttributes ra) throws Exception {
        centralSystem.sendChangeAvailabilityRequest(1, AvailabilityType.Inoperative);
        ra.addFlashAttribute("message", "ChangeAvailabilityRequest Sent!");
        return "redirect:/";
    }

    @GetMapping("/clearCache")
    public String clearCache(RedirectAttributes ra) throws Exception {
        centralSystem.sendClearCacheRequest();
        ra.addFlashAttribute("message", "ClearCacheRequest Sent!");
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
        centralSystem.sendRemoteStopTransactionRequest(1);
        ra.addFlashAttribute("message", "RemoteStopTransactionRequest Sent!");
        return "redirect:/";
    }
}
