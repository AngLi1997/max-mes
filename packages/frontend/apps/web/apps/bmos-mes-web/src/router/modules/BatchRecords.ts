import { RouteRecordRaw } from 'vue-router';

const BatchRecords: RouteRecordRaw = {
  path: '/batch-records',
  redirect: '/batch-records/configuration',
  meta: {
    title: '批记录',
    id: '120080',
    icon: 'BatchRecord',
  },
  children: [
    {
      path: '/batch-records/configuration',
      meta: {
        title: '批记录配置',
        id: '120080001',
      },
      name: 'BatchRecordsConfiguration',
      component: () => import('@/pages/BatchRecords/Configuration/index.vue'),
    },
    {
      path: '/batch-records/management',
      meta: {
        title: '批记录管理',
        id: '120080002',
      },
      name: 'BatchRecordsManagement',
      component: () => import('@/pages/BatchRecords/Management/index.vue'),
    },
    {
      path: '/batch-records/review',
      meta: {
        title: '批记录审核',
        id: '120080003',
      },
      name: 'BatchRecordsReview',
      component: () => import('@/pages/BatchRecords/Review/index.vue'),
    },
  ],
};

export default BatchRecords;
