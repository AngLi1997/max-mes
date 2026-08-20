<template>
  <div class="container">
    <DepartmentManagement :inside="inside"></DepartmentManagement>
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import DepartmentManagement from '@/pages/Permissions/departmentManagement/index.vue';
  const inside = ref<string>('1'); //'1'为部门内部管理,'2'为部门管理
</script>

<style lang="less" scoped>
  .container {
    width: 100%;
    height: 100%;
  }
</style>
