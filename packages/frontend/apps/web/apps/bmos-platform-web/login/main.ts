import '../src/plugins/svg-icon';
import './assets/main.css';

import AntD from 'ant-design-vue';
import { createApp } from 'vue';
import App from './App.vue';
import { setLangResource } from './utils/handleLang';

const app = createApp(App);
setLangResource(localStorage.getItem('LANG') || 'zh_CN').then(() => {
  app.use(AntD);
  app.mount('#app');
});
