<template>
  <InputGroup compact>
    <InputNumber
      type="text"
      :value="limit?.lowerValue"
      style="width: 22.5%"
      string-mode
      :placeholder="t('最小值')"
      @change="onMinChange" />
    <Select :value="limit?.lowerSymbol" style="width: 22.5%" :placeholder="t('表达式')" @change="lowerLimitChange">
      <SelectOption :value="'LESS_THAN'">{{ '<' }}</SelectOption>
      <SelectOption :value="'LESS_AND_EQUAL'">{{ '<=' }}</SelectOption>
    </Select>
    <div class="delimiter">{{ t('值') }}</div>
    <Select :value="limit?.upperSymbol" style="width: 22.5%" :placeholder="t('表达式')" @change="upperLimitChange">
      <SelectOption :value="'LESS_THAN'">{{ '<' }}</SelectOption>
      <SelectOption :value="'LESS_AND_EQUAL'">{{ '<=' }}</SelectOption>
    </Select>
    <InputNumber
      type="text"
      :value="limit?.upperValue"
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
    lowerValue: string;
    upperValue: string;
    lowerSymbol: number;
    upperSymbol: number;
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
        lowerValue?: string;
        upperValue?: string;
        lowerSymbol?: SelectValue;
        upperSymbol?: SelectValue;
      }) => {
        emit('update:limit', { ...props.limit, ...changedValue });
        formItemContext.onFieldChange();
      };
      const onMinChange = (val: ValueType) => {
        triggerChange({ lowerValue: val as string });
      };

      const onMaxChange = (val: ValueType) => {
        triggerChange({ upperValue: val as string });
      };

      const lowerLimitChange = (val: SelectValue) => {
        triggerChange({ lowerSymbol: val });
      };

      const upperLimitChange = (val: SelectValue) => {
        triggerChange({ upperSymbol: val });
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
