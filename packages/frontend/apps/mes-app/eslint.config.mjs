// eslint.config.mjs
import antfu from '@antfu/eslint-config';
import { FlatCompat } from '@eslint/eslintrc';

const compat = new FlatCompat();

export default antfu(
  {
    stylistic: {
      indent: 2, // 4, or 'tab'
      quotes: 'single', // or 'double'
      semi: true,
    },
    vue: true,
    formatters: {
      /**
       * Format CSS, LESS, SCSS files, also the `<style>` blocks in Vue
       * By default uses Prettier
       */
      css: true,
      /**
       * Format HTML files
       * By default uses Prettier
       */
      html: true,
      /**
       * Format Markdown files
       * Supports Prettier and dprint
       * By default uses Prettier
       */
      markdown: 'prettier',
    },
    ignores: ['**/webViewEventCallbacks.js', 'src/hybrid/html/js/**', 'src/BMComponents/NiceCrop/**'],
  },
  // Legacy config
  ...compat.config({
    // 小程序全局变量
    globals: {
      uni: true,
      wx: true,
      WechatMiniprogram: true,
      getCurrentPages: true,
      getApp: true,
      UniApp: true,
      UniHelper: true,
      App: true,
      Page: true,
      Component: true,
      AnyObject: true,
    },
  }),
  {
    rules: {
      'no-console': 'off',
      'vue/block-order': ['error', { order: ['template', 'script', 'style'] }],
      'antfu/top-level-function': 'off',
      'vue/jsx-uses-vars': 'error',
      'no-use-before-define': 'off',
      'no-unused-vars': ['error', { varsIgnorePattern: '^_', argsIgnorePattern: '^_', caughtErrorsIgnorePattern: '^_' }],
      'unused-imports/no-unused-vars': ['error', { varsIgnorePattern: '^_', argsIgnorePattern: '^_', caughtErrorsIgnorePattern: '^_' }],
      '@typescript-eslint/no-use-before-define': 'off',
    },
  },
);
