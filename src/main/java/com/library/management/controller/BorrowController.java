package com.library.management.controller;

import com.library.management.dto.BorrowDTO;
import com.library.management.dto.BorrowRequestDTO;
import com.library.management.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BorrowController {

    private final BorrowService borrowService;
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<BorrowDTO>> getAllBorrowRecords() {
        List<BorrowDTO> records = borrowService.getAllBorrowRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<BorrowDTO> getBorrowRecordById(@PathVariable Long id) {
        BorrowDTO record = borrowService.getBorrowRecordById(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<BorrowDTO>> getBorrowRecordsByMember(@PathVariable Long memberId) {
        List<BorrowDTO> records = borrowService.getBorrowRecordsByMember(memberId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<BorrowDTO>> getOverdueRecords() {
        List<BorrowDTO> records = borrowService.getOverdueRecords();
        return ResponseEntity.ok(records);
    }


//    @PostMapping("/borrow")
//    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
//    public ResponseEntity<BorrowDTO> borrowBook(@Valid @RequestBody BorrowRequestDTO request) {
//        BorrowDTO record = borrowService.borrowBook(
//                request.getBookId(),
//                request.getMemberId(),
//                request.getUserId(),
//                request.getDays()
//        );
//        return ResponseEntity.status(HttpStatus.CREATED).body(record);
//    }

    @PostMapping("/book")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<BorrowDTO> borrowBook(@Valid @RequestBody BorrowRequestDTO request) {
        BorrowDTO record = borrowService.borrowBook(request.getBookId(), request.getMemberId(),
                request.getUserId(), request.getDays());
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @PutMapping("/{borrowId}/return")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<BorrowDTO> returnBook(@PathVariable Long borrowId) {
        BorrowDTO record = borrowService.returnBook(borrowId);
        return ResponseEntity.ok(record);
    }

    @PostMapping("/update-overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateOverdueStatus() {
        borrowService.updateOverdueStatus();
        return ResponseEntity.ok(Map.of("message", "Overdue status updated successfully"));
    }

}