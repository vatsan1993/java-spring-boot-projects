package com.dailycodeworks.dream_shop.service.image;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.dailycodeworks.dream_shop.DreamShopApplication;
import com.dailycodeworks.dream_shop.dto.ImageDto;
import com.dailycodeworks.dream_shop.entity.Image;
import com.dailycodeworks.dream_shop.entity.Product;
import com.dailycodeworks.dream_shop.exceptions.ResourceNotFoundException;
import com.dailycodeworks.dream_shop.repository.CategoryRepository;
import com.dailycodeworks.dream_shop.repository.ImageRepository;
import com.dailycodeworks.dream_shop.service.product.IProductService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ImageService implements IImageService {

	
	private final ImageRepository imageRepository;
	@Autowired
	private final IProductService productService;


	@Override
	public Image getImageById(Long id) {
		// TODO Auto-generated method stub
		return imageRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("No Image found with the id: "+ id));
	}

	@Override
	public void deleteImageById(Long id) {
		// TODO Auto-generated method stub
		imageRepository.findById(id).ifPresentOrElse(
				imageRepository::delete, () -> {
					throw new ResourceNotFoundException("No Image found with id: "+ id);
				}
		);
	}

	@Override
	public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
		System.out.println(productId);
		Product product = productService.getProductById(productId);
		List<ImageDto> imageDtos = new ArrayList<>();
		for(MultipartFile file : files) {
			try {
				Image image = new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setProduct(product);
				
				String buildDownloadUrl = "api/v1/images/image/download/";
				String downloadUrl = buildDownloadUrl+image.getId();
				image.setDownloadUrl(downloadUrl);
				Image savedImage = imageRepository.save(image);
				
				savedImage.setDownloadUrl(buildDownloadUrl +savedImage.getId());
				imageRepository.save(savedImage);
				
				ImageDto imageDto = new ImageDto();
				imageDto.setImageId(savedImage.getId());
				imageDto.setImageName(savedImage.getFileName());
				imageDto.setImageUrl(savedImage.getDownloadUrl());
				imageDtos.add(imageDto);
				
			}catch(IOException | SQLException e){
				throw new RuntimeException(e.getMessage());
			}
		}
		return imageDtos;
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		// TODO Auto-generated method stub
		try {
			Image image = getImageById(imageId);
			image.setFileName(file.getOriginalFilename());
			image.setFileType(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
			imageRepository.save(image);
			
		}catch(IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

}
