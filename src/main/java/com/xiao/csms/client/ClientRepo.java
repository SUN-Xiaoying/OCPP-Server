package com.xiao.csms.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Integer> {
    @Query("SELECT count(c)>0 FROM Client c WHERE c.idToken = ?1 ")
    public boolean ifExist(String idToken);

    @Query("SELECT c FROM Client c WHERE c.idToken = ?1")
    public Optional<Client> findByIdToken(String idToken);
}
