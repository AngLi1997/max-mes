<template>
  <div
    class="state-tag"
    :class="[type ? `state-tag_${type}` : 'state-tag_default']">
    <slot>{{ t('确认') }}</slot>
  </div>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  defineProps({
    type: {
      type: String,
      default: 'primary',
    },
  });
</script>

<style scoped lang="less">
  .state-tag {
    display: flex;
    column-gap: 8px;
    align-items: center;
    color: rgba(96, 98, 102, 1);
    &::before {
      content: '';
      display: block;
      width: 6px;
      height: 6px;
      background-color: currentColor;
      border-radius: 3px;
    }
  }
  .state-tag_primary {
    color: var(--bmos-primary-color);
  }

  .state-tag_success {
    color: var(--bmos-primary-color);
  }
  .state-tag_warning {
    color: var(--bmos-warning-color-hover);
  }

  .state-tag_default {
    color: var(--bmos-fourth-level-text-color);
  }
</style>
