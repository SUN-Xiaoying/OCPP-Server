package com.xiao.csms.reservation;

import com.xiao.csms.connector.Connector;
import eu.chargetime.ocpp.model.reservation.ReserveNowRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired private ReservationRepo repo;

    // SAVE reservation
    public void save(ReserveNowRequest request){
        Reservation r = new Reservation();
        r.setReservationId(request.getReservationId());
        r.setConnectorId(request.getConnectorId());
        r.setIdTag(request.getIdTag());
        r.setExpiryDate(String.valueOf(request.getExpiryDate()));
        repo.save(r);

    }

    // DELETE ALL
    public void cleanAll(){ repo.cleanAll(); }
}
