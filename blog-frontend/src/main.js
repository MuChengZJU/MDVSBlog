import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

const app = createApp(App);

// 动态设置网页标题
router.beforeEach((to, from, next) => {
    document.title = "MuCheng's Secret Base " + to.meta.title || "MuCheng's Secret Base"; // 默认标题
    next();
});

app.use(router).use(ElementPlus).mount('#app');
