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
    private DiscountCodeRepository discountCodeRepository;

    public DiscountCodeService(DiscountCodeRepository discountCodeRepository) {
        this.discountCodeRepository = discountCodeRepository;
    }

    public List<DiscountCode> findByCode(String code){
        List<DiscountCode> list = new ArrayList<>();

        List<DiscountCode> randList = discountCodeRepository.findAll();

        for(DiscountCode pivot : discountCodeRepository.findAll()){
            if(pivot.getCode().equals(code))
                list.add(pivot);
        }

        return list;
    }

    public DiscountCode findById(int id){
        Optional<DiscountCode> discountCodeFound = discountCodeRepository.findById(id);
        if(discountCodeFound.isPresent())
            return discountCodeFound.get();
        else
            throw new NoSuchElementException("There is no discount code with id " + id);
    }

    public void save(DiscountCode discountCode){
        discountCodeRepository.save(discountCode);
    }

    public void deleteById(int id){
        discountCodeRepository.deleteById(id);
    }
}
