package com.xiao.csms.reservation;

import com.xiao.csms.helper.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    @Autowired private ReservationRepo repo;

    // GET all reservations
    public List<Reservation> getAll(){
        return repo.findAll();
    }

    // SAVE reservation
    public void save(Reservation r){
        if(r.getReservationId() == 0){
            r.setReservationId(Helper.randomFiveDigits());
        }
        repo.save(r);
    }

    // DELETE by id
    public void deleteByRid(int rid){repo.deleteByRid(rid);}

    // DELETE ALL
    public void cleanAll(){ repo.cleanAll(); }
}
