<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('物料接收')"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal"></BMModalForm>
  <ValidateModal ref="validateModalRef" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { materialReceive } from '@/services';
  import { useForm } from './hooks';
  import { BMModalForm } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { useWarn } from '@/hooks';
  import { yesOrNoEnum } from '@/types';
  import ValidateModal from './ValidateModal.vue';

  const emit = defineEmits(['submitSuccess']);
  const { warnModal } = useWarn();
  const open = ref(false);

  const { modalFormRef, formProps, setFormModels } = useForm();

  const openModal = async (row: any) => {
    open.value = true;
    await nextTick();
    setFormModels({
      materialIdentify: row.materialIdentify,
      materialNo: row.materialNo,
      materialName: row.materialName,
      warehouseAddressId: row.warehouseAddressId,
      specificationId: row.specificationId,
      keyMaterialCategory: row.keyMaterialCategory?.value,
      materialType: row.materialType?.value,
    });
  };

  const validateModalRef = ref<InstanceType<typeof ValidateModal>>();

  const request = async (formModel: any) => {
    try {
      const params = {
        ...formModel,
        materialNo: formModel.materialNo,
      };
      const { data } = await materialReceive(params);
      if (data) {
        validateModalRef.value?.openModal(formModel, data);
        return Promise.resolve();
      }
      message.success(t('操作成功'));
      closeModal();
      emit('submitSuccess');
    } catch (error: any) {
      return Promise.reject(error);
    }
  };

  const submit = async (formModel: any) => {
    try {
      if (formModel.materialType === 'CORE_MATERIAL' && formModel.needSpotCheck === yesOrNoEnum.NO) {
        warnModal(t('当前接收为关键物料，确认不进行抽检?'), {
          async onOk() {
            try {
              await request(formModel);
              return Promise.resolve();
            } catch (error: any) {
              error.message && message.error(error.message);
              return Promise.reject();
            }
          },
        });
      } else {
        await request(formModel);
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const closeModal = () => {
    open.value = false;
  };

  defineExpose({
    openModal,
    closeModal,
  });
</script>

<style lang="less" scoped></style>
