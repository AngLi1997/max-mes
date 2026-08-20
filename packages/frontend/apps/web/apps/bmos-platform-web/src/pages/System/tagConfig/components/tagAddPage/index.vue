<template>
  <div class="add-code-rule">
    <Row class="add-code-rule-header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item @click="back">{{ t('标签管理') }}</breadcrumb-item>
          <breadcrumb-item>{{ t(modelName[props.state]) }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <Button @click="back">{{ t('返回') }}</Button>
          <Button type="primary" v-if="props.state !== modalStatus.View" @click="save(props.state)">
            {{ t('保存') }}
          </Button>
        </Space>
      </Col>
    </Row>
    <div class="code-rule-form">
      <BMForm
        ref="TagQueryForm"
        v-bind="{
          initialValues: fromProps.initialValues,
          schemas: fromProps?.schemas as FormProps['schemas'], 
          disabled: props.state === modalStatus.View,
          labelWidth: 120,
          baseColProps: {
            span: 8,
          },
          autoAdvancedLine: 3,
          alwaysShowLines: 3,
          showActionButtonGroup: false,
          }" />
    </div>
    <Row class="code-rule-data">
      <Col :span="12" class="gutter-row" v-html="tagStyleContent"></Col>
      <Col :span="12" class="gutter-form">
        <BMForm
          ref="TagInitialStyle"
          v-bind="{
          disabled: props.state === modalStatus.View,
          schemas: toProps?.schemas as FormProps['schemas'],
          showActionButtonGroup: false,
        }" />
      </Col>
    </Row>
  </div>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMForm, FormProps, Recordable } from '@bmos/components';
  import { useFrom } from './hooks';
  import { modalStatus, modelName } from '../../types';
  const emits = defineEmits(['back']);
  const props = withDefaults(
    defineProps<{
      keyTreeData: Recordable;
      clickOnRow: Recordable;
      treeDataId: string;
      state: modalStatus;
    }>(),
    {},
  );
  const { fromProps, toProps, TagQueryForm, tagStyleContent, TagInitialStyle, tagTypeGET, save, back, stateType } =
    useFrom({
      emits,
    });

  watch(
    () => props.state,
    val => {
      stateType(val, props.clickOnRow);
      tagTypeGET(props.keyTreeData, props.treeDataId);
    },
    {
      immediate: true,
      deep: true,
    },
  );
</script>

<style lang="less" scoped>
  .add-code-rule {
    width: 100%;
    height: 100%;
    .add-code-rule-header {
      padding: 4px 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
        li {
          cursor: pointer;
        }
      }
      .action {
        text-align: right;
      }
    }
    .code-rule-form {
      background-color: #fff;
      padding: var(--bmos-padding-small) var(--bmos-padding-small) 0 var(--bmos-padding-small);
      margin-bottom: var(--bmos-margin-small);
    }
    .code-rule-data {
      background-color: #fff;
      height: calc(100% - 68px - 36px - 76px);
      padding: var(--bmos-padding-small);
      .gutter-row {
        background-color: #e6e6e6;
        height: 100%;
        overflow: auto;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .gutter-form {
        position: relative;
        height: 100%;
        padding: 0 var(--bmos-padding-small);
        overflow: auto;
      }
      :deep(.from-col) {
        padding-right: 0 !important;
      }
    }
  }
</style>
