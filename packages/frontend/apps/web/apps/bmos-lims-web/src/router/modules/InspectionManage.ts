import { RouteRecordRaw } from 'vue-router';

const InspectionManage: RouteRecordRaw = {
  path: '/inspection-manage',
  redirect: '/inspection-manage/inspection-query',
  meta: {
    title: '检验管理',
    id: '130020',
    icon: 'InspectionManage',
  },
  children: [
    {
      path: '/inspection-manage/inspection-query',
      component: () =>
        import('@/pages/InspectionManage/InspectionQuery/index.vue'),
      meta: { title: '检验查询', id: '130020001' },
      name: '',
    },
    {
      path: '/inspection-manage/please-verify',
      component: () =>
        import('@/pages/InspectionManage/PleaseVerify/index.vue'),
      meta: { title: '请检确认', id: '130020002' },
      name: '',
    },
    {
      path: '/inspection-manage/get-sample',
      component: () =>
        import('@/pages/InspectionManage/GetSample/index.vue'),
      meta: { title: '取样', id: '130020003' },
      name: '',
    },
    {
      path: '/inspection-manage/inspection-input',
      component: () =>
        import('@/pages/InspectionManage/InspectionInput/index.vue'),
      meta: { title: '检验录入', id: '130020004' },
      name: '',
    },
    {
      path: '/inspection-manage/reports',
      component: () =>
        import('@/pages/InspectionManage/Reports/index.vue'),
      meta: { title: '报告生成', id: '130020005' },
      name: '',
    },
    {
      path: '/inspection-manage/report-audit',
      component: () =>
        import('@/pages/InspectionManage/ReportAudit/index.vue'),
      meta: { title: '报告审核', id: '130020006' },
      name: '',
    },
    {
      path: '/inspection-manage/report-issuance',
      component: () =>
        import('@/pages/InspectionManage/ReportIssuance/index.vue'),
      meta: { title: '报告签发', id: '130020007' },
      name: '',
    }
  ],
};
export default InspectionManage;
