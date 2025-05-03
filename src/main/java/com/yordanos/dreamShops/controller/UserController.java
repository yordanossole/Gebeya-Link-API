package com.yordanos.dreamShops.controller;

import com.yordanos.dreamShops.dto.ImageDto;
import com.yordanos.dreamShops.dto.UserDto;
import com.yordanos.dreamShops.exceptions.AlreadyExistsException;
import com.yordanos.dreamShops.exceptions.ResourceNotFoundException;
import com.yordanos.dreamShops.model.Image;
import com.yordanos.dreamShops.model.User;
import com.yordanos.dreamShops.request.CreateUserRequest;
import com.yordanos.dreamShops.request.UpdateUserRequest;
import com.yordanos.dreamShops.response.ApiResponse;
import com.yordanos.dreamShops.service.image.IImageService;
import com.yordanos.dreamShops.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@CrossOrigin
@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final IImageService imageService;

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getUser() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userService.getUserByUsername(username);
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/user/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @RequestParam(required = false) Long userId) {
        try {
            if (userId == null) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                userId = userService.getUserByUsername(username);
            }
            User user = userService.updateUser(request, userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<ApiResponse> deleteUser(@RequestParam(required = false) Long userId) {
        try {
            if (userId == null) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                userId = userService.getUserByUsername(username);
            }
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

//    image controllers
    @PostMapping("/user/image/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam MultipartFile file) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userService.getUserByUsername(username);
            ImageDto imageDtos = imageService.saveProfilePhoto(file, userId);
            return ResponseEntity.ok(new ApiResponse("Upload success!", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", e.getMessage()));
        }
    }

    @GetMapping("/user/image/download/{imageId}")
    public ResponseEntity<?> downloadImage(@PathVariable Long imageId, HttpEntity<Object> httpEntity) throws SQLException {
        try {
            Image image = imageService.getImageById(imageId);
            ByteArrayResource resource = new ByteArrayResource(imageService.getImageBytes(image));
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                    .body(resource);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/user/image/update")
    public ResponseEntity<ApiResponse> updateImage(@RequestParam MultipartFile file) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long imageId = userService.getImageId(username);
            Image image = imageService.getImageById(imageId);
            ImageDto imageDto;
            if (image != null) {
                image = imageService.updateImage(file, imageId);
                imageDto = imageService.convertToDto(image);
                return ResponseEntity.ok(new ApiResponse("Update success!", imageDto));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!", INTERNAL_SERVER_ERROR));
        }
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Update failed!", NOT_FOUND));
    }

    @DeleteMapping("/user/image/delete")
    public ResponseEntity<ApiResponse> deleteImage() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long imageId = userService.getImageId(username);
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete success!", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed!", INTERNAL_SERVER_ERROR));
    }
}
