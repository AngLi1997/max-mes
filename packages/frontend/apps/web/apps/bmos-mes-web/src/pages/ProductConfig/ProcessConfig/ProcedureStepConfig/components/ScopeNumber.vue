<template>
  <InputGroup compact>
    <InputNumber
      type="text"
      :value="limit.scopeMin"
      style="width: 22.5%"
      string-mode
      :placeholder="t('最小值')"
      @change="onMinChange" />
    <Select :value="limit.lowerLimit" style="width: 22.5%" :placeholder="t('表达式')" @change="lowerLimitChange">
      <SelectOption :value="0">{{ '<' }}</SelectOption>
      <SelectOption :value="1">{{ '<=' }}</SelectOption>
    </Select>
    <div class="delimiter">{{ t('值') }}</div>
    <Select :value="limit.upperLimit" style="width: 22.5%" :placeholder="t('表达式')" @change="upperLimitChange">
      <SelectOption :value="0">{{ '<' }}</SelectOption>
      <SelectOption :value="1">{{ '<=' }}</SelectOption>
    </Select>
    <InputNumber
      type="text"
      :value="limit.scopeMax"
      :placeholder="t('最大值')"
      style="width: 22.5%"
      string-mode
      @change="onMaxChange" />
  </InputGroup>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import type { PropType } from 'vue';
  import { Form } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { InputGroup, InputNumber, Select, SelectOption } from 'ant-design-vue';
  import { ValueType } from 'ant-design-vue/es/input-number/src/utils/MiniDecimal';
  import { SelectValue } from 'ant-design-vue/es/select';

  interface ScopeValue {
    scopeMin: string;
    scopeMax: string;
    lowerLimit: number;
    upperLimit: number;
  }
  export default defineComponent({
    // eslint-disable-next-line vue/no-unused-components
    components: { AInputGroup: InputGroup, AInputNumber: InputNumber, ASelect: Select, SelectOption },
    props: {
      limit: {
        type: Object as PropType<ScopeValue>,
        isRequired: true,
        default: () => {},
      },
    },
    emits: ['update:limit'],
    setup(props, { emit }) {
      const formItemContext = Form.useInjectFormItemContext();
      const triggerChange = (changedValue: {
        scopeMin?: string;
        scopeMax?: string;
        lowerLimit?: SelectValue;
        upperLimit?: SelectValue;
      }) => {
        emit('update:limit', { ...props.limit, ...changedValue });
        formItemContext.onFieldChange();
      };
      const onMinChange = (val: ValueType) => {
        triggerChange({ scopeMin: val as string });
      };

      const onMaxChange = (val: ValueType) => {
        triggerChange({ scopeMax: val as string });
      };

      const lowerLimitChange = (val: SelectValue) => {
        triggerChange({ lowerLimit: val });
      };

      const upperLimitChange = (val: SelectValue) => {
        triggerChange({ upperLimit: val });
      };

      return {
        onMinChange,
        onMaxChange,
        lowerLimitChange,
        upperLimitChange,
        t,
      };
    },
  });
</script>

<style lang="less" scoped>
  .delimiter {
    width: 10%;
    text-align: center;
    line-height: 34px;
    background-color: var(--bmos-disable-color);
    border-top: 1px solid var(--bmos-first-level-border-color);
    border-bottom: 1px solid var(--bmos-first-level-border-color);
  }
</style>
