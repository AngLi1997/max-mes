
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('新增')"
    wrapClassName="modalSizeLarge"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @cancel="cancel"
    @okModal="ok"
  >
    <div class="table">
      <BMTable
        ref="tableRef"
        :data-request="loadData"
        :columns="columns"
        :formProps="formProps"
        :scroll="{ x: 500, y: 100 }"
        :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"
        :showRefresh="false"
      ></BMTable>
    </div>
    
  </BMModalForm>
</template>

<script setup lang="ts">
import { 
  BMModalForm, 
  ModalFormInstance, 
  FormProps,
  BMTable,
  TableProps,
} from '@bmos/components';
import { useTable } from './hooks/useTable';
import { t } from '@bmos/i18n';
import { reactive, ref } from 'vue';
import type { Key } from 'ant-design-vue/lib/_util/type';
import { 
  getInspectionItemPage
} from '@/services/index';
import { message } from 'ant-design-vue';


const modalFormRef = ref<ModalFormInstance>();
const open = ref(false);
const tableRef = ref();

const { columns, formProps, rowData } = useTable();

const emit = defineEmits(['submitSuccess']);

const loadData = async (params: any) => {
  try {
    return await getInspectionItemPage({
      ...params,
      excludeIdList: excludeIdList.value.join(',')
    });
  } catch (error) {
    message.error(error.message);
  }
};

const excludeIdList = ref<String[]>([]);

const openModal = async (IdList: String[]) => {
  excludeIdList.value = [...IdList]
  // await loadData({excludeIdList})
  open.value = true;
};

const cancel = () => {
  open.value = false;
  state.selectedRowKeys = [];
  excludeIdList.value = [];
  selectList.value = [];
};

const selectList = ref<any>([]);

const ok = () => {
  emit('submitSuccess', selectList.value);
  message.success(t('添加成功'));
  cancel()
};

const state = reactive<{
  selectedRowKeys: Key[];
  loading: boolean;
}>({
  selectedRowKeys: [], // Check here to configure the default column
  loading: false,
});

const onSelectChange = (selectedRowKeys: Key[]) => {
  state.selectedRowKeys = selectedRowKeys;
  selectList.value = tableRef.value.tableData.filter((item: any) => selectedRowKeys.includes(item.id));
};

defineExpose({
  openModal,
});
</script>

<style lang="less" scoped>
.table {
  height: calc(100vh - 240px - 128px);
}
</style>