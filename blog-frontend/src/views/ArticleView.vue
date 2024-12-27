<template>
  <div class="article-container light-theme">
    <el-skeleton :loading="loading" animated>
      <template #template>
        <el-skeleton-item variant="h1"></el-skeleton-item>
        <el-skeleton-item variant="text"></el-skeleton-item>
        <el-skeleton-item variant="text"></el-skeleton-item>
        <el-skeleton-item variant="text"></el-skeleton-item>
      </template>
      <template #default>
        <el-alert v-if="error" type="error" :title="error" show-icon></el-alert>
        <el-card v-else class="article-content">
          <h1>{{ article.title }}</h1>
          <el-divider></el-divider>
          <div class="article-meta">
            <el-tag type="info">创建时间: {{ formatDate(article.createdAt) }}</el-tag>
            <el-tag type="warning">修改时间: {{ formatDate(article.updatedAt) }}</el-tag>
            <el-tag v-for="tag in article.tags" :key="tag.id" type="success">{{ tag.name }}</el-tag>
          </div>
          <el-divider></el-divider>
          <div v-html="compiledMarkdown" class="markdown-body"></div>
        </el-card>
      </template>
    </el-skeleton>
  </div>
</template>

<script>
import apiClient from "@/services/api.js";
import MarkdownIt from "markdown-it";
import DOMPurify from "dompurify";
import "github-markdown-css/github-markdown.css";
import { ElTag, ElDivider, ElCard, ElSkeleton, ElSkeletonItem, ElAlert } from 'element-plus';

export default {
  components: {
    ElTag,
    ElDivider,
    ElCard,
    ElSkeleton,
    ElSkeletonItem,
    ElAlert
  },
  data() {
    return {
      article: null,
      loading: true,
      error: null,
      compiledMarkdown: "",
    };
  },
  mounted() {
    const id = this.$route.params.id;
    this.fetchArticle(id);
  },
  methods: {
    async fetchArticle(id) {
      try {
        const response = await apiClient.get(`/articles/${id}`);
        this.article = response.data;
        this.article.createdAt = new Date(this.article.createdTime);
        this.article.updatedAt = new Date(this.article.updatedTime);
        this.compileMarkdown();
      } catch (error) {
        console.error("获取文章失败:", error);
        this.error = "无法加载文章，请稍后重试。";
      } finally {
        this.loading = false;
      }
    },
    compileMarkdown() {
      const md = new MarkdownIt({
        html: true,
        linkify: true,
        typographer: true,
      });
      const rawHtml = md.render(this.article.content);
      this.compiledMarkdown = DOMPurify.sanitize(rawHtml);
    },
    formatDate(date) {
      const options = { year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric', timeZone: 'Asia/Shanghai' };
      return new Date(date).toLocaleDateString('zh-CN', options);
    }
  }
};
</script>

<style scoped>
.article-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  background-color: #fdfdfd;
  color: #2e3a4e;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.markdown-body {
  background-color: #fdfdfd;
  color: #2e3a4e;
}

.markdown-body h1, .markdown-body h2, .markdown-body h3, .markdown-body h4, .markdown-body h5, .markdown-body h6 {
  color: #2e3a4e;
}

.markdown-body a {
  color: #0366d6;
}

.markdown-body blockquote {
  color: #6a737d;
  border-left: 4px solid #dfe2e5;
  background-color: #f6f8fa;
}

.markdown-body code, .markdown-body pre {
  background-color: #f6f8fa;
  color: #e01e5a;
}
</style>