<template>
  <wd-form ref="bmFormRef" class="bm-form" :class="[needPadding ? 'has-padding' : '']" :reset-on-change="getFormProps.resetOnChange" :model="formModel" :rules="dynamicRules">
    <wd-row v-bind="getRowConfig" class="bm-form-row">
      <slot name="formHeader" />
      <slot>
        <template v-for="schemaItem in formSchemasRef" :key="schemaItem.field">
          <FormItem :form-model="formModel" :schema="schemaItem" :style="{ zIndex: 99 }" @update-form-model="setFormModel">
            <template v-for="item in Object.keys($slots)" #[item]="data" :key="item">
              <slot :name="item" v-bind="data || {}" />
            </template>
          </FormItem>
        </template>
      </slot>
      <slot name="formFooter" />
    </wd-row>
  </wd-form>
</template>

<script setup>
import { useAttrs, watch } from 'vue';
import FormItem from './FormItem.vue';
import { formProps } from './formProps.js';
import { createFormContext, useFormEvents, useFormMethods, useFormState } from './hooks';

const props = defineProps(formProps);
const emit = defineEmits([
  'submit',
  'reset',
  'formModelChange',
  'register',
]);
const attrs = useAttrs();

// 表单内部状态
const formState = useFormState({ props, attrs });
const { formModel, getRowConfig, bmFormRef, getFormProps, formSchemasRef, needPadding } = formState;

// 表单内部方法
const formMethods = useFormMethods({ ...formState });
const { initFormValues, handleFormValues, setFormModels } = formMethods;

// a-form表单事件二次封装和扩展
const formEvents = useFormEvents({ ...formState, emit, handleFormValues });

// 当前组件所有的状态和方法
const instance = {
  ...formState,
  ...formEvents,
  ...formMethods,
};

const setFormModel = (val) => {
  setFormModels(val);
};
  // initialValues 变化时，更新表单数据
watch(
  () => props.initialValues,
  (val) => {
    if (val) {
      initFormValues(val);
    }
    else {
      initFormValues();
    }
  },
  {
    immediate: true,
    deep: true,
  },
);

emit('register', instance);

createFormContext(instance);

defineExpose(instance);
</script>

<style lang="scss" scoped>
  .bm-form {
  width: calc(100% - 14.06rpx);
  background-color: var(--bmos-color-white);
  .bm-form-row {
    margin-left: 0;
    margin-right: 0;
  }
}
.has-padding {
  padding-left: 7.03rpx;
  padding-right: 7.03rpx;
}
:deep(.has-error-message) {
  .wd-input {
    border-color: var(--bmos-color-error);
  }
  .wd-textarea {
    border-color: var(--bmos-color-error);
  }
}
</style>
