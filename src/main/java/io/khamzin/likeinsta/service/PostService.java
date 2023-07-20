package io.khamzin.likeinsta.service;

import io.khamzin.likeinsta.dto.PostDTO;
import io.khamzin.likeinsta.entity.PostEntity;
import io.khamzin.likeinsta.entity.UserEntity;
import io.khamzin.likeinsta.exception.PostNotFoundException;
import io.khamzin.likeinsta.repository.ImageRepository;
import io.khamzin.likeinsta.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageRepository imageRepository;

    public PostEntity getById(Long postId, UserEntity user) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post can't be found for username: " + user.getEmail()));
    }

    public PostEntity getById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post can't be found"));
    }


    public PostEntity creatPost(PostDTO postDTO, Principal principal) {
        var user = userService.getCurrentUser(principal);
        var post = new PostEntity();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        log.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public PostEntity getPostById(Long postId, Principal principal) {
        var user = userService.getCurrentUser(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post can't be found for username: " + user.getEmail()));
    }

    public List<PostEntity> getAllPostForUser(Principal principal) {
        var user = userService.getCurrentUser(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }


    public PostEntity likePost(Long postId, String username) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post can't be found"));

        var userLiked = post.getLikedUsers().stream()
                .filter(user -> user.equals(username))
                .findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        return postRepository.save(post);
    }


    public void deletePost(Long postId, Principal principal) {
        var post = getPostById(postId, principal);
        var image = imageRepository.findByPostId(postId);
        postRepository.delete(post);
        image.ifPresent(imageRepository::delete);
    }

}
