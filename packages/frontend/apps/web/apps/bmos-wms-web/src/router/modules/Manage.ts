import { RouteRecordRaw } from 'vue-router';

const Manage: RouteRecordRaw = {
  path: '/manage',
  redirect: '/manage/storage-manage',
  meta: {
    title: '仓库管理',
    id: '150020',
    icon: 'WarehouseManagement',
  },
  children: [
    {
      path: '/manage/storage-manage',
      meta: {
        title: '库存管理',
        id: '150020001',
      },
      name: 'StorageManage',
      component: () => import('@/pages/Manage/StorageManage/index.vue'),
    },
    {
      path: '/manage/inventory-manage',
      meta: {
        title: '货品管理',
        id: '150020002',
      },
      name: 'InventoryManage',
      component: () => import('@/pages/Manage/InventoryManage/index.vue'),
    },
    {
      path: '/manage/send-out',
      meta: {
        title: '仓库发料',
        id: '150020003',
      },
      name: 'SendOut',
      component: () => import('@/pages/Manage/SendOut/index.vue'),
    },
  ],
};

export default Manage;
