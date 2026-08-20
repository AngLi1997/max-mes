import { RouteRecordRaw } from 'vue-router';
import Layout from './Layout.vue';

export const handleMenuList = (list: RouteRecordRaw[]) => {
  return list.filter(item => {
    if (item.children) {
      item.children = handleMenuList(item.children);
    }
    return item.meta && !item.meta.hidden;
  });
};

export default Layout;
