<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验项目编辑')"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
  import { FormItemRest, message } from 'ant-design-vue';
  import { postStaticDataConfigInspectRuleEdit } from '@/services';
  import RoundRuleTable from './RoundRuleTable.vue';
  import PassStandardTable from './PassStandardTable.vue';
  import { PassStandardTypeEnum } from '@/types';
  import { isEmpty } from '@bmos/utils';

  defineOptions({
    inheritAttrs: false,
  });

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);

  const props = withDefaults(
    defineProps<{
      rowData?: Recordable;
    }>(),
    {
      rowData: () => ({}),
    },
  );

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      try {
        if (val) {
          const passStandards = [JSON.parse(props.rowData?.passStandard || '{}')];
          const roundingRules = [JSON.parse(props.rowData?.roundingRule || '{}')];
          roundingRules[0].digits = parseInt(roundingRules[0].digits);
          modalFormRef.value?.formRef?.setFormModels({
            ...props.rowData,
            roundingRules,
            passStandards,
            tabKey: isEmpty(passStandards[0].textOne) ? PassStandardTypeEnum.NUMBER : PassStandardTypeEnum.TEXT,
          });
        }
      } catch (error) {
        //
      }
    },
  );
  const formProps = reactive<FormProps>({
    schemas: [
      {
        field: 'itemNo',
        component: 'Input',
        label: t('检验项目编号'),
        labelWidth: '100',
        componentProps: {
          disabled: true,
        },
        colProps: {
          span: 12,
        },
      },
      {
        field: 'itemName',
        component: 'Input',
        label: t('检验项目名称'),
        labelWidth: '100',
        componentProps: {
          disabled: true,
        },
        colProps: {
          span: 12,
        },
      },
      {
        field: 'field6',
        component: 'Divider',
        label: t('标准规定'),
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
        field: 'standard',
        label: t('标准规定'),
        component: 'Input',
        required: true,
      },
      {
        field: 'field7',
        component: 'Divider',
        label: t('修约规则'),
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
        field: 'roundingRules',
        noLabel: true,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <RoundRuleTable v-model:tableList={formModel.roundingRules} />
              </FormItemRest>
            </>
          );
        },
      },
      {
        field: 'field8',
        component: 'Divider',
        label: t('通过标准'),
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
        field: 'passStandards',
        noLabel: true,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <PassStandardTable v-model:tableList={formModel.passStandards} v-model:tabKey={formModel.tabKey} />
              </FormItemRest>
            </>
          );
        },
      },
    ],
  });

  const submit = async (formModal: Recordable) => {
    try {
      const { itemNo, itemName, standard, roundingRules, passStandards, tabKey } = formModal;
      const roundingRule = roundingRules[0];
      const passStandard = passStandards[0];
      await postStaticDataConfigInspectRuleEdit({
        itemNo,
        itemName,
        standard,
        ruleDetail: {
          name: roundingRule?.name,
          ruleCode: roundingRule?.ruleCode,
          roundValue: roundingRule?.roundValue,
          digits: roundingRule?.digits,
        },
        inspectItemNo: itemNo,
        details:
          tabKey === PassStandardTypeEnum.NUMBER
            ? {
                name: passStandard?.name,
                labelOne: passStandard?.labelOne,
                valueOne: passStandard?.valueOne,
                labelTwo: passStandard?.labelTwo,
                valueTwo: passStandard?.valueTwo,
              }
            : {
                name: passStandard?.name,
                textOne: passStandard?.textOne,
                textValueOne: passStandard?.textValueOne,
              },
      });
      emit('ok');
      message.success(`${t('编辑')}${t('成功')}`);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>

<style scoped lang="less"></style>
