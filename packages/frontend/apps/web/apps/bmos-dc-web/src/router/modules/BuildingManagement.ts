import { RouteRecordRaw } from 'vue-router';

export const BuildingManagement: RouteRecordRaw = {
  path: '/building-manage',
  redirect: '/building-manage/building-manage',
  meta: {
    title: t('楼宇管理'),
    id: '200020',
    icon: 'SystemConfiguration',
  },
  children: [
    {
      path: '/building-manage/building-manage',
      meta: {
        title: t('楼宇管理'),
        id: '200020001',
      },
      name: 'BuildingManage',
      component: () => import('@/pages/buildingManagement/buildingManagement/index.vue'),
    },
  ],
};
