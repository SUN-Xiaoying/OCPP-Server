package com.xiao.csms.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class SampleController {
    @Autowired SampleService service;

    @GetMapping("/sample/{transactionId}")
    public String showSample(@PathVariable("transactionId") int tid, Model m){
        List<Sample> samples = service.getByTid(tid);
        m.addAttribute("samples",samples);
        m.addAttribute("tid",tid);
        return "server/sample";
    }

    @GetMapping("/sample/clean/{transactionId}")
    public String cleanSample(@PathVariable("transactionId") int tid, Model m){
        service.deleteByTid(tid);
        return "redirect:/transactions";
    }

}
