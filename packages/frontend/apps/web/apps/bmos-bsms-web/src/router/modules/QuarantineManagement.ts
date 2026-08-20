import { RouteRecordRaw } from 'vue-router';

const QuarantineManagement: RouteRecordRaw = {
  path: '/quarantineManagement',
  redirect: '/quarantineManagement/check-query',
  meta: {
    title: '检疫期管理',
    id: '170050',
    icon: 'QuarantineManagement',
  },
  children: [
    {
      path: '/quarantineManagement/check-query',
      meta: {
        title: '核查查询',
        id: '170050001',
      },
      name: 'CheckQuery',
      component: () => import('@/pages/QuarantineManagement/CheckQuery/index.vue'),
    },
    {
      path: '/quarantineManagement/verification-quarantine',
      meta: {
        title: '检疫期核查数据',
        id: '170050002',
      },
      name: 'VerificationQuarantine',
      component: () => import('@/pages/QuarantineManagement/VerificationQuarantine/index.vue'),
    },
    {
      path: '/quarantineManagement/quarantine-report-approval',
      meta: {
        title: '检疫期报告送审',
        id: '170050003',
      },
      name: 'QuarantineReportApproval',
      component: () => import('@/pages/QuarantineManagement/QuarantineReportApproval/index.vue'),
    },
    {
      path: '/quarantineManagement/quarantine-report-audit',
      meta: {
        title: '检疫期报告审核',
        id: '170050004',
      },
      name: 'QuarantineReportAudit',
      component: () => import('@/pages/QuarantineManagement/QuarantineReportAudit/index.vue'),
    },
    {
      path: '/quarantineManagement/results-query',
      meta: {
        title: '检疫期结果查询',
        id: '170050005',
      },
      name: 'ResultsQuery',
      component: () => import('@/pages/QuarantineManagement/ResultsQuery/index.vue'),
    },
  ],
};

export default QuarantineManagement;
