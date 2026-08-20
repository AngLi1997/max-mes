<!-- 批次追溯 -->
<template>
  <KeepAlive>
    <BMPageComponent
      v-if="pageHome"
      ref="tableInstance"
      :showAllAddIcon="false"
      :showAction="false"
      :rowKeys="['id']"
      :treeData="treeData"
      :formProps="[
        {
          showAdvancedButton: false,
          fieldMapToTime: [['time', ['startTime', 'endTime'], 'YYYY-MM-DD']],
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
      :columns="[columns]"></BMPageComponent>
  </KeepAlive>
  <!-- 按钮跳转 -->
  <JumpPage v-if="!pageHome" :rowData="rowData" @back="back"></JumpPage>
</template>

<script lang="tsx" setup>
  import { ref, onMounted } from 'vue';
  import { t } from '@bmos/i18n';
  import { reqProductMaterialProductTreeReq, getPlanRetracePage, getPlanRetraceInfo } from '@/services';
  import { DataNode } from 'ant-design-vue/es/tree';
  import type { TableColumn } from '@bmos/components';
  import { BMPageComponent, TableInstance } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';
  import JumpPage from './components/JumpPage.vue'; //跳转批次追溯详情页面
  import { message } from 'ant-design-vue';

  const { hasPermission } = usePermissionStore();
  const pageHome = ref<boolean>(true);
  const treeData = ref<DataNode[]>([]);
  const rowData = ref<any>();
  const tableInstance = ref<TableInstance>();
  const columns: TableColumn[] = [
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
      dataIndex: 'productMergeCode',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品规格'),
      align: 'left',
      dataIndex: 'productSpecification',
      width: 150,
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
      title: t('生产批号'),
      align: 'left',
      dataIndex: 'batchNo',
      width: 180,
      resizable: true,
    },
    {
      title: t('开始时间'),
      dataIndex: 'time',
      hideInTable: true,
      formItemProps: {
        component: 'RangePicker',
      },
    },
    {
      title: t('生产开始时间'),
      align: 'left',
      dataIndex: 'startTime',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产结束时间'),
      align: 'left',
      dataIndex: 'endTime',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 180,
      actions: ({ record }: any) => [
        {
          label: t('批次追溯'),
          ifShow: hasPermission('120050013000001'),
          onClick: async () => {
            const { data } = await getPlanRetraceInfo({ planId: record.id });
            rowData.value = data;
            pageHome.value = false;
          },
        },
      ],
    },
  ];

  const loadData = async (params: any) => {
    const { productId, categoryFlag, ...newParams }: any = params;
    if (categoryFlag) {
      newParams.productCategoryId = productId;
    } else {
      newParams.productId = productId;
    }
    if (productId === 'all') {
      delete newParams.productId;
      delete newParams.productCategoryId;
    }
    return await getPlanRetracePage(newParams as any);
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
          children: res.data,
        },
      ];
    } catch (error: any) {
      message.error(error.message);
    }
  };

  onMounted(() => {
    getTreeData();
  });
</script>
<style lang="less" scoped></style>
