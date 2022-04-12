package com.xiao.csms.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/transaction/delete/{tid}")
    public String deleteTransaction(@PathVariable("tid") int tid, RedirectAttributes ra){
        service.deleteByTransactionId(tid);
        ra.addFlashAttribute("message", "Transaction "+tid+" has been deleted" );
        return "redirect:/transactions";
    }

    @GetMapping("/transaction/cleanAll")
    public String cleanTransactions(){
        service.cleanAll();
        return "redirect:/transactions";
    }
}
