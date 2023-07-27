package io.khamzin.likeinsta.controller;

import io.khamzin.likeinsta.entity.ImageEntity;
import io.khamzin.likeinsta.payload.response.MessageResponse;
import io.khamzin.likeinsta.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image upload successfully"));
    }

    @PostMapping("/{postId}")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId,
                                                             @RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageService.uploadImageToPost(file, principal, Long.parseLong(postId));
        return ResponseEntity.ok(new MessageResponse("Image upload successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageEntity> getImageForUser(Principal principal) {
        var imageToUser = imageService.getImageToUser(principal);
        return ResponseEntity.ok(imageToUser);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ImageEntity> getImageForPost(@PathVariable("postId") String postId) {
        ImageEntity imageToPost = imageService.getImageToPost(Long.parseLong(postId));
        return new ResponseEntity<>(imageToPost, HttpStatus.OK);
    }

}
