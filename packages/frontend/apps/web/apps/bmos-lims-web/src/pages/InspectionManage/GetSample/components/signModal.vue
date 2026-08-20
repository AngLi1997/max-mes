<!-- 签名确认 -->
<template>
  <Sign 
    v-model:open="open"
    @signSuccess="signSuccess"
    :title="status ? t('检验终止') : t('取样')"
    :extraSchemas="status ? extraSchemas : extraSchemas1"
    :signatureDataFn="signatureDataFn"
    :signatureAction="status ? 22 : 19"
  />
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useForm } from './hooks/useForm';
import Sign from '@/components/Sign';
import {
  BMModalForm
} from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import {
  takeCheckOrder,
  terminateCheckOrder
} from '@/services/index';

const emit = defineEmits(['submitSuccess']);

// true --- 终止 false --- 确认
const status = ref(false);
const open = ref(false);

const extraSchemas = ref([{
  field: 'reason',
  label: t('原因'),
  component: 'Input',
  required: true,
}]);

const extraSchemas1 = ref([{
  field: 'amount',
  label: t('取样量'),
  component: 'Input',
  required: true,
}]);

// 选中的数据
const rowData = ref<any>({})

const signatureDataFn = (formModal: any) => {
  const data = {
    id: rowData.value.id,
    reason: formModal.reason,
    amount: formModal.amount,
  }
  return JSON.stringify(data);
}

const openModal = (row: any, flag: boolean) => {
  status.value = flag;
  // setFormProps.schemas[0].vIf = status.value;
  rowData.value = row;
  open.value = true;
}

const {
  setFormRef,
  setFormProps,
  setNodeFormData,
} = useForm({status});

const request = async (formModal: any) => {
  const params = {
    id: rowData.value.id,
    reason: formModal.reason,
    amount: formModal.amount,
  }
  if (status.value) {
    return await terminateCheckOrder(params);
  } else {
    return await takeCheckOrder(params);
  }
}

const signSuccess = async (formModal: any) => {
  try {
    await request(formModal);
    message.success(t('操作成功'));
    emit('submitSuccess');
    open.value = false;
  } catch(error) {
    message.error(error?.message);
  }
}

const close = () => {
  open.value = false;
}

defineExpose({
  openModal,
  close
})
</script>

<style scoped>

</style>