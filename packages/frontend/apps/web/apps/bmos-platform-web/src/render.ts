import { zoomListener } from '@bmos/utils';
import AntD from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import { createPinia } from 'pinia';
import { createApp } from 'vue';
import App from './App.vue';
import './assets/main.css';
import btnPermission from './directives/btnPermission';
import './plugins/svg-icon';
import router from './router';
import { buttonPermissions } from './utils/permission';

const app = createApp(App);

zoomListener();

app.use(createPinia());
app.use(router);
app.use(AntD);
btnPermission(app);
buttonPermissions().finally(() => {
  app.mount('#app');
});
