package com.example.demo.repository;

import com.example.demo.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {

    // ✅ 新增：使用 SQL 查询配置值
    // 对应数据库字段: ConfigValue, ConfigKey
    @Query(value = "SELECT ConfigValue FROM systemconfig WHERE ConfigKey = :key", nativeQuery = true)
    String findValueByKey(@Param("key") String key);

    // 辅助方法：调用上面的 SQL 方法
    default String getValue(String key, String defaultValue) {
        String val = findValueByKey(key);
        return val != null ? val : defaultValue;
    }
}