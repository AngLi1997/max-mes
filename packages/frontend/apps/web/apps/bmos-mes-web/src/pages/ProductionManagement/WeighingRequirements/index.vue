<template>
  <BMPageComponent
    ref="pageRef"
    :hideRightTree="true"
    :columns="[columns]"
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['id']"
    :search="[true, false]"
    :formProps="[formProps]"
    :requests="[reqWeighingRequirementsQueryPage as any]">
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('生产批次配料')"></BMTableTitle>
    </template>
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="120030013000001" type="primary" @click="addWeighingRequire">
        {{ t('新增批次配料') }}
      </Button>
    </template>
  </BMPageComponent>
  <AddModal v-model:modalOpen="addOpen" />
</template>

<script setup lang="ts">
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import { reqWeighingRequirementsQueryPage } from '@/services';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import AddModal from './AddModal/index.vue';
  // import router from '@/router';

  const addOpen = ref(false);

  const { columns, pageRef, formProps } = useTable();

  const addWeighingRequire = () => {
    // router.push({
    //   name: 'weighing-requirements-detail',
    //   query: {
    //     type: 'add',
    //   },
    // });
    addOpen.value = true;
  };
</script>

<style scoped lang="less"></style>
