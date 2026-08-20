<template>
  <BMModalForm
    ref="investigationFormRef"
    v-model:open="openInvestigationModal"
    :title="t('异常重新调查')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium inbound-model">
    <template #footer>
      <Button @click="openInvestigationModal = false">{{ t('取消') }}</Button>
      <Button type="primary" :loading="loading" @click="investigationExceptionSubmit">
        {{ t('确定') }}
      </Button>
    </template>
  </BMModalForm>
  <SignModal
    v-model:open="signOpen"
    :signatureData="JSON.stringify(curFormModal)"
    :labelList="labelList"
    @cancelModal="loading = false"
    @signSuccess="signSuccess"></SignModal>
</template>
<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { openInvestigationModal, investigationFormRef } from '../hooks/datas';
  import { BMModalForm, FormProps } from '@bmos/components';
  import { ref } from 'vue';
  import SignModal from '@/components/SignModal';
  import { Button, message } from 'ant-design-vue';
  import { exceptionReInvestigate } from '@/services';

  const signOpen = ref(false);
  const curFormModal = ref<any>({});
  const labelList = ref([
    {
      label: t('操作人'),
      disabled: false,
      menuId: 120090001000011,
      action: 121,
      currentUser: true,
    },
  ]);
  const props = withDefaults(
    defineProps<{
      rowData: any;
    }>(),
    {
      rowData: () => {},
    },
  );
  const emit = defineEmits(['submit']);
  const loading = ref(false);
  // 异常重新调查确定
  const investigationExceptionSubmit = async () => {
    try {
      loading.value = true;
      // 表单校验
      investigationFormRef.value?.submit();
      const params = await investigationFormRef.value?.validate();
      // 打开签名弹窗
      curFormModal.value = { ...params };
      signOpen.value = true;
    } catch (error) {
      loading.value = false;
    }
  };
  // 签名成功
  const signSuccess = async (value: any) => {
    try {
      await exceptionReInvestigate({
        ...curFormModal.value,
        reInvestigateUserId: value.userId0,
        id: props.rowData.id,
      });
      message.success(t('处理异常成功'));
      openInvestigationModal.value = false;
      emit('submit');
    } catch (error: any) {
      message.error(error.message);
    } finally {
      loading.value = false;
    }
  };
  // 表单属性
  const formProps: Ref<FormProps> = ref({
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 120,
    schemas: [
      {
        field: 'reInvestigateReason',
        component: 'InputTextArea',
        label: t('重新调查原因'),
        required: true,
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
      },
    ],
  });
</script>
