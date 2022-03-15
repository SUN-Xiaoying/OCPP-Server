package com.xiao.csms.client;

import com.xiao.csms.exceptions.ResourceNotFoundException;
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

    // get all clients
    public List<Client> getAllClients(){
        return repo.findAll();
    }

    // get client by idToken
    public Client findByIdToken(String idToken) throws ResourceNotFoundException {
        Optional<Client> result = repo.findByIdToken(idToken);
        if(result.isPresent()){
            return result.get();
        } throw new ResourceNotFoundException("Client not found by idToken " + idToken);
    }

    // get client by id
    public Client get(Integer id) throws ClientNotFoundException {
        Optional<Client> result = repo.findById(id);
        if(result.isPresent()){
            return result.get();
        } throw new ClientNotFoundException("Could not find connector by " + id);
    }

    // save client
    public void save(String idToken) throws ResourceNotFoundException {
        if(repo.ifExist(idToken)){
            updateClient(idToken);
        }else{
            Client c = new Client();
            c.setIdToken(idToken);
            c.setLastTime(ZonedDateTime.now().format(formatter).toString());
            repo.save(c);
        }
    }

    // update client
    @Transactional
    public void updateClient(String idToken){
        repo.findByIdToken(idToken).map(target ->{
            target.setLastTime(ZonedDateTime.now().format(formatter).toString());
            repo.save(target);
            return target;
        });
    }

    // delete client by id
    public ResponseEntity<Client> deleteClient(int id){
        Client result = this.repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
        this.repo.delete(result);
        return ResponseEntity.ok().build();
    }
}
