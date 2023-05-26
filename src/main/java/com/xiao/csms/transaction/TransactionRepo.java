package com.xiao.csms.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.connectorId = ?1")
    Optional<Transaction> getByConnectorId(int connectorId);

    @Query("SELECT t FROM Transaction t WHERE t.transactionId = ?1")
    Optional<Transaction> getByTransactionId(int transactionId);

    @Query("SELECT count(t)>0 FROM Transaction t WHERE t.transactionId = ?1 ")
    boolean ifExists(int transactionId);

    @Query("SELECT max(t.id) FROM Transaction t")
    int getMaxId();

    @Transactional
    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.transactionId=?1")
    void deleteByTid(int tid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Transaction t WHERE t.id>0")
    void cleanAll();
}
