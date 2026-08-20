<!-- 签名确认 -->
<template>
  <BMModalForm
    ref="setFormRef"
    v-model:open="open"
    :title="status ? t('检验终止') : t('检验提交')"
    :formProps="setFormProps"
    wrapClassName="modalSizeMedium"
    @okModal="ok">
  </BMModalForm>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useForm } from './hooks/useForm';
import {
  BMModalForm
} from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';

const emit = defineEmits(['submitSuccess']);

// true --- 终止 false --- 确认
const status = ref(false);
const open = ref(false);

// 选中的数据
const rowData = ref<any>({})

const openModal = (row: any, flag: boolean) => {
  status.value = flag;
  // setFormProps.schemas[0].vIf = status.value;
  rowData.value = row;
  open.value = true;
}

const alertMsg = ref('存在20项未录入分析项，是否批量录为N/A并提交');

const {
  setFormRef,
  setFormProps,
  setNodeFormData,
} = useForm({
  status,
  alertMsg
});

const signatureDataFn = (formModel: any) => {
  const data = {
    id: rowData.value.id,
    reason: formModel.reason,
  }
  return JSON.stringify(data);
}

const request = async (formModal: any) => {
  const params = {
    ...formModal,
    idList: idList.value
  }
  // if (status.value) {
  //   return await updateCategory(params);
  // } else {
  //   return await saveCategory(params);
  // }
  return {code: '0'}
}

const ok = async () => {
  await setFormRef.value?.submit(request);
  message.success(t('操作成功'));
  emit('submitSuccess');
  open.value = false;
}

const close = () => {
  open.value = false;
}

defineExpose({
  openModal,
  close
})
</script>

<style lang="less" scoped>
</style>