<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
  <SignModal
    v-model:open="signOpen"
    :signatureData="JSON.stringify(curFormModal)"
    :labelList="labelList"
    @signSuccess="signSuccess"></SignModal>
</template>
<script lang="tsx" setup>
  import {
    BMModalForm,
    FormProps,
    ModalFormInstance,
    RenderCallbackParams,
    Recordable,
    BMDescriptions,
  } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import SignModal from '@/components/SignModal';
  import { LabelList } from '@/components/SignModal/type';
  import {
    reqPlanInfoQueryNotTerminatedBatchListByProductIdAndProcessId,
    reqProcessListAll,
    reqProductMaterialProductTreeReq,
    reqStorageMaterialInfo,
    reqStorageMaterialReserve,
  } from '@/services';
  import { loopSelectableTree } from '@bmos/utils';
  import { usePositionUserList } from '../hooks';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
      currentNodes?: any;
    }>(),
    {
      rowData: () => ({}),
      currentNodes: () => [],
    },
  );

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const title = ref<string>(t('物料预定'));

  const signOpen = ref<boolean>(false);
  const { getPositionUserList, curPositionId, positionUserList } = usePositionUserList();
  const labelList = computed<LabelList[]>(() => {
    return [
      {
        label: t('操作人'),
        action: 72,
        disabled: true,
      },
      {
        label: t('复核人'),
        action: 73,
        options: positionUserList.value,
      },
    ];
  });

  const request = async () => {
    try {
      await reqStorageMaterialReserve(curFormModal.value);
      emit('updateTable');
      message.success(t('预定成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const signSuccess = (data: Recordable) => {
    const { userId0, userId1 } = data;
    curFormModal.value = {
      ...curFormModal.value,
      operatorId: userId0,
      reCheckerId: userId1,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      await getPositionUserList('120030008000021');
      signOpen.value = true;
      const { productId, processId, batchId, remark } = formModal;
      curFormModal.value = {
        productId,
        processId,
        batchId,
        remark,
        storageMaterialId: props.rowData.id,
      };
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();
  const formProps: Ref<FormProps> = ref({
    baseColProps: {
      span: 11,
    },
    schemas: [
      {
        field: 'field6',
        component: 'Divider',
        label: t('物料信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'materialInfo',
        label: '',
        noLabel: true,
        colProps: {
          span: 24,
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return <BMDescriptions column={2} list={formModel.materialInfo} showBottomBorder={false} />;
        },
      },
      {
        field: 'field7',
        component: 'Divider',
        label: t('预定信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品信息'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            request: async () => {
              try {
                const { data } = await reqProductMaterialProductTreeReq();
                return loopSelectableTree(data, 'categoryFlag', true);
              } catch (error) {
                return [];
              }
            },
            onSelect: () => {
              formModel.processId = undefined;
              formModel.batchId = undefined;
            },
          };
        },
      },
      {
        field: 'processId',
        component: 'Select',
        label: t('生产工艺'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            request: {
              watchFields: ['productId'],
              options: {
                immediate: true,
              },
              callback: async () => {
                try {
                  if (!formModel.productId) {
                    return [];
                  }
                  const { data } = await reqProcessListAll({
                    productId: formModel.productId,
                    // @ts-ignore
                    active: true,
                  });
                  return data;
                } catch (error) {
                  return [];
                }
              },
            },
            onSelect: () => {
              formModel.batchId = undefined;
            },
          };
        },
      },
      {
        field: 'batchId',
        component: 'Select',
        label: t('生产批次'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'batchNo',
              value: 'id',
            },
            request: {
              watchFields: ['processId'],
              options: {
                immediate: true,
              },
              callback: async () => {
                try {
                  if (!formModel.processId || !formModel.productId) {
                    return [];
                  }
                  const { data } = await reqPlanInfoQueryNotTerminatedBatchListByProductIdAndProcessId(
                    formModel.productId,
                    formModel.processId,
                  );
                  return data;
                } catch (error) {
                  return [];
                }
              },
            },
          };
        },
      },
      {
        field: 'remark',
        required: true,
        component: 'InputTextArea',
        label: t('备注'),
      },
    ],
  });

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        try {
          const { data } = await reqStorageMaterialInfo({
            id: props.rowData.id,
          });
          modalFormRef.value?.formRef?.setFormModels({
            materialInfo: [
              { label: t('物料名称'), value: data.materialName },
              { label: t('物料编码'), value: data.mergeCode },
              { label: t('物料规格'), value: data.materialSpecification },
              { label: t('物料批号'), value: data.materialBatchNo },
              { label: t('物料件号'), value: data.materialNo },
              { label: t('可用量'), value: `${data.availableQuantity}${data.unit}` },
            ],
          });
          curPositionId.value = data.materialPositionId;
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
