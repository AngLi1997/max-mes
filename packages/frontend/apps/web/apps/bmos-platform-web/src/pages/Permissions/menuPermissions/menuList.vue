<template>
  <div class="system">
    <div v-if="treeData.length > 0" style="display: flex; width: 100%; height: 100%">
      <div class="systemTree">
        <BMSearchTree
          v-bind="treeProps"
          ref="searchTreeRef"
          :selectedKeys="SelectedKeys"
          :expanded-keys="expandedKeys"
          :tree-data="treeData"
          :fieldNames="{
            title: 'name',
            key: 'id',
          }"
          @select="select"></BMSearchTree>
      </div>
      <div class="systemContent">
        <div v-if="menuId" style="height: 100%; width: 100%">
          <div class="systemContentNav">
            <Row justify="space-between">
              <Col :span="8">{{ t('绑定角色') }}</Col>
              <Col>
                <Button v-hasAuth="100030005000001" type="primary" style="margin-top: -14px" @click="editModal">
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
              :selectable="false"
              show-icon
              :tree-data="displayTreeData"
              :fieldNames="{
                title: 'roleName',
                key: 'id',
              }">
              <template #icon="{ dataRef }">
                <TeamOutlined v-if="dataRef.roleTypeId" />
              </template>
            </Tree>
          </div>
          <Empty v-else :emptyName="t('暂无角色')"></Empty>
        </div>
        <Empty v-else></Empty>
      </div>
    </div>
    <Empty v-else></Empty>
    <EditRole v-model:open="open" :menuId="menuId" :node="currentNode" @success="success"></EditRole>
  </div>
</template>
<script lang="ts" setup>
  import { Row, Col, Button, Tree, message } from 'ant-design-vue';
  import { reactive, ref, onMounted } from 'vue';
  import { t } from '@bmos/i18n';
  import Empty from '../../../components/Empty/index.vue';
  import { BMSearchTree, SearchTreeProps, SearchTreeInstance } from '@bmos/components';
  import EditRole from './editRole/index.vue';
  import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { getRoleTreeAll } from '../../../api/Permissions/menuPermissions';
  import { TeamOutlined } from '@ant-design/icons-vue';
  const props = defineProps({
    activeKey: {
      type: String,
      default: '100',
    },
    treeList: {
      type: Array<any>,
      default: () => [],
    },
  });
  const expandedKeys = ref<any[]>([]);
  const displayTreeData = ref([]);
  const SelectedKeys = ref<any[]>([]);
  const searchTreeRef = ref<SearchTreeInstance>();
  const treeProps: SearchTreeProps = reactive({
    addChildrenNeedCode: true,
    showAddChildren: false,
    showDeleteNode: false,
    showAction: false,
  });

  // 打开弹窗
  const open = ref<boolean>(false);
  const editModal = () => {
    open.value = true;
  };

  const treeData = computed(() => {
    return props.treeList.map((item: any) => ({ ...item, selectable: false }));
  });

  const menuId = ref<any>('');
  const currentNode = ref({});

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
  // 右侧树需要展示分类下的角色
  const loopTree = (data: any) => {
    return data?.map((item: any) => {
      if (item.children && item.children?.length > 0) {
        item.children = [...item.children, ...(item.roleList || [])];
        item.roleName = item.name || item.roleName;
      } else {
        item.children = item?.roleList || [];
        item.roleName = item.name || item.roleName;
      }
      if (item.children) {
        loopTree(item.children);
      }
      return item;
    });
  };
  //渲染右侧角色树
  const getRoleTreeAllApi = async (params: any) => {
    try {
      const result: any = await getRoleTreeAll(params);
      if (result.code === 0) {
        const data = loopTree(result.data);
        displayTreeData.value = data;
        return;
      }
      displayTreeData.value = [];
    } catch (error: any) {
      displayTreeData.value = [];
      message.error(error.message);
    }
  };

  const success = () => {
    getRoleTreeAllApi({ menuId: menuId.value });
  };

  onMounted(async () => {
    if (props.treeList) {
      const first: any = props.treeList[0];
      const f_node = first?.children?.[0];
      if (f_node) {
        menuId.value = f_node.id;
        SelectedKeys.value = [menuId.value];
        expandedKeys.value = [f_node.id];
        getRoleTreeAllApi({ menuId: menuId.value }); //传菜单id
        currentNode.value = { ...f_node, parent: first };
      }
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
  :deep(.bmos-search-tree) {
    height: 100%;
    width: 600px;
    border-right: 1px solid #e8e8e8;
  }

  .systemContent {
    padding: 16px;
    width: 100%;
    height: 100%;
  }
  .system {
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
