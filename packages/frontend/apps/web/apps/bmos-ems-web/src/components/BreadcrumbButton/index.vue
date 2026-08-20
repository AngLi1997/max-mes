<template>
  <div class="container">
    <Row class="components-header">
      <Col :span="8">
        <slot name="breadcrumb"></slot>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <slot name="btns"></slot>
        </Space>
      </Col>
    </Row>
    <div class="content">
      <slot></slot>
    </div>
  </div>
</template>
<script setup></script>
<style lang="less" scoped>
  .container {
    width: 100%;
    height: 100%;
    position: relative;
    padding: 0;
  }
  :deep(.components-header) {
    padding: 0 0 var(--bmos-padding-mini) 0;
    .ems-breadcrumb {
      line-height: 36px;
    }
    .ems-breadcrumb ol li:not(:last-child) {
      cursor: pointer;
    }
    .action {
      text-align: right;
    }
  }
  .content {
    width: 100%;
    height: calc(100% - 56px);
    background-color: var(--bmos-primary-color-white);
    padding: var(--bmos-padding-mini);
    padding-bottom: 0;
    display: flex;
    flex-direction: column;
  }
</style>
