<!-- 任务规划 -->
<template>
  <div class="task-planning-manage">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="back">
            {{ t('称量任务') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ t('任务规划') }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
        <Button type="primary" @click="save">{{ t('保存') }}</Button>
      </template>
      <BMPageComponent
        ref="pageRef"
        :rowKeys="['id']"
        :search="[true]"
        :hideRightTree="true"
        :showToolBars="[false]"
        :showHeader="[false]"
        :showSearchBorders="[false]"
        :formProps="[formFirstProps as Partial<FormProps>]"
        :isSelects="[false, false]"
        :rowSelections="rowSelections"
        :requests="[getWeighCentreRequirementQueryPage as DataRequestFn]"
        :columns="[columnsFirst]"></BMPageComponent>
    </BreadcrumbButton>
  </div>
</template>

<script lang="ts" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import {
    reqWeighCentreRequirementQueryPage,
    reqProductMaterialProductTreeReq,
    reqWeighCentreTaskProgramManual,
  } from '@/services';
  import { DataRequestFn, BMPageComponent, FormProps } from '@bmos/components';
  import { message, Modal } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  const emit = defineEmits(['back']);
  const selectedRowKeys1 = ref<any>([]); //多选时的表格ids
  const operationSelectedRows = ref<any>([]); //存多选的数据
  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      selectedRowKeys: selectedRowKeys1.value,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);
  const pageRef = ref<any>(null);
  const updateTable = () => {
    pageRef.value?.fetchData(0);
  };
  const columnsFirst: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 170,
      resizable: true,
    },
    {
      title: t('物料编码'),
      dataIndex: 'materialMergeCode',
      width: 170,
      resizable: true,
    },
    {
      title: t('物料规格'),
      dataIndex: 'materialSpecification',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('称量中心'),
      dataIndex: 'weighCentreName',
      resizable: true,
      width: 220,
    },
    {
      title: t('需求日期'),
      align: 'left',
      dataIndex: 'requirementDate',
      width: 150,
      resizable: true,
      formItemProps: {
        order: 3,
        colProps: { span: 6 },
        component: 'RangePicker',
        componentProps: () => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'data',
            valueFormat: 'YYYY-MM-DD',
          };
        },
      },
    },
    {
      title: t('需求量'),
      dataIndex: 'requirementQuantity',
      width: 110,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 110,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productId',
      width: 170,
      resizable: true,
      formItemProps: {
        component: 'TreeSelect',
        componentProps: () => {
          return {
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'showName',
            request: async () => {
              return await getProductTree();
            },
          };
        },
      },
      customRender: ({ record }: any) => record.productName,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产工艺'),
      dataIndex: 'processName',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
    },
  ];
  const formFirstProps = reactive<Partial<FormProps>>({
    actionColOptions: {},
    baseColProps: {
      // span: 6,
    },
    fieldMapToTime: [['requirementDate', ['requirementDateStart', 'requirementDateEnd'], 'YYYY-MM-DD']],
  });
  // 任务规划页面列表数据
  const getWeighCentreRequirementQueryPage = async (params: any) => {
    return await reqWeighCentreRequirementQueryPage(params);
  };
  // 获取产品树
  const getProductTree = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      const loop = (data: any[]) => {
        return data.map(item => {
          if (item.categoryFlag) {
            item.selectable = false;
          } else {
            item.selectable = true;
          }
          if (item.children) {
            loop(item.children);
          }
          return item;
        });
      };
      return loop(data);
    } catch (error) {
      //
    }
  };
  // 返回管理页面
  const back = () => {
    emit('back');
  };
  // 保存
  const save = () => {
    if (operationSelectedRows.value.length === 0) return message.error(t('请勾选物料需求进行规划'));
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否对所选称量需求进行任务规划'),
      async onOk() {
        try {
          const ids = operationSelectedRows.value.map((item: any) => item.id);
          await reqWeighCentreTaskProgramManual(ids);
          message.success(t('操作成功'));
          emit('back');
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };
</script>

<style lang="less" scoped>
  .task-planning-manage {
    width: 100%;
    height: 100%;
  }
  :deep(.content) {
    padding: 0;
  }
</style>
