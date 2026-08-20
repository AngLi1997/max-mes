<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('添加物料')"
    :formProps="formProps"
    :okText="t('确定')"
    wrapClassName="modalSizeLarge"
    @cancel="cancel"
    @okModal="ok">
    <template #selectA>
      <div class="setting">
        <FormItemRest>
          <BMDescriptions :column="2" :list="descData" />
          <div class="table">
            <BMTable
              ref="tableRef"
              row-key="id"
              :dataSource="dataSource"
              :columns="columns"
              :pagination="false"
              :search="false"
              :showToolBar="false"
              :scroll="{ x: 800, y: 300 }"
              :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"></BMTable>
          </div>
        </FormItemRest>
      </div>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import {
    BMModalForm,
    ModalFormInstance,
    BMTable,
    Recordable,
    TableColumn,
    BMDescriptions,
    DescriptionsItemProps,
  } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { reactive, ref, nextTick } from 'vue';
  import type { Key } from 'ant-design-vue/lib/_util/type';
  import { FormItemRest, message } from 'ant-design-vue';
  import { reqWeighCentreTaskQueryUnPlanedRequirementListByTaskId } from '@/services';

  const props = defineProps({
    rowData: {
      type: Object,
      default: {},
    },
    selects: {
      type: Array,
      default: [],
    },
  });
  const modalFormRef = ref<ModalFormInstance>();
  const tableRef = ref();
  const emit = defineEmits(['updateTable']);
  const open = ref<boolean>(false);
  const state = reactive<{
    selectedRowKeys: any[]; //勾选的id集合
    selectedAll: any[];
  }>({
    selectedRowKeys: [],
    selectedAll: [],
  });
  const descData = ref<DescriptionsItemProps[]>([
    {
      label: t('物料名称'),
      value: props.rowData?.materialName,
    },
    {
      label: t('物料编码'),
      value: props.rowData?.materialMergeCode,
    },
    {
      label: t('物料规格'),
      value: props.rowData?.materialSpecification,
    },
    {
      label: t('称量中心'),
      value: props.rowData?.weighCentreName,
    },
  ]);

  // 查看的表单
  const formProps = reactive({
    initialValues: {},
    schemas: [
      {
        field: 'field6',
        component: 'Divider',
        label: t('物料信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'productList',
        label: '',
        noLabel: true,
        colProps: {
          span: 24,
        },
        slot: 'selectA',
      },
    ],
  });
  const columns: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('物料编码'),
      dataIndex: 'materialMergeCode',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('物料规格'),
      dataIndex: 'materialSpecification',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('称量中心'),
      dataIndex: 'weighCentreName',
      resizable: true,
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('需求日期'),
      align: 'left',
      dataIndex: 'requirementDate',
      width: 110,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('需求量'),
      dataIndex: 'requirementQuantity',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 70,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 120,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 110,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产工艺'),
      dataIndex: 'processName',
      width: 120,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 130,
      resizable: true,
      hideInSearch: true,
      vIf: false,
    },
  ];
  // 弹框表格数据来源
  const dataSource = ref<any>([]);
  const openModal = () => {
    open.value = true;
  };
  const cancel = () => {
    open.value = false;
    state.selectedRowKeys = [];
    state.selectedAll = [];
  };
  let flag = ref<boolean>(true); //通过回显的打勾
  // 弹框确定按钮
  const ok = () => {
    if (flag.value) {
      state.selectedAll = dataSource.value.filter((item: any) => props.selects.includes(item.id));
    }
    if (state.selectedAll.length === 0) return message.error(t('请勾选物料需求进行规划'));
    emit('updateTable', state.selectedAll);
    message.success(t('操作成功'));
    flag.value = true;
    cancel();
  };
  const onSelectChange = (selectedRowKeys: Key[], selectedRows: any) => {
    state.selectedRowKeys = selectedRowKeys;
    state.selectedAll = selectedRows;
    flag.value = false;
  };
  // 查弹框数据(未规划的数据)
  const getUnplannedData = async () => {
    const { data } = await reqWeighCentreTaskQueryUnPlanedRequirementListByTaskId({ taskId: props.rowData?.id });
    dataSource.value = data;
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        getUnplannedData();
        state.selectedRowKeys = props.selects;
      }
    },
    { immediate: true, deep: true },
  );

  defineExpose({ openModal, formProps });
</script>
<style lang="less" scoped>
  .setting {
    height: 100%;
    background-color: var(--bmos-primary-color-white);
    display: flex;
    flex-direction: column;
    .batch-table {
      flex: 1;
      overflow-y: hidden;
    }
  }
</style>
