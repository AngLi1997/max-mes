<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('绑定人员')"
    wrapClassName="modalSizeMedium"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @okModal="ok">
    <div class="tree-container">
      <BMSearchTree
        v-if="treeActive.treeData[0]?.children.length > 0"
        v-model:checked-keys="treeActive.checkedKeys"
        v-model:expanded-keys="treeActive.expandedKeys"
        :showSearch="true"
        :showAllAddIcon="false"
        :showAction="false"
        :tree-data="treeActive.treeData"
        :checkable="true"
        show-icon
        @check="check">
        <template #icon="{ dataRef }">
          <BMIcons v-if="dataRef.deptFlag" icon="Bag" style="width: 16px"></BMIcons>
          <TeamOutlined v-else />
        </template>
      </BMSearchTree>
      <Empty v-else></Empty>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import { reactive, computed, watch } from 'vue';
  import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
  import { getDeptUserTree, reqStationBindUser } from '@/services';
  import { BMIcons } from '@bmos/icons';
  import { TeamOutlined } from '@ant-design/icons-vue';
  const emit = defineEmits(['updateTable']);
  const open = ref<boolean>(false);
  const props = withDefaults(
    defineProps<{
      rowId: string;
      rootNode: string;
      userIdList: any;
    }>(),
    {
      rowId: '',
      rootNode: '',
      userIdList: [],
    },
  );
  const treeActive = reactive<any>({
    treeData: [],
    checkedKeys: [],
    expandedKeys: [],
  });
  // 遍历树， 找到所有的人员 deptFlag = false
  const flatTree = (tree: any[]): any[] => {
    const data = [];
    for (let index = 0; index < tree.length; index++) {
      const element = tree[index];
      if (element.deptFlag) {
        if (element?.children?.length > 0) {
          const list = flatTree(element?.children);
          data.push(...list);
        }
      } else {
        data.push(element);
      }
    }
    return data;
  };
  const check = async (
    keys: any[] | { checked: any[]; halfChecked: any[] },
    info: {
      event: 'check';
      checked: boolean;
      node: EventDataNode;
      checkedNodes: DataNode[];
      nativeEvent: MouseEvent;
    },
  ) => {
    if (info.checked) {
      //勾选
      // 如果点击的是其他部门中的人员
      info.checkedNodes.forEach((item: any) => {
        // 是人员
        if (!item.deptFlag) {
          if (hasDeptUserMap.has(info.node?.id)) {
            const mapItem = hasDeptUserMap.get(info.node.id);
            mapItem.forEach((mapItem: any) => {
              treeActive.checkedKeys.push(mapItem.key);
            });
            treeActive.checkedKeys = [...new Set(treeActive.checkedKeys)];
          }
        } else {
          // 是部门
          const loop = (treeData: any[]) => {
            treeData.forEach((item: any) => {
              if (hasDeptUserMap.has(item.id)) {
                const mapItem = hasDeptUserMap.get(item.id);
                mapItem.forEach((mapItem: any) => {
                  treeActive.checkedKeys.push(mapItem.key);
                  treeActive.checkedKeys = [...new Set(treeActive.checkedKeys)];
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
          // 去除 treeActive.checkedKeys 中的部门id
          const index = treeActive.checkedKeys.indexOf(item.key as string);
          if (index > -1) {
            treeActive.checkedKeys.splice(index, 1);
          }
        }
      });
    } else {
      //取消勾选
      if (info.node?.dataRef?.deptFlag && info.node?.dataRef?.children?.length) {
        // 如果点击的是部门
        const userInDept = flatTree(info.node?.dataRef?.children as any);
        userInDept.forEach((item: any) => {
          if (hasDeptUserMap.has(item.id)) {
            const mapItem = hasDeptUserMap.get(item.id);
            const newKeys = mapItem.reduce((prev: any[], cur: any) => {
              prev.push(cur.parentId, cur.key);
              return prev;
            }, []);
            treeActive.checkedKeys = treeActive.checkedKeys.filter((item: any) => !newKeys.includes(item));
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
          treeActive.checkedKeys = treeActive.checkedKeys.filter((item: any) => !newKeys.includes(item));
        }
      }
    }
  };
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
    const { data } = await getDeptUserTree();
    const hasDeptUser = data || [];
    const newHasDeptUser = loopHasDeptUser(hasDeptUser);
    treeActive.expandedKeys = ['0'];
    treeActive.treeData = [
      {
        id: '0',
        key: '0',
        title: props.rootNode,
        deptFlag: true,
        children: [...newHasDeptUser],
      },
    ];
    // 选中的节点
    if (hasCheckPeople.length) {
      hasCheckPeople.forEach((item: any) => {
        const mapItem = hasDeptUserMap.get(item);
        if (mapItem) {
          mapItem.forEach((item: any) => {
            treeActive.checkedKeys.push(item.key);
          });
        }
      });
    }
  };

  const IS_CHECK = new Map();
  const CHECK_LIST = computed(() => {
    // console.log('treeActive.checkedKeys', treeActive.checkedKeys);
    for (let index = 0; index < treeActive.checkedKeys.length; index++) {
      const element = treeActive.checkedKeys[index];
      if (element && !IS_CHECK.has(element)) {
        if (element.indexOf('-') > -1) {
          const [_deptId, userId] = element.split('-');
          const mapItem = hasDeptUserMap.get(userId);
          !IS_CHECK.has(userId) && mapItem && mapItem.length && IS_CHECK.set(userId, mapItem[0]);
        }
      }
    }
    const checks = [...IS_CHECK.values()];
    IS_CHECK.clear();
    return checks;
  });
  // 绑定人员确定弹框
  const ok = async () => {
    if (treeActive.treeData[0]?.children.length === 0) {
      open.value = false;
      return;
    }
    const arr: any = [];
    CHECK_LIST.value.forEach((item: any) => {
      arr.push(item.id);
    });
    try {
      const data = {
        stationId: props.rowId,
        userIdList: [...new Set(arr)],
      };
      await reqStationBindUser(data);
      emit('updateTable');
      message.success(t('绑定成功'));
      open.value = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        initTreeData(props.userIdList);
      } else {
        treeActive.checkedKeys = [];
      }
    },
    {
      immediate: true,
    },
  );
  const openModal = () => {
    open.value = true;
  };
  defineExpose({
    openModal,
  });
</script>

<style scoped lang="less">
  .tree-container {
    height: 400px;
    overflow-y: auto;
  }
</style>
