package com.group2.handyman.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByWorker(Worker worker);
    List<Job> findByClient(User client);

    @Query("SELECT j FROM Job j WHERE j.budget BETWEEN :minBudget AND :maxBudget")
    List<Job> findByBudgetBetween(@Param("minBudget") double minBudget, @Param("maxBudget") double maxBudget);

    @Query("SELECT j FROM Job j JOIN j.skills s WHERE s.name IN :skillNames")
    List<Job> findBySkillsNameIn(@Param("skillNames") Set<String> skillNames);

    @Query("SELECT j FROM Job j WHERE j.client.location LIKE %:location%")
    List<Job> findByClientLocation(@Param("location") String location);

    @Query("SELECT j FROM Job j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Job> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT j FROM Job j WHERE j.client.id = :userId AND j.isCompleted = :completed")
    List<Job> findByClientIdAndIsCompleted(@Param("userId") Long userId, @Param("completed") boolean completed);

    @Query("SELECT j FROM Job j WHERE j.worker.id = :workerId AND j.isCompleted = :completed")
    List<Job> findByWorkerIdAndIsCompleted(@Param("workerId") Long workerId, @Param("completed") boolean completed);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.worker.id = :workerId AND j.isCompleted = :completed")
    int countByWorkerIdAndIsCompleted(@Param("workerId") Long workerId, @Param("completed") boolean completed);

    @Query("SELECT j FROM Job j ORDER BY j.createdAt DESC")
    List<Job> findRecentJobs(@Param("limit") int limit);

    @Query("SELECT j FROM Job j WHERE j.isCompleted = false AND j.worker IS NULL")
    List<Job> findUnassignedJobs();
}
