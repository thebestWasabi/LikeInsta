package io.khamzin.likeinsta.entity;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostEntity {

    private Long id;
    private String title;
    private String caption;
    private int likes;

    private Set<UserEntity> likedUsers = new HashSet<>();
    private UserEntity user;
    private List<CommentEntity> comments = new ArrayList<>();
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

}
