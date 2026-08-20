import { RouteRecordRaw } from 'vue-router';

const InspectionManagement: RouteRecordRaw = {
  path: '/inspectionManagement',
  redirect: '/inspectionManagement/inspection-request',
  meta: {
    title: '检验管理',
    id: '210030',
    icon: 'InspectionManagement',
  },
  children: [
    {
      path: '/inspectionManagement/task-center',
      meta: {
        title: '任务中心',
        id: '210030001',
      },
      name: 'TaskCenter',
      component: () => import('@/pages/InspectionManagement/TaskCenter/index.vue'),
    },
    {
      path: '/inspectionManagement/testing-data-center',
      meta: {
        title: '检验数据中心',
        id: '210030002',
      },
      name: 'TestingDataCenter',
      component: () => import('@/pages/InspectionManagement/TestingDataCenter/index.vue'),
    },
    {
      path: '/inspectionManagement/inspection-data-audit',
      meta: {
        title: '检验数据审核',
        id: '210030003',
      },
      name: 'InspectionDataAudit',
      component: () => import('@/pages/InspectionManagement/InspectionDataAudit/index.vue'),
    },
    {
      path: '/inspectionManagement/inspection-execution-management/protein',
      meta: {
        title: '蛋白质含量',
        id: '210030004',
      },
      name: 'Protein',
      component: () => import('@/pages/InspectionManagement/ExecutionManagement/Protein/index.vue'),
    },
    {
      path: '/inspectionManagement/inspection-execution-management/alt',
      meta: {
        title: 'ALT',
        id: '210030005',
      },
      name: 'ALT',
      component: () => import('@/pages/InspectionManagement/ExecutionManagement/ALT/index.vue'),
    },
    {
      path: '/inspectionManagement/inspection-execution-management/HBsAg',
      meta: {
        title: 'HBsAg',
        id: '210030006',
      },
      name: 'HBsAg',
      component: () => import('@/pages/InspectionManagement/ExecutionManagement/HBsAg/index.vue'),
    },
    {
      path: '/inspectionManagement/inspection-execution-management/hcv',
      meta: {
        title: '抗-HCV',
        id: '210030007',
      },
      name: 'HCV',
      component: () => import('@/pages/InspectionManagement/ExecutionManagement/HCV/index.vue'),
    },
    {
      path: '/inspectionManagement/inspection-execution-management/hiv',
      meta: {
        title: '抗-HIV',
        id: '210030008',
      },
      name: 'HIV',
      component: () => import('@/pages/InspectionManagement/ExecutionManagement/HIV/index.vue'),
    },
    {
      path: '/inspectionManagement/inspection-execution-management/tp',
      meta: {
        title: '抗-TP',
        id: '210030009',
      },
      name: 'TP',
      component: () => import('@/pages/InspectionManagement/ExecutionManagement/TP/index.vue'),
    },
    {
      path: '/inspectionManagement/inspection-execution-management/protein-electrophoresis',
      meta: {
        title: '蛋白电泳',
        id: '210030010',
      },
      name: 'ProteinElectrophoresis',
      component: () => import('@/pages/InspectionManagement/ExecutionManagement/ProteinElectrophoresis/index.vue'),
    },
  ],
};

export default InspectionManagement;
