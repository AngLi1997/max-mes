import { RouteRecordRaw } from 'vue-router';

const InspectionManagement: RouteRecordRaw = {
  path: '/inspectionManagement',
  redirect: '/inspectionManagement/inspection-request',
  meta: {
    title: '检验管理',
    id: '170030',
    icon: 'InspectionManagement',
  },
  children: [
    {
      path: '/inspectionManagement/inspection-request',
      meta: {
        title: '标本请验',
        id: '170030001',
      },
      name: 'InspectionRequest',
      component: () => import('@/pages/InspectionManagement/InspectionRequest/components/Page/index.vue'),
    },
    {
      path: '/inspectionManagement/experimental-result',
      meta: {
        title: '检验结果',
        id: '170030002',
      },
      name: 'ExperimentalResult',
      component: () => import('@/pages/InspectionManagement/ExperimentalResult/index.vue'),
    },
    {
      path: '/inspectionManagement/inspection-report',
      meta: {
        title: '检验报告',
        id: '170030003',
      },
      name: 'InspectionReport',
      component: () => import('@/pages/InspectionManagement/InspectionReport/index.vue'),
    },
  ],
};

export default InspectionManagement;
