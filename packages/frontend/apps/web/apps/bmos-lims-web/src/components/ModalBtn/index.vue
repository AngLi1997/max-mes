<template>
  <NormalModalForm
    :maskClosable="false"
    v-bind="$props"
    :title="title"
    :destroyOnClose="true">
    <slot></slot>
    <template #trigger>
      <slot name="trigger">
        <Button type="primary">{{ t('编辑') }}</Button>
      </slot>
    </template>
  </NormalModalForm>
</template>

<script setup lang="ts">
  import { Button } from 'ant-design-vue';
  import { NormalModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineProps({
    submit: {
      type: Function,
    },
    title: String,
  });
</script>

<style scoped lang="less"></style>
