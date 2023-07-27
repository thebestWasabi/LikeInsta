package io.khamzin.likeinsta.controller;

import io.khamzin.likeinsta.dto.CommentDTO;
import io.khamzin.likeinsta.mapper.CommentMapper;
import io.khamzin.likeinsta.payload.response.MessageResponse;
import io.khamzin.likeinsta.service.CommentService;
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
@RequestMapping("api/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@RequestBody @Validated CommentDTO commentDTO,
                                           @PathVariable("postId") String postId,
                                           BindingResult bindingResult,
                                           Principal principal) {
        var errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        var comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentDTO commentCreated = commentMapper.toDTO(comment);
        return new ResponseEntity<>(commentCreated, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToPost(@PathVariable("postId") String postId) {
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId)).stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return ResponseEntity.ok(new MessageResponse("Comment was deleted"));
    }

}
