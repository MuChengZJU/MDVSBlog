import { createRouter, createWebHistory } from 'vue-router';
import ArticleList from '@/views/ArticleList.vue';
import ArticleView from '@/views/ArticleView.vue';
import AdminLogin from '@/views/AdminLogin.vue';
import ArticleForm from '@/views/ArticleForm.vue';
import ArticleManagement from '@/views/ArticleManagement.vue';

const routes = [
    { path: '/', component: ArticleList, meta: { title: '首页' } },
    { path: '/article/:id', component: ArticleView, meta: { title: '文章' } },
    { path: '/login', component: AdminLogin, meta: { title: '管理员登录' } },
    { path: '/publish', component: ArticleForm, meta: { title: '发布文章' } },
    { path: '/manage', component: ArticleManagement, meta: { title: '管理文章' } },
];

const router = createRouter({
    history: createWebHistory(),
    routes, // 确保 routes 被正确使用
});

export default router; // 确保 router 被导出
