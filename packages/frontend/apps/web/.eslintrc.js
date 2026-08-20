// .eslintrc.js
// eslint-disable-next-line @typescript-eslint/no-var-requires
const { defineConfig } = require('eslint-define-config');
// eslint-disable-next-line @typescript-eslint/no-var-requires
// const path = require('path');

module.exports = defineConfig({
  // 指定此配置为根级配置，eslint 不会继续向上层寻找
  root: true,

  // 将浏览器 API、ES API 和 Node API 看做全局变量，不会被特定的规则(如 no-undef)限制。
  env: {
    browser: true,
    es2022: true,
    node: true,
  },
  plugins: ['vue', 'prettier'],

  // 设置自定义全局变量，不会被特定的规则(如 no-undef)限制。
  globals: {
    // 假如我们希望 jquery 的全局变量不被限制，就按照如下方式声明。
    // $: 'readonly',
    luckysheet: 'writable',
  },

  // 规则集以及 vue 相关规则
  extends: [
    './apps/bmos-mes-web/.eslintrc-auto-import.json',
    './apps/bmos-audit-web/.eslintrc-auto-import.json',
    './apps/bmos-platform-web/.eslintrc-auto-import.json',
    './apps/bmos-lims-web/.eslintrc-auto-import.json',
    './apps/bmos-wms-web/.eslintrc-auto-import.json',
    './apps/bmos-ems-web/.eslintrc-auto-import.json',
    './apps/bmos-dc-web/.eslintrc-auto-import.json',
    './apps/bmos-bsms-web/.eslintrc-auto-import.json',
    './apps/bmos-bims-web/.eslintrc-auto-import.json',
    './apps/bmos-lisms-web/.eslintrc-auto-import.json',
    'plugin:vue/base',
    'eslint:recommended',
    'plugin:vue/vue3-recommended',
    'plugin:vue/vue3-essential',
    '@vue/typescript/recommended',
    'plugin:prettier/recommended',
    'plugin:@typescript-eslint/recommended',
    '@vue/eslint-config-typescript/recommended',
    'eslint-config-prettier',
    '@vue/prettier',
    '@vue/eslint-config-typescript',
    '@vue/eslint-config-prettier',
    // 覆盖 ESLint 配置，确保 prettier 放在最后
    'prettier',
  ],

  // 指定 vue 解析器
  parser: 'vue-eslint-parser',
  parserOptions: {
    // 配置 TypeScript 解析器
    parser: '@typescript-eslint/parser',

    // 通过 tsconfig 文件确定解析范围，这里需要绝对路径，否则子模块中 eslint 会出现异常
    // project: path.resolve(__dirname, 'tsconfig.eslint.json'),

    // 支持的 ecmaVersion 版本
    ecmaVersion: 'latest',

    // 我们主要使用 esm，设置为 module
    sourceType: 'module',

    // TypeScript 解析器也要负责 vue 文件的 <script>
    extraFileExtensions: ['.vue'],
    ecmaFeatures: {
      jsx: true,
    },
  },

  // 在已有规则及基础上微调修改
  rules: {
    'import/no-named-as-default': 'off',
    'import/no-named-as-default-member': 'off',
    'import/no-extraneous-dependencies': 'off',
    'import/prefer-default-export': 'off',
    '@typescript-eslint/no-explicit-any': 'off',
    '@typescript-eslint/ban-ts-comment': 'off',
    'vue/max-attributes-per-line': [
      'error',
      {
        singleline: {
          max: 10,
        },
        multiline: {
          max: 1,
        },
      },
    ],
    '@typescript-eslint/ban-types': 'off',

    // vue 允许单单词组件名
    'vue/multi-word-component-names': 'off',
    // 是否注册了但未使用的组件
    'vue/no-unused-components': 'error',
    // 是否有声明了但未使用的变量
    'vue/no-unused-vars': 'error',
    // v-for 指令时为每个项提供唯一的 key 属性
    'vue/require-v-for-key': 'error',
    // 计算属性不能异步
    'vue/no-async-in-computed-properties': 'error',
    // 计算属性里面不能有副作用函数
    'vue/no-side-effects-in-computed-properties': 'error',
    'vue/no-use-v-if-with-v-for': [
      'error',
      {
        allowUsingIterationVar: false,
      },
    ],
    'vue/singleline-html-element-content-newline': [
      'off',
      {
        ignoreWhenNoAttributes: true,
        ignoreWhenEmpty: true,
        ignores: ['pre', 'textarea'],
      },
    ],
    'no-empty': 'off',
    'vue/html-indent': [
      'off',
      2,
      {
        attribute: 1,
        baseIndent: 1,
        closeBracket: 0,
        alignAttributesVertically: true,
        ignores: [],
      },
    ],
    'vue/html-closing-bracket-newline': [
      'off',
      {
        singleline: 'never',
        multiline: 'always',
        selfClosingTag: {
          singleline: 'never',
          multiline: 'always',
        },
      },
    ],
    'vue/html-self-closing': [
      'off',
      {
        html: {
          void: 'never',
          normal: 'always',
          component: 'always',
        },
        svg: 'always',
        math: 'always',
      },
    ],
    'vue/v-on-event-hyphenation': [
      'off',
      'always' | 'never',
      {
        autofix: false,
        ignore: [],
      },
    ],
    'no-extra-parens': ['off', 'all', { nestedBinaryExpressions: false }],
    'operator-linebreak': ['off', 'after'],
    'class-methods-use-this': 'off',

    // 允许使用 ++
    'no-plusplus': 'off',
    // 函数声明样式
    'func-call-spacing': 'off',
    // 函数样式
    'space-before-function-paren': 0,
    // 换行符不作约束
    'linebreak-style': 'off',
    // 禁止变量在声明前使用
    'no-use-before-define': 'off',
    // 禁止使用 var 关键字声明变量
    'no-var': 'error',
    // 禁止出现无法到达的代码
    'no-unreachable': 1,
    // 禁止出现不必要的 return 语句
    'no-useless-return': 1,
    // 禁止使用eval
    'no-eval': 'error',
    // 块的开始和结束处不插入空行
    'padded-blocks': ['error', 'never'],
    // 禁止重复声明
    'no-redeclare': 'error',
    // 成员定界符样式
    'quote-props': ['off', 'always'],
    // 在声明变量时首选使用 const 关键字
    'vue/script-setup-uses-vars': 'error',
    // 对象属性简写
    'object-shorthand': 1,
    'stylelint/order': 0,
    'prefer-const': [
      'off',
      {
        destructuring: 'all',
      },
    ],
    '@typescript-eslint/no-unused-vars': [
      2,
      {
        argsIgnorePattern: '^_',
        varsIgnorePattern: '^_',
      },
    ],
    'vue/attribute-hyphenation': [
      'off',
      'always' | 'never',
      {
        ignore: [],
      },
    ],
  },

  // 文件级别的重写
  overrides: [
    // 对于 vite 和 vitest 的配置文件，不对 console.log 进行错误提示
    {
      files: ['**/vite.config.*', '**/vitest.config.*'],
      rules: {
        'no-console': 'off',
      },
    },
  ],
});
