package com.xiao.csms.client;

import com.xiao.csms.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    // get all clients
    @GetMapping("/")
    public List<Client> getAllClients(Model m){
        return service.getAllClients();
    }

//    // get client by idToken
//    @GetMapping("/{idtoken}")
//    public Client findByIdToken(@PathVariable(value="idtoken") String idToken) throws ResourceNotFoundException {
//        return service.findByIdToken(idToken);
//    }

//    // save client
//    @PostMapping
//    public void save(String idToken) throws ResourceNotFoundException {
//        service.save(idToken);
//    }

    // DELETE client by id
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable("id") int id){
        service.deleteClient(id);
        return "redirect:/api/clients/";
    }

    // DELETE all clients
    @GetMapping("cleanAll")
    public String cleanAllClients(){
        service.cleanAll();
        return "redirect:/api/clients/";
    }
}
