<template>
  <BMPageComponent
    ref="tableInstance"
    :hide-right-tree="true"
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :rowKeys="['id', 'id']"
    :tableFields="tableFields"
    :requests="[reqPlatformCodeRuleGET, reqPlatformCodeRuleVersion]"
    :columns="columns"
    :titles="titles"
    :search="[true, false]"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 12,
        },
      },
      {},
    ]"
    :rowClick="handleClickRow">
    <template #tableHeaderToolbar0>
      <PermissionModal v-model:permissionOpen="permissionOpen" :resourceId="resourceId" @ok="okPermissionModal" />
      <Button v-hasAuth="100020001001001" type="primary" @click="addCodeRule">
        {{ t('新建编号规则') }}
      </Button>
    </template>
    <template #tableHeaderToolbar1="{ currentNodes }">
      <Button
        v-hasAuth="100020001001002"
        type="primary"
        :disabled="buttonDisabled(currentNodes)"
        @click="addVersion(currentNodes)">
        {{ t('新增版本') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script setup lang="tsx">
  import { reqPlatformCodeRuleGET, reqPlatformCodeRuleVersionGET, reqPlatformCodeRulePermissionSavePOST } from '@/api';
  import { useTables } from './hooks';
  import { ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { BMPageComponent } from '@bmos/components';
  import type { Recordable } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import PermissionModal from './components/PermissionModal.vue';

  const buttonDisabled = (currentNodes: any) => {
    return !(currentNodes && currentNodes[0] && currentNodes[1] && currentNodes[1].id);
  };

  const emits = defineEmits(['addCodeRule', 'addVersion', 'viewVersion', 'editVersion']);

  const addCodeRule = () => {
    emits('addCodeRule');
  };

  const addVersion = (currentNodes: any) => {
    emits('addVersion', currentNodes[1], currentNodes[0]);
  };

  const { columns, titles, tableInstance, selectCodeRule, permissionOpen, resourceId } = useTables({
    emits,
  });
  const tableFields = ref([
    {
      field: {
        id: 'id',
      },
    },
    {
      field: {
        code: 'code',
      },
    },
  ]);
  // 获取编号规则版本信息
  const reqPlatformCodeRuleVersion = async (params: any) => {
    if (params.code) {
      return reqPlatformCodeRuleVersionGET(params);
    }
    return Promise.resolve({
      data: [],
      total: 0,
    });
  };

  const handleClickRow = (row: Recordable, index: number) => {
    if (index === 0) {
      selectCodeRule.value = row;
    }
  };

  const okPermissionModal = async (checks: string[]) => {
    try {
      await reqPlatformCodeRulePermissionSavePOST({
        codeRuleId: resourceId.value,
        deptIds: checks,
      });
      message.success(t('保存成功'));
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
</script>

<style scoped></style>
