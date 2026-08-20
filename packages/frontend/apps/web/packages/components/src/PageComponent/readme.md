# pageComponent 页面组件

结合 BMSearchTree 和 BMTable 组件 实现的页面组件四种种情况：1、只要一个table， 传入 hideRightTree 隐藏树，columns 数组只
传一项 2、两个 table，传入 hideRightTree 隐藏树，columns 数组传两项 3、左侧树加上下两个 table，传入 treeData 树数
据，columns 数组传两项 3、左侧树加上下一个 table，传入 treeData 树数据，columns 数组传一项

## 基础用法

```vue
<template>
  <BMPageComponent
    ref="pageRef"
    :columns="columns"
    :requests="requests"
    :showAllAddIcon="hasPermission('120070001000001')"
    :showAction="
      hasPermission('120070001000001') || hasPermission('120070001000002') || hasPermission('120070001000003')
    "
    :actionList="actionList"
    :rowKeys="['id', 'id']"
    :search="[true, false]"
    :treeData="treeData"
    :formProps="[formFirstProps, {}]"
    :rowClick="handleRowClick"
    :fieldNames="{
      title: 'name',
      key: 'id',
    }"
    :treeField="{
      field: {
        datasetCategoryId: 'id',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          datasetId: 'id',
          datasetType: 'datasetType',
        },
      },
    ]"
    :selectedKeys="treeSelectedKeys"
    @treeSelect="selectTreeNode"
    @tree-action="handleTreeAction">
    <template #tableHeaderToolbar0="{ treeNode }">
      <Button v-hasAuth="120070001000004" type="primary" @click="() => handleAddDataset(treeNode)">
        {{ t('新增') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('数据集')"></BMTableTitle>
    </template>
    <template #tableHeaderTitle1>
      <BMTableTitle :title="t('数据点信息')"></BMTableTitle>
    </template>
  </BMPageComponent>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="treeModalOpen"
    :title="treeModalTitle"
    :formProps="treeModalFormProps"
    wrapClassName="modalSizeMedium"
    :submit="treeModalSubmit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, BMPageComponent, BMTableTitle } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Button } from 'ant-design-vue';
  import { useTables } from './hooks';
  import { usePermissionStore } from '@/stores/permission';
  import { OperationType } from './type';

  const router = useRouter();

  const { hasPermission } = usePermissionStore();

  const {
    requests,
    columns,
    treeData,
    pageRef,
    formFirstProps,
    handleRowClick,
    actionList,
    handleTreeAction,
    treeSelectedKeys,

    treeModalOpen,
    treeModalTitle,
    treeModalFormProps,
    treeModalSubmit,
    selectTreeNode,
  } = useTables();

  const handleAddDataset = (treeNode: any) => {
    router.push({
      name: 'data-set-manage-detail',
      query: {
        type: OperationType.Add,
        ...(treeNode.id && treeNode.id !== 'all' && { datasetCategoryId: treeNode.id }),
      },
    });
  };
</script>

<style scoped lang="less"></style>
```

useTables hooks 如下

```tsx
import { NODE_TYPE } from '@/components/Record';
import {
  reqDatasetCategoryCreateCategory,
  reqDatasetCategoryDelete,
  reqDatasetCategoryEditCategory,
  reqDatasetCategoryTree,
  reqDatasetDeleteDataset,
  reqDatasetQueryDatasetLotReleaseLinksPage,
  reqDatasetQueryDatasetPage,
  reqDatasetQueryDatasetPointPage,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type {
  ActionListItem,
  ActionListItemCustomRenderParams,
  DataRequestFn,
  FormProps,
  Recordable,
  TableColumn,
  TableInstance,
} from '@bmos/components';
import { t } from '@bmos/i18n';
import { copyToClipboard } from '@bmos/utils';
import { Button, Dropdown, Menu, MenuItem, Modal, message } from 'ant-design-vue';
import { DataNode, EventDataNode } from 'ant-design-vue/es/tree';
import { createVNode } from 'vue';
import { DatasetType, DatasetTypeMap, OperationType } from '../type';

export const useTables = () => {
  const { hasPermission } = usePermissionStore();
  const router = useRouter();

  const pageRef = ref<any>();

  // 升级版本model
  const upgradeVersionModalOpen = ref<boolean>(false);
  const firstRowData = ref<Recordable>({});

  const handleRowClick = (record: Recordable, index: number, _instance: TableInstance, _keys: KEY[]) => {
    if (index !== 0) return;
    firstRowData.value = record;
    if (record.datasetType === 3) {
      pageRef.value?.getTableRef(1)?.updateColumn([
        {
          dataIndex: 'name',
          hideInTable: true,
        },
        {
          dataIndex: 'batchReleaseName',
          hideInTable: false,
        },
        {
          dataIndex: 'version',
          hideInTable: false,
        },
      ]);
    } else {
      pageRef.value?.getTableRef(1)?.updateColumn([
        {
          dataIndex: 'name',
          hideInTable: false,
        },
        {
          dataIndex: 'batchReleaseName',
          hideInTable: true,
        },
        {
          dataIndex: 'version',
          hideInTable: true,
        },
      ]);
    }
  };

  const templateColumn: TableColumn[] = [
    {
      title: t('名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 200,
    },
    {
      title: t('类型'),
      dataIndex: 'datasetType',
      width: 200,
      customRender: ({ record }) => {
        return <span>{record.type?.label}</span>;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: [
            ...Array.from(DatasetTypeMap).map(([key, value]) => ({
              label: value,
              value: key,
            })),
          ],
        },
      },
    },
    {
      title: t('数据索引'),
      dataIndex: 'datasetKey',
      hideInSearch: true,
      width: 200,
      customRender: ({ record }) => {
        return (
          <Button
            type='link'
            onClick={e => {
              e?.preventDefault();
              copyDataIndex(record.datasetKey);
            }}>
            {record.datasetKey}
          </Button>
        );
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('120070001000006'),
          onClick: () => {
            router.push({
              name: 'data-set-manage-detail',
              query: {
                type: OperationType.View,
                id: record.id,
              },
            });
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('120070001000005'),
          onClick: () => {
            router.push({
              name: 'data-set-manage-detail',
              query: {
                type: OperationType.Edit,
                id: record.id,
              },
            });
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('120070001000007'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除此数据集'),
              icon: h(ExclamationCircleOutlined),
              content: t('数据集删除后无法恢复, 是否删除?'),
              async onOk() {
                try {
                  await reqDatasetDeleteDataset(record.id);
                  message.success(t('操作成功'));
                  pageRef.value?.fetchDataTwo();
                  return Promise.resolve();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
      ],
    },
  ];

  const copyDataIndex = async (text: string) => {
    try {
      await copyToClipboard(text);
      message.success(`${text} ${t('复制成功')}`);
    } catch (error) {
      message.error(`${text} ${t('复制失败')}`);
    }
  };
  const listColumn: TableColumn[] = [
    {
      title: t('数据点名称'),
      dataIndex: 'name',
      headerSearchComponent: 'Input',
      fixed: 'left',
      width: 200,
    },
    {
      title: t('批签发名称'),
      dataIndex: 'batchReleaseName',
      headerSearchComponent: 'Input',
      hideInTable: true,
      fixed: 'left',
      width: 200,
      customRender: ({ record }) => {
        return record.name;
      },
    },
    {
      title: t('批签发版本'),
      dataIndex: 'version',
      headerSearchComponent: 'Input',
      hideInTable: true,
      width: 200,
    },
    {
      title: t('数据索引'),
      dataIndex: 'datasetPointKey',
      width: 200,
      customRender: ({ record }) => {
        return (
          <Dropdown trigger={['contextmenu']}>
            {{
              default: () => (
                <Button
                  type='link'
                  onClick={e => {
                    e?.preventDefault();
                    try {
                      if (
                        JSON.parse(record.extra)?.componentType === NODE_TYPE.RADIO ||
                        JSON.parse(record.extra)?.componentType === NODE_TYPE.CHECKBOX
                      ) {
                        copyDataIndex(
                          '${(' + firstRowData.value?.datasetKey + '.' + record.datasetPointKey + ')[][][][][]}',
                        );
                      } else {
                        copyDataIndex(
                          '${(' + firstRowData.value?.datasetKey + '.' + record.datasetPointKey + ')[][][][]}',
                        );
                      }
                    } catch (error) {
                      copyDataIndex(
                        '${(' + firstRowData.value?.datasetKey + '.' + record.datasetPointKey + ')[][][][]}',
                      );
                    }
                  }}>
                  {record.datasetPointKey}
                </Button>
              ),
              overlay: () => {
                return (
                  <Menu>
                    <MenuItem
                      onClick={() => copyDataIndex(firstRowData.value?.datasetKey + '.' + record.datasetPointKey)}>
                      {firstRowData.value?.datasetKey + '.' + record.datasetPointKey}
                    </MenuItem>
                    <MenuItem
                      onClick={() =>
                        copyDataIndex(',' + firstRowData.value?.datasetKey + '.' + record.datasetPointKey)
                      }>
                      {',' + firstRowData.value?.datasetKey + '.' + record.datasetPointKey}
                    </MenuItem>
                  </Menu>
                );
              },
            }}
          </Dropdown>
        );
      },
    },
  ];

  // 树
  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await reqDatasetCategoryTree();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          categoryFlag: true,
          key: 'all',
          children: data,
        },
      ];
    } catch (error) {}
  };

  const currentNodeSelect = ref<DataNode | null>({
    id: 'all',
    name: t('全部'),
    categoryFlag: true,
    key: 'all',
  });
  const selectTreeNode = (
    selectedKeys: KEY[],
    info: {
      event: 'select';
      selected: boolean;
      node: EventDataNode;
      selectedNodes: DataNode[];
      nativeEvent: MouseEvent;
    },
  ) => {
    currentNodeSelect.value = info.node;
  };

  const treeSelectedKeys = ref<string[]>(['all']);

  const deleteNodeFn = (node: DataNode) => {
    Modal.confirm({
      title: t('是否删除分类信息'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: t('分类信息删除后无法恢复, 是否删除?'),
      okText: t('确定'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          await reqDatasetCategoryDelete(node.id);
          message.success(t('删除成功'));
          getTreeData();
          if (node.selected) {
            treeSelectedKeys.value = ['all'];
          }
        } catch (error: any) {
          message.error(error.message);
        }
      },
    });
  };

  const actionList: ActionListItem[] = [
    {
      title: t('新增子分类'),
      action: 'addChildren',
      ifShow: (node: ActionListItemCustomRenderParams) => {
        return node.nodeLevelInTree < 7 && hasPermission('120070001000001');
      },
    },
    {
      title: t('编辑分类'),
      action: 'editNode',
      ifShow: () => hasPermission('120070001000002'),
    },
    {
      title: t('删除分类'),
      action: 'deleteNode',
      ifShow: () => hasPermission('120070001000003'),
    },
  ];
  const treeModalOpen = ref<boolean>(false);
  const treeModalTitle = ref<string>(t('新增子分类'));
  const treeModalSubmit = async (values: Recordable) => {
    try {
      if (treeOperation.value === OperationType.Add) {
        await reqDatasetCategoryCreateCategory({
          name: values.name,
          ...(values.parentId === 'all' ? {} : { parentId: values.parentId }),
        });
      } else {
        await reqDatasetCategoryEditCategory({
          id: values.id,
          name: values.name,
        });
      }
      getTreeData();
      treeModalOpen.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const treeModalFormProps = reactive<FormProps>({
    initialValues: {
      parentId: 'all',
    },
    schemas: [
      {
        field: 'parentId',
        component: 'TreeSelect',
        label: t('上级分类'),
        required: true,
        componentProps: {
          disabled: true,
          request: async () => {
            return treeData.value;
          },
          fieldNames: {
            label: 'name',
            value: 'id',
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('分类名称'),
        required: true,
      },
    ],
  });
  const treeOperation = ref<OperationType>(OperationType.Add);
  const handleTreeAction = (actionItem: ActionListItem, node: DataNode) => {
    switch (actionItem.action) {
      case 'ADD':
        treeModalTitle.value = t('新增分类');
        treeModalFormProps.initialValues = {
          parentId: 'all',
        };
        treeModalOpen.value = true;
        treeOperation.value = OperationType.Add;
        break;
      case 'addChildren':
        treeModalTitle.value = t('新增分类');
        treeModalFormProps.initialValues = {
          parentId: node.id,
        };
        treeModalOpen.value = true;
        treeOperation.value = OperationType.Add;
        break;
      case 'editNode':
        treeModalTitle.value = t('编辑分类');
        treeModalFormProps.initialValues = {
          id: node.id,
          parentId: node.data?.parentId ? node.data.parentId : 'all',
          name: node.name,
        };
        treeModalOpen.value = true;
        treeOperation.value = OperationType.Edit;
        break;
      case 'deleteNode':
        deleteNodeFn(node);
        break;
      default:
        break;
    }
  };

  onMounted(() => {
    getTreeData();
  });

  const getDatasetPageList = async (params: any) => {
    if (!params.datasetCategoryId || params.datasetCategoryId === 'all') {
      return await reqDatasetQueryDatasetPage({
        ...params,
        datasetCategoryId: undefined,
      });
    }
    return await reqDatasetQueryDatasetPage(params);
  };

  const getDatasetVersionPageList = async (params: any) => {
    if (!params.datasetId) return Promise.resolve({ data: [] });
    if (params.datasetType === DatasetType.LOT_RELEASE_LINK) {
      return await reqDatasetQueryDatasetLotReleaseLinksPage(params);
    }
    return await reqDatasetQueryDatasetPointPage(params);
  };

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
  };

  return {
    columns: [templateColumn, listColumn],
    requests: [getDatasetPageList, getDatasetVersionPageList] as DataRequestFn[],
    treeData,
    upgradeVersionModalOpen,
    pageRef,
    formFirstProps,
    handleRowClick,
    actionList,
    handleTreeAction,
    treeSelectedKeys,
    treeModalOpen,
    treeModalTitle,
    treeModalFormProps,
    treeModalSubmit,
    selectTreeNode,
    currentNodeSelect,
  };
};
```

## API

### pageComponent Props

| 属性                      | 说明                             | 类型                                  | 默认值                   |
| ------------------------- | -------------------------------- | ------------------------------------- | ------------------------ |
| 所有 BMSearchTree属性     | 所有 BMSearchTree属性            | `SearchTreeProps`                     | -                        |
| requests                  | BMTable request 数组             | `DataRequestFn[]`                     | `[]`                     |
| columns                   | BMTable column 数组              | `ColumnType[]`                        | `[]`                     |
| defaultSelectedNode       | 默认选择的树节点                 | `any`                                 | `{}`                     |
| hideRightTree             | 是否隐藏左侧树                   | `boolean`                             | `false`                  |
| search                    | BMTable search 参数              | `Array<boolean> \| boolean`           | `[true, false]`          |
| formProps                 | BMTable formProp                 | `Array<Partial<FormProps>>`           | `[]`                     |
| titles                    | BMTable 标题                     | `Array<string>`                       | `[]`                     |
| showToolBars              | BMTable showToolBar              | `Array<boolean>`                      | `[true, true]`           |
| showHeader                | BMTable showHeader               | `Array<boolean>`                      | `[false, false]`         |
| rowSelections             | BMTable rowSelection             | `Array<any>`                          | `[]`                     |
| isMultipleSelects         | BMTable isMultipleSelect         | `Array<boolean>`                      | `[false, false]`         |
| isSelects                 | BMTable isSelect                 | `Array<boolean>`                      | `[true, true]`           |
| showIndexs                | BMTable showIndex                | `Array<boolean>`                      | `[false, false]`         |
| showSearchBorders         | BMTable showSearchBorder         | `Array<boolean>`                      | `[true, true]`           |
| bordereds                 | BMTable bordered                 | `Array<boolean>`                      | `[false, false]`         |
| rowClassNames             | BMTable rowClassName             | `Array<RowClassName<any>>`            | `[() => '', () => '']`   |
| paginations               | BMTable paginations              | `Array<Object>`                       | `[undefined, undefined]` |
| expandFixeds              | BMTable expandFixed              | `Array<'left' \| 'right' \| boolean>` | `['left', 'left']`       |
| expandedRowKeys           | BMTable expandedRowKey           | `Array<any>`                          | `[]`                     |
| rowExpandables            | BMTable rowExpandable            | `Array<any>`                          | `[]`                     |
| expandedRowsChanges       | BMTable expandedRowsChange       | `Array<any>`                          | `[]`                     |
| isExtraParamsChangeQuerys | BMTable isExtraParamsChangeQuery | `Array<boolean>`                      | `[true, true]`           |
| dataSources               | BMTable dataSource               | `Array<any>`                          | `[]`                     |
| pageSizeChangeToFirsts    | BMTable pageSizeChangeToFirst    | `Array<boolean>`                      | `[false, false]`         |
| virtualScrolls            | BMTable virtualScroll            | `Array<boolean>`                      | `[false, false]`         |
| dragSorts                 | BMTable dragSort                 | `Array<boolean>`                      | `[false, false]`         |
| showEmpty                 | 如果没有选择树的某个节点显示为空 | `boolean`                             | `false`                  |

### Events

| 事件名                 | 说明                     | 回调参数 |
| ---------------------- | ------------------------ | -------- |
| register               | 组件注册触发             | -        |
| 'update:expanded-keys' | expanded-keys改变        |          |
| action                 | 更多按钮中的item触发事件 | -        |
| expand                 | 同 antdV                 | -        |
