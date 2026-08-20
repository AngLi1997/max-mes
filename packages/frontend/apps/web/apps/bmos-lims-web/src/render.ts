import 'ant-design-vue/dist/reset.css';
import { createPinia } from 'pinia';
import { createApp } from 'vue';
import './assets/main.css';
import './plugins/svg-icon';
import './style/index.less'
import App from './App.vue';
import router from './router';
import { zoomListener } from '@bmos/utils';

const app = createApp(App);
zoomListener();

app.use(createPinia());
app.use(router);
app.mount('#app');
