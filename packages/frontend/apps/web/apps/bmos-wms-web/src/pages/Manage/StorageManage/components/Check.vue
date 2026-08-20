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
    :userList="permissionCodeUserList"
    :signatureAction="32"
    :labelList="labelList"
    @signSuccess="signSuccess"></Sign>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams, Recordable } from '@bmos/components';
  import { FormItemRest, Input, InputGroup, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import Sign from '@/components/Sign';
  import { LabelList } from '@/components/Sign/type';
  import { usePermissionCodeUserList } from '@/hooks';
  import { reqInventoryCheck, reqInventoryQueryInventoryById } from '@/services';

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

  const title = ref<string>(t('货品盘点'));

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('盘点人'),
    },
    {
      label: t('复核人'),
    },
  ];

  const { getPermissionCodeUserList, permissionCodeUserList } = usePermissionCodeUserList();

  const request = async () => {
    try {
      await reqInventoryCheck(curFormModal.value);
      emit('updateTable');
      message.success(t('盘点成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const signSuccess = (data: Recordable) => {
    const { receiverId, submitterId } = data;
    curFormModal.value = {
      ...curFormModal.value,
      checkerId: receiverId,
      reCheckerId: submitterId,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      await getPermissionCodeUserList('150020001000009');
      signOpen.value = true;
      const { availableQuantity, consumeQuantity, initQuantity, remark, useUp } = formModal;
      curFormModal.value = {
        availableQuantity,
        consumeQuantity,
        initQuantity,
        remark,
        useUp,
        inventoryId: props.rowData.id,
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
        field: 'cargoName',
        component: 'Input',
        label: t('货品名称'),
        componentProps: {
          disabled: true,
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
        field: 'specification',
        component: 'Input',
        label: t('货品规格'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'batchNo',
        component: 'Input',
        label: t('货品批号'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'inventoryNo',
        component: 'Input',
        label: t('货品件号'),
        componentProps: {
          disabled: true,
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
                availableQuantity: Number(value || 0) - Number(formModel['consumeQuantity'] || 0),
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
                // 整数部分最多为10位,小数位数最多为9位
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
                availableQuantity: Number(formModel['initQuantity'] || 0) - Number(value || 0),
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
                  if (Number(formModel['initQuantity'] || 0) < Number(value || 0)) {
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
        defaultValue: {
          value: undefined,
          unit: undefined,
        },
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
          const { data } = await reqInventoryQueryInventoryById(props.rowData?.id);
          modalFormRef.value?.formRef?.setFormModels({
            ...data,
          });
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
