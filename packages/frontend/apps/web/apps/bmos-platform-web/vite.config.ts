import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import path, { resolve } from 'path';
import AutoImport from 'unplugin-auto-import/vite';
import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers';
import Components from 'unplugin-vue-components/vite';
import { defineConfig, loadEnv } from 'vite';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';
import { vitePlugin } from '../../scripts/plugins';
import { getIconsConfig } from './src/utils/path';

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  return {
    build: {
      rollupOptions: {
        input: {
          bmos: path.resolve(__dirname, 'bmos/index.html'),
          login: path.resolve(__dirname, 'login/index.html'),
          index: path.resolve(__dirname, 'index.html'),
          download: path.resolve(__dirname, 'download/index.html'),
          kskt: path.resolve(__dirname, 'kskt/index.html'),
        },
      },
      minify: 'terser',
      terserOptions: {
        compress: {
          drop_console: true,
          drop_debugger: true,
        },
      },
    },
    plugins: [
      vue(),
      vueJsx(),
      Components({
        dts: false,
        resolvers: [
          AntDesignVueResolver({
            importStyle: false, // css in js
            prefix: '',
          }),
        ],
      }),
      AutoImport({
        imports: ['vue', 'vue-router'],
        dts: false,
        include: [
          /\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
          /\.vue$/,
          /\.vue\?vue/, // .vue
          /\.md$/, // .md
        ],
        eslintrc: {
          enabled: false, // Default `false`
          filepath: './.eslintrc-auto-import.json', // Default `./.eslintrc-auto-import.json`
        },
      }),
      createSvgIconsPlugin(getIconsConfig()),
      vitePlugin(),
    ],
    resolve: {
      alias: {
        '@': resolve(__dirname, './src'),
      },
    },
    server: {
      host: '0.0.0.0',
      port: 8083,
      hmr: {
        overlay: false,
      },
      headers: {
        'Access-Control-Allow-Origin': '*',
      },
      proxy: {
        '/api/app': {
          target: loadEnv(mode, process.cwd()).VITE_API_HOST,
          changeOrigin: true,
          ws: true,
        },
        '/bmos-platform-sign': {
          target: loadEnv(mode, process.cwd()).VITE_API_HOST,
          changeOrigin: true,
          ws: true,
        },
      },
    },
    base: '/app/bmos-platform/',
  };
});
