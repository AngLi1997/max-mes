<template>
  <div class="table-title">
    <BMForm ref="formRef" v-bind="formProps" />
  </div>
</template>

<script setup lang="tsx">
  import { BMForm, RenderCallbackParams, FormProps, formInstance } from '@bmos/components';

  import { BMIcons } from '@bmos/icons';

  const props = defineProps({
    options: {
      type: Array,
      default: () => [],
    },
    modelValue: {
      type: String,
      default: '',
    },
    itemIndex: {
      type: String,
      default: '',
    },
  });
  const emit = defineEmits(['deleteEnvProperty', 'change']);
  const formRef = ref<formInstance>();
  const formProps = ref<FormProps>({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    schemas: [
      {
        field: 'envPropertyCode',
        component: 'Select',
        label: t('环境参数'),
        required: true,
        colProps: {
          span: 8,
        },
        componentProps: () => {
          return {
            onChange: (val: string) => {
              emit('change', val);
            },
          };
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          return [
            {
              message: t('请选择环境参数'),
              trigger: 'change',
              required: true,
              validator: () => {
                if (!formModel['envPropertyCode']) {
                  return Promise.reject(t('请选择环境参数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'itemIndex',
        colProps: {
          span: 16,
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <div class='delete-button'>
                <BMIcons
                  class='delete-icon'
                  icon='Delete'
                  onClick={() => {
                    emit('deleteEnvProperty', formModel.itemIndex);
                  }}
                />
              </div>
            </>
          );
        },
      },
    ],
  });

  watch(
    () => props.options,
    newVal => {
      nextTick(() => {
        formRef.value?.updateSchema([
          {
            field: 'envPropertyCode',
            componentProps: {
              options: newVal,
            },
          },
        ]);
      });
    },
    {
      immediate: true,
      deep: true,
    },
  );
  watch(
    () => [props.itemIndex, props.modelValue],
    () => {
      nextTick(() => {
        formRef.value?.setFormModels({
          itemIndex: props.itemIndex,
          envPropertyCode: props.modelValue,
        });
      });
    },
    {
      immediate: true,
    },
  );
  defineExpose({
    validateForm: () => {
      formRef.value?.validate();
    },
  });
</script>

<style lang="less" scoped>
  .table-title {
    width: 100%;
    padding: 8px 20px;
    border-bottom: 1px solid #e1e3e5;
    background-color: #fafafa;
    :deep(.delete-button) {
      display: flex;
      justify-content: flex-end;
      color: var(--bmos-danger-color);
    }
    :deep(.ems-form-item) {
      margin-bottom: 0;
    }
  }
</style>
