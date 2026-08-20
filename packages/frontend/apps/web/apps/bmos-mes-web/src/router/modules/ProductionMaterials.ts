import { RouteRecordRaw } from 'vue-router';

const ProductionMaterials: RouteRecordRaw = {
  path: '/production-materials',
  redirect: '/production-materials/product-info',
  meta: {
    title: '生产物料',
    id: '120010',
    icon: 'ProductionMaterials',
  },
  children: [
    {
      path: '/production-materials/org-axu-package',
      component: () => import('@/pages/ProductionMaterials/OrgAxuPkgInfo/index.vue'),
      meta: { title: '原辅包信息', id: '120010001' },
    },
    {
      path: '/production-materials/middle-product',
      component: () => import('@/pages/ProductionMaterials/MiddleProductInfo/index.vue'),
      meta: { title: '中间品信息', id: '120010002' },
    },
    {
      path: '/production-materials/product-info',
      component: () => import('@/pages/ProductionMaterials/ProductInfo/index.vue'),
      meta: { title: '产品信息', id: '120010003' },
    },
  ],
};
export default ProductionMaterials;
