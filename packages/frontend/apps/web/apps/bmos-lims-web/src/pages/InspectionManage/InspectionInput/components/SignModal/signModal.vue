<!-- 签名确认 -->
<template>
  <!-- <BMModalForm
    ref="setFormRef"
    v-model:open="open"
    :title="status ? t('检验终止') : t('检验提交')"
    :formProps="setFormProps"
    wrapClassName="modalSizeMedium"
    @okModal="ok">
  </BMModalForm> -->
  <Sign 
    v-model:open="open"
    @signSuccess="signSuccess"
    :title="props.title"
    :extraSchemas="props.extraSchemas"
    :signatureDataFn="props.signatureDataFn"
    :signatureAction="props.signatureAction"
  />
</template>

<script setup lang="tsx">
import { ref } from 'vue';
import { useForm } from './hooks/useForm';
import {
  BMModalForm
} from '@bmos/components';
import Sign from '@/components/Sign';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';

const props = defineProps({
  title: {
    type: String,
    default: t('检验终止')
  },
  extraSchemas: {
    type: Array,
    default: () => [{
      field: 'reason',
      label: t('原因'),
      component: 'Input',
      required: true,
      componentProps: {
        maxLength: 100
      }
    }]
  },
  signatureAction: {
    type: Number,
    default: 22
  },
  signatureDataFn: {
    type: Function
  }
})

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

const {
  setFormRef,
  setFormProps,
  setNodeFormData,
} = useForm({
  status
});


const signSuccess = async (formModal: any) => {
  emit('submitSuccess', formModal);
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