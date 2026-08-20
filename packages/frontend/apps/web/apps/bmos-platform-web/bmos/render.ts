import { zoomListener } from '@bmos/utils';
import AntD from 'ant-design-vue';
import { createApp } from 'vue';
import '../src/plugins/svg-icon';
import App from './App.vue';
import './assets/main.css';

const app = createApp(App);

zoomListener();
app.use(AntD);
app.mount('#app');
