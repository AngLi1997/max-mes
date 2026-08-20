<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
  <Sign
    v-model:open="signOpen"
    :signatureData="JSON.stringify(curFormModal)"
    :signatureAction="34"
    :labelList="labelList"
    @signSuccess="signSuccess"></Sign>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, Recordable, RenderCallbackParams } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import Sign from '@/components/Sign';
  import { LabelList } from '@/components/Sign/type';
  import { message } from 'ant-design-vue';
  import { reqInventoryEditInventoryBatch, reqInventoryQueryInventoryBatchById } from '@/services';
  import { useCargoInfo } from '../../StorageManage/hooks';
  import { isEmpty } from '@bmos/utils';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
      treeData?: any[];
      currentNodes: any;
    }>(),
    {
      rowData: () => ({}),
      treeData: () => [],
      currentNodes: () => [],
      isView: false,
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

  const title = ref<string>(t('编辑货品批次'));

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('操作人'),
    },
  ];

  const request = async () => {
    try {
      await reqInventoryEditInventoryBatch(curFormModal.value);
      message.success(t('编辑批次成功'));
      emit('updateTable');
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = (_data: Recordable) => {
    curFormModal.value = {
      ...curFormModal.value,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      signOpen.value = true;
      const {
        cargoId,
        inventoryBatchNo,
        expiredDate,
        factoryBatchNo,
        hydration,
        licenceNo,
        noHydrationContent,
        produceDate,
        reportNo,
      } = formModal;
      curFormModal.value = {
        cargoId,
        batchNo: inventoryBatchNo,
        factoryBatchNo,
        produceDate,
        expiredDate,
        hydration,
        noHydrationContent,
        reportNo,
        licenceNo,
        batchId: props.rowData?.id,
      };
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();
  const { getCargoCategoryQueryTree, setCodeAndSpecification, setBatchNoAndDateDisable } = useCargoInfo({
    modalFormRef,
  });

  const formProps: Ref<FormProps> = ref({
    baseColProps: {
      span: 12,
    },
    // 处理日期为YYYY-MM-DD格式
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    schemas: [
      {
        field: 'cargoId',
        component: 'TreeSelect',
        label: t('货品名称'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            disabled: true,
            treeData: [],
            fieldNames: {
              value: 'id',
            },
            request: async () => {
              return await getCargoCategoryQueryTree();
            },
            showSearch: true,
            treeNodeFilterProp: 'label',
            onChange: (value: string) => {
              formInstance.setFieldsValue({
                batchNo: undefined,
                batchNoId: undefined,
                mergeCode: undefined,
                specification: undefined,
              });
              setBatchNoAndDateDisable(false);
              setCodeAndSpecification(value);
            },
          };
        },
      },
      {
        field: 'mergeCode',
        component: 'Input',
        label: t('货品编码'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'inventoryBatchNo',
        component: 'Input',
        label: t('货品批号'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'factoryBatchNo',
        component: 'Input',
        label: t('原厂批号'),
      },
      {
        field: 'produceDate',
        component: 'DatePicker',
        label: t('生产日期'),
        componentProps: {
          disabledDate: (current: any) => {
            return current && current > Date.now();
          },
        },
      },
      {
        field: 'expiredDate',
        component: 'DatePicker',
        label: t('有效期至'),
        required: true,
        componentProps: {
          disabledDate: (current: any) => {
            return current && current < Date.now() - 86400000;
          },
        },
      },
      {
        field: 'hydration',
        component: 'InputNumber',
        label: t('水分') + '(%)',
        componentProps: {
          min: 0,
          style: {
            width: '100%',
          },
        },
        dynamicRules: () => {
          return [
            {
              trigger: 'blur',
              validator: async (_rule: any, value: string) => {
                if (isEmpty(value)) {
                  return Promise.resolve();
                }
                if (isNaN(Number(value)) || Number(value) <= 0) {
                  return Promise.reject(t('请输入为正数'));
                }
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^-?\d{1,3}(\.\d{1,4})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为3位,小数位数最多为4位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'noHydrationContent',
        component: 'InputNumber',
        label: t('含量') + '(%)',
        componentProps: {
          min: 0,
          style: {
            width: '100%',
          },
        },
        dynamicRules: () => {
          return [
            {
              trigger: 'blur',
              validator: async (_rule: any, value: string) => {
                if (isEmpty(value)) {
                  return Promise.resolve();
                }
                if (isNaN(Number(value)) || Number(value) <= 0) {
                  return Promise.reject(t('请输入为正数'));
                }
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^-?\d{1,3}(\.\d{1,4})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为3位,小数位数最多为4位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'reportNo',
        component: 'Input',
        label: t('报告单编号'),
      },
      {
        field: 'licenceNo',
        component: 'Input',
        label: t('放行单编号'),
      },
      {
        field: 'supplier',
        component: 'Input',
        label: t('供应商'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'producer',
        component: 'Input',
        label: t('生产商'),
        componentProps: {
          disabled: true,
        },
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
          const { data } = await reqInventoryQueryInventoryBatchById(props.rowData?.id as string);
          modalFormRef.value?.formRef?.setFieldsValue({
            ...data,
          });
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
