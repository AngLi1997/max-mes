import { RouteRecordRaw } from "vue-router";

const SpecificationRouter: RouteRecordRaw = { 
  path: '/Specification',
  redirect: '/SpecificationText',
  meta: {
    title: '组件规范',
  },
  children: [
    {
      path: '/SpecificationText',
      component: () => import('../../views/specification/specificationText/index.vue'),
      meta: { title: '组件规范测试'},
      name:''
    }    
  ]
 }
 export default SpecificationRouter;
