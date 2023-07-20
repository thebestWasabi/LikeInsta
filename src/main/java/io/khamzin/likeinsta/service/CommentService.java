package io.khamzin.likeinsta.service;

import io.khamzin.likeinsta.dto.CommentDTO;
import io.khamzin.likeinsta.entity.CommentEntity;
import io.khamzin.likeinsta.entity.PostEntity;
import io.khamzin.likeinsta.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    public CommentEntity saveComment(Long postId, CommentDTO commentDTO, Principal principal) {
        var user = userService.getCurrentUser(principal);
        PostEntity post = postService.getById(postId, user);

        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        log.info("Saving Comment for Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<CommentEntity> getAllCommentsForPost(Long postId) {
        var post = postService.getById(postId);
        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long commentId) {
        var comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

}
