package com.tudorgiu.springboot.TicketShopApplication.controller.service;

import com.tudorgiu.springboot.TicketShopApplication.controller.dao.DiscountCodeRepository;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.DiscountCode;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.Ticket;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class DiscountCodeService {

    // Repository for interacting with the DiscountCode entity in the database
    private DiscountCodeRepository discountCodeRepository;

    // Constructor for Dependency Injection
    public DiscountCodeService(DiscountCodeRepository discountCodeRepository) {
        this.discountCodeRepository = discountCodeRepository;
    }

    // Find DiscountCode(s) by code
    public List<DiscountCode> findByCode(String code){
        List<DiscountCode> list = new ArrayList<>();

        // Retrieve all DiscountCodes from the repository
        List<DiscountCode> randList = discountCodeRepository.findAll();

        // Iterate through the DiscountCodes to find matching codes
        for(DiscountCode pivot : discountCodeRepository.findAll()){
            if(pivot.getCode().equals(code))
                list.add(pivot);
        }

        return list;
    }

    // Find DiscountCode by id
    public DiscountCode findById(int id){
        // Attempt to find the DiscountCode by id in the repository
        Optional<DiscountCode> discountCodeFound = discountCodeRepository.findById(id);

        // If found, return it; otherwise, throw an exception
        if(discountCodeFound.isPresent())
            return discountCodeFound.get();
        else
            throw new NoSuchElementException("There is no discount code with id " + id);
    }

    // Save a DiscountCode to the repository
    public void save(DiscountCode discountCode){
        discountCodeRepository.save(discountCode);
    }

    // Delete a DiscountCode by id from the repository
    public void deleteById(int id){
        discountCodeRepository.deleteById(id);
    }
}
