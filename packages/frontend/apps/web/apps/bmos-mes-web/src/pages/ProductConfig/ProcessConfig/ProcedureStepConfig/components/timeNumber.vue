<template>
  <!-- <span style="display: inline-flex; align-items: center"> -->
  <div v-if="type != 'numericalValue'" style="margin-bottom: 10px">
    {{ label }}
    <Select
      :value="limit.lowerLimit"
      style="width: 22.5%; border-radius: 5px"
      :placeholder="t('表达式')"
      allowClear
      @change="lowerLimitChange">
      <SelectOption v-if="type == 'max'" :value="0">{{ '<' }}</SelectOption>
      <SelectOption v-else :value="0">{{ '>' }}</SelectOption>
      <SelectOption v-if="type == 'max'" :value="1">{{ '<=' }}</SelectOption>
      <SelectOption v-else :value="1">>=</SelectOption>
    </Select>
  </div>
  <InputGroup v-if="limit.lowerLimit === 0 || limit.lowerLimit == 1 || type == 'numericalValue'" compact>
    <InputNumber
      type="text"
      :value="limit.day"
      style="width: 17%; border-radius: 5px"
      string-mode
      :min="0"
      @change="onDayChange" />
    <div class="time_number_title">{{ t('日') }}</div>
    <InputNumber
      type="text"
      :value="limit.hour"
      style="width: 17%; border-radius: 5px"
      string-mode
      :min="0"
      @change="onHourChange" />
    <div class="time_number_title">{{ t('时') }}</div>
    <InputNumber
      type="text"
      :value="limit.minute"
      style="width: 17%; border-radius: 5px"
      string-mode
      :min="0"
      @change="onMinuteChange" />
    <div class="time_number_title">{{ t('分') }}</div>
    <InputNumber
      type="text"
      :value="limit.second"
      style="width: 17%; border-radius: 5px"
      string-mode
      :min="0"
      @change="onSecondChange" />
    <div class="time_number_title">{{ t('秒') }}</div>
  </InputGroup>
  <!-- </span> -->
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import type { PropType } from 'vue';
  import { Form } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { ValueType } from 'ant-design-vue/es/input-number/src/utils/MiniDecimal';
  import { SelectValue } from 'ant-design-vue/es/select';

  interface ScopeValue {
    lowerLimit: number;
    day: number;
    hour: number;
    minute: number;
    second: number;
  }
  export default defineComponent({
    props: {
      limit: {
        type: Object as PropType<ScopeValue>,
        isRequired: true,
        default: () => {},
      },
      type: {
        type: String,
        default: () => 'min',
      },
      label: {
        type: String,
        default: () => t('最小值'),
      },
    },
    emits: ['update:limit'],
    setup(props, { emit }) {
      const formItemContext = Form.useInjectFormItemContext();
      const triggerChange = (changedValue: any) => {
        emit('update:limit', { ...props.limit, ...changedValue });
        formItemContext.onFieldChange();
      };
      const lowerLimitChange = (val: ValueType) => {
        triggerChange({ lowerLimit: val, day: '', hour: 0, minute: 0, second: 0 });
      };
      const onDayChange = (val: ValueType) => {
        triggerChange({ day: val });
      };

      const onHourChange = (val: ValueType) => {
        triggerChange({ hour: val });
      };

      const onMinuteChange = (val: SelectValue) => {
        triggerChange({ minute: val });
      };

      const onSecondChange = (val: SelectValue) => {
        triggerChange({ second: val });
      };

      return {
        onDayChange,
        onHourChange,
        onMinuteChange,
        onSecondChange,
        lowerLimitChange,
        t,
      };
    },
  });
</script>

<style lang="less" scoped>
  .time_number_title {
    height: 100%;
    margin: 0 5px !important;
    line-height: 35px;
  }
</style>
