import { zoomListener } from '@bmos/utils';
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

const focusMixin = {
  mounted() {
    this.$el.addEventListener('focusin', (event: FocusEvent) => {
      const target = event.target as HTMLElement;
      if (target && (target.hasAttribute('aria-hidden') || target.hasAttribute('inert'))) {
        target.blur(); // 移除焦点
        // console.log('Prevented focusing on aria-hidden or inert element.');
      }
    });
  },
};

const app = createApp(App);
zoomListener();

app.use(createPinia());
app.use(router);
btnPermission(app);
app.mixin(focusMixin); // 注册全局混入
getPermissionMenuList({ rootMenuCode: 220, containsFunc: true })
  .then(({ data }: any) => {
    const { setPermissions } = usePermissionStore();
    setPermissions(flatMenuTreeData(data));
  })
  .finally(() => {
    app.mount('#app');
  });
