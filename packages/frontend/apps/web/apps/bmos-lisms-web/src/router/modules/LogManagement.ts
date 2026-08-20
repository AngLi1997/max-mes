import { RouteRecordRaw } from 'vue-router';

const LogManagement: RouteRecordRaw = {
  path: '/logManagement',
  redirect: '/logManagement/audit-log',
  meta: {
    title: '日志管理',
    id: '210070',
    icon: 'LismsLogManagement',
  },
  children: [
    {
      path: '/logManagement/audit-log',
      meta: {
        title: '审核日志',
        id: '210070001',
      },
      name: 'AuditLog',
      component: () => import('@/pages/LogManagement/AuditLog/index.vue'),
    },
    {
      path: '/logManagement/inspection-data-record',
      meta: {
        title: '检验数据记录',
        id: '210070002',
      },
      name: 'InspectionDataRecord',
      component: () => import('@/pages/LogManagement/InspectionDataRecord/index.vue'),
    },
    {
      path: '/logManagement/revision-log',
      meta: {
        title: '修约日志',
        id: '210070003',
      },
      name: 'RevisionLog',
      component: () => import('@/pages/LogManagement/RevisionLog/index.vue'),
    },
  ],
};

export default LogManagement;
