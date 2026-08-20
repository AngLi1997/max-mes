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
    :signatureAction="29"
    :userList="permissionCodeUserList"
    :labelList="labelList"
    @signSuccess="signSuccess"></Sign>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, Recordable, RenderCallbackParams } from '@bmos/components';
  import { FormItemRest, InputGroup, InputNumber, Select, Space, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import Sign from '@/components/Sign';
  import { LabelList } from '@/components/Sign/type';
  import { useCargoInfo } from '../hooks';
  import { StorageLevel } from '../types';
  import { usePermissionCodeUserList } from '@/hooks';
  import { cloneDeep, isEmpty, loopSelectableNotValueTree } from '@bmos/utils';
  import { reqStorageInventoryInbound } from '@/services';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
      treeData?: Recordable;
      treeNode?: Recordable;
    }>(),
    {
      rowData: () => ({}),
      treeData: () => ({}),
      treeNode: () => ({}),
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

  const title = ref<string>(t('货品入库'));

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('接收人'),
    },
    {
      label: t('递交人'),
    },
  ];
  const { getPermissionCodeUserList, permissionCodeUserList } = usePermissionCodeUserList();

  const request = async () => {
    try {
      await reqStorageInventoryInbound(curFormModal.value);
      message.success(t('入库成功'));
      emit('updateTable');
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = (data: Recordable) => {
    const { receiverId, submitterId } = data;
    curFormModal.value = {
      ...curFormModal.value,
      receiverId,
      senderId: submitterId,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      await getPermissionCodeUserList('150020001000007');
      signOpen.value = true;
      const {
        cargoId,
        batchNo,
        factoryBatchNo,
        expiredDate,
        hydration,
        linkExplain,
        noHydrationContent,
        oddQuantity,
        oddUnitId,
        positionId,
        produceDate,
        singleQuantity,
        singleUnitId,
        size,
      } = formModal;
      curFormModal.value = {
        cargoId,
        batchNo,
        factoryBatchNo,
        expiredDate,
        hydration,
        linkExplain,
        noHydrationContent,
        oddQuantity,
        oddUnitId,
        positionId,
        produceDate,
        singleQuantity,
        singleUnitId,
        size,
      };
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();
  const {
    getCargoCategoryQueryTree,
    setCodeAndSpecification,
    setBatchNoAndDateDisable,
    getBatchNumberOptions,
    unitOptions,
  } = useCargoInfo({
    modalFormRef,
  });

  const formProps: Ref<FormProps> = ref({
    baseColProps: {
      span: 11,
    },
    // 处理日期为YYYY-MM-DD格式
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    schemas: [
      {
        field: 'cargoInfo',
        component: 'Divider',
        label: t('货品信息'),
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
        field: 'cargoId',
        component: 'TreeSelect',
        label: t('货品名称'),
        required: true,
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
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
              formInstance.setFormModels({
                batchNo: undefined,
                batchNoId: undefined,
                singleQuantity: undefined,
                singleUnitId: undefined,
                singleUnit: undefined,
                mergeCode: undefined,
                specification: undefined,
                oddQuantity: undefined,
                oddUnitId: undefined,
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
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'specification',
        component: 'Input',
        label: t('货品规格'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'batchNo',
        component: 'AutoComplete',
        label: t('货品批次'),
        required: true,
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            options: [],
            onSearch: async (value: string) => {
              value && getBatchNumberOptions(value, formModel['cargoId']);
            },
            onChange: () => {
              formModel['batchNoId'] = undefined;
              setBatchNoAndDateDisable(false);
            },
            onSelect: (value: string, option: Recordable) => {
              setBatchNoAndDateDisable(true);
              formModel['batchNoId'] = option.id;
              formInstance?.setFieldsValue({
                factoryBatchNo: option.factoryBatchNo,
                produceDate: option.produceDate,
                expiredDate: option.expiredDate,
              });
            },
          };
        },
      },
      {
        field: 'factoryBatchNo',
        component: 'Input',
        label: t('原厂批号'),
        componentProps: {
          disabled: false,
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
        field: 'expiredDate',
        component: 'DatePicker',
        label: t('有效期至'),
        required: true,
        componentProps: {
          disabledDate: (current: any) => {
            return current && current < Date.now() - 86400000;
          },
          style: {
            width: '100%',
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
                if (isNaN(Number(value)) || Number(value) < 0) {
                  return Promise.reject(t('请输入为非负数'));
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
                if (isNaN(Number(value)) || Number(value) < 0) {
                  return Promise.reject(t('请输入为非负数'));
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
        field: 'positionId',
        component: 'TreeSelect',
        label: t('接收货位'),
        required: true,
        componentProps: {
          fieldNames: {
            value: 'id',
            label: 'name',
          },
          showSearch: true,
          treeNodeFilterProp: 'name',
          dropdownMatchSelectWidth: 340,
        },
      },
      {
        field: 'inboundInfo',
        component: 'Divider',
        label: t('入库信息'),
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
        field: 'singleQuantity',
        label: t('单件量'),
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <InputGroup compact>
              <InputNumber
                v-model:value={formModel['singleQuantity']}
                stringMode={true}
                style={{ width: '60%' }}
                placeholder={t('请输入单件量')}
              />
              <FormItemRest>
                <Select
                  v-model:value={formModel['singleUnitId']}
                  style={{ width: '40%' }}
                  placeholder={t('请输入单位')}
                  options={unitOptions.value}
                  dropdownMatchSelectWidth={300}
                  fieldNames={{
                    label: 'name',
                    value: 'unitId',
                  }}
                  onChange={(value: any, option: any) => {
                    formModel['singleUnit'] = option.name;
                  }}>
                  {{
                    option: (option: any) => {
                      return (
                        <div class='flex-between'>
                          <span>{option.name}</span>
                          <span class='fourth-level-text'>{option.expression}</span>
                        </div>
                      );
                    },
                  }}
                </Select>
              </FormItemRest>
            </InputGroup>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: (_: any, value: any) => {
                if (isNaN(Number(value)) || Number(value) < 0) {
                  return Promise.reject(t('请输入为非负数'));
                }
                // 整数部分最多为10位,小数位数最多为9位
                const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                if (!formModel['singleUnitId']) {
                  return Promise.reject(t('请输入单位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'size',
        component: 'InputNumber',
        componentProps: {
          min: 1,
          max: 99,
          precision: 0,
          style: {
            width: '100%',
          },
        },
        label: t('入库件数'),
        required: true,
      },
      {
        field: 'oddQuantity',
        label: t('零头'),
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <InputGroup compact>
              <InputNumber
                v-model:value={formModel['oddQuantity']}
                stringMode={true}
                style={{ width: '60%' }}
                placeholder={t('请输入零头量')}
              />
              <FormItemRest>
                <Select
                  v-model:value={formModel['oddUnitId']}
                  style={{ width: '40%' }}
                  placeholder={t('请输入零头单位')}
                  options={unitOptions.value}
                  fieldNames={{
                    label: 'name',
                    value: 'unitId',
                  }}
                  dropdownMatchSelectWidth={300}
                  onChange={(value: any, option: any) => {
                    formModel['oddUnit'] = option.name;
                    modalFormRef.value?.formRef?.validateFields(['oddQuantity']);
                  }}>
                  {{
                    option: (option: any) => {
                      return (
                        <div class='flex-between'>
                          <span>{option.name}</span>
                          <span class='fourth-level-text'>{option.expression}</span>
                        </div>
                      );
                    },
                  }}
                </Select>
              </FormItemRest>
            </InputGroup>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: formModel['oddUnitId'] || formModel['oddQuantity'] ? true : false,
              trigger: 'blur',
              validator: (_: any, value: any) => {
                if (formModel['oddUnitId']) {
                  if (isNaN(Number(value)) || Number(value) < 0) {
                    return Promise.reject(t('请输入为非负数'));
                  }
                  // 整数部分最多为10位,小数位数最多为9位
                  const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                  if (!reg.test(value)) {
                    return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                  }
                }
                if (formModel['oddQuantity']) {
                  if (!formModel['oddUnitId']) {
                    return Promise.reject(t('请输入零头单位'));
                  }
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'total',
        label: '',
        noLabel: true,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <Space>
              <span>
                {t('总件数')}：
                <span style={{ color: 'var(--bmos-primary-color)' }}>
                  {Number(formModel['size'] || 0) + (formModel['oddQuantity'] ? 1 : 0)}
                </span>
              </span>
              <span>
                {t('总计')}：
                <span style={{ color: 'var(--bmos-primary-color)' }}>
                  {Number(formModel['size'] || 0) * Number(formModel['singleQuantity'] || 0)}
                  {formModel['singleUnit']} + {formModel['oddQuantity'] || 0}
                  {formModel['oddUnit']}
                </span>
              </span>
            </Space>
          );
        },
      },
      {
        field: 'linkExplain',
        component: 'InputTextArea',
        label: t('来源去向'),
        colProps: {
          span: 22,
        },
        required: true,
      },
    ],
  });

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        modalFormRef.value?.formRef?.updateSchema([
          {
            field: 'positionId',
            componentProps: {
              treeData: loopSelectableNotValueTree(
                cloneDeep(props.treeData) as Record<string, any>[],
                'level.value',
                StorageLevel.POSITION,
              ),
            },
          },
        ]);
        if (props.treeNode && props.treeNode?.id !== 'all' && props.treeNode?.level?.value === StorageLevel.POSITION) {
          modalFormRef.value?.formRef?.setFieldsValue({
            positionId: props.treeNode.id,
          });
        }
        try {
        } catch (error) {}
      } else {
        unitOptions.value = [];
      }
    },
  );
</script>
