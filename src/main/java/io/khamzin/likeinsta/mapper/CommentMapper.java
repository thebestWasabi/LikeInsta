package io.khamzin.likeinsta.mapper;

import io.khamzin.likeinsta.dto.CommentDTO;
import io.khamzin.likeinsta.entity.CommentEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDTO toDTO(CommentEntity comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .username(comment.getUsername())
                .message(comment.getMessage())
                .build();
    }

}
