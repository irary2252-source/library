package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrow")
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BorrowID") // ✅ 必须加，否则找不到列
    private Long id;

    @Column(name = "BookID")   // ✅ 对应数据库 BookID
    private String isbn;

    @Column(name = "CardID")   // ✅ 对应数据库 CardID
    private String cardId;

    @Column(name = "BorrowTime")
    private LocalDateTime borrowTime;

    @Column(name = "ReturnTime")
    private LocalDateTime returnTime;

    @Column(name = "DueTime")
    private LocalDateTime dueDate;

    @Column(name = "FineAmount")
    private BigDecimal fineAmount;

    @Column(name = "OverdueDays")
    private Integer overdueDays = 0;

    @Column(name = "IsPaid")
    private Boolean isPaid = false; // 0=未付, 1=已付

    // Getters and Setters (保持原样，只需确认 id 的 getter/setter 存在)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }
    public LocalDateTime getBorrowTime() { return borrowTime; }
    public void setBorrowTime(LocalDateTime borrowTime) { this.borrowTime = borrowTime; }
    public LocalDateTime getReturnTime() { return returnTime; }
    public void setReturnTime(LocalDateTime returnTime) { this.returnTime = returnTime; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
    public Integer getOverdueDays() { return overdueDays; }
    public void setOverdueDays(Integer overdueDays) { this.overdueDays = overdueDays; }
    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }
}