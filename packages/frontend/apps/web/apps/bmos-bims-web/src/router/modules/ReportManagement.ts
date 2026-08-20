import { RouteRecordRaw } from 'vue-router';

const ReportManagement: RouteRecordRaw = {
  path: '/reportManagement',
  redirect: '/reportManagement/inspection-report-management',
  meta: {
    title: '报告管理',
    id: '180040',
    icon: 'ReportManagement',
  },
  children: [
    {
      path: '/reportManagement/inspection-report-management',
      meta: {
        title: '检验报告管理',
        id: '180040001',
      },
      name: 'InspectionReportManagement',
      component: () => import('@/pages/ReportManagement/InspectionReportManagement/components/Page/index.vue'),
    },
    {
      path: '/reportManagement/inspection-report-review',
      meta: {
        title: '检验报告审核',
        id: '180040002',
      },
      name: 'InspectionReportReview',
      component: () => import('@/pages/ReportManagement/InspectionReportReview/components/Page/index.vue'),
    },
  ],
};

export default ReportManagement;
