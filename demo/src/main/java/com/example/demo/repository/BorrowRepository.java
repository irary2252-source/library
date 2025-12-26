package com.example.demo.repository;

import com.example.demo.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    // (原有方法保持不变...)
    @Query(value = "SELECT * FROM borrow WHERE CardID = :cardId AND ReturnTime IS NULL", nativeQuery = true)
    List<Borrow> findByCardIdAndReturnTimeIsNull(@Param("cardId") String cardId);

    @Query(value = "SELECT * FROM borrow WHERE BookID = :isbn AND ReturnTime IS NULL", nativeQuery = true)
    Borrow findByIsbnAndReturnTimeIsNull(@Param("isbn") String isbn);

    // ✅ 新增：查询某人未缴纳罚款的记录 (已还书但 IsPaid=0)
    @Query(value = "SELECT * FROM borrow WHERE CardID = :cardId AND ReturnTime IS NOT NULL AND IsPaid = 0", nativeQuery = true)
    List<Borrow> findUnpaidFines(@Param("cardId") String cardId);
}