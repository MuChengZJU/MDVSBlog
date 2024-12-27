<template>
  <el-card class="article-management-card">
    <el-table :data="formattedArticles" style="width: 100%">
      <el-table-column prop="id" label="编号" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="compiledContent" label="内容">
        <template #default="scope">
          <div v-html="scope.row.compiledContent"></div>
        </template>
      </el-table-column>
      <el-table-column prop="createdTime" label="发布日期" />
      <el-table-column prop="updatedTime" label="最后修改" />
      <el-table-column prop="tags" label="标签">
        <template #default="scope">
          <el-tag v-for="tag in scope.row.tags" :key="tag.id" type="success">{{ tag.name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="editArticle(scope.row)">修改</el-button>
          <el-button type="danger" @click="deleteArticle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import apiClient from '../services/api.js';
import MarkdownIt from "markdown-it";
import DOMPurify from "dompurify";

export default {
  data() {
    return {
      articles: [],
      md: new MarkdownIt({
        html: true,
        linkify: true,
        typographer: true,
      }),
    };
  },
  computed: {
    formattedArticles() {
      return this.articles.map(article => ({
        ...article,
        compiledContent: this.compileMarkdown(article.content),
        createdTime: new Date(article.createdTime).toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' }),
        updatedTime: new Date(article.updatedTime).toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' }),
      }));
    },
  },
  methods: {
    async fetchArticles() {
      try {
        const response = await apiClient.get('/articles');
        this.articles = response.data;
      } catch (error) {
        this.$message.error("无法加载文章，请稍后重试。");
      }
    },
    editArticle(article) {
      this.$router.push({
        path: '/publish',
        query: { id: article.id },
      });
    },
    async deleteArticle(id) {
      this.$confirm('确定删除这篇文章吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
          .then(async () => {
            try {
              await apiClient.delete(`/articles/${id}`);
              this.fetchArticles();
              this.$message.success("文章删除成功！");
            } catch (error) {
              this.$message.error("无法删除文章，请稍后重试。");
            }
          })
          .catch(() => {
            console.log('取消删除');
          });
    },
    compileMarkdown(content) {
      const truncatedContent = content.length > 100 ? content.substring(0, 100) + "..." : content;
      const rawHtml = this.md.render(truncatedContent);
      return DOMPurify.sanitize(rawHtml);
    },
  },
  created() {
    this.fetchArticles();
  },
};
</script>

<style scoped>
.article-management-card {
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>