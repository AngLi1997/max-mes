import { RouteRecordRaw } from 'vue-router';

const DataSet: RouteRecordRaw = {
  path: '/data-set',
  redirect: '/data-set/manage',
  meta: {
    title: '数据集',
    id: '120070',
    icon: 'DataSet',
  },
  children: [
    {
      path: '/data-set/manage',
      meta: {
        title: '数据集管理',
        id: '120070001',
      },
      name: 'DataSetManage',
      component: () => import('@/pages/DataSet/Manage/index.vue'),
    },
  ],
};

export default DataSet;
