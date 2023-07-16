package io.khamzin.likeinsta.entity;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

public class CommentEntity {

    private Long id;
    private PostEntity post;
    private String username;
    private Long userId;
    private String message;
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

}
