package com.group2.handyman.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailOrUsername(String email, String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.location) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE u.averageRating >= :rating")
    List<User> findByRating(@Param("rating") double rating);

    @Query("SELECT u FROM User u WHERE LOWER(u.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<User> findByLocationContaining(@Param("location") String location);
}
