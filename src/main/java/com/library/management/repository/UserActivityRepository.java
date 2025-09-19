package com.library.management.repository;

import com.library.management.entity.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    Page<UserActivity> findByUserUserIdOrderByTimestampDesc(Long userId, Pageable pageable);

    Page<UserActivity> findAllByOrderByTimestampDesc(Pageable pageable);
//
//    Page<UserActivity> findByActionContainingIgnoreCaseOrderByTimestampDesc(String action, Pageable pageable);
//
//    Page<UserActivity> findByEntityTypeOrderByTimestampDesc(String entityType, Pageable pageable);
//
//    Page<UserActivity> findByTimestampBetweenOrderByTimestampDesc(
//            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
//
//    @Query("SELECT ua FROM UserActivity ua WHERE ua.timestamp >= :since ORDER BY ua.timestamp DESC")
//    Page<UserActivity> findRecentActivities(@Param("since") LocalDateTime since, Pageable pageable);
}