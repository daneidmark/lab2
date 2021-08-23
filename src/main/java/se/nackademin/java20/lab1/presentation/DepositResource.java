package se.nackademin.java20.lab1.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import se.nackademin.java20.lab1.application.PersonalFinanceService;
import se.nackademin.java20.lab1.domain.Account;

@Controller
public class DepositResource {

    private final PersonalFinanceService personalFinanceService;

    public DepositResource(PersonalFinanceService personalFinanceService) {
        this.personalFinanceService = personalFinanceService;
    }

    @GetMapping("/bank/{userId}/account")
    public String openAccount(@PathVariable("userId") String userId) {
        final Account account = personalFinanceService.openAccount(userId);
        return "redirect:/bank/" + account.getHolder() + "/account/" + account.getId();
    }

    @GetMapping("/bank/{userId}/account/{accountId}")
    public String depositForm(@PathVariable("userId") String userId, @PathVariable("accountId") Long accountId, Model model) {
        Account account = personalFinanceService.findAccount(userId, accountId);
        model.addAttribute("balance", account.getBalance());
        model.addAttribute("userId", account.getHolder());
        model.addAttribute("accountId", account.getId());
        model.addAttribute("deposit", new TransferDto());
        model.addAttribute("withdraw", new TransferDto());
        return "deposit";
    }

    @PostMapping("/bank/{userId}/account/{accountId}/deposit")
    public String deposit(@PathVariable("userId") String userId, @PathVariable("accountId") Long accountId, @ModelAttribute TransferDto deposit, Model model) {
        final Account account = personalFinanceService.deposit(accountId, userId, deposit.getAmount());
        return "redirect:/bank/" + account.getHolder() + "/account/" + account.getId();
    }


    @PostMapping("/bank/{userId}/account/{accountId}/withdraw")
    public String withdraw(@PathVariable("userId") String userId, @PathVariable("accountId") Long accountId, @ModelAttribute TransferDto withdraw, Model model) {
        final Account account = personalFinanceService.withdraw(accountId, userId, withdraw.getAmount());
        return "redirect:/bank/" + account.getHolder() + "/account/" + account.getId();
    }


}
