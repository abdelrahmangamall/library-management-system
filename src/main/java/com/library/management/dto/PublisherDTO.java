package com.library.management.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PublisherDTO {
    private Long publisherId;
    private String name;
    private String address;
    private String website;
    private LocalDateTime createdAt;
    private Long bookCount;
}