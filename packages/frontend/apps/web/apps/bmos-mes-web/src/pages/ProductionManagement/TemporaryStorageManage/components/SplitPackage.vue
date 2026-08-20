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
  import { usePositionUserList } from '../hooks';
  import { reqStorageMaterialInfo, reqStorageMaterialSplitPackage, reqTagScanScanWeighContainerCode } from '@/services';
  import { debounce, isEmpty, gtNumber } from '@bmos/utils';

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

  const title = ref<string>(t('拆包出库'));

  const signOpen = ref<boolean>(false);
  const { getPositionUserList, curPositionId, positionUserList } = usePositionUserList();
  const labelList = computed<LabelList[]>(() => {
    return [
      {
        label: t('出库人'),
        action: 70,
        disabled: true,
      },
      {
        label: t('领用人'),
        action: 71,
        options: positionUserList.value,
      },
    ];
  });
  const request = async () => {
    try {
      await reqStorageMaterialSplitPackage(curFormModal.value);
      emit('updateTable');
      message.success(t('出库成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const signSuccess = (data: Recordable) => {
    const { userId0, userId1 } = data;
    curFormModal.value = {
      ...curFormModal.value,
      senderId: userId0,
      receiverId: userId1,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      await getPositionUserList('120030008000020');
      signOpen.value = true;
      const { containerId, quantity, remark } = formModal;
      curFormModal.value = {
        containerId,
        quantity,
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

  const updateContainerOption = (options: any[] = []) => {
    modalFormRef.value?.formRef?.updateSchema({
      field: 'containerId',
      componentProps: {
        options,
      },
    });
  };

  const searchContainer = async (value: string) => {
    if (value) {
      try {
        const { data } = await reqTagScanScanWeighContainerCode(value);
        if (data.deviceId) {
          updateContainerOption([
            {
              label: `${data.deviceCode}-${data.deviceName}`,
              value: data.deviceId,
            },
          ]);
        } else {
          updateContainerOption();
        }
      } catch (error: any) {
        updateContainerOption();
        error.message && message.error(error.message);
      }
    } else {
      updateContainerOption();
    }
  };

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
        label: t('出库信息'),
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
        field: 'quantity',
        component: 'InputNumber',
        label: t('出库量'),
        required: true,
        componentProps: () => {
          return {
            min: 0,
            stringMode: true,
            style: {
              width: '100%',
            },
          };
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
                if (!value) return Promise.reject(t('请输入出库量'));
                // 输入正数
                if (Number(value) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                if (isEmpty(props.rowData?.productPlanId)) {
                  if (gtNumber(value, props.rowData?.quantity)) {
                    return Promise.reject(t('出库量不能大于物料量'));
                  }
                } else {
                  if (gtNumber(value, formModel['reserveQuantity'])) {
                    return Promise.reject(t('出库量不能大于预定量'));
                  }
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'unit',
        required: true,
        component: 'Input',
        label: t('单位'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'containerId',
        component: 'Select',
        label: t('容器'),
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            defaultActiveFirstOption: false,
            showArrow: false,
            filterOption: false,
            notFoundContent: false,
            options: [],
            showSearch: true,
            onSearch: debounce((value: string) => {
              formModel.containerId = undefined;
              searchContainer(value);
            }, 300),
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
            ...props.rowData,
            ...data,
            materialInfo: [
              { label: t('物料名称'), value: data.materialName },
              { label: t('物料编码'), value: data.mergeCode },
              { label: t('物料规格'), value: data.materialSpecification },
              { label: t('物料批号'), value: data.materialBatchNo },
              { label: t('物料件号'), value: data.materialNo },
              { label: t('物料量'), value: `${data.quantity}${data.unit}` },
            ],
            quantity: undefined,
          });
          curPositionId.value = data.materialPositionId;
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
