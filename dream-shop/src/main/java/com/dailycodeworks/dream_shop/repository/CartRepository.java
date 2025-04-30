package com.dailycodeworks.dream_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodeworks.dream_shop.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	Cart findByUserId(Long userId);

  

}
