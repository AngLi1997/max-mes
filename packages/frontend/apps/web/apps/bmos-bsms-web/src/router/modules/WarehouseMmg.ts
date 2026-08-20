import { RouteRecordRaw } from 'vue-router';

const WarehouseMmg: RouteRecordRaw = {
  path: '/warehouseMmg',
  redirect: '/warehouseMmg/plasma-stock-mng',
  meta: {
    title: '仓库管理',
    id: '170090',
    icon: 'WarehouseMmg',
  },
  children: [
    {
      path: '/warehouseMmg/plasma-stock-mng',
      meta: {
        title: '血浆库存管理',
        id: '170090001',
      },
      name: 'PlasmaStockMng',
      component: () => import('@/pages/WarehouseMmg/PlasmaStockMng/index.vue'),
    },
    {
      path: '/warehouseMmg/unqualified-plasma-stock',
      meta: {
        title: '不合格血浆库存管理',
        id: '170090002',
      },
      name: 'UnqualifiedPlasmaStock',
      component: () => import('@/pages/WarehouseMmg/UnqualifiedPlasmaStock/index.vue'),
    },
    {
      path: '/warehouseMmg/specimen-stock-mng',
      meta: {
        title: '标本库存管理',
        id: '170090003',
      },
      name: 'SpecimenStockMng',
      component: () => import('@/pages/WarehouseMmg/SpecimenStockMng/index.vue'),
    },
    {
      path: '/warehouseMmg/unqualified-specimen-stock',
      meta: {
        title: '不合格标本库存管理',
        id: '170090004',
      },
      name: 'UnqualifiedSpecimenStock',
      component: () => import('@/pages/WarehouseMmg/UnqualifiedSpecimenStock/index.vue'),
    },
  ],
};

export default WarehouseMmg;
