package com.sparta.newsfeedproject.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {
    @NotBlank
    @Size(min = 1, max = 20)
    private String title;

    @NotBlank
    private String content;
}
