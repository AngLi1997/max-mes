import { RouteRecordRaw } from 'vue-router';

const MaterialWarehouse: RouteRecordRaw = {
  path: '/materialWarehouse',
  redirect: '/materialWarehouse/basic-info',
  meta: {
    title: '物料库管理',
    id: '210060',
    icon: 'MaterialWarehouse',
  },
  children: [
    {
      path: '/materialWarehouse/basic-info/supplier-info',
      meta: {
        title: '供应商信息',
        id: '210060001',
      },
      name: 'SupplierInfo',
      component: () => import('@/pages/MaterialWarehouse/BasicInfo/SupplierInfo/index.vue'),
    },
    {
      path: '/materialWarehouse/basic-info/material-info',
      meta: {
        title: '物料基础信息',
        id: '210060002',
      },
      name: 'MaterialInfo',
      component: () => import('@/pages/MaterialWarehouse/BasicInfo/MaterialInfo/index.vue'),
    },
    {
      path: '/materialWarehouse/receipt',
      meta: {
        title: '物料接收',
        id: '210060003',
      },
      name: 'MaterialReceipt',
      component: () => import('@/pages/MaterialWarehouse/Receipt/index.vue'),
    },
    {
      path: '/materialWarehouse/put-in-storage',
      meta: {
        title: '物料入库',
        id: '210060004',
      },
      name: 'MaterialInPutInStorage',
      component: () => import('@/pages/MaterialWarehouse/PutInStorage/index.vue'),
    },
    {
      path: '/materialWarehouse/inventory-management',
      meta: {
        title: '物料库存管理',
        id: '210060005',
      },
      name: 'MaterialInventoryManagement',
      component: () => import('@/pages/MaterialWarehouse/InventoryManagement/index.vue'),
    },
    {
      path: '/materialWarehouse/spot-check',
      meta: {
        title: '物料抽检',
        id: '210060006',
      },
      name: 'MaterialSpotCheck',
      component: () => import('@/pages/MaterialWarehouse/SpotCheck/index.vue'),
    },
    {
      path: '/materialWarehouse/spot-check-audit',
      meta: {
        title: '抽检申请审核',
        id: '210060007',
      },
      name: 'MaterialSpotCheckAudit',
      component: () => import('@/pages/MaterialWarehouse/SpotCheckAudit/index.vue'),
    },
    {
      path: '/materialWarehouse/spot-check-release',
      meta: {
        title: '物料抽检放行',
        id: '210060008',
      },
      name: 'MaterialSpotCheckRelease',
      component: () => import('@/pages/MaterialWarehouse/SpotCheckRelease/index.vue'),
    },
    {
      path: '/materialWarehouse/collect-use-audit',
      meta: {
        title: '物料领用审核',
        id: '210060009',
      },
      name: 'MaterialCollectUseAudit',
      component: () => import('@/pages/MaterialWarehouse/CollectUseAudit/index.vue'),
    },
    {
      path: '/materialWarehouse/scrap-audit',
      meta: {
        title: '物料报废审核',
        id: '210060010',
      },
      name: 'MaterialScrapAudit',
      component: () => import('@/pages/MaterialWarehouse/ScrapAudit/index.vue'),
    },
    {
      path: '/materialWarehouse/goods-return-audit',
      meta: {
        title: '物料退货审核',
        id: '210060011',
      },
      name: 'MaterialGoodsReturnAudit',
      component: () => import('@/pages/MaterialWarehouse/GoodsReturnAudit/index.vue'),
    },
    {
      path: '/materialWarehouse/outbound',
      meta: {
        title: '物料出库',
        id: '210060012',
      },
      name: 'MaterialOutbound',
      component: () => import('@/pages/MaterialWarehouse/Outbound/index.vue'),
    },
    {
      path: '/materialWarehouse/scrap-approval',
      meta: {
        title: '物料报废批准',
        id: '210060014',
      },
      name: 'MaterialScrapApproval',
      component: () => import('@/pages/MaterialWarehouse/ScrapApproval/index.vue'),
    },
    {
      path: '/materialWarehouse/goods-return-approval',
      meta: {
        title: '物料退货批准',
        id: '210060015',
      },
      name: 'MaterialGoodsReturnApproval',
      component: () => import('@/pages/MaterialWarehouse/GoodsReturnApproval/index.vue'),
    },
    {
      path: '/materialWarehouse/early-warning',
      meta: {
        title: '预警管理',
        id: '210060013',
      },
      name: 'MaterialEarlyWarning',
      component: () => import('@/pages/MaterialWarehouse/EarlyWarning/index.vue'),
    },
  ],
};

export default MaterialWarehouse;
