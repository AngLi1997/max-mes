<template>
  <div class="system">
    <div v-if="treeData.length > 0" style="display: flex; width: 100%; height: 100%">
      <div class="systemTree">
        <BMSearchTree
          :selectedKeys="SelectedKeys"
          :fieldNames="{ title: 'name', key: 'id' }"
          v-bind="treeProps"
          :expandedKeys="expandedKeys"
          :tree-data="treeData"
          @select="select"></BMSearchTree>
      </div>
      <div class="systemContent">
        <div v-if="SelectedKeys.length > 0" style="height: 100%">
          <div class="systemContentNav">
            <Row justify="space-between">
              <Col :span="8">{{ t('绑定角色') }}</Col>
              <Col>
                <Button v-hasAuth="100030004000001" type="primary" style="margin-top: -14px" @click="editModal">
                  {{ t('编辑') }}
                </Button>
              </Col>
            </Row>
          </div>
          <div v-if="displayTreeData.length > 0" class="system-content-tree">
            <Tree
              v-if="displayTreeData.length"
              class="displayTree"
              :defaultExpandAll="true"
              :tree-data="displayTreeData"
              :selectable="false"
              show-icon
              :fieldNames="{
                title: 'name',
                key: 'id',
              }">
              <template #icon="{ dataRef }">
                <TeamOutlined v-if="!dataRef.roleTypeFlag" />
              </template>
            </Tree>
          </div>
          <Empty v-else :emptyName="t('暂无角色')"></Empty>
        </div>
        <Empty v-else></Empty>
      </div>
    </div>
    <Empty v-else></Empty>
    <EditRole v-model:open="open" :node="currentNode" :menuId="menuId" @success="success"></EditRole>
  </div>
</template>
<script lang="ts" setup>
  import { Button, Row, Col, Tree } from 'ant-design-vue';
  import { BMSearchTree, SearchTreeProps } from '@bmos/components';
  import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
  import Empty from '../../../components/Empty/index.vue';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { ref, onMounted } from 'vue';
  import EditRole from './editRole/index.vue';
  import { t } from '@bmos/i18n';
  import { getPermissionMenuList2, getPerrmissionRoleTreeAll } from '../../../api/Permissions/authorization';
  import { TeamOutlined } from '@ant-design/icons-vue';
  const props = defineProps({
    activeKey: {
      type: String,
      default: '100',
    },
  });
  const currentNode = ref();
  const expandedKeys = ref<any[]>([]);
  const SelectedKeys = ref<any[]>([]);
  const displayTreeData = ref([]);
  const treeProps: SearchTreeProps = {
    addChildrenNeedCode: true,
    showAddChildren: false,
    showDeleteNode: false,
    showAction: false,
  };

  // 打开弹窗
  const open = ref<boolean>(false);
  const editModal = () => {
    open.value = true;
  };

  const treeData = ref<any[]>([]);
  const menuId = ref<any>('');

  //渲染左侧菜单树
  const getMenuListApi = async (params: any) => {
    try {
      const res: any = await getPermissionMenuList2(params);
      if (res.code === 0) {
        treeData.value = res.data.map((item: any) => ({
          ...item,
          selectable: false,
        }));
        const first = res.data?.[0];
        const f_node = first?.children?.[0];
        menuId.value = f_node?.id;
        expandedKeys.value = [f_node?.id];
        SelectedKeys.value = [f_node?.id];
        getRoleTreeAllApi({ menuId: menuId.value }); //传菜单id
        currentNode.value = { ...f_node };
        currentNode.value.parent = first;
      }
    } catch (error) {
      //
    }
  };

  //选中
  const select = (
    selectedKeys: Key[],
    info: {
      event: 'select';
      selected: boolean;
      node: EventDataNode;
      selectedNodes: DataNode[];
      nativeEvent: MouseEvent;
    },
  ) => {
    if (selectedKeys.length === 0) return;
    SelectedKeys.value = selectedKeys;
    menuId.value = selectedKeys[0];
    getRoleTreeAllApi({ menuId: menuId.value });
    currentNode.value = info.node;
  };

  //渲染右侧角色树
  const getRoleTreeAllApi = async (params?: any) => {
    try {
      const result: any = await getPerrmissionRoleTreeAll(params);
      if (result.code === 0) {
        const data = result.data;
        displayTreeData.value = data;
        return;
      }
      displayTreeData.value = [];
    } catch (error) {
      displayTreeData.value = [];
    }
  };

  const success = () => {
    getRoleTreeAllApi({ menuId: menuId.value });
  };

  onMounted(async () => {
    if (props.activeKey) {
      await getMenuListApi({ rootMenuCode: props.activeKey });
    }
  });
</script>

<style lang="less" scoped>
  .systemTree {
    width: 600px;
    height: 100%;
    .bmos-search-tree {
      max-height: 750px;
      overflow: auto;
      position: relative;
    }
    :deep(.plat-input-group-wrapper) {
      position: sticky;
      top: 0;
      margin: 0;
      padding: 16px;
      z-index: 1;
      background: #fff;
      width: 100%;
    }
  }

  :deep(.plat-tabs-nav) {
    margin-bottom: 0;
  }

  .systemContent {
    padding: 16px;
    width: 100%;
    height: 100%;
    border-left: 1px solid #e8e8e8;
  }

  .system {
    display: flex;
    height: 100%;
  }

  .system-content-tree {
    padding: 16px 0px;
    overflow-y: scroll;
    height: 99%;
  }
  :deep(.plat-tree-treenode) {
    width: 50%;
  }
</style>
