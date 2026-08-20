import { createPinia } from 'pinia';
import 'virtual:svg-icons-register';
import { createApp } from 'vue';
import App from './App.vue';
import './assets/main.css';
import btnPermission from './directives/btnPermission';
import router from './router';
import { getPermissionMenuList } from './services';
import { usePermissionStore } from './stores/permission';
import { flatMenuTreeData } from './utils/utils';
import { zoomListener } from '@bmos/utils';

const app = createApp(App);
zoomListener();

app.use(createPinia());
app.use(router);
btnPermission(app);
getPermissionMenuList({ rootMenuCode: 111, containsFunc: true })
  .then(({ data }: any) => {
    const { setPermissions } = usePermissionStore();
    setPermissions(flatMenuTreeData(data));
  })
  .finally(() => {
    app.mount('#app');
  });
