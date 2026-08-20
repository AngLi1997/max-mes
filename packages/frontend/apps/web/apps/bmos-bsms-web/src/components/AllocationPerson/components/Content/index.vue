<template>
  <div class="common shuttle-content">
    <div class="shuttle-content-top">
      <Row justify="space-between" align="middle" style="height: 100%">
        <Col :span="8">
          <span>{{ title }}</span>
          <span style="margin-left: 20px; color: #909398">
            {{ count }}
          </span>
        </Col>
        <Col v-if="!isView">
          <slot name="icon" v-if="icon">
            <Button type="link" @click="handleIconClick" class="clear-btn">{{t('清除')}}</Button>
          </slot>
        </Col>
      </Row>
    </div>
    <div class="common shuttle-content-detail">
      <div>
        <slot></slot>
      </div>
    </div>
    <!-- 搜索树 -->
  </div>
</template>

<script setup lang="ts">
  import { ContentProps, ContentEmits } from '../props/content';
  import { Row, Button } from 'ant-design-vue';
  import { useContent } from '../hooks/useContent';
  import { t } from '@bmos/i18n';

  const emits = defineEmits(ContentEmits);
  defineProps(ContentProps);
  const { handleIconClick } = useContent(emits);
</script>

<style scoped lang="less">
  .shuttle-content {
    box-sizing: border-box;
  }
  .shuttle-content-top {
    background-color: #fafafa;
    line-height: 1;
    height: 40px;
    padding-block: 2px;
    padding-inline: 16px;
  }
  .clear-btn{
    padding: 0;
  }
  .shuttle-content-detail {
    padding-inline: 16px;
    padding-block: 16px;
    height: calc(100% - 40px);
    overflow: auto;
    padding-bottom: 0;
    padding-top: 0;
    width: 100%;
    position: relative;
  }
</style>
