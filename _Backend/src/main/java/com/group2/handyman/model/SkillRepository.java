package com.group2.handyman.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByName(String name);
    List<Skill> findByNameIn(Set<String> names);

    @Query("SELECT s FROM Skill s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Skill> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT s FROM Skill s JOIN s.workers w WHERE w.id = :workerId")
    List<Skill> findByWorkerId(@Param("workerId") Long workerId);

    @Query("SELECT s FROM Skill s JOIN s.jobs j WHERE j.id = :jobId")
    List<Skill> findByJobId(@Param("jobId") Long jobId);

    @Query("SELECT COUNT(s) FROM Skill s JOIN s.workers w WHERE w.id = :workerId")
    int countByWorkerId(@Param("workerId") Long workerId);
}
