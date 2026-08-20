<!-- 管理页 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[true]"
    :formProps="[formFirstProps as any]"
    :requests="[reqWeighingTaskListReq as any]"
    :columns="[columnsFirst as any]">
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="120030010000001" type="primary" @click="taskPlanning()">
        {{ t('任务规划') }}
      </Button>
      <Button v-hasAuth="120030010000002" @click="programAuto">
        {{ t('自动规划') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('称量任务')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="tsx" setup>
  import {} from '@/services';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import type { FormProps } from '@bmos/components';
  import { reactive } from 'vue';
  import { t } from '@bmos/i18n';
  import { message, Modal } from 'ant-design-vue';
  import StateTag from '@/components/StateTag/index.vue';
  // import dayjs from 'dayjs';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { usePermissionStore } from '@/stores/permission';
  import {
    reqWeighCentreTaskQueryPage,
    reqWeighCentreTaskCancel, //取消
    reqWeighCentreTaskSend, //下发
    reqWeighCentreTaskMakeSure, //确认,
    reqWeighCentreTaskProgramAuto, //自动规划
  } from '@/services';

  const emit = defineEmits(['editOrLook', 'taskPlanning']);
  const { hasPermission } = usePermissionStore();
  const pageRef = ref<any>();
  // 状态: 编辑/待下发蓝色 已下发/已执行绿色
  const Status: Record<string, string> = {
    0: 'primary',
    1: 'purple',
    2: 'success',
    3: 'success',
  };
  const formFirstProps = reactive<Partial<FormProps>>({
    actionColOptions: {},
    baseColProps: {
      // span: 6,
    },
    fieldMapToTime: [['executeDate', ['executeDateStart', 'executeDateEnd'], 'YYYY-MM-DD']],
  });
  const columnsFirst = ref<any>([
    {
      title: t('任务编号'),
      dataIndex: 'taskNo',
      fixed: 'left',
      width: 170,
      resizable: true,
    },
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
      title: t('执行时间'),
      align: 'left',
      dataIndex: 'executeDate',
      width: 150,
      resizable: true,
      formItemProps: {
        order: 3,
        colProps: { span: 6 },
        component: 'RangePicker',
        // defaultValue: [dayjs().subtract(29, 'day').format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')], //默认最近30天
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
      title: t('状态'),
      dataIndex: 'taskStatus',
      width: 100,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => (
        <StateTag type={Status[record.taskStatus.value]}>{record.taskStatus.label}</StateTag>
      ),
    },
    {
      title: t('操作'),
      fixed: 'right',
      hideInSearch: true,
      width: 240,
      resizable: true,
      key: 'ACTION',
      actions: ({ record }: any) => [
        {
          label: t('编辑'),
          ifShow: record.taskStatus.value === 0 && hasPermission('120030010000003'),
          onClick: () => {
            emit('editOrLook', record, 'edit');
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('120030010000004'),
          onClick: () => {
            emit('editOrLook', record, 'look');
          },
        },
        {
          label: t('确认'),
          ifShow: record.taskStatus.value === 0 && hasPermission('120030010000005'),
          onClick: () => {
            confirm(record);
          },
        },
        {
          label: t('下发'),
          ifShow: record.taskStatus.value === 1 && hasPermission('120030010000006'),
          onClick: () => {
            Distribute(record);
          },
        },
        {
          label: t('取消'),
          ifShow: record.cancelAble && hasPermission('120030010000007'),
          onClick: () => {
            cancel(record);
          },
        },
      ],
    },
  ]);
  // 获取表格数据
  const reqWeighingTaskListReq = async (params: any) => {
    return await reqWeighCentreTaskQueryPage({
      ...params,
      weighCentre: params?.weighCentreName || undefined,
      weighCentreName: undefined,
    });
  };
  // 任务规划
  const taskPlanning = () => {
    emit('taskPlanning');
  };
  // 自动规划
  const programAuto = async () => {
    try {
      Modal.confirm({
        title: t('提示'),
        icon: h(ExclamationCircleOutlined),
        content: t('是否对称量需求自动规划'),
        async onOk() {
          try {
            await reqWeighCentreTaskProgramAuto();
            message.success(t('操作成功'));
            pageRef.value?.fetchData(0);
            return Promise.resolve();
          } catch (error: any) {
            error.message && message.error(error.message);
            return Promise.reject();
          }
        },
        onCancel() {},
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 确认
  const confirm = async (record: any) => {
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否确认该称量任务'),
      async onOk() {
        try {
          await reqWeighCentreTaskMakeSure({ taskId: record.id });
          message.success(t('操作成功'));
          pageRef.value?.fetchData(0);
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };
  // 下发
  const Distribute = async (record: any) => {
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否下发该称量任务'),
      async onOk() {
        try {
          await reqWeighCentreTaskSend({ taskId: record.id });
          message.success(t('操作成功'));
          pageRef.value?.fetchData(0);
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };
  // 取消
  const cancel = async (record: any) => {
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否取消该称量任务'),
      async onOk() {
        try {
          await reqWeighCentreTaskCancel({ taskId: record.id });
          message.success(t('操作成功'));
          pageRef.value?.fetchData(0);
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
