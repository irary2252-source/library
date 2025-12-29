package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.entity.PurchaseRequest;
import com.example.demo.entity.Reader;
import com.example.demo.entity.SystemConfig; // 确保 SystemConfig 实体被导入
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.SystemConfigRepository; // ✅ 导入 Repository
import com.example.demo.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LibraryController {

    @Autowired private LibraryService libraryService;
    @Autowired private BookRepository bookRepo;
    @Autowired private SystemConfigRepository configRepo; // ✅ 注入 SystemConfigRepository

    // =====================================================================
    //  ✅ 1. 统一登录认证接口
    // =====================================================================

    @PostMapping("/login/unified")
    public Map<String, Object> unifiedLogin(@RequestBody Map<String, String> payload) {
        String identifier = payload.get("identifier");
        String password = payload.get("password");

        Map<String, String> result = libraryService.unifiedLogin(identifier, password);
        String role = result.get("role");
        String id = result.get("id");

        if ("admin".equals(role)) {
            return Map.of("success", true, "role", "admin", "id", id);
        } else if ("reader".equals(role)) {
            return Map.of("success", true, "role", "reader", "id", id);
        } else {
            return Map.of("success", false, "msg", "账号或密码错误");
        }
    }

    // =====================================================================
    //  ✅ 2. 个人中心 (修改密码)
    // =====================================================================

    @PostMapping("/reader/password")
    public Map<String, Object> updateReaderPassword(@RequestBody Map<String, String> payload) {
        try {
            String msg = libraryService.updateReaderPassword(
                    payload.get("cardId"),
                    payload.get("oldPass"),
                    payload.get("newPass")
            );
            return Map.of("success", true, "msg", msg);
        } catch (Exception e) {
            return Map.of("success", false, "msg", e.getMessage());
        }
    }

    @PostMapping("/admin/password")
    public Map<String, Object> updateAdminPassword(@RequestBody Map<String, String> payload) {
        try {
            String msg = libraryService.updateAdminPassword(
                    payload.get("username"),
                    payload.get("oldPass"),
                    payload.get("newPass")
            );
            return Map.of("success", true, "msg", msg);
        } catch (Exception e) {
            return Map.of("success", false, "msg", e.getMessage());
        }
    }

    // =====================================================================
    //  ✅ 3. 图书管理接口 (查询、入库)
    // =====================================================================

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    @GetMapping("/books/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookRepo.searchByTitle(keyword);
    }

    @PostMapping("/book")
    public Book addBook(@RequestBody Book book) {
        if (book.getStatus() == null) book.setStatus("在库");
        if (book.getCategory() == null) book.setCategory("其他");
        return bookRepo.save(book);
    }

    // =====================================================================
    //  ✅ 4. 读者管理接口 (注册、注销)
    // =====================================================================

    @PostMapping("/reader")
    public Object addReader(@RequestBody Map<String, Object> payload) {
        try {
            Reader reader = new Reader();
            reader.setCardId((String) payload.get("cardId"));
            reader.setName((String) payload.get("name"));
            reader.setPassword((String) payload.get("password"));
            reader.setSex((String) payload.get("sex"));
            reader.setType((String) payload.get("type"));
            reader.setLevel((String) payload.get("level"));

            String deptInput = String.valueOf(payload.get("deptId"));
            return libraryService.createReader(reader, deptInput);
        } catch (RuntimeException e) {
            return Map.of("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "注册失败，请检查卡号是否重复");
        }
    }

    @PostMapping("/reader/cancel")
    public Map<String, Object> cancelReader(@RequestBody Map<String, String> payload) {
        try {
            String msg = libraryService.cancelReader(payload.get("cardId"));
            return Map.of("success", true, "msg", msg);
        } catch (Exception e) {
            return Map.of("success", false, "msg", e.getMessage());
        }
    }

    // =====================================================================
    //  ✅ 5. 核心业务办理 (借书、还书、配置)
    // =====================================================================

    @PostMapping("/borrow")
    public String borrow(@RequestBody Map<String, String> request) {
        return libraryService.borrowBook(request.get("cardId"), request.get("isbn"));
    }

    @PostMapping("/return")
    public String returnBook(@RequestBody Map<String, String> request) {
        return libraryService.returnBook(request.get("isbn"));
    }

    @PostMapping("/config/update")
    public Map<String, Object> updateConfig(@RequestBody Map<String, String> payload) {
        try {
            String msg = libraryService.updateConfig(payload.get("key"), payload.get("value"));
            return Map.of("success", true, "msg", msg);
        } catch (Exception e) {
            return Map.of("success", false, "msg", "更新失败：" + e.getMessage());
        }
    }

    // =====================================================================
    //  ✅ 6. 读者自助功能 (我的借阅、罚款、自助还书)
    // =====================================================================

    @GetMapping("/reader/borrowings")
    public List<Map<String, Object>> getReaderBorrowings(@RequestParam String cardId) {
        return libraryService.getReaderBorrowings(cardId);
    }

    @GetMapping("/reader/fines")
    public List<Map<String, Object>> getReaderFines(@RequestParam String cardId) {
        return libraryService.getReaderFines(cardId);
    }

    @PostMapping("/reader/return")
    public Map<String, Object> readerReturn(@RequestBody Map<String, Long> payload) {
        try {
            Long borrowId = payload.get("borrowId");
            if (borrowId == null) throw new RuntimeException("参数缺失");

            String msg = libraryService.returnBookById(borrowId);
            boolean isSuccess = msg.contains("成功") || msg.contains("逾期");

            return Map.of("success", isSuccess, "msg", msg);
        } catch (Exception e) {
            return Map.of("success", false, "msg", "系统错误: " + e.getMessage());
        }
    }

    @PostMapping("/reader/pay")
    public Map<String, Object> payFine(@RequestBody Map<String, Long> payload) {
        try {
            Long borrowId = payload.get("borrowId");
            String msg = libraryService.payFine(borrowId);
            return Map.of("success", true, "msg", msg);
        } catch (Exception e) {
            return Map.of("success", false, "msg", "缴费失败");
        }
    }

    // =====================================================================
    //  ✅ 7. 图书荐购功能 API
    // =====================================================================

    @PostMapping("/recommend/add")
    public Map<String, Object> addRecommend(@RequestBody PurchaseRequest req) {
        try {
            String msg = libraryService.addRecommendation(req);
            boolean isSuccess = msg.contains("成功");
            return Map.of("success", isSuccess, "msg", msg);
        } catch (Exception e) {
            return Map.of("success", false, "msg", "系统错误");
        }
    }

    @GetMapping("/recommend/list")
    public List<PurchaseRequest> getRecommendList(@RequestParam(required = false) String readerId) {
        return libraryService.getRecommendations(readerId);
    }

    @PostMapping("/recommend/handle")
    public Map<String, Object> handleRecommend(@RequestBody Map<String, Object> payload) {
        try {
            Integer id = Integer.parseInt(payload.get("id").toString());
            boolean approved = Boolean.parseBoolean(payload.get("approved").toString());
            String msg = libraryService.handleRecommendation(id, approved);
            return Map.of("success", true, "msg", msg);
        } catch (Exception e) {
            return Map.of("success", false, "msg", "操作失败");
        }
    }

    // =====================================================================
    //  ✅ 8. 配置查询接口 (供前端使用)
    // =====================================================================

    // 此接口依赖于 SystemConfigRepository 的注入
    @GetMapping("/config/value")
    public String getConfigValue(@RequestParam String key) {

        return configRepo.findById(key).map(SystemConfig::getConfigValue).orElse(null);
    }
}