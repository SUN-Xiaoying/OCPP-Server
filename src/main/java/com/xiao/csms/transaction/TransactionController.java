package com.xiao.csms.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TransactionController {
    @Autowired TransactionService service;

    @GetMapping("/transactions")
    public String showTransactions(Model m){
        List<Transaction> trans = service.getAll();
        m.addAttribute("trans", trans);
        return "server/transactions";
    }

    @GetMapping("/cleanTransactions")
    public String cleanTransactions(){
        service.cleanAll();
        return "redirect:/transactions";
    }
}
