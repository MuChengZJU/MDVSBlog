<template>
  <div>
    <div class="search-container">
      <el-input
          v-model="searchQuery"
          placeholder="搜索标题或内容"
          clearable
          @clear="clearSearch"
          @input="searchArticles"
      />
    </div>
    <div class="tag-list">
      <el-tag
          v-for="tag in tags"
          :key="tag.id"
          @click="filterByTag(tag.id)"
          :type="selectedTag === tag.id ? 'primary' : 'info'"
          class="tag-item"
      >
        {{ tag.name }}
      </el-tag>
      <el-tag
          @click="clearFilter"
          :type="selectedTag === null ? 'primary' : 'info'"
          class="tag-item"
      >
        全部
      </el-tag>
    </div>
    <el-timeline>
      <el-timeline-item
          v-for="article in filteredArticles"
          :key="article.id"
          :timestamp="article.createdTime"
      >
        <el-card class="article-card">
          <h3 class="article-title">{{ article.title }}</h3>
          <div class="article-meta">
            <el-tag type="info">创建时间: {{ formatDate(article.createdTime) }}</el-tag>
            <el-tag type="warning">修改时间: {{ formatDate(article.updatedTime) }}</el-tag>
            <el-tag v-for="tag in article.tags" :key="tag.id" type="success">{{ tag.name }}</el-tag>
          </div>
          <div v-html="article.compiledContent" class="markdown-body"></div>
          <el-button @click="viewArticle(article.id)">查看详情</el-button>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script>
import apiClient from '../services/api.js';
import MarkdownIt from "markdown-it";
import DOMPurify from "dompurify";
import "github-markdown-css/github-markdown.css";

export default {
  data() {
    return {
      articles: [],
      tags: [],
      selectedTag: null,
      searchQuery: '',
      md: new MarkdownIt({
        html: true,
        linkify: true,
        typographer: true,
      }),
    };
  },
  computed: {
    filteredArticles() {
      let filtered = this.articles;
      if (this.selectedTag !== null) {
        filtered = filtered.filter(article => article.tags.some(tag => tag.id === this.selectedTag));
      }
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        filtered = filtered.filter(article =>
            article.title.toLowerCase().includes(query) ||
            article.content.toLowerCase().includes(query)
        );
      }
      return filtered;
    }
  },
  methods: {
    async fetchArticles() {
      try {
        const response = await apiClient.get('/articles');
        const sortedArticles = response.data.sort(
            (a, b) => new Date(b.createdTime) - new Date(a.createdTime)
        );
        this.articles = sortedArticles.map(article => ({
          ...article,
          compiledContent: this.compileMarkdown(article.content)
        }));
      } catch (error) {
        console.error("Failed to fetch articles:", error);
      }
    },
    async fetchTags() {
      try {
        const response = await apiClient.get('/tags');
        this.tags = response.data;
      } catch (error) {
        console.error("Failed to fetch tags:", error);
      }
    },
    viewArticle(id) {
      this.$router.push(`/article/${id}`);
    },
    compileMarkdown(content) {
      const truncatedContent = content.length > 100 ? content.substring(0, 100) + "..." : content;
      const rawHtml = this.md.render(truncatedContent);
      return DOMPurify.sanitize(rawHtml);
    },
    formatDate(date) {
      const options = { year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric', timeZone: 'Asia/Shanghai' };
      return new Date(date).toLocaleDateString('zh-CN', options);
    },
    filterByTag(tagId) {
      this.selectedTag = tagId;
    },
    clearFilter() {
      this.selectedTag = null;
    },
    searchArticles() {
      // Trigger re-computation of filteredArticles
    },
    clearSearch() {
      this.searchQuery = '';
    }
  },
  created() {
    this.fetchArticles();
    this.fetchTags();
  },
};
</script>

<style scoped>
@import "github-markdown-css/github-markdown.css";

.search-container {
  margin-bottom: 20px;
  text-align: center;
}

.search-container .el-input {
  width: 50%; /* Adjust the width as needed */
  max-width: 600px; /* Optional: set a maximum width */
  min-width: 300px; /* Optional: set a minimum width */
  margin: 0 auto; /* Center the input */
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
  justify-content: center;
}

.tag-item {
  cursor: pointer;
}

.article-card {
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.article-title {
  font-size: 28px;
  font-weight: bold;
}

.markdown-body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #2e3a4e;
  background-color: #fdfdfd;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}
</style>