import { RouteRecordRaw } from 'vue-router';

const ReportingManagement: RouteRecordRaw = {
  path: '/reportingManagement',
  redirect: '/reportingManagement/center',
  meta: {
    title: '报告管理',
    id: '210040',
    icon: 'ReportingManagement',
  },
  children: [
    {
      path: '/reportingManagement/center',
      meta: {
        title: '检验报告中心',
        id: '210040001',
      },
      name: 'ReportingManagerCenter',
      component: () => import('@/pages/ReportingManagement/Center/index.vue'),
    },
    {
      path: '/reportingManagement/process-check',
      meta: {
        title: '检验过程检查',
        id: '210040003',
      },
      name: 'ReportingManagerProcessCheck',
      component: () => import('@/pages/ReportingManagement/ProcessCheck/index.vue'),
    },
    {
      path: '/reportingManagement/sign',
      meta: {
        title: '检验报告签发',
        id: '210040002',
      },
      name: 'ReportingManagerSign',
      component: () => import('@/pages/ReportingManagement/Sign/index.vue'),
    },
  ],
};

export default ReportingManagement;
