<template>
  <el-card class="article-form-card">
    <el-form :model="article" @submit.prevent="submitArticle" label-width="80px">
      <el-form-item label="标题">
        <el-input v-model="article.title" placeholder="请输入标题"></el-input>
      </el-form-item>
      <el-form-item label="内容">
        <div class="editor-container">
          <textarea
              v-model="article.content"
              placeholder="在此输入 Markdown 源码"
              class="markdown-input"
              :style="{ height: editorHeight + 'px' }"
          ></textarea>
          <div
              class="markdown-preview"
              :style="{ height: editorHeight + 'px' }"
              v-html="renderedMarkdown"
          ></div>
        </div>
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="article.tags" multiple placeholder="选择标签">
          <el-option
              v-for="tag in tags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id"
          />
        </el-select>
        <div class="add-tag-container">
          <el-input
              v-model="newTagName"
              placeholder="请输入新标签名称"
              class="new-tag-input"
          ></el-input>
          <el-button type="primary" @click="addTag(newTagName)">
            添加标签
          </el-button>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitArticle">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import MarkdownIt from 'markdown-it';
import apiClient from "@/services/api.js";
import "github-markdown-css/github-markdown-dark.css";

export default {
  name: "ArticleEditor",
  data() {
    return {
      article: {
        id: null,
        title: "",
        content: "",
        tags: []
      },
      tags: [],
      newTagName: "",
      editorHeight: 400,
      md: new MarkdownIt(),
    };
  },
  computed: {
    renderedMarkdown() {
      return this.md.render(this.article.content);
    }
  },
  methods: {
    async fetchTags() {
      try {
        const response = await apiClient.get("/tags");
        this.tags = response.data;
      } catch (error) {
        this.$message.error("无法加载标签，请稍后重试。");
      }
    },
    async fetchArticle(id) {
      try {
        const response = await apiClient.get(`/articles/${id}`);
        this.article = response.data;
      } catch (error) {
        this.$message.error("无法加载文章，请稍后重试。");
      }
    },
    async submitArticle() {
      try {
        const articleId = this.article.id;
        if (articleId) {
          await apiClient.put(`/articles/${articleId}`, this.article);
          this.$message.success("文章更新成功！");
        } else {
          await apiClient.post("/articles", this.article);
          this.$message.success("文章发布成功！");
        }
        this.$router.push("/");
      } catch (error) {
        this.$message.error("无法保存文章，请稍后重试。");
      }
    },
    async addTag(newTagName) {
      if (!newTagName || typeof newTagName !== "string") {
        this.$message.error("标签名无效，请输入正确的标签名。");
        return;
      }

      try {
        const response = await apiClient.post("/tags", {name: newTagName});
        this.tags.push(response.data);
        this.$message.success("标签添加成功！");
        this.newTagName = "";
      } catch (error) {
        this.$message.error("无法添加标签，请稍后重试。");
      }
    },
    adjustEditorHeight() {
      this.editorHeight = window.innerHeight - 200;
    }
  },
  created() {
    this.fetchTags();
    window.addEventListener("resize", this.adjustEditorHeight);
    this.adjustEditorHeight();

    const articleId = this.$route.query.id;
    if (articleId) {
      this.fetchArticle(articleId);
    }
  },
  beforeUnmount() {
    window.removeEventListener("resize", this.adjustEditorHeight);
  }
};
</script>

<style scoped>
.article-form-card {
  max-width: 800px;
  margin: 40px auto;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.editor-container {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

.markdown-input {
  flex: 1;
  min-width: 300px;
  resize: none;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #dcdcdc;
  border-radius: 4px;
}

.markdown-preview {
  flex: 1;
  min-width: 300px;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #dcdcdc;
  border-radius: 4px;
  background-color: #f9f9f9;
}

.add-tag-container {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}

.new-tag-input {
  flex: 1;
}
</style>