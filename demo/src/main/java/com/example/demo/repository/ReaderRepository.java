package com.example.demo.repository;

import com.example.demo.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, String> {

    @Override
    @Query(value = "SELECT * FROM reader WHERE CardID = :id", nativeQuery = true)
    Optional<Reader> findById(@Param("id") String id);

    // ✅ 新增：使用 SQL 校验读者登录
    @Query(value = "SELECT * FROM reader WHERE CardID = :cardId AND Password = :password", nativeQuery = true)
    Reader login(@Param("cardId") String cardId, @Param("password") String password);
}