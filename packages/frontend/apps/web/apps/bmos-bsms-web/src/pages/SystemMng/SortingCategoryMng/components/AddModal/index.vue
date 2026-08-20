<!-- 新增/编辑弹窗 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="actionType == 'create' ? t('新增') : t('编辑')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import {
    getSortingCategoryById,
    createSortingCategory,
    updateSortingCategory,
    getSortingCategoryOptions,
  } from '@/services';
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { message } from 'ant-design-vue';
  import { BMModalForm } from '@bmos/components';

  const open = ref(false);

  const actionType = ref<'create' | 'edit'>('create');

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const openModal = async (row: any, type: 'create' | 'edit' = 'create') => {
    actionType.value = type;
    const res = await getSortingCategoryOptions({
      type: row.manageType,
    });
    open.value = true;
    await nextTick();
    modalFormRef.value?.formRef?.updateSchema({
      field: 'sortingType',
      componentProps: {
        fieldNames: {
          label: 'immunityName',
          value: 'sortingType',
        },
        options: res.data || [],
      },
    });
    let formData = {
      manageType: row.manageType,
      useFlag: 1,
    };
    if (type === 'edit') {
      const { data } = await getSortingCategoryById(row.id);
      formData = {
        ...data,
        manageType: row.manageType,
        useFlag: data.useFlag?.value ?? 1,
        voiceFile: data.voiceFile
          ? [
              {
                uid: '1',
                name: data.voiceFile.split('/').pop(),
                status: 'done',
                response: {
                  url: `${window.location.origin}/${data.voiceFile}`,
                  name: data.voiceFile.split('/').pop(),
                },
                url: `${window.location.origin}/${data.voiceFile}`,
                thumbUrl: data.voiceFile,
              },
            ]
          : null,
      };
    }
    setFormModels(formData);
  };

  const submitApi = {
    create: createSortingCategory,
    edit: updateSortingCategory,
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      return await submitApi[actionType.value]({
        ...formModal,
        voiceFile: formModal.voiceFile?.length ? formModal.voiceFile[0].response.url : '',
      });
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      await request(formModal);
      message.success(t('操作成功'));
      emits('submitSuccess');
      cancel();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
