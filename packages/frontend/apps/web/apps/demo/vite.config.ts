import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

export default defineConfig({
  plugins: [vue(), vueJsx()],
  build:{
    lib: {
      entry: './index.html',
      name: 'demo',
      fileName: 'demo',
    },
  },
  resolve: {
    alias: {
      '@/*': 'src/*', // 设置别名 "@"
    },
  },
})
