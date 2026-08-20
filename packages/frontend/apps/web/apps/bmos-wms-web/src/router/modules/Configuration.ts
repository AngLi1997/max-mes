import { RouteRecordRaw } from 'vue-router';

const Configuration: RouteRecordRaw = {
  path: '/configuration',
  redirect: '/configuration/product-name',
  meta: {
    title: '仓库配置',
    id: '150010',
    icon: 'WarehouseConfiguration',
  },
  children: [
    {
      path: '/configuration/product-name',
      meta: {
        title: '货品名称',
        id: '150010001',
      },
      component: () => import('@/pages/Configuration/ProductName/index.vue'),
    },
    {
      path: '/configuration/product-configuration',
      meta: {
        title: '货位配置',
        id: '150010002',
      },
      component: () => import('@/pages/Configuration/ProductConfiguration/index.vue'),
    },
  ],
};

export default Configuration;
