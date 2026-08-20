<!-- 新增/编辑浆站 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="titleMap[dialogType]"
    :formProps="formProps"
    :submit="submit"></BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { useForm } from './hooks/useForm';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { ref } from 'vue';
  import { getPlasmaStationById, createPlasmaStation, updatePlasmaStation } from '@/services';
  import { usePlasmaStation } from '@/stores/plasmaStation';

  const { setPlasmaStation } = usePlasmaStation();

  const open = ref(false);

  const dialogType = ref<'create' | 'edit'>('create');
  const titleMap = {
    create: t('新增'),
    edit: t('编辑'),
  };

  const emits = defineEmits(['submitSuccess']);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const openModal = async (type: 'create' | 'edit', row?: any) => {
    open.value = true;
    dialogType.value = type;
    await nextTick();
    if (type === 'edit' && row?.id) {
      try {
        const { data } = await getPlasmaStationById(row.id);
        // 编辑，进行数据回显
        setFormModels({
          ...data,
          useFlag: data.useFlag?.value,
        });
      } catch (error) {
        console.log('error', error);
      }
    }
  };

  const cancel = () => {
    open.value = false;
  };

  const request = async (formModal: any) => {
    try {
      const params = {
        ...formModal,
      };
      console.log('params', params);
      if (dialogType.value === 'edit') {
        return await updatePlasmaStation(params);
      } else {
        return await createPlasmaStation(params);
      }
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async (formModal: any) => {
    try {
      await request(formModal);
      await setPlasmaStation();
      message.success(t('操作成功'));
      emits('submitSuccess');
      cancel();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  defineExpose({ openModal });
</script>

<style scoped></style>
