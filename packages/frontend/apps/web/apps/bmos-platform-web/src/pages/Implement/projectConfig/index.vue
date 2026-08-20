<template>
  <div class="container">
    <BMTableTitle :title="t('logo设置')" />
    <Select
      ref="select"
      v-model:value="logo"
      style="width: 120px"
      :options="logoOptions"
      :field-names="{ value: 'key' }"
      @change="handleChange"></Select>
  </div>
</template>

<script setup lang="ts">
  import { getParameter } from '@/api/Permissions/menuPermissions';
  import { BMTableTitle } from '@bmos/components';
  import { Select } from 'ant-design-vue';
  import { t } from '@bmos/i18n';

  interface OptionItem {
    label: string;
    value: string;
  }
  const logo = ref<string>('');
  const logoOptions = ref<OptionItem[]>([]);
  const getLogoConfig = async () => {
    const res = await getParameter('platform.sys.project-config');
    logoOptions.value = res.data.value ? JSON.parse(res.data.value) : [];
  };

  const handleChange = () => {
    localStorage.setItem('SYSTEM-LOGO', logo.value);
    window.location.reload();
  };

  getLogoConfig();
  logo.value = localStorage.getItem('SYSTEM-LOGO') || 'bmos';
</script>

<style lang="less" scoped>
  .container {
    width: 100%;
    height: 100%;
    padding: var(--bmos-padding-small);
    background-color: var(--bmos-primary-color-white);
  }
</style>
