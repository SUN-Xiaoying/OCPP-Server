package com.xiao.csms.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SampleController {
    @Autowired SampleService service;
    @GetMapping("/sample/{transactionId}")
    public String showSample(@PathVariable("transactionId") int tid, Model m){
        List<Sample> samples = service.getByTid(tid);
        if(samples.size()<5){
            return "redirect:/transaction/delete/{transactionId}";
        }
        m.addAttribute("samples",samples);
        m.addAttribute("tid",tid);
        return "server/sample";
    }

    @GetMapping("/sample/delete/{transactionId}")
    public String deleteSamples(@PathVariable("transactionId") int tid, RedirectAttributes ra){
        service.deleteByTid(tid);
        ra.addFlashAttribute("message", "Transaction "+tid+" has been deleted" );
        return "redirect:/transactions";
    }
}
