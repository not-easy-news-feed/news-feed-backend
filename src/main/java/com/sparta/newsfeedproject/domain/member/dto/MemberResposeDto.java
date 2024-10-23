package com.sparta.newsfeedproject.domain.member.dto;

import lombok.Getter;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDateTime;

@Getter
public class MemberResposeDto {
    private Long id;
    private String email;
    private String name;
    private LocalDateTime modifiedAt;
}

