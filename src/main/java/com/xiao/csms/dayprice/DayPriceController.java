package com.xiao.csms.dayprice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DayPriceController {
    @Autowired
    DayPriceService dayPriceService;

    @GetMapping("/dayPrice")
    public String showDayPrice(Model m){
        dayPriceService.saveToday();
        List<DayPrice> prices = dayPriceService.getAll();
        m.addAttribute("prices", prices);
        return "prices";
    }

    @GetMapping("/dayPrice/update")
    public String refreshPrice(RedirectAttributes ra){
        dayPriceService.cleanCache();
        ra.addFlashAttribute("message","Prices Updated");
        return "redirect:/dayPrice";
    }

}
