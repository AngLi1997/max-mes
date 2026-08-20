<template>
  <div class="emphasize-container">
            <p class="popover-content">
            {{ t('点击') }}
            <BMIcons icon="InnnerAdd" class="emphasize-icon"/>
            {{ t('图标后，再') }}
            <span class="emphasize-content">{{ t('单击') }}</span>
            {{ t('右侧记录项中的组件，关联当前记录项的所需组件') }}
          </p>
          <p class="popover-content">
            {{ t('点击') }}
            <BMIcons icon="OuterAdd" class="emphasize-icon"/>
            {{ t('图标后，可从弹出框中跨批记录或记录项，关联所需组件') }}
          </p>
          </div>
</template>

<script setup lang="ts">
import { BMIcons } from '@bmos/icons'
import { t } from '@bmos/i18n';
</script>

<style scoped lang="less">
.emphasize-container{
    width: 260px;
    font-size: 12px;
    font-weight: 400;
    line-height: 20px;
    .popover-content{
      margin-bottom: .5rem;
    }
    .emphasize-icon{
      height: 12px;
      width: 12px;
      vertical-align: middle;
    }
  }
</style>