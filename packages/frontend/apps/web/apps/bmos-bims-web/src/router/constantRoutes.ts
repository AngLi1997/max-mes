export const constantRoutes = [
  {
    path: '/',
    name: 'Index',
    meta: {
      title: '首页',
      code: '首页',
    },
    redirect: '/home',
    component: () => import('@/pages/Main/index.vue'),
    children: [
      {
        path: '/home',
        component: () => import('@/pages/Home/index.vue'),
      },
      {
        path: '/detail',
        component: () => import('@/pages/Home/index.vue'),
      },
      // apps\bmos-bims-web\src\components\ImportExcel\index.vue
      {
        path: '/import-excel/:templateFile',
        meta: {
          title: '导入Excel',
          hidden: true,
        },
        name: 'ImportExcelComponent',
        component: () => import('@/components/ImportExcel/index.vue'),
      },
      {
        path: '/report-management/inspection-report-management/view-com/:id',
        meta: {
          title: '检验报告管理详情',
          hidden: true,
        },
        name: 'InspectionReportManagementViewCom',
        component: () => import('@/pages/ReportManagement/ViewCom/index.vue'),
      },
    ],
  },
];
