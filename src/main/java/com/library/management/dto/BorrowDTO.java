package com.library.management.dto;

import com.library.management.entity.BorrowStatus;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BorrowDTO {
    private Long borrowId;
    private Long bookId;
    private Long memberId;
    private Long userId;
    private LocalDateTime borrowDate;
    private LocalDate dueDate;
    private LocalDateTime returnDate;
    private Double fineAmount;
    private BorrowStatus status;
}