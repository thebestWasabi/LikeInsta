package io.khamzin.likeinsta.service;

import io.khamzin.likeinsta.entity.ImageEntity;
import io.khamzin.likeinsta.exception.ImageNotFoundException;
import io.khamzin.likeinsta.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserService userService;
    private final PostService postService;

    public ImageEntity uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        var user = userService.getCurrentUser(principal);
        log.info("Upload image profile to User: {}", user.getUsername());

        var userProfileImage = imageRepository.findByUserId(user.getId())
                .orElse(null);

        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }

        ImageEntity image = new ImageEntity();
        image.setUserId(user.getId());
        image.setImageBytes(compressBytes(file.getBytes()));
        image.setName(file.getOriginalFilename());
        return imageRepository.save(image);
    }

    public ImageEntity uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        var user = userService.getCurrentUser(principal);
        var post = user.getPosts().stream()
                .filter(p -> p.getId().equals(postId))
                .collect(toSinglePostCollector());
        var image = new ImageEntity();
        image.setPostId(post.getId());
        image.setImageBytes(file.getBytes());
        image.setImageBytes(compressBytes(file.getBytes()));
        image.setName(file.getOriginalFilename());
        log.info("Upload image to Post: {}", post.getId());
        return imageRepository.save(image);
    }

    public ImageEntity getImageToUser(Principal principal) {
        var user = userService.getCurrentUser(principal);
        var image = imageRepository.findByUserId(user.getId())
                .orElse(null);
        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(decompressBytes(image.getImageBytes()));
        }
        return image;
    }

    public ImageEntity getImageToPost(Long postId) {
        ImageEntity image = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Can't find image to Post: " + postId));
        if (!ObjectUtils.isEmpty(image)) {
            image.setImageBytes(decompressBytes(image.getImageBytes()));
        }
        return image;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Can't compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error("Can't decompress Bytes");
        }
        return outputStream.toByteArray();
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

}
