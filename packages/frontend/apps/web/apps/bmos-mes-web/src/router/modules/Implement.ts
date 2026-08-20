import { RouteRecordRaw } from 'vue-router';

const ImplementRouter: RouteRecordRaw = {
  path: '/Implement',
  redirect: '/Implement/record-manage',
  meta: {
    title: '实施配置',
    id: '120060',
    icon: 'ImplementConfiguration',
  },
  children: [
    {
      path: '/Implement/record-manage',
      component: () => import('@/pages/Implement/RecordManage/index.vue'),
      meta: { title: '记录管理', id: '120060001' },
      name: 'record-manage',
    },
  ],
};
export default ImplementRouter;
