package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.exceptions.ApiException;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    @Autowired
    public AccountController(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @GetMapping
    public List<Account> findAll(){
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Account find(@PathVariable long id){
        Account account = accountService.find(id);
        if(account != null){
            return accountService.find(id);
        }
        throw new ApiException("account not found with given id: "+id, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{customerId}")
    public AccountResponse save(@RequestBody Account account, @PathVariable long customerId){
        Customer customer = customerService.find(customerId);
        if(customer != null){
            customer.getAccounts().add(account);
            account.setCustomer(customer);
            accountService.save(account);
        }
        else{
            throw new ApiException("no customer found with given customer id "+customerId, HttpStatus.NOT_FOUND);
        }
        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount(), new CustomerResponse(customerId, customer.getEmail(), customer.getSalary()));
    }

    @PutMapping("/{customerId}")
    public AccountResponse update(@RequestBody Account account, @PathVariable long customerId){
        Customer customer = customerService.find(customerId);
        Account foundAccount= null;
        for(Account cAccount: customer.getAccounts()){
            if(account.getId() == cAccount.getId()){
                foundAccount = cAccount;
            }
        }
        if(foundAccount == null){
            throw new ApiException("Account ("+account.getId()+") not found for this customer"+ customerId, HttpStatus.NOT_FOUND);
        }
        int indexOfFound = customer.getAccounts().indexOf(foundAccount);
        customer.getAccounts().set(indexOfFound, account);
        account.setCustomer(customer);
        accountService.save(account);

        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount(), new CustomerResponse(customerId, customer.getEmail(), customer.getSalary()));
    }

    @DeleteMapping("/{id}")
    public AccountResponse remove(@PathVariable long id){
        Account account= accountService.find(id);
        if(account != null){
            accountService.delete(id);
            return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount(), new CustomerResponse(account.getCustomer().getId(), account.getCustomer().getEmail(), account.getCustomer().getSalary()));
        }
        else{
            throw new ApiException("account not found with given customer id to delete "+id, HttpStatus.NOT_FOUND);
        }
    }
}
