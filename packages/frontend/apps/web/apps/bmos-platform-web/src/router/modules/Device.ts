import { RouteRecordRaw } from 'vue-router';

const SystemRouter: RouteRecordRaw = {
  path: '/Device',
  redirect: '/FunctionPointTemplate',
  meta: {
    title: '设备管理',
    id: '100060',
    icon: 'DeviceManagement',
  },
  children: [
    {
      path: '/FunctionPointTemplate',
      component: () => import('@/pages/Device/FunctionPointTemplate/index.vue'),
      meta: { title: '功能点模版', id: '100060001' },
      name: 'functionPointTemplate',
    },
  ],
};
export default SystemRouter;
