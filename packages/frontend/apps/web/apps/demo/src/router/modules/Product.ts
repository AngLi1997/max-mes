import { RouteRecordRaw } from "vue-router";

const ProductRouter: RouteRecordRaw = { 
  path: '/Product',
  redirect: '/ProductPage',
  meta: {
    title: '产品页',
  },
  children: [
    {
      path: '/ProductPage',
      component: () => import('../../views/product/productPage/index.vue'),
      meta: { title: '产品信息'},
      name:''
    },
    {
      path: '/ProductQuality',
      component: () => import('../../views/product/productQuality/index.vue'),
      meta: { title: '产品质量'},
      name:''
    }
  ]
 }
 export default ProductRouter;
