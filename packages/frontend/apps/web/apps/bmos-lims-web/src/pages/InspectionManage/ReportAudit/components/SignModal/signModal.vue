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
    ref="signModalRef"
    v-model:open="open"
    @signSuccess="signSuccess"
    :title="props.title"
    :extraSchemas="props.extraSchemas"
    :signatureDataFn="props.signatureDataFn"
    :signatureAction="props.signatureAction"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useForm } from './hooks/useForm';
import {
  BMModalForm
} from '@bmos/components';
import { t } from '@bmos/i18n';
import Sign from '@/components/Sign';
import { message } from 'ant-design-vue';

const emit = defineEmits(['submitSuccess']);

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

const signModalRef = ref<InstanceType<typeof Sign>>();

// const {
//   setFormRef,
//   setFormProps,
//   setNodeFormData,
// } = useForm({
//   status
// });

const signSuccess = async (formModel) => {
  // await setFormRef.value?.submit(request);
  emit('submitSuccess', formModel);
  open.value = false;
}

const close = () => {
  open.value = false;
}

defineExpose({
  openModal,
  close,
  signModalRef
})
</script>

<style lang="less" scoped>
</style>