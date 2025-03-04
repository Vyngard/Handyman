package com.group2.handyman.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUserAndWorker(User user, Worker worker);
    List<Message> findByWorkerAndUser(Worker worker, User user);
    List<Message> findByUser(User user);
    List<Message> findByWorker(Worker worker);

    @Query("SELECT m FROM Message m WHERE m.user.id = :userId AND m.worker.id = :workerId ORDER BY m.timestamp DESC LIMIT :limit")
    List<Message> findRecentMessages(@Param("userId") Long userId, @Param("workerId") Long workerId, @Param("limit") int limit);

    @Query("SELECT m FROM Message m WHERE m.user.id = :userId AND m.worker.id = :workerId AND LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Message> searchMessages(@Param("userId") Long userId, @Param("workerId") Long workerId, @Param("keyword") String keyword);

    @Query("SELECT m FROM Message m WHERE m.user.id = :userId AND m.worker.id = :workerId AND m.timestamp BETWEEN :startDate AND :endDate")
    List<Message> findByDateRange(@Param("userId") Long userId, @Param("workerId") Long workerId, 
                                 @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
