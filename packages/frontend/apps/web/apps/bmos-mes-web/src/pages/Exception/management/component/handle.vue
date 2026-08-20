<template>
  <div>
    <BMModalForm
      ref="handleFormRef"
      v-model:open="openHandleModal"
      :title="t('异常处理')"
      :formProps="formProps"
      wrapClassName="modalSizeMedium inbound-model">
      <template #footer>
        <Button @click="openHandleModal = false">{{ t('取消') }}</Button>
        <Button type="primary" :loading="loading" @click="handleExceptionSubmit">
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
  </div>
</template>
<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { openHandleModal, handleFormRef } from '../hooks/datas';
  import { BMModalForm, FormProps } from '@bmos/components';
  import { Button, message } from 'ant-design-vue';
  import { ref } from 'vue';
  import SignModal from '@/components/SignModal';
  import { exceptionHandle } from '@/services';

  const signOpen = ref(false);
  const curFormModal = ref<any>({});
  const labelList = ref([
    {
      label: t('操作人'),
      disabled: false,
      menuId: 120090001000009,
      action: 119,
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

  // 异常处理确定
  const handleExceptionSubmit = async () => {
    try {
      loading.value = true;
      // 表单校验
      handleFormRef.value?.submit();
      const params = await handleFormRef.value?.validate();
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
      await exceptionHandle({
        ...curFormModal.value,
        handleUserId: value.userId0,
        id: props.rowData.id,
      });
      message.success(t('处理异常成功'));
      openHandleModal.value = false;
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
        field: 'handleResult',
        component: 'InputTextArea',
        label: t('处理结果'),
        required: true,
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
      },
      {
        field: 'handleTime',
        label: t('处理时间'),
        required: true,
        component: 'DatePicker',
        componentProps: () => {
          return {
            showTime: true,
            showNow: true,
            format: 'YYYY-MM-DD HH:mm:ss',
            valueFormat: 'YYYY-MM-DD HH:mm:ss',
          };
        },
      },
    ],
  });
</script>
