package com.xiao.csms.reservation;

import com.xiao.csms.connector.Connector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r WHERE r.connectorId = ?1")
    public Optional<Reservation> getByConnectorId(int connectorId);

    @Query("SELECT count(c)>0 FROM Connector c WHERE c.connectorId = ?1 ")
    public boolean ifExists(int connectorId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.reservationId=?1")
    public void deleteByRid(int rid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.id>0")
    public void cleanAll();
}
