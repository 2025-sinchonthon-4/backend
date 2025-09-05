package com.example.sinchonthon4.repository;

import com.example.sinchonthon4.entity.Category;
import com.example.sinchonthon4.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // 1개 → N개로 변경
    @Query(value = "SELECT * FROM quiz ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Quiz> findRandomQuizzes(@org.springframework.data.repository.query.Param("count") int count);

    List<Quiz> findByCategoryIn(Collection<Category> categories);
}