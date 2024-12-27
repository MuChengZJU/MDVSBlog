package com.example.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    // 多对一关系：一个文章属于一个分类
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // 多对多关系：一个文章可以有多个标签
    @ManyToMany
    @JoinTable(
            name = "article_tag", // 中间表名称
            joinColumns = @JoinColumn(name = "article_id"), // 当前表主键
            inverseJoinColumns = @JoinColumn(name = "tag_id") // 关联表主键
    )
    private Set<Tag> tags = new HashSet<>();

    // 无参构造方法（JPA 要求）
    public Article() {
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    // toString 方法（可选，用于调试）
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", category=" + (category != null ? category.getName() : "null") +
                ", tags=" + tags +
                '}';
    }
}
