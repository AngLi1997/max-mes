<!-- 请验单配置 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[true]"
    :titles="['请验单列表']"
    :formProps="[formFirstProps]"
    :requests="[reqInspectConfigQueryList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="120100001000001" type="primary" @click="handleAdd">{{ t('新增请验单') }}</Button>
    </template>
  </BMPageComponent>
  <BindMaterialModal v-model:open="bindMaterialModalOpen" :rowData></BindMaterialModal>
</template>

<script lang="ts" setup>
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { reqInspectConfigQueryList } from '@/services';
  import { t } from '@bmos/i18n';
  import { OperationType } from './type';
  import BindMaterialModal from './components/BindMaterialModal.vue';

  const router = useRouter();

  const { pageRef, columnsFirst, formFirstProps, bindMaterialModalOpen, rowData } = useTable();

  const handleAdd = () => {
    router.push({
      name: 'PleaseCheckConfigDetail',
      query: {
        status: OperationType.Add,
      },
    });
  };
</script>
