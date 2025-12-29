package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fine")
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FineID")
    private Integer fineId;

    @Column(name = "BorrowID")
    private Integer borrowId;

    @Column(name = "CardID")
    private String cardId;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "PaidDate")
    private LocalDateTime paidDate;

    @Column(name = "IsPaid")
    private Boolean isPaid = false;

    // --- Getters and Setters ---
    public Integer getFineId() { return fineId; }
    public void setFineId(Integer fineId) { this.fineId = fineId; }

    public Integer getBorrowId() { return borrowId; }
    public void setBorrowId(Integer borrowId) { this.borrowId = borrowId; }

    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDateTime paidDate) { this.paidDate = paidDate; }

    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }
}