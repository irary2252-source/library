package com.example.demo.repository;

import com.example.demo.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Integer> {
    // 根据读者卡号查询罚款记录
    List<Fine> findByCardId(String cardId);
}