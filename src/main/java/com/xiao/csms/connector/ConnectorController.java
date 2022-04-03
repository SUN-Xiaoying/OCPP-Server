package com.xiao.csms.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ConnectorController {
    @Autowired ConnectorService service;

    @GetMapping("/connectors")
    public String showConnectors(Model m){
        List<Connector> connects = service.getAll();
        m.addAttribute("connects", connects);
        return "server/connectors";
    }
}
