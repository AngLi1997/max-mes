<template>
  <Breadcrumb>
    <BreadcrumbItem>
      <a v-if="implement === '1'" @click="$router.push('/Implement/record-manage')">
        {{ t('记录管理') }}
      </a>
      <a v-else @click="$router.push('/product-config')">
        {{ t('记录配置') }}
      </a>
    </BreadcrumbItem>
    <BreadcrumbItem>{{ page }}</BreadcrumbItem>
  </Breadcrumb>
</template>

<script setup lang="ts">
  import { Breadcrumb, BreadcrumbItem } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  defineProps({
    page: {
      type: String,
      default: t('记录编辑'),
    },
    implement: {
      type: String,
      default: '2',
    },
  });
</script>

<style scoped lang="less"></style>
