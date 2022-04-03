package com.xiao.csms.connector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ConnectorRepo extends JpaRepository<Connector, Integer> {

    @Query("SELECT c FROM Connector c WHERE c.connectorId = ?1")
    public Optional<Connector> getByConnectorId(int connectorId);

    @Query("SELECT count(c)>0 FROM Connector c WHERE c.connectorId = ?1 ")
    public boolean ifExists(int connectorId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Connector c WHERE c.id>0")
    public void cleanAll();
}
