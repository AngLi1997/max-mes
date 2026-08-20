import { LogoBackgroundMode } from '@bmos/components';
import { sendMessage } from '@bmos/utils';
import { RouteRecordRaw, createRouter, createWebHistory } from 'vue-router';
import AreaRouter from './modules/Area';
import DeviceRouter from './modules/Device';
import ImplementRouter from './modules/Implement';
import MaterialPlatformRouter from './modules/MaterialPlatform';
import OutsideRouter from './modules/Outside';
import PermissionsRouter from './modules/Permissions';
import SystemRouter from './modules/System';

export const asyncRoutes: RouteRecordRaw[] = [
  SystemRouter,
  PermissionsRouter,
  MaterialPlatformRouter,
  ImplementRouter,
  AreaRouter,
  DeviceRouter,
  OutsideRouter,
];
export const constantRoutes = [];

export const rootRouter: RouteRecordRaw = {
  path: '/',
  name: 'Home',
  meta: {
    title: '首页',
    code: '首页',
  },
  component: () => import('../pages/Home/index.vue'),
  children: [],
};

const router = createRouter({
  // @ts-ignore
  history: createWebHistory(import.meta.env.BASE_URL), // 设置基地址为根路径
  routes: [rootRouter], // 合并固定路由和异步路由
  // routes: [...constantRoutes, ...asyncRoutes], // 合并固定路由和异步路由
});

let routerChangeFlag = false;
router.afterEach(to => {
  routerChangeFlag && sendMessage('routerChange', { fullPath: to.fullPath.slice(1) });
  routerChangeFlag = true;

  if (to.meta && to.meta.hiddenMenu) {
    sendMessage('logoColor', { color: LogoBackgroundMode.WHITE });
  } else {
    sendMessage('logoColor', { color: LogoBackgroundMode.BLUE });
  }
});

export default router;
