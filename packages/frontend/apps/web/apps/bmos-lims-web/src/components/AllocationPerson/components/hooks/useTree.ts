import {
  reqPlatformDeptTreeAllList,
  reqPlatformDeptTreeUnassignedList,
  reqPlatformDeptUserTreeList,
} from '@/services';
import { useConfig } from '@/stores/config';
import { SearchTreeProps } from '@bmos/components';
import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
import { storeToRefs } from 'pinia';
import { flatTree } from '../../util';
import { COMPANY, COMPANY_CODE, non_item } from '../const';
import { DetailListItemType } from '../types';

type TreeActive = {
  treeData: DataNode[];
  expandedKeys: string[];
  checkedKeys: string[];
};

const getUserTree = async () => {
  try {
    const { data } = await reqPlatformDeptUserTreeList();
    return data?.length ? data : [];
  } catch (error: any) {
    return [];
  }
};

const getDepartmentTreeApi = async () => {
  try {
    const { data } = await reqPlatformDeptTreeAllList();
    return data?.length ? data : [];
  } catch (error: any) {
    return [];
  }
};

export const useTree = () => {
  const store = useConfig();
  const { configs } = storeToRefs(store);
  const treeActive = reactive<TreeActive>({
    treeData: [],
    expandedKeys: [],
    checkedKeys: [],
  });
  const treeProps: SearchTreeProps = {
    addChildrenNeedCode: true,
    showAddChildren: false,
    showDeleteNode: false,
    showAction: false,
  };
  const personCount = ref<number>(0);

  const check = async (
    keys: KEY[] | { checked: KEY[]; halfChecked: KEY[] },
    info: {
      event: 'check';
      checked: boolean;
      node: EventDataNode;
      checkedNodes: DataNode[];
      nativeEvent: MouseEvent;
    },
  ) => {
    // 如果点击的是未分配
    if (info.node.key === non_item.key) {
      return;
    }
    // 如果点击的是未分配中的人员
    if (nonUserMap.has(info.node?.userId)) {
      return;
    }
    if (info.checked) {
      // 如果点击的是其他部门中的人员
      info.checkedNodes.forEach((item: any) => {
        // 是人员
        if (!item.deptFlag) {
          if (hasDeptUserMap.has(info.node?.id)) {
            const mapItem = hasDeptUserMap.get(info.node.id);
            mapItem.forEach((mapItem: any) => {
              // if (mapItem.key !== info.node?.key) {
              treeActive.checkedKeys.push(mapItem.key);
              // }
            });
            treeActive.checkedKeys = [...new Set(treeActive.checkedKeys)];
          }
        } else {
          // 是部门
          if (info.node?.key !== non_item.key) {
            const loop = (treeData: any[]) => {
              treeData.forEach((item: any) => {
                if (hasDeptUserMap.has(item.id)) {
                  const mapItem = hasDeptUserMap.get(item.id);
                  mapItem.forEach((mapItem: any) => {
                    // if (mapItem.key !== item?.key) {
                    treeActive.checkedKeys.push(mapItem.key);
                    // }
                    treeActive.checkedKeys = [
                      ...new Set(treeActive.checkedKeys),
                    ];
                  });
                }
                if (item.children) {
                  loop(item.children);
                }
              });
            };
            if (info.node?.children && info.node?.children?.length) {
              loop(info.node?.children);
            }
          }
          // 去除 treeActive.checkedKeys 中的部门id
          const index = treeActive.checkedKeys.indexOf(
            info.node?.key as string,
          );
          if (index > -1) {
            treeActive.checkedKeys.splice(index, 1);
          }
        }
      });
    } else {
      if (
        info.node?.dataRef?.deptFlag &&
        info.node?.dataRef?.children?.length
      ) {
        // 如果点击的是部门
        const userInDept = flatTree(info.node?.dataRef?.children as any);
        userInDept.forEach((item: any) => {
          if (hasDeptUserMap.has(item.id)) {
            const mapItem = hasDeptUserMap.get(item.id);
            const newKeys = mapItem.reduce((prev: any[], cur: any) => {
              prev.push(cur.parentId, cur.key);
              return prev;
            }, []);
            treeActive.checkedKeys = treeActive.checkedKeys.filter(
              (item: any) => !newKeys.includes(item),
            );
          }
        });
      } else {
        // 如果点击的是人员
        if (hasDeptUserMap.has(info.node?.id)) {
          const mapItem = hasDeptUserMap.get(info.node.id);
          const newKeys = mapItem.reduce((prev: any[], cur: any) => {
            prev.push(cur.parentId, cur.key);
            return prev;
          }, []);
          treeActive.checkedKeys = treeActive.checkedKeys.filter(
            (item: any) => !newKeys.includes(item),
          );
        }
      }
    }
  };

  const getUserApi = async () => {
    try {
      const { data } = await reqPlatformDeptTreeUnassignedList();
      return data?.list?.length ? data.list : [];
    } catch (error: any) {
      return [];
    }
  };

  const removeCheckNode = (id: string, node: DetailListItemType) => {
    if (nonUserMap.has(id)) {
      const index = treeActive.checkedKeys.indexOf(nonUserMap.get(id).key);
      if (index > -1) {
        treeActive.checkedKeys.splice(index, 1);
      }
      return;
    }
    const mapItem = hasDeptUserMap.get(id);
    const keys = mapItem?.map((item: any) => item.key);
    if (keys) {
      treeActive.checkedKeys = treeActive.checkedKeys.filter(
        key => !keys.includes(key),
      );
    }
  };

  const nonUserMap = new Map();
  const hasDeptUserMap = new Map();

  const loopHasDeptUser = (hasDeptUser: any[]) => {
    hasDeptUser.forEach((item: any) => {
      if (item.deptFlag) {
        item.title = item.name;
        item.key = item.id;
      } else {
        item.title = item.name + '-' + item.loginName;
        item.key = item.parentId + '-' + item.id;
        if (hasDeptUserMap.has(item.id)) {
          const prevMapItem = hasDeptUserMap.get(item.id);
          prevMapItem.push(item);
          hasDeptUserMap.set(item.id, prevMapItem);
        } else {
          hasDeptUserMap.set(item.id, [item]);
        }
      }
      if (item.children) {
        loopHasDeptUser(item.children);
      }
    });
    return hasDeptUser;
  };

  const initTreeData = async (hasCheckPeople: any[]) => {
    const treeList: any[] = await getDepartmentTreeApi();
    if (treeList.length === 0) {
      treeActive.treeData === treeList;
    }
    if (!configs.value[COMPANY_CODE]) {
      await store.findConfigByCode(COMPANY_CODE);
    }
    const rootName = configs.value[COMPANY_CODE].value;
    const non_list = await getUserApi();
    const hasDeptUser = await getUserTree();

    const newNonList = non_list.map((item: any) => {
      const newItem = {
        ...item,
        title: item.userName + '-' + item.loginName,
        key: 'non-' + item.userId,
        id: item.userId,
        deptFlag: false,
        name: item.userName,
      };
      nonUserMap.set(item.userId, newItem);
      return newItem;
    });

    const newHasDeptUser = loopHasDeptUser(hasDeptUser);

    treeActive.expandedKeys = [COMPANY.key];
    treeActive.treeData = [
      {
        ...COMPANY,
        title: rootName,
        deptFlag: true,
        children: [{ ...non_item, children: newNonList }, ...newHasDeptUser],
      },
    ];

    // 人数
    personCount.value = nonUserMap.size + hasDeptUserMap.size;
    // 选中的节点
    if (hasCheckPeople.length) {
      hasCheckPeople.forEach((item: any) => {
        if (nonUserMap.has(item.id)) {
          treeActive.checkedKeys.push(nonUserMap.get(item.id).key);
        } else {
          const mapItem = hasDeptUserMap.get(item.id);
          if (mapItem) {
            mapItem.forEach((item: any) => {
              treeActive.checkedKeys.push(item.key);
            });
          }
        }
      });
    }
  };

  const clearAll = () => {
    treeActive.checkedKeys = [];
  };

  const IS_CHECK = new Map();
  const CHECK_LIST = computed(() => {
    for (let index = 0; index < treeActive.checkedKeys.length; index++) {
      const element = treeActive.checkedKeys[index];
      if (element && !IS_CHECK.has(element)) {
        if (element.indexOf('non-') > -1) {
          const [_deptId, userId] = element.split('-');

          !IS_CHECK.has(userId) && IS_CHECK.set(userId, nonUserMap.get(userId));
        } else if (element.indexOf('-') > -1) {
          const [_deptId, userId] = element.split('-');
          const mapItem = hasDeptUserMap.get(userId);
          !IS_CHECK.has(userId) &&
            mapItem &&
            mapItem.length &&
            IS_CHECK.set(userId, mapItem[0]);
        }
      }
    }
    const checks = [...IS_CHECK.values()];
    IS_CHECK.clear();
    return checks;
  });
  return {
    check,
    treeActive,
    personCount,
    initTreeData,
    treeProps,
    CHECK_LIST,
    removeCheckNode,
    clearAll,
  };
};
