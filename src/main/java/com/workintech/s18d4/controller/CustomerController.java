package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.exceptions.ApiException;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> findAll(){
        return this.customerService.findAll();
    }

    @GetMapping("/{id}")
    public CustomerResponse findById(@PathVariable long id){
        Customer customer= this.customerService.find(id);
        if(customer != null){
            CustomerResponse customerResponse= new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSalary());
            return customerResponse;
        }
        throw new ApiException("customer is not found with given id: "+id, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public CustomerResponse save(@RequestBody Customer customer){
        Customer saved= customerService.save(customer);
        return new CustomerResponse(saved.getId(), saved.getEmail(), saved.getSalary());
    }

}
