package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "BookID")
    private String bookId; // 对应数据库 BookID (如 B001)

    @Column(name = "ISBN")
    private String isbn;   // 对应数据库 ISBN (如 9787...)

    @Column(name = "Title")
    private String title;

    @Column(name = "Category")
    private String category;

    @Column(name = "Author")
    private String author;

    @Column(name = "Price")
    private BigDecimal price;

    @Column(name = "Publisher")
    private String publisher;

    @Column(name = "Status")
    private String status = "在库";

    // --- Getters and Setters ---
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}