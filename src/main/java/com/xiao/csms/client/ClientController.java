package com.xiao.csms.client;

import com.xiao.csms.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public List<Client> getAllClients(){
        return service.getAllClients();
    }

    // get client by idToken
    @GetMapping("/{idtoken}")
    public Client findByIdToken(@PathVariable(value="idtoken") String idToken) throws ResourceNotFoundException {
        return service.findByIdToken(idToken);
    }

    // save client
    @PostMapping
    public void save(String idToken) throws ResourceNotFoundException {
        service.save(idToken);
    }

    // delete client by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable("id") int id){
        return service.deleteClient(id);
    }

}
