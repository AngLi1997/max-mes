import { RouteRecordRaw } from 'vue-router';

const BatchRelease: RouteRecordRaw = {
  path: '/batch-release',
  redirect: '/batch-release/configuration',
  meta: {
    title: '批签发',
    id: '120040',
    icon: 'BatchRelease',
  },
  children: [
    {
      path: '/batch-release/configuration',
      meta: {
        title: '批签发配置',
        id: '120040002',
      },
      name: 'BatchReleaseConfiguration',
      component: () => import('@/pages/BatchRelease/Configuration/index.vue'),
    },
    {
      path: '/batch-release/management',
      meta: {
        title: '批签发管理',
        id: '120040003',
      },
      name: 'BatchReleaseManagement',
      component: () => import('@/pages/BatchRelease/Management/index.vue'),
    },
    {
      path: '/batch-release/review',
      meta: {
        title: '批签发审核',
        id: '120040005',
      },
      name: 'BatchReleaseReview',
      component: () => import('@/pages/BatchRelease/Review/index.vue'),
    },
  ],
};

export default BatchRelease;
