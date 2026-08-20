import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import { resolve } from 'path';
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
        imports: [
          'vue',
          'vue-router',
          {
            '@bmos/i18n': ['t'],
          },
          {
            '@/types': ['getDicts'],
          },
        ],
        dts: false,
        include: [
          /\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
          /\.vue$/,
          /\.vue\?vue/, // .vue
          /\.md$/, // .md
        ],
        exclude: [/node_modules/, /\.git/, /apps[\\/]web[\\/]packages[\\/]/],
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
    build: {
      minify: 'terser',
      terserOptions: {
        compress: {
          drop_console: true,
          drop_debugger: true,
        },
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
        '/api': {
          target: loadEnv(mode, process.cwd()).VITE_API_HOST,
          changeOrigin: true,
          ws: true,
        },
      },
    },
    base: '/app/bmos-bims',
  };
});
