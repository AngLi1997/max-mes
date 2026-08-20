// .commitlintrc.js
module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    //0为disable，1为warning，2为error
    //always|never
    //第三位该rule的值
    'type-enum': [2, 'always', ['fix', 'config', 'init', 'feat', 'refactor', 'revert', 'style', 'chore']],
    'subject-full-stop': [0, 'never'],
    'subject-case': [0, 'never'],
  },
};
