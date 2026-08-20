<template>
  <Row>
    <Col
      span="8"
      :style="{
        paddingRight: '16px',
      }">
      <FormItem
        :name="[props.typeField]"
        :label="t(props.label)"
        :style="props.labelStyle || { marginLeft: '34px' }"
        :labelCol="props.labelCol || { style: { width: '80px', textAlign: 'right' } }"
        :wrapperCol="props.wrapperCol || { style: { width: '100%' } }">
        <Select
          v-model:value="formModel[props.typeField]"
          :style="{ width: '100%' }"
          :fieldNames="{
            label: 'label',
            value: 'value',
          }"
          :disabled="props.viewMode"
          :placeholder="t('请选择')"
          :options="[
            { label: t('百分比'), value: 0 },
            { label: t('固定值'), value: 1 },
          ]"
          @change="handleTypeChange"></Select>
      </FormItem>
    </Col>
    <Col
      span="8"
      :style="{
        paddingRight: '8px',
        paddingLeft: '8px',
      }">
      <FormItem
        :name="[props.upperField]"
        :label="t('允差上限')"
        :labelCol="{ style: { width: '80px' } }"
        :rules="formRules"
        :wrapperCol="{ style: { width: '100%' } }">
        <InputNumber
          v-model:value="formModel[props.upperField]"
          :stringMode="true"
          :max="formModel[props.typeField] === 0 ? 100 : undefined"
          :style="{ width: '90%' }"
          :placeholder="t('请输入')"
          :addon-after="formModel[props.unitField]"
          :disabled="isUpperLowerDisabled() || props.viewMode"
          @blur="() => modalFormRef?.formRef?.validateFields([[props.upperField]])" />
      </FormItem>
    </Col>
    <Col
      span="8"
      :style="{
        paddingLeft: '16px',
      }">
      <FormItem
        :name="[props.lowerField]"
        :label="t('允差下限')"
        :labelCol="{ style: { width: '80px' } }"
        :rules="formRules"
        :wrapperCol="{ style: { width: '100%' } }">
        <InputNumber
          v-model:value="formModel[props.lowerField]"
          :stringMode="true"
          :max="formModel[props.typeField] === 0 ? 100 : undefined"
          :style="{
            width: '90%',
          }"
          :placeholder="t('请输入')"
          :addon-after="formModel[props.unitField]"
          :disabled="isUpperLowerDisabled() || props.viewMode"
          @blur="() => modalFormRef?.formRef?.validateFields([[props.lowerField]])" />
      </FormItem>
    </Col>
  </Row>
</template>

<script lang="tsx" setup>
  import { Col, FormItem, InputNumber, Row, Select } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { PropType, toRefs } from 'vue';

  const props = defineProps({
    modalFormRef: {
      type: Object as PropType<any>,
      required: true,
    },
    formModel: {
      type: Object as PropType<any>,
      required: true,
    },
    label: {
      type: String,
      required: true,
    },
    typeField: {
      type: String,
      required: true,
    },
    upperField: {
      type: String,
      required: true,
    },
    lowerField: {
      type: String,
      required: true,
    },
    unitField: {
      type: String,
      required: true,
    },
    baseUnitName: {
      type: String,
      default: '',
    },
    viewMode: {
      type: Boolean,
      default: false,
    },
    labelStyle: {
      type: Object as PropType<any>,
      default: undefined,
    },
    labelCol: {
      type: Object as PropType<any>,
      default: undefined,
    },
    wrapperCol: {
      type: Object as PropType<any>,
      default: undefined,
    },
  });

  const { formModel, typeField, upperField, lowerField, unitField, baseUnitName, modalFormRef } = toRefs(props);

  const handleTypeChange = () => {
    formModel.value[upperField.value] = undefined;
    formModel.value[lowerField.value] = undefined;
    if (formModel.value[typeField.value] === 0) {
      formModel.value[unitField.value] = '%';
    } else if (formModel.value[typeField.value] === 1) {
      formModel.value[unitField.value] = baseUnitName.value || '';
    } else {
      formModel.value[unitField.value] = '';
    }
    modalFormRef.value?.formRef?.validateFields([[upperField.value], [lowerField.value]]);
  };

  const isUpperLowerDisabled = () => {
    const type = formModel.value[typeField.value];
    return type === undefined || type === null;
  };

  const formRules: any = [
    {
      trigger: 'blur',
      validator: async (_rule: any, value: string) => {
        if (value === undefined || value === null || value === '') return Promise.resolve();
        const toleranceType = formModel.value[typeField.value];

        if (toleranceType === 1) {
          // 固定值
          if (Number(value) <= 0) {
            return Promise.reject(t('请输入正数'));
          }
          const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
          if (!reg.test(value)) {
            return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
          }
        }

        if (toleranceType === 0) {
          // 百分比
          if (Number(value) < 0) {
            return Promise.reject(t('请输入非负数'));
          }
          const reg = /^-?\d{1,3}(\.\d{1,3})?$/; // 允许整数部分最多3位，小数部分最多3位
          if (!reg.test(value)) {
            return Promise.reject(t('整数或小数位数最多为3位'));
          }
          if (Number(value) > 100) {
            return Promise.reject(t('百分比值不能超过100'));
          }
        }
        return Promise.resolve();
      },
    },
  ];

  // const commonRules = (value: string) => {
  //   if (value === undefined || value === null || value === '') return Promise.resolve();
  //   const toleranceType = formModel.value[typeField.value];

  //   if (toleranceType === 1) {
  //     // 固定值
  //     if (Number(value) <= 0) {
  //       return Promise.reject(t('请输入正数'));
  //     }
  //     const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
  //     if (!reg.test(value)) {
  //       return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
  //     }
  //   }

  //   if (toleranceType === 0) {
  //     // 百分比
  //     if (Number(value) < 0) {
  //       return Promise.reject(t('请输入非负数'));
  //     }
  //     const reg = /^-?\d{1,3}(\.\d{1,3})?$/; // 允许整数部分最多3位，小数部分最多3位
  //     if (!reg.test(value)) {
  //       return Promise.reject(t('整数或小数位数最多为3位'));
  //     }
  //     if (Number(value) > 100) {
  //       return Promise.reject(t('百分比值不能超过100'));
  //     }
  //   }
  //   return Promise.resolve();
  // };

  // const upperRules = computed(() => [
  //   {
  //     trigger: 'blur',
  //     validator: async (_rule: any, value: string) => {
  //       await commonRules(value);
  //       const lower = formModel.value[lowerField.value];
  //       if (value !== undefined && lower !== undefined && Number(value) < Number(lower)) {
  //         return Promise.reject(t('允差上限不能小于允差下限'));
  //       }
  //       return Promise.resolve();
  //     },
  //   },
  // ]);

  // const lowerRules = computed(() => [
  //   {
  //     trigger: 'blur',
  //     validator: async (_rule: any, value: string) => {
  //       await commonRules(value);
  //       const upper = formModel.value[upperField.value];
  //       if (value !== undefined && upper !== undefined && Number(value) > Number(upper)) {
  //         return Promise.reject(t('允差下限不能大于允差上限'));
  //       }
  //       return Promise.resolve();
  //     },
  //   },
  // ]);
</script>
