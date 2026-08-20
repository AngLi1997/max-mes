<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    :show-cancel-button="!isView"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
  <Sign
    v-model:open="signOpen"
    :signatureData="JSON.stringify(curFormModal)"
    :signatureAction="signatureAction"
    :labelList="labelList"
    @signSuccess="signSuccess"></Sign>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, Recordable, RenderCallbackParams } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import Sign from '@/components/Sign';
  import { LabelList } from '@/components/Sign/type';
  import { message } from 'ant-design-vue';
  import { OperationType } from '../types';
  import { useMaterialInfo } from '../hooks';
  import {
    reqStorageMaterialManageAddBatch,
    reqStorageMaterialManageEditBatch,
    reqStorageMaterialBatchFieldList,
    reqMaterialFieldInfo,
  } from '@/services';
  import { isEmpty } from '@bmos/utils';
  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
      operationType?: keyof typeof OperationType;
    }>(),
    {
      rowData: () => ({}),
      operationType: OperationType.ADD,
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

  const isView = computed(() => {
    return props.operationType === OperationType.VIEW;
  });

  const title = ref<string>(t('新增物料批次'));

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('操作人'),
    },
  ];
  const signatureAction = computed(() => {
    return props.operationType === OperationType.ADD ? 39 : 40;
  });

  const request = async () => {
    try {
      if (props.operationType === OperationType.ADD) {
        await reqStorageMaterialManageAddBatch(curFormModal.value);
        message.success(t('新增批次成功'));
      } else {
        await reqStorageMaterialManageEditBatch(curFormModal.value);
        message.success(t('编辑批次成功'));
      }
      emit('updateTable');
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = (data: Recordable) => {
    const { receiverId } = data;
    curFormModal.value = {
      ...curFormModal.value,
      operatorId: receiverId,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    if (isView.value) {
      open.value = false;
      return Promise.resolve(true);
    }
    try {
      signOpen.value = true;
      const {
        expiredDate,
        factoryBatchNo,
        hydration,
        licenceNo,
        materialId,
        noHydrationContent,
        originalBatchNo,
        produceDate,
        producer,
        reportNo,
        materialBatchNo,
        supplier,
        qualityStatus,
      } = formModal;
      const materialBatchFieldDTOList = materialFieldList.value?.map((item: any) => {
        return {
          ...item,
          fieldValue: formModal[item.field.replace('.', '*')] || '',
        };
      });
      curFormModal.value = {
        expiredDate,
        factoryBatchNo,
        hydration,
        licenceNo,
        materialId,
        noHydrationContent,
        originalBatchNo,
        produceDate,
        producer,
        reportNo,
        materialBatchNo,
        supplier,
        storageMaterialBatchId: props.rowData?.storageMaterialBatchId,
        materialBatchFieldDTOList,
        qualityStatus,
      };
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();
  const { setMaterialOptions, setCodeAndSpecification, materialFieldList } = useMaterialInfo({
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
        field: 'basicInfo',
        component: 'Divider',
        label: t('基础信息'),
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
        field: 'categoryType',
        component: 'Select',
        label: t('物料类型'),
        required: true,
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            options: [
              { label: t('原辅包'), value: 0 },
              { label: t('中间品'), value: 1 },
            ],
            onChange: (value: number) => {
              formInstance.setFieldsValue({
                materialId: undefined,
                mergeCode: undefined,
                materialSpecification: undefined,
              });
              setMaterialOptions(value);
            },
          };
        },
      },
      {
        field: 'materialId',
        component: 'TreeSelect',
        label: t('物料名称'),
        required: true,
        vIf: () => {
          return props.operationType === OperationType.ADD;
        },
        componentProps: () => {
          return {
            treeData: [],
            fieldNames: {
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'label',
            onChange: (value: string) => {
              setCodeAndSpecification(value);
            },
          };
        },
      },
      {
        field: 'materialName',
        component: 'Input',
        vIf: () => {
          return props.operationType !== OperationType.ADD;
        },
        label: t('物料名称'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'mergeCode',
        component: 'Input',
        label: t('物料编码'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'materialSpecification',
        component: 'Input',
        label: t('物料规格'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'materialBatchNo',
        component: 'Input',
        label: t('物料批号'),
        required: true,
      },
      {
        field: 'factoryBatchNo',
        component: 'Input',
        label: t('原厂批号'),
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
        field: 'hydration',
        component: 'Input',
        label: t('水分') + '(%)',
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
        component: 'Input',
        label: t('含量') + '(%)',
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
        field: 'originalBatchNo',
        component: 'Input',
        label: t('原始编码'),
      },
      {
        field: 'supplier',
        component: 'Input',
        label: t('供应商'),
      },
      {
        field: 'producer',
        component: 'Input',
        label: t('生产商'),
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
      },
      {
        field: 'qualityStatus',
        component: 'Select',
        label: t('质量状态'),
        required: true,
        defaultValue: 'QUARANTINE',
        componentProps: () => {
          return {
            options: [
              { label: t('待验'), value: 'QUARANTINE' },
              { label: t('合格'), value: 'QUALIFIED' },
              { label: t('不合格'), value: 'UNQUALIFIED' },
              { label: t('已取样'), value: 'SAMPLED' },
              { label: t('限制性放行'), value: 'RESTRICTED_RELEASE' },
            ],
          };
        },
      },
      {
        field: 'expandInfo',
        component: 'Divider',
        label: t('扩展信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
    ],
  });
  // 查看和编辑时查对应物料批次的自定义字段
  const getStorageMaterialBatchFieldList = async () => {
    const res = await reqStorageMaterialBatchFieldList(props.rowData?.storageMaterialBatchId); //物料批次id查
    const res2 = await reqMaterialFieldInfo(props.rowData?.materialId); //物料名称id查
    const temp = res.data?.map((item: any) => item.field);
    const filterRes2 = res2.data?.filter((item: any) => !temp.includes(item.field));
    const resAll = [...filterRes2, ...res.data];
    materialFieldList.value = resAll;
    resAll?.forEach((item: any, index: any) => {
      modalFormRef.value?.formRef?.appendSchemaByField({
        field: item.field.replace('.', '*'),
        component: 'Input',
        label: item.fieldName,
        defaultValue: item.fieldValue,
        componentProps: {
          disabled: item.fieldType === 'MaterialCustomFields' || item.fieldType === 'MaterialPieceCustomFields',
        },
        colProps:
          (index + 1) % 2 == 1 && index + 1 == resAll?.length
            ? {
                style: {
                  marginRight: 'auto',
                },
              }
            : {},
      });
    });
  };

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        try {
          switch (props.operationType) {
            case OperationType.ADD:
              title.value = t('新增物料批次');
              break;
            case OperationType.EDIT:
              title.value = t('编辑物料批次');
              await nextTick();
              getStorageMaterialBatchFieldList();
              modalFormRef.value?.formRef?.updateSchema([
                {
                  field: 'categoryType',
                  componentProps: {
                    disabled: true,
                  },
                },
                {
                  field: 'materialId',
                  componentProps: {
                    disabled: true,
                  },
                },
                {
                  field: 'materialBatchNo',
                  componentProps: {
                    disabled: true,
                  },
                },
              ]);
              break;
            case OperationType.VIEW:
              title.value = t('查看物料批次');
              await nextTick();
              getStorageMaterialBatchFieldList();
              modalFormRef.value?.formRef?.setFormProps({
                disabled: true,
              });
              break;
          }
          modalFormRef.value?.formRef?.setFieldsValue({
            ...props.rowData,
            qualityStatus: props.rowData?.qualityStatus ? props.rowData?.qualityStatus.value : 'QUARANTINE',
          });
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
