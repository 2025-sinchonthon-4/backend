package com.example.sinchonthon4.repository;

import com.example.sinchonthon4.entity.QuizLog;
import com.example.sinchonthon4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizLogRepository extends JpaRepository<QuizLog, Long> {

    List<QuizLog> findByUser(User user);
}
