import 'ant-design-vue/dist/reset.css';
import { createPinia } from 'pinia';
import { createApp } from 'vue';
import App from './App.vue';
import './assets/main.css';
import btnPermission from './directives/btnPermission';
import './plugins/svg-icon';
import router from './router';
import { getPermissionMenuList } from './services';
import { usePermissionStore } from './stores/permission';
import { flatMenuTreeData } from './utils';

const app = createApp(App);

app.use(createPinia());
app.use(router);
btnPermission(app);
getPermissionMenuList({ rootMenuCode: 210, containsFunc: true })
  .then(({ data }: any) => {
    const { setPermissions } = usePermissionStore();
    setPermissions(flatMenuTreeData(data));
  })
  .finally(() => {
    app.mount('#app');
  });
