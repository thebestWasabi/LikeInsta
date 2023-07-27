package io.khamzin.likeinsta.controller;

import io.khamzin.likeinsta.dto.PostDTO;
import io.khamzin.likeinsta.mapper.PostMapper;
import io.khamzin.likeinsta.payload.response.MessageResponse;
import io.khamzin.likeinsta.service.PostService;
import io.khamzin.likeinsta.validation.ResponseErrorValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Validated PostDTO postDTO,
                                        BindingResult bindingResult,
                                        Principal principal) {
        var errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        var post = postService.creatPost(postDTO, principal);
        var postCreated = postMapper.toDTO(post);
        return new ResponseEntity<>(postCreated, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> allPostDTOList = postService.getAllPosts().stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allPostDTOList, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostDTO>> getAllPostsFromUser(Principal principal) {
        List<PostDTO> allPostDTOList = postService.getAllPostForUser(principal).stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allPostDTOList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") String postId,
                                            @PathVariable("username") String username) {
        var post = postService.likePost(Long.parseLong(postId), username);
        var postDTO = postMapper.toDTO(post);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId,
                                                      Principal principal) {
        postService.deletePost(Long.parseLong(postId), principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }

}
