package com.xiao.csms.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ReservationController {
    @Autowired ReservationService service;

    @GetMapping("/reserve")
    public String showReservationForm(Model m){
        m.addAttribute("reservation", new Reservation());
        return "server/reserve";
    }

    @GetMapping("/reserve/save")
    public String saveReservationForm(@ModelAttribute("reservation") Reservation r){
        System.out.println(r.toString());
        return "server/reserve";
    }
}
