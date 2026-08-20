import { RouteRecordRaw } from 'vue-router';

export const ProcessView: RouteRecordRaw = {
  path: '/process',
  redirect: '/process/process-view',
  meta: {
    title: t('工艺大屏显示'),
    id: '200040',
    // icon: 'ProcessViewIcon',
  },
  children: [
    {
      path: '/process/process-view',
      meta: {
        title: t('工艺大屏显示'),
        id: '200040001',
      },
      name: 'ProcessView',
      component: () => import('@/pages/process/processView/index.vue'),
    },
  ],
};
