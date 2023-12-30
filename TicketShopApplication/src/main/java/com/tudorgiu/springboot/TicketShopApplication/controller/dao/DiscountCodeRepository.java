package com.tudorgiu.springboot.TicketShopApplication.controller.dao;

import com.tudorgiu.springboot.TicketShopApplication.model.entity.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountCodeRepository extends JpaRepository<DiscountCode, Integer> {
}
