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
  import { FormItemRest, Input, InputGroup, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import SignModal from '@/components/SignModal';
  import { LabelList } from '@/components/SignModal/type';
  import { usePositionUserList } from '../hooks';
  import { reqStorageMaterialCheck, reqStorageMaterialInfo } from '@/services';
  import { subtractNumber, ltNumber } from '@bmos/utils';

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

  const title = ref<string>(t('物料盘点'));

  const signOpen = ref<boolean>(false);

  const { getPositionUserList, curPositionId, positionUserList } = usePositionUserList();
  const labelList = computed<LabelList[]>(() => {
    return [
      {
        label: t('盘点人'),
        action: 17,
        disabled: true,
      },
      {
        label: t('复核人'),
        action: 74,
        options: positionUserList.value,
      },
    ];
  });
  const request = async () => {
    try {
      await reqStorageMaterialCheck(curFormModal.value);
      emit('updateTable');
      message.success(t('盘点成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const signSuccess = (data: Recordable) => {
    const { userId0, userId1 } = data;
    curFormModal.value = {
      ...curFormModal.value,
      checkerId: userId0,
      reCheckerId: userId1,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      await getPositionUserList('120030008000018');
      signOpen.value = true;
      const { availableQuantity, consumeQuantity, initQuantity, remark, useUp } = formModal;
      curFormModal.value = {
        availableQuantity,
        consumeQuantity,
        initQuantity,
        remark,
        useUp,
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
        label: t('盘点信息'),
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
        field: 'initQuantity',
        component: 'InputNumber',
        label: t('初始量'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            min: 0,
            stringMode: true,
            style: {
              width: '100%',
            },
            onChange: (value: any) => {
              modalFormRef.value?.formRef?.setFieldsValue({
                unit: formModel['unit'],
                availableQuantity: subtractNumber(value, formModel['consumeQuantity']),
              });
            },
          };
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
                if (!value) return Promise.reject(t('请输入初始量'));
                // 输入正数
                if (Number(value) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'consumeQuantity',
        component: 'InputNumber',
        label: t('消耗量'),
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            min: 0,
            stringMode: true,
            style: {
              width: '100%',
            },
            onChange: (value: any) => {
              modalFormRef.value?.formRef?.setFieldsValue({
                unit: formModel['unit'],
                availableQuantity: subtractNumber(formModel['initQuantity'], value),
              });
            },
          };
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
                try {
                  if (!value) return Promise.reject(t('请输入消耗量'));
                  // 如果值 整数或小数不能超过15位 则报错，否则通过
                  const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                  if (!reg.test(value)) {
                    return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                  }
                  // 如果消耗量大于初始量则报错
                  if (ltNumber(formModel['initQuantity'], value)) {
                    return Promise.reject(t('消耗量不能大于初始量'));
                  }
                  return Promise.resolve();
                } catch (error) {
                  return Promise.reject(t('请输入消耗量'));
                }
              },
            },
          ];
        },
      },
      {
        field: 'availableQuantity',
        label: t('可用量'),
        defaultValue: undefined,
        componentProps: {
          disabled: true,
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <InputGroup compact>
              <Input
                v-model:value={formModel['availableQuantity']}
                style={{ width: '60%' }}
                disabled={true}
                placeholder={t('请输入单件量')}
              />
              <FormItemRest>
                <Input
                  v-model:value={formModel['unit']}
                  style={{ width: '40%' }}
                  disabled={true}
                  placeholder={t('请输入单件量')}
                />
              </FormItemRest>
            </InputGroup>
          );
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请输入单件量'),
            },
          ];
        },
      },
      {
        field: 'useUp',
        component: 'RadioGroup',
        label: t('是否耗尽'),
        defaultValue: false,
        componentProps: {
          options: [
            {
              label: t('是'),
              value: true,
            },
            {
              label: t('否'),
              value: false,
            },
          ],
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              type: 'boolean',
              message: t('请选择是否耗尽'),
            },
          ];
        },
      },
      {
        field: 'remark',
        required: true,
        colProps: {
          span: 24,
        },
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
            ...data,
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
