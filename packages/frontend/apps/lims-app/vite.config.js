import Uni from '@dcloudio/vite-plugin-uni';
import vueJsx from '@vitejs/plugin-vue-jsx';
import { defineConfig } from 'vite';
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    Uni.default(),
    vueJsx(),
  ],
  css: {
    preprocessorOptions: {
      scss: {
        api: 'modern-compiler', // 修改api调用方式
      },
    },
  },
  server: {
    proxy: {
      '/api': {
        // target: 'http://192.168.112.6:60300/api', // 晋光
        // target: 'http://192.168.110.10:60300/api', // 张若雨
        // target: 'http://192.168.200.99:60300/api', // 李昂
        // target: 'http://192.168.200.206:60300/api', // 康林
        target: 'http://172.30.1.103:80/api', // 测试
        // target: 'http://172.30.1.169:80/api', // 自测
        // target: 'http://172.30.1.160:80/api', // 开发
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, ''),
      },
      '/bmos-platform-sign': {
        target: 'http://172.30.1.160:80',
        changeOrigin: true,
        ws: true,
      },
    },
  },

});
