<template>
  <!-- 编辑框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('编号规则')"
    :formProps="formProps"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium"
    @okModal="ok">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
      <Button type="primary" @click="ok">
        {{ t('确定') }}
      </Button>
    </template>
  </BMModalForm>
</template>
<script lang="ts" setup>
  import { BMModalForm } from '@bmos/components';
  import { reactive, ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { message, Button } from 'ant-design-vue';
  import { updateNoRules, getDictNoRulesList } from '@/services';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
    tableData: {
      type: Array,
      default: () => [],
    },
    type: {
      type: String || undefined,
      default: '',
    },
  });

  const emits = defineEmits(['updateTableData']);
  const modalFormRef = ref<any>();
  const open = ref<boolean>(false);
  const codeRuleName = ref('');
  const openModal = () => {
    open.value = true;
  };
  // 编辑的表单
  const formProps = reactive({
    initialValues: {},
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelCol: { span: 6 },
    wrapperCol: { span: 17 },
    schemas: [
      {
        field: 'productName',
        component: 'Input',
        label: t('产品名称'),
        componentProps: { disabled: true },
      },
      {
        field: 'productCode',
        component: 'Input',
        label: t('产品编码'),
        componentProps: { disabled: true },
      },
      {
        field: 'processName',
        component: 'Input',
        label: t('工艺名称'),
        componentProps: { disabled: true },
      },
      {
        field: 'codeRuleCode',
        component: 'Select',
        label: props.type == 'PRODUCT_PLAN_BATCH_NO' ? t('生产批号规则') : t('指令单编号规则'),
        required: true,
        componentProps: ({ formModel }: any) => {
          return {
            options: [],
            placeholder: t('请选择'),
            onChange: (val: any, options: any) => {
              if (options) {
                formModel.codeRuleName = options.name;
                formModel.codeRuleCode = options.value;
                codeRuleName.value = options.name;
              } else {
                //清空下拉时
                formModel.codeRuleName = '';
                formModel.codeRuleCode = '';
              }
            },
          };
        },
      },
    ],
  });
  // 弹窗确定按钮
  const ok = async () => {
    const data: any = await modalFormRef.value?.validate();
    const row = {
      processId: props.rowData.processId,
      type: props.type,
      id: props.rowData.id,
    };
    try {
      const datas = {
        codeRuleName: codeRuleName.value ? codeRuleName.value : undefined,
        ...data,
        ...row,
      };
      await updateNoRules(datas);
      emits('updateTableData');
      message.success(t('编辑成功'));
      open.value = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 获取编号规则的下拉框列表
  const getNoRulesList = async () => {
    try {
      const data = { dictId: props.type == 'PRODUCT_PLAN_BATCH_NO' ? '1729066680262463488' : '120020009002' };
      const res = await getDictNoRulesList(data);
      const datas = res.data.map((item: any) => {
        return {
          ...item,
          label: item.label + '-' + item.value,
          name: item.label,
        };
      });
      modalFormRef.value?.formRef?.updateSchema({
        field: 'codeRuleCode',
        componentProps: {
          options: datas,
        },
        label: props.type == 'PRODUCT_PLAN_BATCH_NO' ? t('生产批号规则') : t('指令单编号规则'),
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        modalFormRef.value?.formRef.setFieldsValue(props.rowData); //回显编辑框
        codeRuleName.value = props.rowData.codeRuleName;
        getNoRulesList();
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal, formProps });
</script>
<style lang="less" scoped></style>
