package com.yordanos.dreamShops.service.image;

import com.yordanos.dreamShops.dto.ImageDto;
import com.yordanos.dreamShops.model.Image;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

public interface IImageService {
    Image getImageById (Long id);
    void deleteImageById (Long id);
    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);

    ImageDto saveProfilePhoto(MultipartFile file, Long userId);

    Image updateImage(MultipartFile file, Long imageId);

    ImageDto convertToDto(Image image);

    @Transactional
        // Ensures Blob access happens in a transaction
    byte[] getImageBytes(Image image) throws SQLException;
}
