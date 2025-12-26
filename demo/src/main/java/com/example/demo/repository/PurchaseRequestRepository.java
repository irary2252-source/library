package com.example.demo.repository;

import com.example.demo.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Integer> {
    // 查询某位读者的所有荐购
    List<PurchaseRequest> findByReaderIdOrderByRequestDateDesc(String readerId);

    // 查询所有待处理的请求 (管理员用)
    List<PurchaseRequest> findByStatusOrderByRequestDateDesc(String status);
}