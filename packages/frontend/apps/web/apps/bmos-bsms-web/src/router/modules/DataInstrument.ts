import { RouteRecordRaw } from 'vue-router';

const DataInstrument: RouteRecordRaw = {
  path: '/dataInstrument',
  redirect: '/dataInstrument/data-board',
  meta: {
    title: '数据仪表',
    id: '170010',
    icon: 'DataInstrument',
  },
  children: [
    {
      path: '/dataInstrument/data-board',
      meta: {
        title: '数据看板',
        id: '170010001',
      },
      name: 'DataBoard',
      component: () => import('@/pages/DataInstrument/DataBoard/index.vue'),
    },
    {
      path: '/dataInstrument/task-board',
      meta: {
        title: '任务看板',
        id: '170010002',
      },
      name: 'TaskBoard',
      component: () => import('@/pages/DataInstrument/TaskBoard/index.vue'),
    },
  ],
};

export default DataInstrument;
