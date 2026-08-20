<template>
  <!-- 编辑框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('指令单编辑')"
    :rowData="props.rowData"
    :formProps="formProps"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
      <Button @click="planExtension">
        {{ t('后续计划顺延') }}
      </Button>
      <Button type="primary" @click="ok">
        {{ t('确定') }}
      </Button>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
  import { reactive, ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { message, Button, InputGroup, InputNumber, FormItemRest, Input } from 'ant-design-vue';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => ({}) as any,
    },
    tableData: {
      type: Array,
      default: () => [] as any[],
    },
  });

  const emits = defineEmits(['updateTableData']);
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const openModal = () => {
    open.value = true;
  };
  // 编辑的表单
  const formProps = reactive<any>({
    initialValues: {
      // id:''
      //默认值
      // planNo: '999',
      // gender:1,
      // test: '',
    },
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    // disabled:true,
    labelCol: { span: 6 },
    wrapperCol: { span: 17 },
    schemas: [
      {
        field: 'planNo',
        component: 'Input',
        label: t('指令单编号'),
        required: true,
      },
      {
        field: 'batchNo',
        component: 'Input',
        label: t('生产批号'),
        required: true,
        componentProps: { disabled: false },
      },
      {
        field: 'productDate',
        component: 'DatePicker',
        label: t('计划生产时间'),
        required: true,
      },
      {
        field: 'type',
        component: 'Select',
        label: t('指令单类型'),
        required: true,
        componentProps: {
          options: [
            {
              label: t('生产批次'),
              value: 'PRODUCT',
            },
            {
              label: t('实验批次'),
              value: 'EXPERIMENT',
            },
            {
              label: t('验证批次'),
              value: 'VERIFY',
            },
          ],
        },
      },
      {
        field: 'batchQuantity',
        label: t('生产批量'),
        required: true,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <InputGroup compact>
              <InputNumber
                v-model:value={formModel['batchQuantity']}
                style={{ width: '60%' }}
                stringMode={true}
                placeholder={t('请输入生产批量')}
              />
              <FormItemRest>
                <Input
                  v-model:value={formModel['unitName']}
                  style={{ width: '40%' }}
                  disabled={true}
                  placeholder={t('请输入生产批量单位')}
                />
              </FormItemRest>
            </InputGroup>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
                // 判断是否为正数
                if (Number(value) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                if (!reg.test(Number(value))) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                if (!Number(value)) {
                  return Promise.reject(t('请输入生产批量'));
                }
                if (!formModel['unitId']) {
                  return Promise.reject(t('请输入生产批量单位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
    ],
    // disabled:'',
  });
  // 后续计划顺延按钮
  const planExtension = async () => {
    const data: any = await modalFormRef.value?.validate();
    const res = { ...data, id: props.rowData?.id };
    try {
      const isPlanNoExist = props.tableData.some((item: any) => {
        return item.planNo === res.planNo && item.planNo !== props.rowData?.planNo;
      });
      const isBatchNoExist = props.tableData.some((item: any) => {
        return item.batchNo === res.batchNo && item.batchNo !== props.rowData?.batchNo;
      });
      if (isPlanNoExist) {
        return message.error(t('指令单编号已存在'));
      }
      if (isBatchNoExist) {
        return message.error(t('计划生产批号已存在'));
      }
      emits('updateTableData', res, res.id, res.productDate, 'timeExtension');
      message.success(t('编辑成功'));
      open.value = false;
    } catch (error) {}
  };

  // 弹窗确定按钮
  const ok = async () => {
    const data: any = await modalFormRef.value?.validate();
    const res = { ...data, id: props.rowData?.id };
    try {
      const isPlanNoExist = props.tableData.some((item: any) => {
        return item.planNo === res.planNo && item.planNo !== props.rowData?.planNo;
      });
      const isBatchNoExist = props.tableData.some((item: any) => {
        return item.batchNo === res.batchNo && item.batchNo !== props.rowData?.batchNo;
      });
      if (isPlanNoExist) {
        return message.error(t('指令单编号已存在'));
      }
      if (isBatchNoExist) {
        return message.error(t('计划生产批号已存在'));
      }
      emits('updateTableData', res, res.id, res.productDate);
      message.success(t('编辑成功'));
      open.value = false;
    } catch (error) {}
  };

  const resetForm = () => {
    modalFormRef.value?.resetForm();
  };

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef?.setFieldsValue(props.rowData);
        modalFormRef.value?.formRef?.setFormModels({
          unitName: props.rowData?.unitName,
          unitId: props.rowData?.unitId,
          batchQuantity: props.rowData?.batchQuantity,
        });
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal, formProps, resetForm });
</script>
<style lang="less" scoped></style>
