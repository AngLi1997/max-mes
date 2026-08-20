<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['id', 'id']"
    :treeData="treeData"
    :default-selected-keys="['all']"
    :search="[true, false]"
    :formProps="[formFirstProps, {}]"
    :fieldNames="{
      title: 'showName',
      key: 'id',
      children: 'children',
    }"
    :treeField="{
      field: {
        productId: 'id',
        categoryFlag: 'categoryFlag',
      },
    }"
    :tableFields="[
      {},
      {
        field: {
          processId: 'id',
        },
      },
    ]"
    :requests="[reqProcessListReq as DataRequestFn, reqProcessVersionListReq as DataRequestFn]"
    :columns="[columnsFirst, columnsSecond]">
    <template #tableHeaderToolbar1="{ currentNodes }">
      <Button
        :type="
          currentNodes[0]?.dashboardConfigVersion &&
          currentNodes[0]?.dashboardConfigVersion === currentNodes[0]?.activeVersion
            ? 'default'
            : 'primary'
        "
        @click="() => handleLargeScreen(currentNodes)">
        {{ t('大屏显示') }}
      </Button>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('工艺配置')"></BMTableTitle>
    </template>
    <template #tableHeaderTitle1>
      <BMTableTitle :title="t('版本信息')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { reqProductMaterialProductTreeReq, reqProcessVersionList, reqProcessList } from '@/services';
  import { DataNode } from 'ant-design-vue/es/tree';
  import { useTable } from './hooks';
  import { DataRequestFn, BMPageComponent, BMTableTitle } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { VersionStatus } from './enum';
  import { filterEmpty } from '@bmos/utils';

  const router = useRouter();

  const reqProcessListReq = async (params: any) => {
    const { productId, categoryFlag, ...newParams }: any = params;
    if (categoryFlag) {
      newParams.productCategoryId = productId;
    } else {
      newParams.productId = productId;
    }
    if (productId === 'all') {
      delete newParams.productId;
      delete newParams.productCategoryId;
    }
    return await reqProcessList(filterEmpty(newParams) as any);
  };

  const reqProcessVersionListReq = async (params: any) => {
    if (!params.processId) return Promise.resolve({ data: [] });
    return await reqProcessVersionList(params);
  };

  const { columnsFirst, formFirstProps, columnsSecond, pageRef } = useTable();

  const handleLargeScreen = (currentNodes: any) => {
    if (currentNodes?.[0]?.id) {
      router.push({
        name: 'process-view-largeScreenDisplay',
        query: {
          processName: currentNodes[0]?.name,
          processId: currentNodes[0]?.id,
          version: currentNodes[1]?.version,
          allowEdit: currentNodes[1].actionState?.value === VersionStatus.VALID ? '1' : '0', //生效版本才可操作
        },
      });
    } else {
      message.warning(t('请先选择工艺'));
    }
  };

  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          showName: t('全部'),
          key: 'all',
          categoryFlag: true,
          children: data,
        },
      ];
    } catch (error) {
      //
    }
  };
  onMounted(() => {
    getTreeData();
  });
</script>
<style lang="less" scoped>
  :deep(.status-content) {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    .status-icon {
      width: 7px;
      height: 7px;
      border-radius: 50%;
      display: inline-block;
      margin-right: var(--bmos-margin-small);
    }
    &.edit {
      color: var(--bmos-primary-color);
    }
    &.approval {
      color: var(--bmos-warning-color);
    }
    &.confirm {
      color: var(--bmos-success-color);
    }
    & .status-icon.edit {
      background-color: var(--bmos-primary-color);
    }
    & .status-icon.approval {
      background-color: var(--bmos-warning-color);
    }
    & .status-icon.confirm {
      background-color: var(--bmos-success-color);
    }
  }
</style>
