import { createRouter, createWebHistory, RouteRecordRaw  } from "vue-router"; 
import SystemRouter from "./modules/System";
import DetailRouter from "./modules/Detail";
import ProductRouter from "./modules/Product";
import SpecificationRouter from "./modules/Specification";
import Layout from "../views/layout/index.vue";

const asyncRoutes:RouteRecordRaw[] = [DetailRouter, ProductRouter, SystemRouter, SpecificationRouter];
const constantRoutes = [{
  path: '/',
  name: '/',
  redirect: '/dashboard',
  component: Layout,

  meta:{
    title:'首页'
  },
  children: [
    {
      path: 'dashboard',
      component: () => import('../views/dashboard/index.vue'),
      name: 'Dashboard',
      meta: {
        title:'组件'
      },
      hidden: false
    },
 
  ]
}];

const router = createRouter({
  history: createWebHistory("/"), // 设置基地址为根路径
  routes: [...constantRoutes, ...asyncRoutes] // 合并固定路由和异步路由
});

export default router;
