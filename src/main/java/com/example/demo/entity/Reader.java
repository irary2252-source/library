package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reader")
public class Reader {
    @Id
    @Column(name = "CardID")
    private String cardId;

    @Column(name = "name")
    private String name;

    @Column(name = "Sex")
    private String sex = "男";

    @Column(name = "DeptID")
    private Integer deptId;

    @Column(name = "Type")
    private String type = "学生";

    @Column(name = "Level")
    private String level;

    @Column(name = "Password")
    private String password = "123456"; // 默认密码

    @Column(name = "MaxBorrow")
    private Integer maxBorrow = 5; // 默认最大借5本

    @Column(name = "BorrowDays")
    private Integer borrowDays = 30; // 默认借30天

    @Column(name = "CurrentBorrow")
    private Integer currentBorrow = 0; // 当前已借数量

    // --- ✅ 新增字段结束 ---

    @Column(name = "IsActive")
    private Boolean isActive = true;

    // --- Getters and Setters ---
    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    // --- ✅ 新增 Getter/Setter ---
    public Integer getMaxBorrow() { return maxBorrow; }
    public void setMaxBorrow(Integer maxBorrow) { this.maxBorrow = maxBorrow; }

    public Integer getBorrowDays() { return borrowDays; }
    public void setBorrowDays(Integer borrowDays) { this.borrowDays = borrowDays; }

    public Integer getCurrentBorrow() { return currentBorrow; }
    public void setCurrentBorrow(Integer currentBorrow) { this.currentBorrow = currentBorrow; }
    // ---------------------------

    public Boolean getActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }

    // ✅ 新增：密码的 Getter/Setter
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}