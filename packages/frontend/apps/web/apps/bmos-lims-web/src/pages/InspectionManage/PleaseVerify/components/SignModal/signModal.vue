<!-- 签名确认 -->
<template>
  <Sign 
    v-model:open="open"
    @signSuccess="signSuccess"
    :title="status ? t('检验终止') : t('请验确认')"
    :extraSchemas="status ? extraSchemas : []"
    :signatureDataFn="signatureDataFn"
    :signatureAction="status ? 22 : 18"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useForm } from './hooks/useForm';
import Sign from '@/components/Sign';
import {
  BMModalForm
} from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import {
  confirmCheckOrder,
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
  componentProps: {
    // disabled: true,
  },
},]);

const signatureDataFn = (formModal: any) => {
  const data = formModal.reason ? {
    id: idList.value[0],
    reason: formModal.reason,
  } : {
    idList: idList.value,
  }
  return JSON.stringify(data);
}

// 选中的数据
const idList = ref<String[]>([])

const openModal = (list: any[], flag: boolean) => {
  status.value = flag;
  // setFormProps.schemas[0].vIf = status.value;
  idList.value = [...list];
  open.value = true;
}

const {
  setFormRef,
  setFormProps,
  setNodeFormData,
} = useForm({status});

const request = async (formModal: any) => {
  const params = formModal.reason ? {
    id: idList.value[0],
    reason: formModal.reason,
  } : {
    idList: idList.value,
  }
  if (status.value) {
    return await terminateCheckOrder(params);
  } else {
    return await confirmCheckOrder(params);
  }
}

const signSuccess = async (formModal: any) => {
  try {
    await request(formModal);
    message.success(t('操作成功'));
    emit('submitSuccess', formModal);
    open.value = false;
  } catch(error: any) {
    message.error(error?.message);
  }
}

const close = () => {
  status.value = false;
  open.value = false;
}

defineExpose({
  openModal,
  close
})
</script>

<style scoped>

</style>