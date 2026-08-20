import { RouteRecordRaw } from 'vue-router';

const DataInstrument: RouteRecordRaw = {
  path: '/dataInstrument',
  redirect: '/dataInstrument/data-board',
  meta: {
    title: '数据仪表',
    id: '210010',
    icon: 'DataInstrument',
  },
  children: [
    {
      path: '/dataInstrument/data-board',
      meta: {
        title: '数据看板',
        id: '210010001',
      },
      name: 'DataBoard',
      component: () => import('@/pages/DataInstrument/DataBoard/index.vue'),
    },
    {
      path: '/dataInstrument/pending-inspection-tasks',
      meta: {
        title: '待检验任务',
        id: '210010002',
      },
      name: 'PendingInspectionTasks',
      component: () => import('@/pages/DataInstrument/PendingInspectionTasks/index.vue'),
    },
  ],
};

export default DataInstrument;
