<!-- 指令单分解 -->
<template>
  <keep-alive>
    <EmptyBlock v-if="!open">
      <div class="main bg-white">
        <BMTable
          ref="tableInstance"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          auto-height
          :autoHeightOffset="24"
          :scroll="{ x: 1144, y: 400 }"
          :show-tool-bar="false"
          :formProps="formProps"
          showSearchBorder></BMTable>
        <!-- 分解页 -->
      </div>
    </EmptyBlock>
  </keep-alive>
  <Decompose v-if="open" :showType="DecomposeType" :modalJson="modalJson" :rowData="rowData" @close="decomposeClose" />
</template>

<script lang="ts" setup>
  import type { DataRequestFn, FormProps, TableInstance } from '@bmos/components';
  import { BMTable } from '@bmos/components';
  import { Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { reactive, ref } from 'vue';
  import { useTable } from './hooks/useTable';
  import { t } from '@bmos/i18n';
  import Decompose from './components/Decompose.vue';
  import { createVNode } from 'vue';
  import { planInfoPage } from '@/services';
  import type { PlanPageVO } from './types';
  import {
    reqGetDetailUsingGET,
    reqGetProcessModelUsingGET,
    planInstructionSend,
    planInstructionDetail,
  } from '@/services';
  import EmptyBlock from '@/components/EmptyBlock/index.vue';

  const tableInstance = ref<TableInstance>();
  const open = ref<boolean>(false);
  // 详情页/分解页type
  const DecomposeType = ref('');
  // 流程图json
  const modalJson = ref('');
  // 当前行数据
  const rowData = ref<PlanPageVO>({});
  // 点击分解/详情
  const openDecompose = async (type: string, row: PlanPageVO) => {
    try {
      // 指令单分解详情
      const res = await planInstructionDetail(`${row.id}`);
      const { data } = await reqGetDetailUsingGET({
        processId: row.processId,
        version: row.processVersion,
      } as unknown as API.MesProcessDetailReq);
      const modalRes = await reqGetProcessModelUsingGET({
        processModelId: data.processModelId,
      });
      modalJson.value = JSON.parse(modalRes.data).map((item: any) => {
        const metaInfo = JSON.parse(item.metaInfo);
        const procedure = data.procedures.find((it: any) => it.nodeId === metaInfo.id);

        return {
          ...metaInfo,
          ...(procedure?.name ? { label: procedure.name } : {}),
          data: {
            ...metaInfo.data,
            principal: '',
            teams: [], //默认值
            status: { value: '' },
            ...res.data.instructions.find((it: any) => it.nodeId === metaInfo.id),
          },
          shape: metaInfo.shape == 'custom-vue-node' ? 'custom-vue-item-node' : metaInfo.shape,
        };
      });

      open.value = true;
      DecomposeType.value = type;
      rowData.value = row;
      rowData.value.processModelId = data.processModelId;
      rowData.value.planDetailVO = res.data.planDetailVO;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  // 点击下发
  const distribute = (record: PlanPageVO) => {
    Modal.confirm({
      title: t('确认下发此次生产指令单'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: '',
      okText: t('确定'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          await planInstructionSend(`${record.id}`);
          message.success(t('下发成功！'));
          // 刷新列表
          tableInstance.value?.fetchData();
        } catch (error: any) {
          message.error(error.message);
        }
      },
    });
  };
  const { columns } = useTable({ openDecompose, distribute });
  // 表单配置
  const formProps = reactive<Partial<FormProps>>({
    // actionColOptions: {
    //   span: 4,
    // },
    baseColProps: {
      span: 6,
    },
    schemas: [
      {
        field: 'planNo',
        component: 'Input',
        label: t('指令单编号'),
      },
      {
        field: 'productName',
        component: 'Input',
        label: t('产品名称'),
      },
      {
        field: 'processName',
        component: 'Input',
        label: t('工艺名称'),
      },
      {
        field: 'batchNo',
        component: 'Input',
        label: t('生产批号'),
      },
      {
        field: 'instructStatus',
        component: 'Select',
        label: t('状态'),
        componentProps: {
          options: [
            {
              label: t('待分解'),
              value: 'WAIT_DECOMPOSE',
            },
            {
              label: t('待确认'),
              value: 'WAIT_CONFIRM',
            },
            {
              label: t('待下发'),
              value: 'WAIT_SEND',
            },
            {
              label: t('已下发'),
              value: 'SEND',
            },
          ],
        },
      },
      {
        field: 'type',
        component: 'Select',
        label: t('指令单类型'),
        componentProps: {
          options: [
            {
              label: t('生产批次'),
              value: 'PRODUCT',
            },
            {
              label: t('实验批次'),
              value: 'EXPERIMENT',
            },
            {
              label: t('验证批次'),
              value: 'VERIFY',
            },
          ],
        },
      },
    ],
  });
  const loadData: DataRequestFn = async (params): Promise<any> => {
    return planInfoPage({
      orderBy: 'confirmTime',
      dir: 'desc',
      status: 'CONFIRM',
      ...params,
    });
  };
  // 详情页关闭
  const decomposeClose = () => {
    open.value = false;
    tableInstance.value?.fetchData();
  };
</script>
<style scoped lang="less">
  .main {
    height: 100%;
    min-height: 100%;
    background-color: white;
    padding: 16px;
  }
  :deep(.action-list) {
    display: flex;
    justify-content: flex-start;
  }
  :deep(.bmos-table .bmos-action-list .mes-btn) {
    padding: 6px 0 !important;
  }
</style>
