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

    // H2 DB에서는 RAND() 대신 RANDOM() 사용
    @Query(value = "SELECT * FROM quiz ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Quiz findRandomQuiz();

    List<Quiz> findByCategoryIn(Collection<Category> categories);
}