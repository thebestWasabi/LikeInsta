package io.khamzin.likeinsta.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PostDTO {

    private Long id;
    private String title;
    private String caption;
    private String location;
    private String username;
    private Integer likes;
    private Set<String> usersLiked;
}
