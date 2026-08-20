<template>
  <div class="permissionInfo">
    <Empty v-if="treeList?.length === 0"></Empty>
    <div v-else class="info-container">
      <div class="menuDetails">
        <div class="topNav">
          <span>{{ t('菜单列表') }}</span>
        </div>
        <div class="menuList">
          <!-- 树 -->
          <Tree
            v-model:selectedKeys="selectKeys"
            class="menu-tree"
            checkable
            :checkedKeys="checkedKeys"
            :checkStrictly="true"
            :fieldNames="{
              children: 'children',
              title: 'name',
              key: 'id',
            }"
            :selectable="true"
            :tree-data="treeList"
            @select="select"
            @check="checkNode"></Tree>
        </div>
      </div>
      <div class="feature">
        <div class="topNav">
          <span>{{ t('功能列表') }}</span>
        </div>
        <div class="menuList">
          <!-- 树 -->
          <Tree
            v-model:expandedKeys="expandedKeys"
            class="displayTree"
            checkable
            :checkedKeys="featureCheckedKeys"
            :tree-data="displayTreeData"
            :checkStrictly="true"
            :selectable="true"
            :fieldNames="{
              children: 'children',
              title: 'name',
              key: 'id',
            }"
            @check="funCheckNode"></Tree>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { postMenu, getMenuFunction, reqRoleAuthMenuId, reqMenuRoleFunction } from '@/api/Permissions/roleManagement';
  import Empty from '../../../../components/Empty/index.vue';
  import { Tree } from 'ant-design-vue';
  import { onMounted, ref, watch } from 'vue';
  import { t } from '@bmos/i18n';
  const props = defineProps({
    activeKey: {
      type: String,
      default: '100',
    },
    roleId: {
      type: String,
      default: '',
    },
    type: {
      //判断是菜单权限还是权限授权 '1'为菜单权限 '2'为权限授权
      type: String,
      default: '1',
    },
    treeList: {
      type: Array<any>,
      default: () => [],
    },
  });

  let checkNodes = new Map();

  const keyMap = new Map();
  const activeMap = new Map();
  const rightNodeFlagTrue = new Map(); //存右边树的flag为true的(左侧树点击后调的接口)
  // 功能树
  const displayTreeData = ref<any>([
    {
      name: t('全部'),
      id: '0-0-0',
      checkable: false,
      children: [],
    },
  ]);
  const expandedKeys = ref<any[]>(['0-0-0']); //功能列表默认展开全部
  const selectKeys = ref<any[]>([]);
  const curentNode = ref<any>({});
  const checkedKeys = ref<string[]>(['']);
  const featureCheckedKeys = ref<any[]>([]);
  const rightNodeNum = ref(); //左边树select时存右边树总节点数量(除'全部'节点的所有节点(包括存在的父级节点))
  let allCheckKeys: any[] = [];

  // 默认选中(回显)
  const postMenuApi = async (params: any) => {
    try {
      const res: any = props.type === '1' ? await postMenu(params) : await reqRoleAuthMenuId(params);
      if (res.code === 0) {
        checkedKeys.value = res.data.find((item: any) => item.rootMenuId === props.activeKey)?.menuIds || [];
        allCheckKeys = res.data;
        return;
      }
      checkedKeys.value = [];
    } catch (error) {
      checkedKeys.value = [];
    }
  };
  // 拿所有树id集合(flag为true的)
  const loopTree = (data: any) => {
    const arr: any[] = [];
    data.forEach((item: any) => {
      if (item.flag) {
        arr.push(item.id);
      }
      if (item.children && item.children.length > 0) {
        arr.push(...loopTree(item.children));
      }
    });
    return arr;
  };
  // 判断是否所有flag均为true(是否回显全部)
  const loopTree2 = (data: any) => {
    const flag: any[] = [];
    data.forEach((item: any) => {
      flag.push(item.flag);
      if (item.children && item.children.length > 0) {
        flag.push(...loopTree2(item.children));
      }
    });
    return flag;
  };
  //选中左侧菜单获取功能
  const select = async (selected_keys: any, e: any) => {
    if (selected_keys.length === 0) return;
    selectKeys.value = selected_keys;
    let data: any;
    let checked;
    let allFlag: any; //是否需要回显全部
    const id = selected_keys[0];

    try {
      data =
        props.type === '1'
          ? await getMenuFunction({
              menuId: selected_keys[0],
              roleId: props.roleId,
            })
          : await reqMenuRoleFunction({
              menuId: selected_keys[0],
              roleId: props.roleId,
            });
      rightNodeNum.value = loopTree2(data?.data).length;
      // 角色管理的按钮分配需要增加一键勾选功能
      if (data?.data.length > 0) {
        displayTreeData.value[0].checkable = true;
      } else {
        displayTreeData.value[0].checkable = false;
      }
    } catch (error) {}
    if (keyMap.has(id)) {
      //点击过
      checked = keyMap.get(id).split(',');
    } else {
      //未点击过(通过接口的flag回显)
      checked = loopTree(data?.data);
      rightNodeFlagTrue.set(id, checked);
      allFlag = !loopTree2(data?.data).includes(false);
    }
    if (curentNode.value) {
      keyMap.set(curentNode.value.id, featureCheckedKeys.value.join(','));
      keyMap.delete(undefined);
    }
    curentNode.value = e.node;
    featureCheckedKeys.value = [...checked, allFlag ? '0-0-0' : ''];
    displayTreeData.value[0].children = data?.data || [];
    displayTreeData.value = [...displayTreeData.value];
  };

  onMounted(() => {
    if (props.activeKey && props.roleId) {
      postMenuApi({ roleId: props.roleId });
    }
  });
  // 左边树处理父子节点关联
  const filterFa = (node: any) => {
    const fils: any[] = [];
    let cur = node;
    // eslint-disable-next-line no-constant-condition
    while (true) {
      const flag = cur.parent.children.every((item: any) => {
        if (item.key !== cur.key) {
          return !checkedKeys.value.includes(item.key);
        }
        return true;
      });
      if (!flag) break;

      cur = cur.parent;
      fils.push(cur.key);
      if (!cur || !cur.parent) break;
    }
    if (fils.length === 0) return;
    checkedKeys.value = checkedKeys.value.filter(i => !fils.includes(i));
  };
  const addFa = (node: any) => {
    let par = node.parent;
    while (par) {
      if (checkedKeys.value.includes(par.key)) {
        break;
      }
      checkedKeys.value.push(par.key);
      par = par.parent;
    }
  };
  // 左边树check方法
  const checkNode = (keys: any, e: any) => {
    checkedKeys.value = keys.checked;
    if (!e.checked) {
      //取消勾选
      // const eid = e.node.id;
      // keyMap.set(eid, '');
      // 取消左边菜单权限后，取消右侧所有功能权限
      // if (eid === selectKeys.value[0]) {
      //   featureCheckedKeys.value = [];
      // }
      if (e.node.children && e.node.children.length > 0) {
        const ids: any[] = e.node.children.map((item: any) => item.id);
        checkedKeys.value = checkedKeys.value.filter(item => !ids.includes(item));
      }
      e.node?.parent && filterFa(e.node);
    } else {
      //勾选
      if (e.node.children && e.node.children.length > 0) {
        const ids: any[] = e.node.children.map((item: any) => item.id);
        checkedKeys.value.push(...ids);
      }
      checkNodes.set(e.node.id, e.node);
      e.node?.parent && addFa(e.node);
    }
  };
  // 调整右边树父子节点关联
  const filterFa2 = (node: any) => {
    featureCheckedKeys.value = featureCheckedKeys.value.filter(item => item !== '0-0-0'); //取消勾选都去掉'全部'节点
    const fils: any[] = [];
    let cur = node;
    // eslint-disable-next-line no-constant-condition
    while (true) {
      const flag = cur.parent.children.every((item: any) => {
        if (item.key !== cur.key) {
          return !featureCheckedKeys.value.includes(item.key);
        }
        return true;
      });
      if (!flag) break;

      cur = cur.parent;
      fils.push(cur.key);
      if (!cur || !cur.parent) break;
    }
    if (fils.length === 0) return;
    featureCheckedKeys.value = featureCheckedKeys.value.filter(i => !fils.includes(i));
  };

  const addFa2 = (node: any) => {
    let par = node.parent;
    while (par) {
      if (featureCheckedKeys.value.includes(par.key) || par.key == '0-0-0') {
        break;
      }
      featureCheckedKeys.value.push(par.key);
      par = par.parent;
    }
  };
  // 右边树的check方法
  const funCheckNode = (keys: any, obj: any) => {
    const { node, checkedNodes, checked } = obj;
    const curKey = selectKeys.value?.[0];
    featureCheckedKeys.value = checkedNodes.map((item: any) => item.id);
    if (checked) {
      //勾选
      // 勾选全部
      if (node.id === '0-0-0') {
        const loop = (treeData: any[]) => {
          treeData.forEach((item: any) => {
            featureCheckedKeys.value.push(item.id);
            if (item.children) {
              loop(item.children);
            }
          });
        };
        if (node?.children && node?.children?.length) {
          loop(node?.children);
        }
      }
      node?.parent && addFa2(node);
      featureCheckedKeys.value = [...new Set(featureCheckedKeys.value)];
      // 判断回显'全部'节点
      if (featureCheckedKeys.value.length === rightNodeNum.value) {
        featureCheckedKeys.value.push('0-0-0');
      }
    } else {
      //取消勾选
      if (node.id === '0-0-0') {
        //取消的全部
        featureCheckedKeys.value = [];
        keys.checked = [];
      } else if (node.children && node.children.length > 0) {
        const ids: any[] = node.children.map((item: any) => item.id);
        featureCheckedKeys.value = featureCheckedKeys.value.filter(item => !ids.includes(item));
        keys.checked = keys?.checked.filter((item: any) => !ids.includes(item));
      }
      node?.parent && filterFa2(node);
    }
    //如果右边选中的长度大于0
    if (keys?.checked ? keys?.checked.length : keys.length > 0) {
      const arr: any[] = [];
      checkedKeys.value.forEach(item => {
        if (item.length > 9 && item.includes(curKey)) {
          arr.push(item);
        }
      });
      if (checkedKeys.value.some(item => item === curKey)) {
        checkedKeys.value = checkedKeys.value.filter(item => !arr.includes(item));
        checkedKeys.value = [
          ...checkedKeys.value.filter(item => item !== '0-0-0'),
          ...keys.checked.filter(item => item && item.length !== 12),
        ];
        return;
      }
      //没有就添加进去
      checkedKeys.value.push(curKey);
      addFa(curentNode.value);
    } else {
      const arr: any[] = [];
      checkedKeys.value.forEach(item => {
        if (item.length > 9 && item.includes(curKey)) {
          arr.push(item);
        }
      });
      if (checkedKeys.value.some(item => item === curKey)) {
        checkedKeys.value = checkedKeys.value.filter(item => !arr.includes(item));
        checkedKeys.value = [...checkedKeys.value.filter(item => item !== '0-0-0'), ...keys];
      }
    }
  };

  const getCheckedData = () => {
    keyMap.set(selectKeys.value[0], featureCheckedKeys.value.join(','));
    let items = [...keyMap.values()].join(',').split(',');
    items = items.filter((item: any) => item && item !== '0-0-0' && item.length > 9);
    let set: any = [];
    set = new Set([...checkedKeys.value.filter(item => item !== '0-0-0'), ...items]);
    return [...set];
  };
  // 确定时所传数据
  const allCheckedData = () => {
    activeMap.set(props.activeKey, getCheckedData().join(','));
    const data: any[] = [];
    const rightNodeFlagTrues = [...rightNodeFlagTrue.values()].join(',').split(',');
    activeMap.forEach((item, key) => {
      const items = [...new Set(item?.split(','))].filter(item => item && item !== key) || [];
      const items2 = items.filter(item => item && item?.length > 9); //存右边树选上的节点
      // 找到取消的
      const filteredStrings = rightNodeFlagTrues.filter(item => !items2.includes(item));
      data.push({
        rootMenuId: key,
        menuIds: items,
        delFuncIds: filteredStrings.filter(item => item.slice(0, 3) == key),
      });
    });
    return data;
  };

  defineExpose({
    allCheckedData,
    displayTreeData,
  });

  watch(
    () => props.activeKey,
    (val, oldval) => {
      displayTreeData.value[0].children = [];
      displayTreeData.value = [...displayTreeData.value];
      if (oldval) activeMap.set(oldval, getCheckedData().join(','));
      if (val) {
        if (activeMap.has(val)) {
          checkedKeys.value = activeMap.get(val).split(',');
        } else {
          checkedKeys.value = allCheckKeys.find((item: any) => item.rootMenuId === props.activeKey)?.menuIds || [];
        }
      }
    },
    { immediate: true },
  );
</script>
<style lang="less" scoped>
  .menuList {
    height: calc(100% - 48px);
    overflow: auto;
    box-sizing: border-box;
  }
  .permissionInfo {
    width: 100%;
    height: 100%;
    border: 1px solid #ccc;
  }
  .info-container {
    width: 100%;
    height: 100%;
    display: flex;
  }
  .menuDetails {
    width: 50%;
    height: 100%;
    border-right: 1px solid #ccc;
  }

  .topNav {
    background-color: #fafafa;
    padding: 16px;
    height: 48px;
  }

  .feature {
    width: 50%;
    height: 100%;
  }
  :deep(.menu-tree) {
    .plat-tree-treenode-selected {
      background-color: rgb(235, 241, 255);
    }
  }
</style>
