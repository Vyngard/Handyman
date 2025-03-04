package com.group2.handyman.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    @Query("SELECT w FROM Worker w JOIN w.skills s WHERE s.name = :skill")
    List<Worker> findBySkillsName(@Param("skill") String skill);
    
    @Query("SELECT w FROM Worker w WHERE w.averageRating >= :rating")
    List<Worker> findByRating(@Param("rating") double rating);

    @Query("SELECT w FROM Worker w WHERE w.location LIKE %:location%")
    List<Worker> findByLocationContaining(@Param("location") String location);

    @Query("SELECT w FROM Worker w WHERE LOWER(w.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(w.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(w.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Worker> findByKeyword(@Param("keyword") String keyword);

    Worker findByEmailOrUsername(String email, String username);
}
