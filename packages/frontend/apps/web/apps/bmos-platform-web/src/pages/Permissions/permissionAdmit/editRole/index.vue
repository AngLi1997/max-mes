<template>
  <div>
    <Modal
      :okText="t('确定')"
      :cancelText="t('取消')"
      :open="props.open"
      :title="t('角色编辑')"
      :maskClosable="false"
      wrapClassName="modalSizeMedium"
      @ok="handleOk"
      @cancel="close">
      <div style="height: 510px; overflow: auto">
        <Tree
          v-if="treeData.length > 0 && allRoles.length > 0"
          v-model:expandedKeys="expandedKeys"
          v-model:checkedKeys="checkedKeys"
          checkable
          :selectable="false"
          :tree-data="treeData"
          :fieldNames="{
            title: 'name',
            key: 'id',
          }"
          @check="checkNode"></Tree>
        <Empty v-else :emptyName="t('请先配置菜单的权限')"></Empty>
      </div>
    </Modal>
  </div>
</template>
<script lang="ts" setup>
  import { Modal, Tree, message } from 'ant-design-vue';
  import { ref, watch } from 'vue';
  import {
    getPermissionRoleTree,
    postPermissionMenuSave,
    getRoleTreeAll,
    getPerrmissionRoleTreeAll,
  } from '../../../../api/Permissions/authorization';
  import Empty from '../../../../components/Empty/index.vue';
  import { diffArray } from '../../utils';
  import { t } from '@bmos/i18n';
  const ALL_CONST = {
    name: t('全部'),
    id: '0-0',
    children: [],
  };
  const treeData = ref<any>([ALL_CONST]);

  const expandedKeys = ref<string[]>(['0-0']);
  const checkedKeys = ref<string[]>(['0-0-0', '0-0-1']);
  const allRoles = ref([]); //查所有树结构数据(非回显数据),若为空就展示暂无数据
  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
    menuId: {
      type: String,
      default: '',
      required: true,
    },
    node: {
      type: Object,
      require: true,
    },
  });

  const emit = defineEmits(['update:open', 'success']);
  const close = () => {
    emit('update:open', false);
  };
  let B_CHECKS: string[] = [];

  const CONST_KEY = ['0-0'];
  const checkNode = (keys: any, e: { checkedNodes: any[] }) => {
    const roleKeys = e.checkedNodes.reduce((prev: any, cur: any) => {
      if (!cur.roleTypeFlag) {
        prev.push(cur.id);
      }
      return prev;
    }, []);
    checkedKeys.value = roleKeys;
  };

  const getRoleListModal = async (params?: any) => {
    try {
      const res: any = await getRoleTreeAll(params);
      allRoles.value = res.data || [];
      let list = res.data || [];
      treeData.value = [{ ...ALL_CONST, children: list }];
    } catch (error: any) {
      treeData.value = [];
      message.error(error.message);
    }
  };
  const getRoleTreeApi = async (params: any) => {
    try {
      const result: any = await getPermissionRoleTree(params);
      checkedKeys.value = result.data;
      B_CHECKS = [...result.data];
    } catch (error) {
      checkedKeys.value = [];
    }
  };

  const getPermissionRoleList = async (params: any) => {
    try {
      const res: any = await getPerrmissionRoleTreeAll(params);
      let list = [];
      if (res.code === 0) {
        list = res.data;
        allRoles.value = res.data || [];
      }
      treeData.value = [{ ...ALL_CONST, children: list }];
    } catch (error) {
      treeData.value = [{ ...ALL_CONST }];
    }
  };

  const getParentIds = () => {
    let cur = props.node;
    const ids = [];
    while (cur) {
      ids.push(cur.id || cur.key);
      cur = cur.parent;
    }
    return ids.filter(item => item);
  };

  const handleOk = async () => {
    const data = {
      menuIds: getParentIds(),
      roleIds: checkedKeys.value.filter((item: any) => !CONST_KEY.includes(item)),
      isMenu: Boolean(props.node?.isMenu),
      deletedRoleIds: diffArray<string>(B_CHECKS, checkedKeys.value),
    };
    try {
      const res: any = await postPermissionMenuSave(data);
      if (res.code === 0) {
        message.success(t('操作成功'));
        emit('success');
        return close();
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  watch(
    () => props.open,
    newVal => {
      checkedKeys.value = [];

      if (newVal) {
        if (props.node?.isMenu) {
          getRoleListModal({ menuId: props.menuId });
        } else {
          getPermissionRoleList({ menuId: props.node?.parentId });
        }

        getRoleTreeApi({ menuId: props.menuId });
      }
    },
  );
</script>
