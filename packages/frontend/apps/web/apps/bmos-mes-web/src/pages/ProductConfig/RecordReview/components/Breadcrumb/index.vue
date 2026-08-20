<template>
  <Breadcrumb>
    <BreadcrumbItem>
      <a @click="$router.push('/product-config')">
        {{ t('记录审核') }}
      </a>
    </BreadcrumbItem>
    <BreadcrumbItem>{{ page }}</BreadcrumbItem>
  </Breadcrumb>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';

  defineProps({
    page: {
      type: String,
      default: t('审核处理'),
    },
  });
</script>

<style scoped lang="less"></style>
