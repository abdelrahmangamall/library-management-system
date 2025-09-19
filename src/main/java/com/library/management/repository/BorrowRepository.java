package com.library.management.repository;

import com.library.management.entity.BorrowRecord;
import com.library.management.entity.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByMemberMemberId(Long memberId);

    @Query("SELECT br FROM BorrowRecord br WHERE br.dueDate < :currentDate AND br.status = 'BORROWED'")
    List<BorrowRecord> findOverdueRecords(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.member.memberId = :memberId AND br.status = 'BORROWED'")
    long countActiveBorrowsByMember(@Param("memberId") Long memberId);

}