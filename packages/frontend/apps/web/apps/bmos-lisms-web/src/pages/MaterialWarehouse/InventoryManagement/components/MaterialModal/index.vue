<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="titleArr[modalType]"
    wrapClassName="modalSizeMedium"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal"></BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import {
    getMaterialInventoryOperation,
    getMaterialUseValidReceive,
    materialUseReceive,
    materialUseScrap,
    materialUseReturn,
    materialUseSpotCheck,
  } from '@/services';
  import { useForm } from './hooks';
  import { BMModalForm } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { useWarn } from '@/hooks';
  import { MaterialModelTypeEnum, yesOrNoEnum } from '@/types';

  const emit = defineEmits(['submitSuccess']);
  const { warnModal } = useWarn();
  const open = ref(false);

  const titleArr = [t('物料领用'), t('物料报废'), t('物料抽检'), t('物料退货')];

  const { modalType, modalFormRef, formProps, setFormModels, isQueryHistory, updateSchema } = useForm();

  const openModal = async (type: MaterialModelTypeEnum, row: any) => {
    modalType.value = type;
    let params = {} as any;
    if (type !== MaterialModelTypeEnum.RECEIVE) {
      try {
        const { data } = await getMaterialInventoryOperation(row.inWarehouseNo);
        params = data;
      } catch (error: any) {
        error.message && message.error(error.message);
      }
    } else {
      params = { ...row };
    }
    open.value = true;

    await nextTick();
    updateSchema({
      field: 'useCount',
      componentProps: {
        max: params?.availableStock,
      },
    });
    setFormModels({
      ...params,
      warehouseArea: row.warehouseArea?.value,
      needSpotCheck: type === MaterialModelTypeEnum.SPOT_CHECK ? yesOrNoEnum.YES : undefined,
    });
  };

  // 物料领用
  const receiveRequest = async (formModel: any) => {
    try {
      const params = {
        materialNo: formModel.materialNo,
        materialInstanceIdentify: formModel.materialInstanceIdentify,
        useCount: formModel.useCount,
        reasonId: formModel.reasonId,
        targetWarehouseId: formModel.targetWarehouseId,
        remark: formModel.remark,
      };
      const { data } = await materialUseReceive(params);
      if (data) {
        return Promise.resolve();
      }
      message.success(t('操作成功'));
      closeModal();
      emit('submitSuccess');
    } catch (error: any) {
      return Promise.reject(error);
    }
  };
  const receiveSubmit = async (formModel: any) => {
    try {
      const { data: valid } = await getMaterialUseValidReceive(formModel.materialInstanceIdentify);
      if (valid?.materialInstanceIdentify) {
        warnModal(t('存在有效期更近的批次，是否更换领用批次?'), {
          async onOk() {
            try {
              // await receiveRequest({ ...formModel, materialInstanceIdentify: valid?.materialInstanceIdentify });
              updateSchema({
                field: 'useCount',
                componentProps: {
                  max: valid?.availableStock,
                },
              });
              setFormModels({
                ...valid,
                useCount: Math.min(valid?.availableStock, formModel.useCount),
              });
              return Promise.resolve();
            } catch (error: any) {
              error.message && message.error(error.message);
              return Promise.reject();
            }
          },
          async onCancel() {
            try {
              await receiveRequest({ ...formModel });
              return Promise.resolve();
            } catch (error: any) {
              error.message && message.error(error.message);
              return Promise.reject();
            }
          },
        });
      } else {
        await receiveRequest(formModel);
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const request = async (formModel: any, func: (param: any) => Promise<any>) => {
    try {
      const params = {
        materialNo: formModel.materialNo,
        warehouseArea: formModel.warehouseArea,
        materialInstanceIdentify: formModel.materialInstanceIdentify,
        inWarehouseNo: formModel.inWarehouseNo,
        useCount: formModel.useCount,
        reasonId: formModel.reasonId,
        remark: formModel.remark,
      };
      await func(params);
      message.success(t('操作成功'));
      closeModal();
      emit('submitSuccess');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 物料报废
  const scrapRequest = async (formModel: any) => {
    await request(formModel, materialUseScrap);
  };

  // 物料退货
  const returnRequest = async (formModel: any) => {
    await request(formModel, materialUseReturn);
  };

  // 物料抽检
  const spotCheckRequest = async (formModel: any) => {
    await request(formModel, materialUseSpotCheck);
  };

  const submit = async (formModel: any) => {
    if (modalType.value === MaterialModelTypeEnum.RECEIVE) {
      await receiveSubmit(formModel);
    } else if (modalType.value === MaterialModelTypeEnum.SCRAP) {
      await scrapRequest(formModel);
    } else if (modalType.value === MaterialModelTypeEnum.RETURN) {
      await returnRequest(formModel);
    } else if (modalType.value === MaterialModelTypeEnum.SPOT_CHECK) {
      await spotCheckRequest(formModel);
    }
  };

  const closeModal = () => {
    open.value = false;
    isQueryHistory.value = false;
  };

  defineExpose({
    openModal,
    closeModal,
  });
</script>

<style lang="less" scoped></style>
