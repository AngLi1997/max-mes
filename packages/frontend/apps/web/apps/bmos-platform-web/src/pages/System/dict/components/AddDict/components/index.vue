<template>
  <div class="add-dict-data-table">
    <div class="table">
      <BMTable
        ref="tableInstance"
        :dataSource="dataSource"
        :columns="columns"
        row-key="id"
        :show-refresh="false"
        :scroll="{ x: 1380, y: 400 }"
        :search="false">
        <template #toolbar>
          <Button
            v-if="type1 !== 'look'"
            type="primary"
            @click="handleAdd"
            :disabled="isView">
            {{ t('添加数据') }}
          </Button>
        </template>
      </BMTable>
    </div>
  </div>
  <AddDetailModel
    ref="addDetailModelRef"
    v-model:open="addDetailModalOpen"
    :tableData="dataSource"
    :rowData="rowData"
    :status="modalStatus"
    @addTableData="addTableData"
    @updateTableData="updateTableData" />
</template>

<script lang="ts" setup>
  import { BMTable, Recordable } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import AddDetailModel from './AddDictDataModel.vue';
  import { t } from '@bmos/i18n';
  import { MODAL_STATUS } from '../../../types';
  import {
    reqPlatformDictListDetailGET,
    reqPlatformDictListWatchGET,
  } from '@/api';

  const emit = defineEmits<{
    (e: 'addDict'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      isView: boolean;
      type1?: any;
      dictRowData?: any;
    }>(),
    {
      isView: false,
      type1: '',
      dictRowData: {},
    },
  );

  watch(
    () => props.isView,
    val => {
      if (val) {
        modalStatus.value = MODAL_STATUS.VIEW;
      }
    },
  );
  // watch(
  //   () => props.type1,
  //   val => {
  //     console.log(val,'props.type1...');
  //     extraParams.value = {
  //       dictId: props.dictRowData.id,
  //     };
  //   },
  // );

  const dataSource = ref<any[]>([]);
  const addDetailModalOpen = ref<boolean>(false);
  const addDetailModelRef = ref();
  const modalStatus = ref<MODAL_STATUS>(MODAL_STATUS.ADD);
  const deleteIds = ref<any[]>([]);
  const { tableInstance, columns, rowData } = useTable({
    modalStatus,
    dataSource,
    addDetailModalOpen,
    isView: props.isView,
    type1: props.type1,
    deleteIds,
  });
  // 添加数据
  const handleAdd = () => {
    addDetailModalOpen.value = true;
    modalStatus.value = MODAL_STATUS.ADD;
  };

  const addTableData = (params: Recordable) => {
    dataSource.value.push(params);
  };
  const updateTableData = (params: Recordable, dictValue: string) => {
    const index = dataSource.value.findIndex(
      item => item.dictValue === dictValue,
    );
    if (index !== -1) {
      dataSource.value.splice(index, 1, params);
    } else {
      dataSource.value.push(params);
    }
  };
  // 额外参数
  // const extraParams = ref<Recordable>({});
  // 接口获取字典对应的字典数据
  // const loadData: DataRequestFn = async (params): Promise<any> => {
  //     extraParams.value = {
  //       dictId: props.dictRowData.id,
  //     };
  //   const newParams: Recordable = filterEmpty(params);
  //   console.log(newParams,'参数....');
  //   if (!newParams.dictId) {
  //     return Promise.resolve({
  //       data: [],
  //       total: 0,
  //     });
  //   }
  //   return reqPlatformDictListDetailGET(newParams);
  // };

  const getDataSource = () => {
    return dataSource.value;
  };
  onMounted(async () => {
    await nextTick();
    if (props.type1 === 'edit' || props.type1 === 'look') {
      const data = { id: props.dictRowData.id };
      const res = await reqPlatformDictListWatchGET(data);
      dataSource.value = res.data.detailList;
    }
  });
  defineExpose({
    getDataSource,
    deleteIds,
  });
</script>

<style lang="less" scoped>
  .add-dict-data-table {
    height: 100%;
    display: flex;
    flex-direction: column;
    .table {
      flex: 1;
      overflow-y: hidden;
    }
  }
</style>
