package io.khamzin.likeinsta.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentDTO {

    private Long id;
    @NotBlank
    private String message;
    private String username;
}
