package com.mshenguDev.hfservice.imageManager.service;

import com.mshenguDev.hfservice.imageManager.entity.ImageData;
import com.mshenguDev.hfservice.imageManager.repository.ImageRepository;
import com.mshenguDev.hfservice.imageManager.util.ImageUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {
    private ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    @Transactional
    public String uploadImage(MultipartFile file) throws IOException{
        ImageData imageData = ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(file.getBytes())
                .build();
        imageRepository.save(imageData);
        return "File uploaded successfully: " + file.getOriginalFilename();

    }

    public byte[] downloadImage(String fileName){
        Optional<ImageData> dbImageData = imageRepository.findImageByName(fileName);
        byte[] images = ImageUtil.decompressImage(dbImageData.get().getImageData());
    }
}
