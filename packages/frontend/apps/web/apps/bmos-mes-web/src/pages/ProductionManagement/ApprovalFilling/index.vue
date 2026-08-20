<!-- 生产管理-批次审核 -->
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
        },
      ]"
      :defaultSelectedNode="treeData[0]"
      :expanded-keys="firstTreeKey"
      :fieldNames="{
        title: 'showName',
        key: 'id',
      }"
      :treeField="{
        field: {
          // productId: 'id',
          categoryFlag: 'categoryFlag',
          productId: 'id',
        },
      }"
      :requests="[loadData as any]"
      :columns="[columns]">
      <template #tableHeaderTitle0>
        <BMTableTitle :title="t('批次审核')"></BMTableTitle>
      </template>
    </BMPageComponent>
  </KeepAlive>
  <!-- 审核结论弹框 -->
  <conclusionModal
    ref="conclusionModalRef"
    :rowData="rowData"
    :type="type"
    @updateTable="updateTable"></conclusionModal>
  <!-- 工序审核按钮跳转 -->
  <processApprovalPage v-if="!pageHome" ref="processApprovalRef" :rowData="rowData" @back="back"></processApprovalPage>
</template>

<script lang="tsx" setup>
  import { ref, onMounted } from 'vue';
  import { t } from '@bmos/i18n';
  import { reqProductMaterialProductTreeReq, getBatchApprovalPage, getProcessNameList } from '@/services';
  import { DataNode } from 'ant-design-vue/es/tree';
  import type { TableColumn } from '@bmos/components';
  import { BMPageComponent, BMTableTitle, TableInstance } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { usePermissionStore } from '@/stores/permission';
  import conclusionModal from './components/conclusionModal.vue';
  import processApprovalPage from './components/processApproval.vue'; //跳转工序审核
  import StateTag from '@/components/StateTag/index.vue';

  const { hasPermission } = usePermissionStore();
  const pageHome = ref<boolean>(true);
  const firstTreeKey = ref<any>([]);
  const treeData = ref<DataNode[]>([]);
  const conclusionModalRef = ref<any>();
  const processApprovalRef = ref<any>();
  const rowData = ref<any>();
  const tableInstance = ref<TableInstance>();
  const type = ref<string>('process'); //type为工艺
  const processList = ref<any>([]); //工艺名称下拉

  const columns: TableColumn[] = [
    {
      title: t('产品名称'),
      align: 'left',
      dataIndex: 'productName',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      align: 'left',
      dataIndex: 'productCode',
      width: 190,
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
      width: 200,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: processList.value,
        }),
      },
    },
    {
      title: t('生产批号'),
      align: 'left',
      dataIndex: 'planBatchNo',
      width: 190,
      resizable: true,
      sorter: true,
    },
    {
      title: t('生产开始时间'),
      align: 'left',
      dataIndex: 'startTime',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产结束时间'),
      align: 'left',
      dataIndex: 'endTime',
      width: 190,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('审核备注'),
      align: 'left',
      dataIndex: 'remark',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('审核结论'),
      align: 'left',
      dataIndex: 'confirmOpinion',
      fixed: 'right',
      width: 110,
      resizable: true,
      formItemProps: {
        component: 'Select',
        order: 1,
        componentProps: () => ({
          options: [
            {
              label: t('合格'),
              value: 'ELIGIBLE',
            },
            {
              label: t('不合格'),
              value: 'NOT_ELIGIBLE',
            },
            {
              label: t('其他'),
              value: 'RESTS',
            },
          ],
        }),
      },
      customRender: ({ record }: any) => {
        return record.confirmOpinion ? (
          <StateTag
            type={
              record.confirmOpinion?.value === 'ELIGIBLE'
                ? 'success'
                : record.confirmOpinion?.value === 'NOT_ELIGIBLE'
                ? 'danger'
                : record.confirmOpinion?.value === 'RESTS'
                ? 'default'
                : ''
            }>
            {record.confirmOpinion?.label || '-'}
          </StateTag>
        ) : (
          '-'
        );
      },
    },

    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 240,
      actions: ({ record }: any) => [
        {
          label: t('审核结论'),
          ifShow: hasPermission('120030009000001'),
          onClick: () => {
            rowData.value = record;
            conclusionModalRef.value.openModal();
          },
        },
        {
          label: t('工序审核'),
          ifShow: hasPermission('120030009000002'),
          onClick: () => {
            rowData.value = record;
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
    return await getBatchApprovalPage(newParams as any);
    // return new Promise(resolve => {
    //   setTimeout(() => {
    //     resolve({
    //       data: [
    //         {
    //           id: '1',
    //           productName: 'productPlan001',
    //           productCode: '人血白蛋白',
    //           productSpecification: '1', //产品id
    //           processName: '工艺1',
    //           planBatchNo: '10g/支',
    //           startTime: '人血白蛋白投浆工艺',
    //           endTime: 'YX001231001',
    //           confirmOpinion: '',
    //           remark: '备注111111111',
    //         },
    //       ],
    //       total: 3,
    //     });
    //   }, 500);
    // });
  };
  // 刷新表格
  const updateTable = () => {
    tableInstance.value?.fetchData(0);
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
      console.log(error);
    }
  };
  // 获取工艺名称下拉数据
  const getProcessList = async () => {
    try {
      const res = await getProcessNameList();
      processList.value = res.data.map((item: any) => {
        return {
          ...item,
          value: item.label,
        };
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };

  onMounted(() => {
    getTreeData();
    getProcessList();
  });
</script>
<style lang="less" scoped></style>
