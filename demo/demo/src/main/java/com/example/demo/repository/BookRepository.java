package com.example.demo.repository;

import com.example.demo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

    @Override
    @Query(value = "SELECT * FROM book", nativeQuery = true)
    List<Book> findAll();

    // ✅ 升级：支持搜索 书名、作者、分类、ISBN
    @Query(value = "SELECT * FROM book WHERE " +
            "Title LIKE CONCAT('%', :keyword, '%') OR " +
            "Author LIKE CONCAT('%', :keyword, '%') OR " +
            "Category LIKE CONCAT('%', :keyword, '%') OR " +
            "BookID LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Book> searchByTitle(@Param("keyword") String keyword);
}