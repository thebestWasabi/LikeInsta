package io.khamzin.likeinsta.mapper;

import io.khamzin.likeinsta.dto.PostDTO;
import io.khamzin.likeinsta.entity.PostEntity;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDTO toDTO(PostEntity post) {
        return PostDTO.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .caption(post.getCaption())
                .likes(post.getLikes())
                .usersLiked(post.getLikedUsers())
                .location(post.getLocation())
                .title(post.getTitle())
                .build();
    }

}
