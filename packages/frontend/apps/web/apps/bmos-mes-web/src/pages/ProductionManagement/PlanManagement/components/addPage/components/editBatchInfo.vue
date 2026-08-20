<!-- 编辑指令单批次信息页面 -->
<template>
  <div class="edit-batch-Info">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item>
            {{ t('生产计划管理') }}
          </breadcrumb-item>
          <breadcrumb-item @click="back">{{ t('新建生产计划') }}</breadcrumb-item>
          <breadcrumb-item>{{ t('编辑生产计划') }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
        <Button type="primary" @click="save">{{ t('保存') }}</Button>
      </template>
      <BMTableTitle :title="t('生产信息')" />
      <BMForm ref="formRef" v-bind="formProps"></BMForm>
      <BMTableTitle :title="t('关联批次')" />
      <div class="table">
        <BMTable
          :dataSource="tableData"
          :columns="columns"
          :search="false"
          :scroll="{ x: 1044, y: 400 }"
          :showRefresh="false"
          :showSearchBorder="true"
          :showToolBar="false"
          :pagination="false"></BMTable>
      </div>
    </BreadcrumbButton>
    <BatchModal
      ref="batchModalRef"
      :rowData="rowData"
      :relatedPlanBatch="relatedPlanBatch1"
      @modalOk="modalOk"></BatchModal>
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMForm, formInstance, BMTable, TableColumn, BMTableTitle } from '@bmos/components';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { reqFactoryLineListByProcessVersion, reqProcessRelationProcesses } from '@/services';
  import { message } from 'ant-design-vue';
  import BatchModal from './batchModal.vue';

  const formRef = ref<formInstance>();
  const emits = defineEmits(['backAdd', 'updateBatchInfo']);
  const props = withDefaults(
    defineProps<{
      batchInfoRowData: any; ////指令单批次信息的行数据
      relatedPlanBatch: any; //关联的计划批次
    }>(),
    {},
  );
  const relatedPlanBatch1 = ref<any>();
  const productionLineList = ref<any>(); //工艺绑定的对应产线下拉框
  const batchModalRef = ref();
  const rowData = ref();
  const tableData = ref<any>([
    {
      relatedProcesses: '关联的工艺名称',
      relatedBatch: '123132',
    },
  ]);
  // 关联批次表格列
  const columns: TableColumn[] = [
    {
      title: t('关联工艺'),
      dataIndex: 'name',
    },
    {
      title: t('关联批次'),
      dataIndex: 'relatedBatch',
      customRender: ({ record }: any) => (
        <div class='relatedBatch' onClick={() => openBatchModal(record)}>
          {record?.batchNos?.join('，')}
        </div>
      ),
    },
  ];
  const formProps = reactive<any>({
    initialValues: {},
    baseColProps: {
      span: 8,
    },
    showActionButtonGroup: false,
    schemas: [
      {
        label: t('产线'),
        field: 'productionLineId',
        component: 'Select',
        required: true,
        componentProps: () => {
          return {
            options: productionLineList.value,
            fieldNames: {
              label: 'name',
              value: 'id',
            },
          };
        },
      },
      {
        label: t('指令单编号'),
        field: 'planNo',
        component: 'Input',
        required: true,
      },
      {
        label: t('生产批号'),
        field: 'batchNo',
        component: 'Input',
        required: true,
      },
      {
        field: 'batchQuantity',
        component: 'Input',
        label: t('生产批量'),
        required: true,
        componentSlots: ({ formModel }: any) => {
          return {
            addonAfter: () => <div style='min-width:40px'>{formModel.unitName}</div>,
          };
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              trigger: 'blur',
              validator: (_rule: any, value: any) => {
                if (!value) {
                  return Promise.reject(t('请输入生产批量'));
                }
                // 判断是否为正数
                if (Number(value) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                const reg = /^\d{1,10}(\.\d{1,9})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                if (!Number(value)) {
                  return Promise.reject(t('请输入生产批量'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
    ],
  });
  // 点击关联批次div
  const openBatchModal = (row: any) => {
    rowData.value = row;
    batchModalRef.value.openModal();
  };
  // 弹框确定
  const modalOk = (val1: any, val2: any, rowId: any, checkedNodes1: any, sorts: any, batchNoList: any) => {
    tableData.value.forEach((item: any) => {
      if (item.id === rowId) {
        item.planIds = val1;
        item.batchNos = val2;
        item.checkedNodes1 = checkedNodes1;
        item.sorts = sorts;
        item.planIdss = val1;
        item.batchNoss = val2;
        item.batchNoList = batchNoList;
      }
    });
  };
  // 保存
  const save = async () => {
    const res = await formRef.value?.validate();
    // 通过产线id查对应产线名称
    const temp = productionLineList.value?.find((item: any) => res.productionLineId == item.id);

    const temp2 = tableData.value.map((item: any) => {
      return {
        processId: item.processId,
        id: item.processId,
        name: item.name, //工艺名称
        sorts: item.sorts || [],
        //所有batchNos
        planIdss: item.planIdss || [],
        //所有batchNos
        batchNoss: item.batchNoss || [],
        planIds: item.planIds?.filter((item2: any) => !item.sorts?.includes(item2)), //非计划批次集合
        checkedNodes1: item.checkedNodes1, //历史批次
        batchNoList: item.batchNoList,
      };
    });
    const temp3 = temp2
      .filter(item => item.batchNoss?.length > 0)
      ?.map(item1 => {
        return {
          showInfo: item1.name + '-' + item1.batchNoss?.join('、'),
        };
      });
    const relatedBatchInfo = temp3?.map(item => item?.showInfo)?.join(' ; ');
    const data = {
      ...res,
      productionLineName: temp?.name,
      productionLineCode: temp?.code,
      relationTable: temp2,
      relatedBatchInfo,
      updateContinueBatchNoSort: props.batchInfoRowData?.sort, //用于更新需要沿用的批号
      oldBatchNo: props.batchInfoRowData?.batchNo,
    };
    emits('updateBatchInfo', data); //更新指令单批次信息表格
    message.success(t('保存成功'));
  };
  // 返回到新增页
  const back = () => {
    emits('backAdd');
  };
  // 回显表单数据（用新增页暂存的数据回显）
  const echoData = async () => {
    formRef.value?.setFormModels({
      productionLineId: props.batchInfoRowData?.productionLineId,
      planNo: props.batchInfoRowData?.planNo,
      batchNo: props.batchInfoRowData?.batchNo,
      batchQuantity: props.batchInfoRowData?.batchQuantity,
      unitName: props.batchInfoRowData?.unitName,
    });
  };
  // 获取该工艺绑定的产线下拉框
  const getProductionLineList = async () => {
    try {
      const { data } = await reqFactoryLineListByProcessVersion({
        //查工艺对应的产线list
        id: props.batchInfoRowData?.processId,
        version: props.batchInfoRowData?.processVersion,
      }); //工艺版本 参数名待改

      productionLineList.value = data;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  //第一次进来时回显下方关联批次表格
  const echoTableData = async () => {
    try {
      const { data } = await reqProcessRelationProcesses({ processId: props.batchInfoRowData?.processId });
      tableData.value = data.map((item: any) => {
        return {
          ...item,
          processId: item.id,
          // planIds: [],
          // batchNos: [],
          checkedNodes1: [], //历史批次勾选的数组对象
          batchNos:
            props.batchInfoRowData?.relationBatchSortListAO
              .filter((item2: any) => item.id == item2.processId)
              ?.map((item3: any) => item3?.batchNo || '') || [],
          batchNoss:
            props.batchInfoRowData?.relationBatchSortListAO
              .filter((item2: any) => item.id == item2.processId)
              ?.map((item3: any) => item3?.batchNo || '') || [],
          planIds:
            props.batchInfoRowData?.relationBatchSortListAO
              .filter((item2: any) => item.id == item2.processId)
              ?.map((item3: any) => item3.value) || [],
          planIdss:
            props.batchInfoRowData?.relationBatchSortListAO
              .filter((item2: any) => item.id == item2.processId)
              ?.map((item3: any) => item3.value) || [],
          sorts:
            props.batchInfoRowData?.relationBatchSortListAO
              .filter((item2: any) => item.id == item2.processId)
              ?.map((item3: any) => item3.value) || [],
        };
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 编辑过之后且有数据则用前端暂存回显
  const echoTableData2 = async () => {
    let temp =
      props.batchInfoRowData?.relationBatchSortListAO
        ?.map((item: any) => item.batchNo)
        .filter((item2: any) => props.batchInfoRowData.relatedBatchInfo?.includes(item2)) || []; //计划批次的批号
    temp = [...new Set(temp)];
    tableData.value = props.batchInfoRowData?.relationTable?.map((item: any) => {
      const uniqueElements = item.batchNoss?.filter((item2: any) => !item.batchNoList?.includes(item2)) || []; //不相同的(说明该项含有计划批次的批号)
      const uniqueElements2 = item.batchNoss?.filter((item2: any) => item.batchNoList?.includes(item2)) || []; //非计划批次的批号
      return {
        ...item,
        // batchNos: item.batchNoss,
        batchNos: uniqueElements.length > 0 ? temp.concat(uniqueElements2) : item.batchNoss,
        planIds: item.planIdss,
      };
    });
  };

  onMounted(async () => {
    await getProductionLineList();
    echoData();
    relatedPlanBatch1.value = props.relatedPlanBatch; //用于展示能选的计划批次
    if (props.batchInfoRowData.relationTable && props.batchInfoRowData?.relationTable?.length > 0) {
      echoTableData2();
    } else {
      echoTableData();
    }
  });
</script>
<style lang="less" scoped>
  .edit-batch-Info {
    width: 100%;
    height: 100%;
    .container {
      padding: 0px;
    }
  }
  :deep(.mes-table-cell) {
    overflow: visible;
  }
  :deep(.mes-input-number) {
    width: 100%;
  }
  :deep(.mes-input-number-group-wrapper) {
    width: 100%;
  }
  .table {
    height: calc(100% - 250px);
  }
  .bmos-table-title {
    margin-bottom: 10px;
  }
  :deep(.relatedBatch) {
    width: 100%;
    height: 36px;
    padding: 6px;
    border: 1px solid #d4d7d9;
    border-radius: 4px;
    overflow: hidden;
    /* 超出部分用省略号表示 */
    text-overflow: ellipsis;
  }
  :deep(.relatedBatch):hover {
    border: 1px solid rgb(40, 113, 255);
  }
</style>
