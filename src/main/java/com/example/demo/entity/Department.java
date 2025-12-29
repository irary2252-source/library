package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DeptID")
    private Integer id;

    @Column(name = "DeptName")
    private String deptName;

    // Getters
    public Integer getId() { return id; }
    public String getDeptName() { return deptName; }
}