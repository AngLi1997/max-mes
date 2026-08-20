import { RouteRecordRaw } from 'vue-router';

const ExceptionManagement: RouteRecordRaw = {
  path: '/exception',
  redirect: '/exception/management',
  meta: {
    title: '异常管理',
    id: '120090',
    icon: 'ExceptionManage',
  },
  children: [
    {
      path: '/exception/management',
      component: () => import('@/pages/Exception/management/index.vue'),
      meta: { title: '异常管理', id: '120090001' },
      name: 'exceptionManagement',
    },
  ],
};
export default ExceptionManagement;
