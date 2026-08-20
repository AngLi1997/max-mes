<template>
  <div>
    <Modal
      :okText="t('确定')"
      :cancelText="t('取消')"
      :open="props.openMenu"
      :maskClosable="false"
      :title="type === '1' ? t('菜单分配') : t('权限授权')"
      :destroyOnClose="true"
      wrapClassName="modalSizeLarge"
      @ok="handleOk"
      @cancel="close">
      <div class="menuContent">
        <Empty v-if="tabs.length === 0"></Empty>
        <div v-else class="systemNav">
          <Tabs :activeKey="activeKey" @change="tabChange">
            <TabPane v-for="tab in tabs" :key="tab.id" :tab="tab.name"></TabPane>
          </Tabs>
          <MenuTab
            v-if="activeKey"
            ref="menu_tab"
            :treeList="treeList"
            :activeKey="activeKey"
            :roleId="roleId"
            :type="type"></MenuTab>
        </div>
      </div>
    </Modal>
  </div>
</template>
<script lang="ts" setup>
  import { Modal, Tabs, TabPane, message } from 'ant-design-vue';
  import { ref, computed } from 'vue';
  import { t } from '@bmos/i18n';

  import Empty from '../../../../components/Empty/index.vue';
  import {
    postMenuSave,
    reqRoleAtuhMenuSave,
    getTreePermissionManage,
    reqMenuAuthMenuTreeList,
  } from '@/api/Permissions/roleManagement';
  import { buttonPermissions } from '@/utils/permission';
  import MenuTab from './menuTab.vue';
  const activeKey = ref('');
  // 此处菜单列表是由超级管理员已经给予配置相应的菜单权限
  const menu_tab = ref();
  const tabs = ref<any[]>([]);

  const props = defineProps({
    openMenu: {
      type: Boolean,
      default: false,
    },
    roleId: {
      type: String,
      default: '0',
    },
    type: {
      //判断是菜单权限还是权限授权 '1'为菜单权限 '2'为权限授权
      type: String,
      default: '1',
    },
  });

  const getTerminalTypeApi = async (params: any) => {
    try {
      const res: any = props.type === '1' ? await getTreePermissionManage(params) : await reqMenuAuthMenuTreeList();
      if (res.code === 0) {
        tabs.value = res.data;
        activeKey.value = tabs.value[0]?.id;
        return;
      }
      tabs.value = [];
    } catch (error) {
      tabs.value = [];
    }
  };

  const treeList = computed(() => {
    const dataList = tabs.value.find(item => item.id === activeKey.value)?.children || [];
    return dataList;
  });

  const tabChange = (active_key: any) => {
    activeKey.value = active_key;
    menu_tab.value.displayTreeData[0].checkable = false; //右边树'全部节点取消checkable'
  };

  //打开弹窗
  const emit = defineEmits(['update:openMenu']);
  const close = () => {
    activeKey.value = '';
    emit('update:openMenu', false);
  };

  //点击确定
  const handleOk = async () => {
    const allData = menu_tab.value.allCheckedData();
    console.log([...allData], 'menu_tab.value.getCheckedData()');
    try {
      const res: any =
        props.type === '1'
          ? await postMenuSave({
              items: allData,
              roleId: props.roleId,
            })
          : await reqRoleAtuhMenuSave({
              items: allData,
              roleId: props.roleId,
            });
      if (res.code === 0) {
        message.success(t('操作成功'));
        close();
        buttonPermissions();
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  watch(
    () => props.openMenu,
    val => {
      if (val) {
        getTerminalTypeApi({ type: 0, containsFunc: false });
      }
    },
  );
</script>

<style scoped lang="less">
  .systemNav {
    height: 100%;
  }
  :deep(.modalSizeLarge .plat-modal-body) {
    overflow: hidden;
  }
  .permissionInfo {
    width: 100%;
    height: calc(100% - 56px);
    border: 1px solid #ccc;
    display: flex;
  }
  .menuContent {
    height: 510px;
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
</style>
