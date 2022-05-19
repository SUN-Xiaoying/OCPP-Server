package com.xiao.csms.reservation;

import com.xiao.csms.dayprice.DayPriceService;
import com.xiao.csms.helper.Helper;
import com.xiao.csms.sample.SampleService;
import com.xiao.csms.transaction.Transaction;
import com.xiao.csms.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
public class ReservationController {
    @Autowired ReservationService service;
    @Autowired TransactionService transactionService;
    @Autowired SampleService sampleService;
    @Autowired DayPriceService dayPriceService;
    // Max(Outlet.Power) of KemPower Connector, kW
    private static final double maxPower=131400.0;

    private boolean debug = true;
    @GetMapping("/reservations")
    public String showReservations(Model m){
        List<Reservation> reservations = service.getAll();
        m.addAttribute("reservations", reservations);
        return "smart/reservations";
    }

    @GetMapping("reservation/{transactionId}")
    public String showReservationForm(@PathVariable("transactionId") int tid, Model m){
        Reservation reservation = new Reservation();
        Transaction t = transactionService.getByTid(tid);
        reservation.setTransactionId(tid);
        reservation.setConnectorId(t.getConnectorId());
        ZonedDateTime dateTime = ZonedDateTime.parse(t.getStartTime());
        System.out.println("RAW: "+dateTime);

        reservation.setDate(Helper.DotFormatter(dateTime));
        String start= dateTime.getHour()+":"+dateTime.getMinute();
        System.out.println("START: "+start);
        reservation.setStart(start);
        reservation.setStartSoC(sampleService.getStartSoC(tid));
        m.addAttribute("title", "Transaction "+tid);
        m.addAttribute("reservation", reservation);
        m.addAttribute("transactionId", tid);
        return "smart/reservation";
    }

    @PostMapping("/reservation/save/{transactionId}")
    public String saveReservationForm(@PathVariable("transactionId") int tid, @ModelAttribute("reservation") Reservation r, Model m){
        service.save(r);
        int interval = Helper.getInterval(r.getStart(), r.getStop());
        Comparison comparison = this.makeComparison(r.getTargetSoC(), interval, tid);
        m.addAttribute("comparison", comparison);
        return "smart/result";
    }

    @GetMapping("reservation/delete/{reservationId}")
    public String deleteReservation(@PathVariable("reservationId") int rid, RedirectAttributes ra){
        service.deleteByRid(rid);
        ra.addFlashAttribute("message", "Reservation "+rid+" has been deleted" );
        return "redirect:/reservations";
    }

    public Comparison makeComparison(int targetSoC, int interval, int tid){
        Comparison comparison = new Comparison();
        // Unit hour
        Transaction t = transactionService.getByTid(tid);
        double energy = sampleService.estimateEnergy(targetSoC, tid);
        double totalTime = energy/maxPower;

        // Hour remains for current hour
        String startTime = t.getStartTime();
        double remainingHour = 1.0 - (ZonedDateTime.parse(startTime).getMinute()/60.0);
        String endTry = ZonedDateTime.parse(startTime).plusMinutes((long)(totalTime*60)).toString();

        comparison.setCurrentEndTime(endTry);
        // GET normal price
        double[] prices = Helper.getPriceList(dayPriceService.getPricesInNextHours(ZonedDateTime.now().toString(), 1));
        if(totalTime <= remainingHour){
            comparison.setCurrentCost(totalTime* prices[0]);
        }else{
            double timeLeft = totalTime - remainingHour;
            comparison.setCurrentCost(totalTime* prices[0] + timeLeft*prices[1]);
        }

        // GET smart Prices
        double[] orderedPrices = Helper.getPriceList(dayPriceService.getPricesInNextHours(ZonedDateTime.now().toString(), interval));
        Arrays.sort(orderedPrices);
        if(totalTime<=1.0){
            comparison.setSmartCost(totalTime*orderedPrices[0]);
        }else{
            double timeLeft = totalTime - 1.0;
            comparison.setSmartCost(orderedPrices[0] + (timeLeft*orderedPrices[1]));
        }
        // GET profit
        comparison.setProfit();

        return comparison;
    }

}
