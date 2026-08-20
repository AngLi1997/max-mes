<template>
  <div class="config-form-container">
    <template v-if="getConfigRef.formKeys">
      <template v-if="!getConfigRef.notShowTitle && getConfigRef.title">
        <BMTableTitle :title="getConfigRef.title" />
      </template>
      <BMForm
        :key="getConfigRef?.formKeys"
        ref="formRef"
        :class="['form', getConfigRef?.notShowTitle ? 'no-title' : '']"
        v-bind="formProps"></BMForm>
      <div class="form-footer">
        <Space>
          <Button @click="cancelForm">
            {{ t('取消') }}
          </Button>
          <Button v-if="!isCurView" @click="clearForm">
            {{ t('清除') }}
          </Button>
          <Button v-if="!isCurView" type="primary" @click="submitForm">
            {{ t('确定') }}
          </Button>
        </Space>
      </div>
    </template>
    <Empty v-else />
  </div>
</template>

<script setup lang="tsx">
  import type { ConfigFormProps } from '../types';
  import { useForm } from '../hooks';
  import { BMForm, BMTableTitle } from '@bmos/components';
  import { Button, Space } from 'ant-design-vue';
  import { formEmits } from '../types';
  import { computed } from 'vue';
  import { PROCESS_STATE } from '../../enum';
  import { useRoute } from 'vue-router';
  import { t } from '@bmos/i18n';

  const emit = defineEmits(formEmits);

  const props = withDefaults(defineProps<ConfigFormProps>(), {
    activeComponentType: null,
    isView: false,
    procedureId: '',
    versionId: '',
    configList: () => [],
    activeNodeData: () => ({}),
    nodeList: () => [],
  });
  // 获取路由上的 query 参数
  const route = useRoute();
  const { status } = route.query;

  const isCurView = computed(() => {
    return status === PROCESS_STATE.VIEW_VERSION || props.isView;
  });
  const formConfig = useForm({ props, emit, isCurView });
  const { formRef, getConfigRef, formProps, submitForm, cancelForm, clearForm } = formConfig;

  watch(
    () => isCurView.value,
    async (val: boolean) => {
      await nextTick();
      if (val) {
        formRef.value?.setFormProps({
          disabled: true,
        });
      }
    },
    {
      immediate: true,
    },
  );

  defineExpose({
    ...formConfig,
  });
</script>

<style lang="less" scoped>
  .config-form-container {
    height: 100%;
    display: flex;
    flex-direction: column;
    .form-header {
      display: inline-block;
      border-left: 3px solid var(--bmos-primary-color);
      line-height: 14px;
      padding-left: 4px;
    }
    .form {
      flex: 1;
      overflow: hidden;
      overflow-y: auto;
    }
    .no-title {
      margin-top: 0;
    }
    .form-footer {
      width: 100%;
      display: flex;
      justify-content: flex-end;
    }
  }
  :deep(.mes-input-number) {
    width: 100%;
  }
  :deep(.scope-label) {
    text-align: center;
    line-height: 36px;
  }

  :deep(.mes-radio-button-wrapper) {
    height: 36px;
    line-height: 36px;
    border-radius: 6px;
    width: 70px;
    text-align: center;
    border-inline-start-width: 1px;
    &:hover {
      border-color: var(--bmos-primary-color);
    }
  }
  :deep(.waring-false) {
    margin-left: var(--bmos-module-margin-medium);
  }
  :deep(.mes-radio-button-wrapper:not(:first-child)::before) {
    display: none;
  }
  :deep(.delete-option-icon) {
    display: inline-flex;
    justify-content: center;
    align-items: center;
    width: 14%;
    height: 36px;
    display: none;
  }
  :deep(.show-delete-option-icon) {
    visibility: visible;
    display: inline-flex;
  }

  :deep(.add-icon) {
    cursor: pointer;
  }
  :deep(.condition-label) {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 342px;
    z-index: 101;
    background: #fff;
    top: 0;
  }
</style>
