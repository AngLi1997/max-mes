import { RouteRecordRaw } from 'vue-router';

const SystemRouter: RouteRecordRaw = {
  path: '/Area',
  redirect: '/AreaManagement',
  meta: {
    title: '区域管理',
    id: '100050',
    icon: 'AreaManagement',
  },
  children: [
    {
      path: '/AreaManagement',
      component: () => import('@/pages/Area/areaManagement/index.vue'),
      meta: { title: '编号规则', id: '100050001' },
      name: 'areaManagement',
    },
  ],
};
export default SystemRouter;
