package com.xiao.csms.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Client c WHERE c.id>0")
    public void cleanAll();

    @Query("SELECT count(c)>0 FROM Client c WHERE c.connectorId = ?1 ")
    public boolean ifConnectorExists(int connectorId);

    @Query("SELECT c FROM Client c WHERE c.idTag = ?1")
    public Optional<Client> findByIdTag(String idTag);

    @Query("SELECT c FROM Client c WHERE c.connectorId = ?1")
    public Optional<Client> findByConnectorId(int connectorId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Client c WHERE c.connectorId = ?1")
    public void cleanByConnectorId(int connectorId);
}
