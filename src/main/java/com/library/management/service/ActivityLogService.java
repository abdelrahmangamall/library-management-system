package com.library.management.service;

import com.library.management.entity.User;
import com.library.management.entity.UserActivity;
import com.library.management.repository.UserActivityRepository;
import com.library.management.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final UserActivityRepository activityRepository;
    private final UserRepository userRepository;

    @Transactional
    public void logActivity(String action, String entityType, Long entityId, String description) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                return;
            }

            String username = auth.getName();
            User user = userRepository.findByUsername(username)
                    .orElse(null);

            if (user == null) {
                return;
            }

            UserActivity activity = new UserActivity();
            activity.setAction(action);
            activity.setEntityType(entityType);
            activity.setEntityId(entityId);
            activity.setDescription(description);
            activity.setUser(user);

            // Get request details
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                activity.setIpAddress(getClientIpAddress(request));
                activity.setUserAgent(request.getHeader("User-Agent"));
            }

            activityRepository.save(activity);
        } catch (Exception e) {
            System.err.println("Failed to log activity: " + e.getMessage());
        }
    }

    public Page<UserActivity> getUserActivities(Long userId, Pageable pageable) {
        return activityRepository.findByUserUserIdOrderByTimestampDesc(userId, pageable);
    }

    public Page<UserActivity> getAllActivities(Pageable pageable) {
        return activityRepository.findAllByOrderByTimestampDesc(pageable);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}