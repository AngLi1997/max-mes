import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import { RouteRecordRaw } from 'vue-router';
import { asyncRoutes } from '../router';
import { getMenuList } from '../services';

export const flatTreeData = (routerData: RouteRecordRaw[]) => {
  const res = new Map();
  const loop = (data: any) => {
    data.forEach((item: any) => {
      if (item.children) {
        loop(item.children);
      }
      res.set(item?.meta?.id, {
        ...item,
        children: [],
      });
    });
  };
  loop(routerData);
  return res;
};

export const getMenuTreeData = (resData: any, mapRoutes: Map<string, RouteRecordRaw>): RouteRecordRaw[] => {
  if (!resData) return [];
  let result: any[] = [];
  const loop = (data: any) => {
    data.forEach((item: any) => {
      let route: any = {};
      if (mapRoutes.has(item.id)) {
        const router = mapRoutes.get(item.id)!;
        route = {
          ...router,
          meta: {
            ...router.meta,
            ...item,
            title: item.name,
            parentId: item.parentId,
            code: item.id,
            keepAlive: true,
          },
          title: item.name,
          children: [],
        };
      } else {
        route = {
          path: `/${item.id}`,
          component: () => '',
          meta: {
            parentId: item.parentId,
            title: item.name,
            id: item.id,
            code: item.id,
          },
          title: item.name,
          children: [],
        };
      }
      if (item.children) {
        route.children = getMenuTreeData(item.children, mapRoutes) as any;
      }
      result.push(route);
    });
  };
  loop(resData);
  return result;
};

let asyncMenus: any[];
export const getAsyncMenu = async (): Promise<RouteRecordRaw[]> => {
  const mapRoutes = flatTreeData(asyncRoutes);
  try {
    if (asyncMenus) {
      return asyncMenus;
    }
    const res = await getMenuList({ rootMenuCode: 130 });
    if (res.code === 0) {
      const data = getMenuTreeData(res.data[0]?.children, mapRoutes);
      asyncMenus = data;
      return data;
    }
    message.error(res.message);
    return [];
  } catch (error) {
    message.error(t('查询菜单失败'));
    return [];
  }
};
