package com.example.sinchonthon4.repository;

import com.example.sinchonthon4.entity.User;
import com.example.sinchonthon4.entity.QuizLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizLogRepository extends JpaRepository<QuizLog, Long> {

    // 특정 회원이 푼 모든 퀴즈 기록을 조회하는 메서드
    List<QuizLog> findAllByUser(User user);

}