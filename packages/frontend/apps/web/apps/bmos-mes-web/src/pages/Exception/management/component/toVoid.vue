<template>
  <BMModalForm
    ref="toVoidFormRef"
    v-model:open="openToVoidModal"
    :title="t('异常作废')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium inbound-model">
    <template #footer>
      <Button @click="openToVoidModal = false">{{ t('取消') }}</Button>
      <Button type="primary" :loading="loading" @click="toVoidExceptionSubmit">
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
  import { openToVoidModal, toVoidFormRef } from '../hooks/datas';
  import { BMModalForm, FormProps } from '@bmos/components';
  import SignModal from '@/components/SignModal';
  import { ref } from 'vue';
  import { Button, message } from 'ant-design-vue';
  import { exceptionCancel } from '@/services';

  const signOpen = ref(false);
  const curFormModal = ref<any>({});
  const labelList = ref([
    {
      label: t('操作人'),
      disabled: false,
      menuId: 120090001000010,
      action: 120,
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
  const toVoidExceptionSubmit = async () => {
    try {
      loading.value = true;
      // 表单校验
      toVoidFormRef.value?.submit();
      const params = await toVoidFormRef.value?.validate();
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
      await exceptionCancel({
        ...curFormModal.value,
        cancelUserId: value.userId0,
        id: props.rowData.id,
      });
      message.success(t('处理异常成功'));
      openToVoidModal.value = false;
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
        field: 'cancelReason',
        component: 'InputTextArea',
        label: t('作废原因'),
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
