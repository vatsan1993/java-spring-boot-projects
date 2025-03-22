package com.dailycodeworks.dream_shop.service.image;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dailycodeworks.dream_shop.dto.ImageDto;
import com.dailycodeworks.dream_shop.entity.Image;

public interface IImageService {
	Image getImageById(Long id);
	void deleteImageById(Long id);
	
	void updateImage(MultipartFile file, Long imageId);
	List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
	
}
