package com.example.blog.repository;

import com.example.blog.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Use JpaRepository to operate the database.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
