package com.dailycodeworks.dream_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodeworks.dream_shop.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	

}
