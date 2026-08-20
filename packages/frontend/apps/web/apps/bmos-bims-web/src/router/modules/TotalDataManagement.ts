import { RouteRecordRaw } from 'vue-router';

const TotalDataManagement: RouteRecordRaw = {
  path: '/totalDataManagement',
  redirect: '/totalDataManagement/immune-type-selection',
  meta: {
    title: '总数据管理',
    id: '180030',
    icon: 'TotalDataManagement',
  },
  children: [
    {
      path: '/totalDataManagement/immune-type-selection',
      meta: {
        title: '免疫类型选择',
        id: '180030001',
      },
      name: 'ImmuneTypeSelection',
      component: () => import('@/pages/TotalDataManagement/ImmuneTypeSelection/index.vue'),
    },
    {
      path: '/totalDataManagement/release-of-test-results',
      meta: {
        title: '检验结果汇总发布',
        id: '180030002',
      },
      name: 'ReleaseOfTestResults',
      component: () => import('@/pages/TotalDataManagement/ReleaseOfTestResults/index.vue'),
    },
    {
      path: '/totalDataManagement/test-result-review',
      meta: {
        title: '检验结果发布审核',
        id: '180030003',
      },
      name: 'TestResultReview',
      component: () => import('@/pages/TotalDataManagement/TestResultReview/index.vue'),
    },
  ],
};

export default TotalDataManagement;
