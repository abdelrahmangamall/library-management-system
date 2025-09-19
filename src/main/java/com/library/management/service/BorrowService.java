package com.library.management.service;

import com.library.management.dto.BorrowDTO;
import com.library.management.entity.*;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowRepository;
import com.library.management.repository.MemberRepository;
import com.library.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    private static final double DAILY_FINE_RATE = 1.0;

    public List<BorrowDTO> getAllBorrowRecords() {
        return borrowRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BorrowDTO getBorrowRecordById(Long id) {
        BorrowRecord record = borrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + id));
        return convertToDTO(record);
    }

    public List<BorrowDTO> getBorrowRecordsByMember(Long memberId) {
        return borrowRepository.findByMemberMemberId(memberId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowDTO> getOverdueRecords() {
        return borrowRepository.findOverdueRecords(LocalDate.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BorrowDTO borrowBook(Long bookId, Long memberId, Long userId, int days) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        if (!book.getIsAvailable()) {
            throw new RuntimeException("Book is not available for borrowing");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));

        if (!member.getIsActive()) {
            throw new RuntimeException("Member account is deactivated");
        }

        long activeBorrows = borrowRepository.countActiveBorrowsByMember(memberId);
        if (activeBorrows >= 5) {
            throw new RuntimeException("Member has reached the maximum number of borrowed books");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setMember(member);
        record.setUser(user);
        record.setDueDate(LocalDate.now().plusDays(days));
        record.setStatus(BorrowStatus.BORROWED);

        book.setIsAvailable(false);
        bookRepository.save(book);

        BorrowRecord savedRecord = borrowRepository.save(record);
        return convertToDTO(savedRecord);
    }

    @Transactional
    public BorrowDTO returnBook(Long borrowId) {
        BorrowRecord record = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found with id: " + borrowId));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new RuntimeException("Book has already been returned");
        }

        record.setReturnDate(LocalDateTime.now());
        record.setStatus(BorrowStatus.RETURNED);

        if (record.getDueDate().isBefore(LocalDate.now())) {
            long daysOverdue = LocalDate.now().toEpochDay() - record.getDueDate().toEpochDay();
            record.setFineAmount(daysOverdue * DAILY_FINE_RATE);
        }

        Book book = record.getBook();
        book.setIsAvailable(true);
        bookRepository.save(book);

        BorrowRecord updatedRecord = borrowRepository.save(record);
        return convertToDTO(updatedRecord);
    }

    @Transactional
    public void updateOverdueStatus() {
        List<BorrowRecord> overdueRecords = borrowRepository.findOverdueRecords(LocalDate.now());
        for (BorrowRecord record : overdueRecords) {
            record.setStatus(BorrowStatus.OVERDUE);
            borrowRepository.save(record);
        }
    }

    private BorrowDTO convertToDTO(BorrowRecord record) {
        BorrowDTO dto = new BorrowDTO();
        dto.setBorrowId(record.getBorrowId());
        dto.setBookId(record.getBook().getBookId());
        dto.setMemberId(record.getMember().getMemberId());
        dto.setUserId(record.getUser().getUserId());
        dto.setBorrowDate(record.getBorrowDate());
        dto.setDueDate(record.getDueDate());
        dto.setReturnDate(record.getReturnDate());
        dto.setFineAmount(record.getFineAmount());
        dto.setStatus(record.getStatus());
        return dto;
    }
}