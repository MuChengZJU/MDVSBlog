<template>
  <el-container>
    <!-- 顶部导航栏 -->
    <el-header style="background-color: #409eff; color: white; display: flex; justify-content: space-between; align-items: center;">
      <el-menu
          :default-active="activeIndex"
          mode="horizontal"
          background-color="#409eff"
          text-color="#fff"
          active-text-color="#ffd04b"
          style="flex: 1;"
      >
        <el-menu-item index="1" @click="goTo('/')">
          <i class="el-icon-house"></i> 首页
        </el-menu-item>
        <el-menu-item index="2" v-if="isAdmin" @click="goTo('/publish')">
          <i class="el-icon-edit"></i> 发布文章
        </el-menu-item>
        <el-menu-item index="3" v-if="isAdmin" @click="goTo('/manage')">
          <i class="el-icon-s-management"></i> 管理文章
        </el-menu-item>
        <el-menu-item index="4" v-if="!isAdmin" @click="goTo('/login')">
          <i class="el-icon-user"></i> 管理员登录
        </el-menu-item>
        <el-menu-item index="5" v-if="isAdmin">
          <el-button type="text" @click="logout" style="color: #f56c6c">
            <i class="el-icon-switch-button"></i> 退出管理员
          </el-button>
        </el-menu-item>
      </el-menu>

      <!-- 头像和弹窗 -->
      <el-popover
          placement="bottom"
          width="200"
          trigger="click"
      >
        <template #reference>

          <el-avatar
              src="https://sns-avatar-qc.xhscdn.com/avatar/1040g2jo30vmf1nq35q605odt2c9k13egu7d272o?imageView2/2/w/540/format/webp|imageMogr2/strip2"
              size="default"
              style="cursor: pointer;"
          />
        </template>
        <p><a href="https://github.com/MuChengZJU" target="_blank">GitHub</a></p>
        <p><a href="https://space.bilibili.com/37153037" target="_blank">Bilibili</a></p>
        <p><a href="https://www.xiaohongshu.com/discovery/item/674d60cf000000000202d0de?source=webshare&xhsshare=pc_web&xsec_token=ABlYegu0k67_Hjn99CEoarr5B4P1aBAvwptESLuoKpqCY=&xsec_source=pc_share" target="_blank">小红书</a></p>
      </el-popover>
    </el-header>

    <!-- 主体内容 -->
    <el-main>
      <div style="padding: 20px; text-align: center;">
        <h1 style="font-size: 36px;">Welcome to MuCheng's Secret Base</h1>
        <p v-if="isAdmin">管理员模式已启用，您可以管理文章。</p>
        <p v-else>芝士，与你分享！</p>
      </div>
      <router-view />
    </el-main>

    <!-- 页脚 -->
    <el-footer style="text-align: center; color: #909399; padding: 10px;">
      © 2024 Blog | Designed by MuCheng
    </el-footer>
  </el-container>
</template>

<script>
import avatar from '@/assets/avatar.png';
export default {
  data() {
    return {
      avatarImg: avatar,
    };
  },
  computed: {
    isAdmin() {
      // 判断是否登录，通过本地存储中的 token 判断
      return !!localStorage.getItem('token');
    },
  },
  methods: {
    goTo(path) {
      this.$router.push(path);
    },
    logout() {
      // 清除 token
      localStorage.removeItem('token');
      this.$message.success('您已退出管理员模式');
      // 强制刷新页面
      window.location.reload();
    },
    login() {
      // 模拟管理员登录逻辑（通常在登录页面实现）
      localStorage.setItem('token', 'your_token_here');
      this.$message.success('管理员登录成功');
      // 强制刷新页面
      window.location.reload();
    },
  },
};
</script>

<style scoped>
/* 自定义导航栏样式 */
.el-menu {
  font-size: 16px;
  font-weight: bold;
}

/* 主体内容 */
h1 {
  color: #409eff;
}

p {
  font-size: 18px;
  color: #606266;
}

/* 弹窗链接样式 */
a {
  text-decoration: none;
  color: #409eff;
}

a:hover {
  text-decoration: underline;
}
</style>
