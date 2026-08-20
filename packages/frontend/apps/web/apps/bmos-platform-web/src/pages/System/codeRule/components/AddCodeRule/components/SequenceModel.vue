<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('流水号规则')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    class="code-rule-sequence-modal"
    @okModal="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import {
    BMModalForm,
    ModalFormType,
    FormProps,
    ModalFormInstance,
  } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { DetailsType } from '../../../types';
  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateResetRule', resetRule: any): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      tableData: any[];
      parameterIdOptions: any[];
      deleteValue: Object;
      status: boolean;
    }>(),
    {},
  );
  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });
  const resetRuleRef = ref<KEY[]>([]);
  const request = async (formModal: any) => {
    resetRuleRef.value = formModal.resetRule;
    return Promise.resolve();
  };
  const submit = async (modalFormType: ModalFormType) => {
    try {
      await modalFormType?.submit(request);
      // message.success(t('设置成功'));
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const resetRuleSet = async (data: any) =>{
    resetRuleRef.value = data || [];
  }
  const deleteType = async (data:any) =>{
     resetRuleRef.value = resetRuleRef.value.filter(item => item != data.sort);
  }
  const modalFormRef = ref<ModalFormInstance>();
  const getLabel = (record: any) => {
    switch (record.type) {
      case DetailsType.SEQUENCE:
        // 显示为起始流水号-增量-最大位数；如：0001-1-4
        return `${record.startNo}-${record.step}-${record.maxLength}`;
      case DetailsType.CONSTANT:
        return record.value;
      case DetailsType.PARAMETER:
        return (
          props.parameterIdOptions.find(
            (item: { id: string }) => item.id === record.parameterId,
          )?.label || '-'
        );
      case DetailsType.DATE:
        return [t('年'),t('月'),t('日'),t('年月'),t('年月日')][record.dateType];
      default:
        return '-';
    }
  };
  
  const formProps = reactive<FormProps>({
    initialValues: {
      resetRule:resetRuleRef
    },
    schemas: [
      {
        field: 'resetRule',
        component: 'Select',
        label: t('重置属性'),
        componentProps: () => {
          return {
            mode: 'multiple',
            maxTagCount: 1,
            maxTagTextLength: 15,
            allowClear: !props.status,
            options: props.tableData
              .filter(item => item.type !== DetailsType.SEQUENCE)
              .map((item, index) => ({
                label: `${t('序号')}${index + 1}: ${getLabel(item)}`,
                value: item.sort,
                disabled: !!props.status,
              })),
          };
        },
      },
    ],
  });
  
  defineExpose({
    resetRuleRef,
    resetRuleSet,
    deleteType
  })
  watch(
    () => open.value,
    async val => {
      await nextTick();
    },
    {
      immediate: true,
    },
  );
</script>

<style lang="less" scoped>
  .code-rule-sequence-modal {
  }
</style>
