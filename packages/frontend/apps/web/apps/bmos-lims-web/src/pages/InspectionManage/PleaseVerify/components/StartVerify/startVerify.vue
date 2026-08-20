<!-- 发起请验 -->
<template>
  <BMModalForm
    ref="setFormRef"
    v-model:open="open"
    :title="t('发起请验')"
    :formProps="setFormProps"
    wrapClassName="modalSizeLarge"
    @okModal="ok"></BMModalForm>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import { useForm } from './hooks/useForm';
  import { BMModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import { getTestArticleListAll, saveCheckOrder } from '@/services/index';

  const emit = defineEmits(['submitSuccess']);

  const open = ref(false);

  const { setFormRef, setFormProps, setNodeFormData } = useForm();

  const openModal = async () => {
    try {
      const { data } = await getTestArticleListAll();

      open.value = true;
      await nextTick();
      setFormRef.value?.formRef.updateSchema({
        field: 'productId',
        componentProps: {
          options: data.map((item: any) => {
            return {
              label: item.name,
              value: item.id,
              code: item.mergeCode,
              specification: item.specification,
            };
          }),
        },
      });
      // setFormProps.schemas[0].componentProps.options = productList.value;
    } catch (error: any) {
      message.error(error?.message);
    }
    // open.value = true;
  };

  const printFlag = ref(false);

  const request = async (formModal: any) => {
    const params = {
      ...formModal,
    };
    printFlag.value = formModal.print;
    return await saveCheckOrder(params);
  };

  const ok = async () => {
    try {
      const res = await setFormRef.value?.submit(request);
      message.success(t('发起成功'));
      emit('submitSuccess', printFlag.value, res.data);
      close();
    } catch (error: any) {
      message.error(error?.message);
    }
  };

  const close = () => {
    open.value = false;
  };

  defineExpose({
    openModal,
  });
</script>

<style scoped></style>
