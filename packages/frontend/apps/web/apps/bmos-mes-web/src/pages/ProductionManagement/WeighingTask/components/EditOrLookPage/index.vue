<!-- 编辑查看页 -->
<template>
  <div class="task-planning-manage">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="back">
            {{ t('称量任务') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ type === 'edit' ? t('编辑任务') : t('查看任务') }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
        <Button v-if="type === 'edit'" type="primary" @click="save">{{ t('保存') }}</Button>
      </template>
      <div class="setting">
        <!-- 上方表单 -->
        <BMForm ref="myFormRef" v-bind="formProps"></BMForm>
        <div class="batch-table">
          <BMTable
            ref="tableInstance"
            :dataSource="dataSource"
            :columns="columns"
            row-key="id"
            :headerTitle="t('物料称量需求')"
            :scroll="{ x: 844, y: 400 }"
            :showRefresh="false"
            :pagination="{
              pageSize: 20,
            }"
            :formProps="formPropsTable">
            <template #toolbar>
              <Button v-if="type === 'edit'" type="primary" @click="addMaterials">
                {{ t('添加物料') }}
              </Button>
            </template>
          </BMTable>
        </div>
      </div>
    </BreadcrumbButton>
  </div>
  <!-- 添加物料弹框 -->
  <AddModal ref="addModalRef" :rowData="rowData" @updateTable="updateTable" :selects="selects"></AddModal>
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import { createVNode } from 'vue';
  import { reqWeighCentreTaskQueryRequirementListByTaskId, reqWeighCentreTaskEdit } from '@/services';
  import { BMTable, TableColumn, BMForm, FormProps } from '@bmos/components';
  import { message, Modal, Space, Button } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import AddModal from './components/addModal.vue';
  import dayjs from 'dayjs';
  const props = withDefaults(
    defineProps<{
      rowData?: Object;
      type?: String;
    }>(),
    {
      rowData: {},
      type: '',
    },
  );
  const emit = defineEmits(['back']);
  const myFormRef = ref();
  const addModalRef = ref<any>();
  const tableInstance = ref<any>();
  const selects = ref<any>([]); //回显弹框勾选
  // 表格展示数据
  const dataSource = ref<any>([]);
  // 初始总数据
  const initDataSource = ref<any>([]);
  const initIds = ref<any>([]); //存初始接口所返的id
  // 表单属性
  const formProps = reactive<any>({
    initialValues: {},
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 80,
    baseColProps: {
      span: 6,
    },
    autoAdvancedLine: 10,
    alwaysShowLines: 6,
    actionColOptions: {
      span: 2,
    },
    showAdvancedButton: true,
    showSubmitButton: false, //是否展示查询按钮
    showResetButton: false, //是否展示重置按钮
    schemas: [
      {
        field: 'taskNo',
        component: 'Input',
        label: t('任务编号'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'executeDate',
        component: 'DatePicker',
        label: t('执行时间'),
        required: true,
        componentProps: {
          placeholder: t('请选择日期'),
          format: 'YYYY-MM-DD',
          // picker: 'data',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
  });
  // 表格属性
  const formPropsTable = reactive<Partial<FormProps>>({
    actionColOptions: {
      span: 4,
    },
    baseColProps: {
      span: 6,
    },
    showAdvancedButtonBadge: false,
    showAdvancedButton: false,
    showActionButtonGroup: false,
  });
  const columns: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('物料编码'),
      dataIndex: 'materialMergeCode',
      width: 170,
      resizable: true,
      hideInSearch: true,
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
      hideInSearch: true,
    },
    {
      title: t('需求日期'),
      align: 'left',
      dataIndex: 'requirementDate',
      width: 150,
      resizable: true,
      hideInSearch: true,
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
      dataIndex: 'productName',
      width: 170,
      resizable: true,
      hideInSearch: true,
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
      hideInSearch: true,
    },
    {
      title: t('操作'),
      fixed: 'right',
      hideInSearch: true,
      width: 100,
      resizable: true,
      key: 'ACTION',
      hideInTable: props.type !== 'edit',
      actions: ({ record }) => [
        {
          label: t('删除'),
          ifShow: props.type === 'edit',
          onClick: () => {
            Delete(record);
          },
        },
      ],
    },
  ];
  // 添加物料按钮
  const addMaterials = () => {
    addModalRef.value?.openModal();
  };
  // 删除
  const Delete = (row: any) => {
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否确认删除，确认后，列表中该物料需求清除'),
      onOk() {
        dataSource.value = dataSource.value.filter((item: any) => item.id !== row.id);
        initDataSource.value = dataSource.value;
        selects.value = selects.value.filter((item: any) => item != row.id);
      },
      onCancel() {},
    });
  };
  // 弹框里确定所传数据
  const updateTable = (selectedAll: any) => {
    dataSource.value = [...new Set([...initDataSource.value, ...selectedAll])]; //待改
    selects.value = dataSource.value?.map((item: any) => item.id);
  };
  // 返回管理页面
  const back = () => {
    if (props.type === 'look') {
      emit('back');
      return;
    }
    // 编辑时点返回需监听页面内容是否改变
    if (props.type === 'edit') {
      const { executeDate } = myFormRef.value?.getFormValues();
      const dataSourceIds = dataSource.value?.map((item: any) => item.id);
      const addIds = dataSourceIds.filter((item: any) => !initIds.value.includes(item));
      const removeIds = initIds.value.filter((item: any) => !dataSourceIds.includes(item));
      if (executeDate === props.rowData?.executeDate && addIds.length === 0 && removeIds.length === 0) {
        // 没变化可直接返回
        emit('back');
        return;
      }
      // 有变化时点返回会弹框提示
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'config-return-modal',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否保存称量任务？'),
        footer() {
          return (
            <>
              <Space class='footer-btns'>
                <Button onClick={() => cancelModal()}>{t('取消')}</Button>
                <Button onClick={() => noSaveBack()}>{t('不保存')}</Button>
                <Button type='primary' onClick={() => save()}>
                  {t('保存')}
                </Button>
              </Space>
            </>
          );
        },
      });
      // emit('back');
    }
  };
  // 取消
  const cancelModal = () => {
    Modal.destroyAll();
  };
  // 不保存
  const noSaveBack = () => {
    cancelModal();
    emit('back');
  };
  // 保存
  const save = async () => {
    const { executeDate } = await myFormRef.value?.validate();
    if (dataSource.value.length === 0) return message.error(t('请添加称量需求'));
    const dataSourceIds = dataSource.value?.map((item: any) => item.id);
    const addIds = dataSourceIds.filter((item: any) => !initIds.value.includes(item)); //新增的称量需求id列表
    const removeIds = initIds.value.filter((item: any) => !dataSourceIds.includes(item));
    try {
      const data = {
        addIds,
        removeIds,
        executeDate,
        taskId: props.rowData?.id,
      };
      await reqWeighCentreTaskEdit(data);
      message.success(t('操作成功'));
      Modal.destroyAll();
      emit('back');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  // 获取任务详情和称量需求分页
  const init = async () => {
    const data = { taskId: props.rowData?.id };
    const res = await reqWeighCentreTaskQueryRequirementListByTaskId(data);
    initDataSource.value = res.data.requirementList; //存最初接口返回的表格数据
    dataSource.value = res.data.requirementList; //渲染开始的表格
    initIds.value = res.data.requirementList?.map((item: any) => item.id);
  };
  onMounted(() => {
    init();
    if (props.type === 'look') {
      myFormRef.value?.setFormProps({
        disabled: true,
      });
      myFormRef.value?.setFormModels({
        taskNo: props.rowData?.taskNo, //回显任务编号
        executeDate: props.rowData?.executeDate,
      });
    }
    if (props.type === 'edit') {
      myFormRef.value?.setFormModels({
        taskNo: props.rowData?.taskNo, //回显任务编号
        executeDate: props.rowData?.executeDate,
      });
    }
  });
</script>

<style lang="less" scoped>
  .task-planning-manage {
    width: 100%;
    height: 100%;
  }
  :deep(.content) {
    padding: 0;
  }
  .setting {
    height: 100%;
    background-color: var(--bmos-primary-color-white);
    padding-top: 5px;
    box-sizing: border-box;
    padding: 16px 16px 0px 16px;
    display: flex;
    flex-direction: column;
    .batch-table {
      flex: 1;
      overflow-y: hidden;
    }
  }
</style>
