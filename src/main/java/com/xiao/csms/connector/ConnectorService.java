package com.xiao.csms.connector;

import com.xiao.csms.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectorService {
    @Autowired(required=false)
    private ConnectorRepo repo;

    // GET all connectors
    public List<Connector> getAll(){return repo.findAll();}

    // GET connector by id
    public Connector getById(int cid){
        Optional<Connector> result = repo.getByConnectorId(cid);
        if(result.isPresent()){
            return result.get();
        } throw new ResourceNotFoundException("Cannot find connector by connectorId " + cid);
    }

    // SAVE connector
    public void save(Connector c){
        int cid = c.getConnectorId();
        if(repo.ifExists(cid)){
            repo.getByConnectorId(cid).map(target -> {
                target.setErrorCode(c.errorCode);
                target.setStatus(c.status);
                target.setTimeStamp(c.timeStamp);
                repo.save(target);
                return target;
            });
        }else{
            repo.save(c);
        }
    }

    // DELETE ALL
    public void cleanAll(){ repo.cleanAll(); }
}
