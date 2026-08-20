<template>
  <div class="add-code-rule-data-table">
    <BMTable
      ref="tableInstance"
      :dataSource="dataSource"
      :columns="columns"
      row-key="id"
      :show-refresh="false"
      :show-index="true"
      :scroll="{ x: 1000, y: 400 }"
      v-if='showTableFlag'
      :search="false">
      <template #headerTitle>
        <Space>
          <Button @click="handlePreviewCode">
            {{ t('编号预览') }}
          </Button>
          <Input v-model:value="codeView" style="width: 200px" />
        </Space>
      </template>
      <template #toolbar>
        <Button @click="handleSequence">
          {{ t('流水号规则') }}
        </Button>
        <Button type="primary" :disabled="isView" @click="handleAdd">
          {{ t('添加属性') }}
        </Button>
      </template>
    </BMTable>
  </div>
  <AddDetailModel
    v-model:open="addDetailModalOpen"
    :tableData="dataSource"
    :status="modalStatus"
    :rowData="rowData"
    :selectDictId="selectDictId"
    :parameterIdOptions="parameterIdOptions"
    @addTableData="addTableData"
    @updateTableData="updateTableData" />
  <SequenceModel
    ref="sequenceModelRef"
    v-model:open="sequenceModalOpen"
    :status="isView"
    :tableData="dataSource"
    :parameterIdOptions="parameterIdOptions"
    :delete-value="{}" />
</template>

<script lang="ts" setup>
  import { BMTable, Recordable } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import AddDetailModel from './AddDetailModel.vue';
  import { t } from '@bmos/i18n';
  import { MODAL_STATUS } from '@/pages/System/dict/types';
  import SequenceModel from './SequenceModel.vue';
  import { AddRuleDataStatus, DetailsType } from '../../../types';
  import dayjs from 'dayjs';

  const props = withDefaults(
    defineProps<{
      currentStatus?: AddRuleDataStatus;
      selectDictId?: string;
      codeObj?: Record<string, any>;
    }>(),
    {
      currentStatus: AddRuleDataStatus.ADD,
      selectDictId: '',
      codeObj: () => ({}),
    },
  );

  const isView = computed(() => {
    return props.currentStatus === AddRuleDataStatus.VIEW;
  });
  const showTableFlag = ref<boolean>(true)

  const dataSource = ref<any[]>([]);
  const addDetailModalOpen = ref<boolean>(false);
  const modalStatus = ref<MODAL_STATUS>(MODAL_STATUS.ADD);
  const { tableInstance, columns, rowData, parameterIdOptions, deleteRecord } = useTable({
    props,
    isView,
    modalStatus,
    dataSource,
    addDetailModalOpen,
    showTableFlag
  });

  const handleAdd = () => {
    modalStatus.value = MODAL_STATUS.ADD;
    addDetailModalOpen.value = true;
  };

  const addTableData = (params: Recordable) => {
    let sort: Number = 1;
    if (dataSource.value.length !== 0) sort = Number(dataSource.value[dataSource.value.length - 1].sort) + 1;
    dataSource.value.push({
      ...params,
      id: params.type + new Date().getTime(),
      sort,
    });
  };

  const updateTableData = (params: Recordable, id: string) => {
    const index = dataSource.value.findIndex(item => item.id === id);
    const sort = Number(dataSource.value[index].sort);
    dataSource.value.splice(index, 1, { ...params, id, sort });
    switch (params.type) {
      case DetailsType.SEQUENCE:
        if (sequenceModelRef.value.resetRuleRef.some((item: any) => item == sort)) deleteType({ sort });
        break;
    }
  };

  const getDataSource = () => {
    return dataSource.value;
  };

  const setDataSource = (data: any[]) => {
    dataSource.value = data;
  };

  // 编号预览
  const codeView = ref<string>('');
  const handlePreviewCode = () => {
    // return codeView.value;
    // [
    // {
    //     "type": "PARAMETER",
    //     "parameterId": "1727877870782672896",
    //     "id": "PARAMETER1701323864807"
    // },
    // {
    //     "type": "CONSTANT",
    //     "value": "-",
    //     "id": "CONSTANT1701323868986"
    // },
    // {
    //     "type": "DATE",
    //     "dateFormat": "yyMMdd",
    //     "dateType": "年月日",
    //     "id": "DATE1701323897055"
    // },
    // {
    //     "type": "CONSTANT",
    //     "value": "-",
    //     "id": "CONSTANT1701323901270"
    // },
    // {
    //     "type": "SEQUENCE",
    //     "fillZero": "TRUE",
    //     "startNo": "1",
    //     "maxLength": 3,
    //     "step": 1
    // }
    // ]
    let res: string = '';
    dataSource.value.forEach(item => {
      switch (item.type) {
        case DetailsType.PARAMETER:
          res += parameterIdOptions.value?.find((dict: any) => dict.id === item.parameterId)?.value;
          break;
        case DetailsType.CONSTANT:
          res += item.value;
          break;
        case DetailsType.DATE:
          res += dayjs().format(item.dateFormat.toString().toUpperCase()).toString();
          break;
        case DetailsType.SEQUENCE:
          // 流水号 startNo + maxLength + fillZero
          // 如 1 + 3 + true
          // 001
          res += item.startNo.padStart(item.maxLength, item.fillZero === 'TRUE' ? '0' : '');
          break;
        default:
          break;
      }
    });
    codeView.value = res;
  };

  // 流水号规则
  const sequenceModalOpen = ref<boolean>(false);
  const handleSequence = () => {
    deleteType(deleteRecord.value);
    sequenceModalOpen.value = true;
  };

  const sequenceModelRef = ref<any>();

  const getResetRule = () => {
    return sequenceModelRef.value.resetRuleRef;
  };

  const setResetRule = (data: any) => {
    sequenceModelRef.value.resetRuleSet(data);
  };
  const deleteType = (data: any) => {
    sequenceModelRef.value.deleteType(data);
  };
  defineExpose({
    getDataSource,
    getResetRule,
    setDataSource,
    setResetRule,
  });
</script>

<style lang="less" scoped>
  .add-code-rule-data-table {
    height: 100%;
  }
  :deep(.index-box) {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    .index-btn-box {
      display: none;
      cursor: pointer;
      position: absolute;
      left: -10px;
      top: -5px;
      bottom: 0;
      margin: auto;
    }
    &:hover {
      .index-btn-box {
        display: flex;
        align-items: center;
        flex-direction: column;
      }
    }
  }
</style>
