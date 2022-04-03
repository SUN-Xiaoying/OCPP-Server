package com.xiao.csms.dayprice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DayPriceController {
    @Autowired
    DayPriceService dayPriceService;

    @GetMapping("/dayPrice")
    public String showDayPrice(Model m){
        dayPriceService.cleanCache();
        dayPriceService.saveToday();
        List<DayPrice> prices = dayPriceService.getAll();
        m.addAttribute("prices", prices);
        return "prices";
    }

}
