<!-- 物料追溯配置 -->
<template>
  <keep-alive>
    <BMPageComponent
      v-if="pageHome"
      ref="tableInstance"
      :showAllAddIcon="false"
      :showAction="false"
      :rowKeys="['id']"
      :treeData="treeData"
      :formProps="[
        {
          actionColOptions: {
            span: 12,
          },
          showAdvancedButton: false,
        },
      ]"
      :fieldNames="{
        title: 'showName',
        key: 'id',
      }"
      :treeField="{
        field: {
          categoryFlag: 'categoryFlag',
          productId: 'id',
        },
      }"
      :requests="[loadData as any]"
      :columns="[columns]">
      <template #tableHeaderToolbar0="{ treeNode }">
        <Button v-hasAuth="120020016000001" type="primary" @click="add(treeNode)">
          {{ t('新增模板') }}
        </Button>
      </template>
      <template #tableHeaderTitle0>
        <BMTableTitle :title="t('物料追溯模板')"></BMTableTitle>
      </template>
    </BMPageComponent>
  </keep-alive>
  <!-- 按钮跳转 -->
  <JumpPage
    v-if="!pageHome"
    :treeNodeId="treeNodeId"
    :productTree="productTree"
    :rowData="rowData"
    :type="type"
    @back="back"></JumpPage>
</template>

<script lang="tsx" setup>
  import { ref, onMounted } from 'vue';
  import { t } from '@bmos/i18n';
  import { reqProductMaterialProductTreeReq } from '@/services';
  import { DataNode } from 'ant-design-vue/es/tree';
  import type { TableColumn } from '@bmos/components';
  import { BMPageComponent, TableInstance, BMTableTitle } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';
  import JumpPage from './components/JumpPage.vue'; //跳转新增编辑详情页面
  import { message, Switch, Modal } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import {
    reqMaterialTraceTemplateQueryPage,
    reqMaterialTraceTemplateEnable,
    reqMaterialTraceTemplateDisable,
    reqMaterialTraceTemplateDelete,
  } from '@/services';

  const { hasPermission } = usePermissionStore();
  const pageHome = ref<boolean>(true);
  const treeData = ref<DataNode[]>([]);
  const rowData = ref<any>();
  const type = ref<any>();
  const treeNodeId = ref<any>();
  const tableInstance = ref<TableInstance>();
  const productTree = ref<any>();
  const columns: TableColumn[] = [
    {
      title: t('模板名称'),
      align: 'left',
      dataIndex: 'templateName',
      width: 180,
      resizable: true,
    },
    {
      title: t('产品名称'),
      align: 'left',
      dataIndex: 'productName',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      align: 'left',
      dataIndex: 'mergeCode',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      resizable: true,
      width: 180,
    },
    {
      title: t('启停'),
      dataIndex: 'enabled',
      width: 76,
      fixed: 'right',
      resizable: true,
      hideInSearch: true,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        const enabled = record?.enabled === true ? true : false;
        return (
          <Switch
            v-hasAuth='120020016000005'
            checked={enabled}
            onChange={checked => {
              {
                changeState(record, checked as boolean, tableAction);
              }
            }}
          />
        );
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 180,
      actions: ({ record }: any, tableAction: any) => [
        {
          label: t('查看'),
          ifShow: hasPermission('120020016000003'),
          onClick: () => {
            rowData.value = record;
            pageHome.value = false;
            type.value = 'look';
          },
        },
        {
          label: t('编辑'),
          ifShow: !record.enabled && hasPermission('120020016000002'),
          onClick: () => {
            rowData.value = record;
            pageHome.value = false;
            type.value = 'edit';
          },
        },
        {
          label: t('复制'),
          ifShow: hasPermission('120020016000006'),
          onClick: () => {
            rowData.value = record;
            pageHome.value = false;
            type.value = 'copy';
          },
        },
        {
          label: t('删除'),
          ifShow: !record.enabled && hasPermission('120020016000004'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('删除确认'),
              icon: h(ExclamationCircleOutlined),
              closable: true,
              content: t('是否删除该模板'),
              onOk: async () => {
                try {
                  await reqMaterialTraceTemplateDelete({ id: record.id });
                  message.success(t('删除成功'));
                  tableAction.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                }
              },
            });
          },
        },
      ],
    },
  ];

  const loadData = async (params: any) => {
    const { productId, categoryFlag, ...newParams }: any = params;
    if (categoryFlag) {
      newParams.categoryId = productId;
    } else {
      newParams.productId = productId;
    }
    if (productId === 'all') {
      delete newParams.productId;
      delete newParams.categoryId;
    }
    return await reqMaterialTraceTemplateQueryPage(newParams as any);
  };
  // 新增模板
  const add = (treeNode: any) => {
    pageHome.value = false;
    type.value = 'add';
    treeNodeId.value = !treeNode.categoryFlag && treeNode?.id ? treeNode?.id : undefined;
  };

  // 操作栏启停
  const changeState = async (record: any, checked: boolean, tableAction: any) => {
    const title = checked ? t('是否启用此模板') : t('是否停用此模板');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          if (checked) {
            await reqMaterialTraceTemplateEnable({ id: record.id });
            message.success(t('启用成功'));
            tableAction.fetchData();
          } else {
            await reqMaterialTraceTemplateDisable({ id: record.id });
            message.success(t('停用成功'));
            tableAction.fetchData();
          }
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
      onCancel() {},
    });
  };
  const back = () => {
    pageHome.value = true;
  };
  //获取所有节点
  const getTreeData = async () => {
    try {
      const res: any = await reqProductMaterialProductTreeReq();
      treeData.value = [
        {
          id: 'all',
          showName: t('全部'),
          key: 'all',
          disabled: false,
          children: res.data,
          categoryFlag: true,
        },
      ];
      productTree.value = res.data;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  onMounted(() => {
    getTreeData();
  });
</script>
<style lang="less" scoped></style>
