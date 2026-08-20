import { RouteRecordRaw } from 'vue-router';

const Query: RouteRecordRaw = {
  path: '/query',
  redirect: '/query/product-log',
  meta: {
    title: '仓库查询',
    id: '150030',
    icon: 'WarehouseQuery',
  },
  children: [
    {
      path: '/query/product-log',
      meta: {
        title: '货品日志',
        id: '150030001',
      },
      component: () => import('@/pages/Query/ProductLog/index.vue'),
    },
    {
      path: '/query/storage-log',
      meta: {
        title: '货位日志',
        id: '150030002',
      },
      component: () => import('@/pages/Query/StorageLog/index.vue'),
    },
  ],
};

export default Query;
