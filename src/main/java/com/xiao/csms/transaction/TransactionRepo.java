package com.xiao.csms.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.connectorId = ?1")
    public Optional<Transaction> getByConnectorId(int connectorId);

    @Query("SELECT t FROM Transaction t WHERE t.transactionId = ?1")
    public Optional<Transaction> getByTransactionId(int transactionId);

    @Query("SELECT count(t)>0 FROM Transaction t WHERE t.transactionId = ?1 ")
    public boolean ifExists(int transactionId);

    @Query("SELECT max(t.id) FROM Transaction t")
    public int getMaxId();

    @Transactional
    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.id>0")
    public void cleanAll();
}
