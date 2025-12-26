package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "systemconfig")
public class SystemConfig {
    @Id
    @Column(name = "ConfigKey")
    private String configKey;

    @Column(name = "ConfigValue")
    private String configValue;

    @Column(name = "Description")
    private String description;

    // 构造函数
    public SystemConfig() {}

    // Getters and Setters
    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }

    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}