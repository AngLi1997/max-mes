<template>
  <Row>
    <Col span="24">
      <FormItem
        :name="[props.quantityValueField]"
        :label="t('数量')"
        :labelCol="props.labelCol || { style: { width: '80px' } }"
        :rules="quantityRules">
        <InputGroup compact>
          <Select
            v-model:value="formModel[props.quantityTypeField]"
            :style="{ width: '40%' }"
            :fieldNames="{
              label: 'name',
              value: 'value',
            }"
            :disabled="props.viewMode"
            :placeholder="t('请选择类型')"
            :options="[
              { name: t('标准量'), value: 0 },
              { name: t('固定量'), value: 1 },
              { name: t('适量'), value: 2 },
            ]"
            @change="handleQuantityTypeChange"></Select>
          <FormItemRest>
            <InputNumber
              v-model:value="formModel[props.quantityValueField]"
              :stringMode="true"
              :disabled="isQuantityValueDisabled() || props.viewMode"
              :style="{ width: '60%' }"
              :placeholder="t('请输入数量')"
              @blur="() => modalFormRef?.formRef?.validateFields([[props.quantityValueField]])" />
          </FormItemRest>
        </InputGroup>
      </FormItem>
    </Col>
  </Row>
</template>

<script lang="tsx" setup>
  import { Col, FormItem, InputGroup, InputNumber, Row, Select, FormItemRest } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { computed, PropType, toRefs } from 'vue';

  const props = defineProps({
    modalFormRef: {
      type: Object as PropType<any>,
      required: true,
    },
    formModel: {
      type: Object as PropType<any>,
      required: true,
    },
    quantityTypeField: {
      type: String,
      required: true,
    },
    quantityValueField: {
      type: String,
      required: true,
    },
    viewMode: {
      type: Boolean,
      default: false,
    },
    labelCol: {
      type: Object as PropType<any>,
      default: undefined,
    },
  });

  const { formModel, quantityTypeField, quantityValueField, modalFormRef } = toRefs(props);

  const handleQuantityTypeChange = () => {
    formModel.value[quantityValueField.value] = undefined;
    modalFormRef.value?.formRef?.validateFields([[quantityValueField.value]]);
  };

  const isQuantityValueDisabled = () => {
    return formModel.value[quantityTypeField.value] === 2; // 适量时禁用
  };

  const quantityRules = computed(() => [
    {
      required: true,
      trigger: 'blur',
      validator: async (_rule: any) => {
        if (formModel.value[quantityTypeField.value] === undefined) return Promise.reject(t('请选择类型'));
        if (!formModel.value[quantityValueField.value] && formModel.value[quantityTypeField.value] !== 2) {
          return Promise.reject(t('请输入数量'));
        }
        if (formModel.value[quantityTypeField.value] !== 2) {
          if (Number(formModel.value[quantityValueField.value]) <= 0) {
            return Promise.reject(t('请输入正数'));
          }
          const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
          if (!reg.test(formModel.value[quantityValueField.value])) {
            return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
          }
        }
        return Promise.resolve();
      },
    },
  ]);
</script>
