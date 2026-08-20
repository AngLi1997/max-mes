import { RouteRecordRaw } from 'vue-router';

const LogRouter: RouteRecordRaw = {
  path: '/Log',
  redirect: '/LoginLog',
  meta: {
    title: '日志管理',
    id: '111010',
    icon: 'LogManagement',
  },
  children: [
    {
      path: '/LoginLog',
      component: () => import('../../pages/LogManagement/LoginLog/index.vue'),
      meta: { title: '登录日志', id: '111010001' },
      name: 'LoginLog',
    },
    {
      path: '/OperationLog',
      component: () => import('../../pages/LogManagement/OperationLog/index.vue'),
      meta: { title: '操作日志', id: '111010002' },
      name: 'OperationLog',
    },
  ],
};

export default LogRouter;
