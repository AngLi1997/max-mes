module.exports = {
  // 一行最多字符
  printWidth: 120,
  // 使用 2 个空格缩进
  tabWidth: 2,
  // 不使用缩进符，而使用空格
  useTabs: false,
  // 行尾需要有分号
  semi: true,
  // 使用单引号
  singleQuote: true,
  // 末尾需要有逗号
  trailingComma: 'all',
  // 标签闭合不换行
  bracketSameLine: true,
  // 箭头函数尽量简写
  arrowParens: 'avoid',
  // 行位换行符
  // endOfLine: 'lf',
  // JSX中使用单引号
  jsxSingleQuote: true,
  //在对象前后添加空格-eg: { foo: bar }
  bracketSpacing: true,
  //多属性html标签的‘>’折行放置
  jsxBracketSameLine: true,
  // 是否缩进Vue文件中<script>和<style>标签内的代码
  vueIndentScriptAndStyle: true,
  // vue template 中的结束标签结尾尖括号掉到了下一行的开头
  htmlWhitespaceSensitivity: 'ignore',
  // 自动检测并插入适当的行尾符
  endOfLine: 'auto',
  // 块的开始不能有空行
  proseWrap: 'always',
  //对引用代码进行格式化
  embeddedLanguageFormatting: 'auto',
  overrides: [
    {
      files: '.prettierrc',
      options: {
        parser: 'json',
      },
    },
    {
      files: '*.js,*.ts,*.jsx,*.tsx',
      options: {
        htmlWhitespaceSensitivity: 'css',
        requirePragma: true,
        insertPragma: true,
      },
    },
  ],
  plugins: ['prettier-plugin-organize-imports', 'prettier-plugin-packagejson'],
};
