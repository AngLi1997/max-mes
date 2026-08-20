<!-- 分拣类别管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[false]"
    :showAllAddIcon="false"
    :showAction="false"
    :fieldNames="{
      title: 'desc',
      key: 'sortingType',
    }"
    :treeField="{
      field: {
        sortingType: 'sortingType',
      },
    }"
    :expandedKeys="['all', 0]"
    :treeData="treeData"
    :showHeader="[false]"
    :showToolBars="[true]"
    :formProps="[formFirstProps]"
    :paginations="[paginationFirst]"
    :requests="[getLists as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #customizeTitle="{ title, colour }">
      <div style="display: flex; align-items: center; justify-content: flex-start">
        <div
          v-if="colour"
          :style="{
            backgroundColor: colour,
            width: '6px',
            height: '6px',
            borderRadius: '50%',
            marginRight: '8px',
          }"></div>
        <div>
          {{ title }}
        </div>
      </div>
    </template>
    <template #tableHeaderTitle0>
      <div>
        <Button v-if="hasPermission('170110006000001') && tableType === 1" type="primary" @click="openModal">
          {{ t('新增') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <Segmented v-model:value="tableType" :options="typeOpts" @change="changeType"></Segmented>
    </template>
  </BMPageComponent>
  <AddModal ref="addModalRef" @submitSuccess="() => pageRef.fetchData()" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMPageComponent, DataRequestFn } from '@bmos/components';
  import { useTable } from './hooks/useTable';
  import { getSortingCategoryList, getSortingCategoryTree, getSampleSortingCategoryTree } from '@/services';
  import { AddModal } from './components';
  import { usePermissionStore } from '@/stores/permission';

  defineOptions({
    name: 'SortingCategoryMng',
  });

  const { hasPermission } = usePermissionStore();

  // 新增/编辑操作
  const addModalRef = ref<any>();

  const openModal = (row: any = {}, type: 'create' | 'edit' = 'create') => {
    const data = {
      ...row,
      manageType: tableType.value,
    };
    addModalRef.value.openModal(data, type);
  };

  const { pageRef, columnsFirst, formFirstProps, paginationFirst } = useTable(openModal);

  const tableType = ref<any>(1);

  const getTreeApi = {
    1: getSortingCategoryTree,
    2: getSampleSortingCategoryTree,
  };

  const typeOpts = [
    {
      label: t('血浆'),
      value: 1,
    },
    {
      label: t('标本'),
      value: 2,
    },
  ];

  // 选项变化时重新获取数据
  const changeType = async (_val: any) => {
    await getTree();
    await pageRef.value?.fetchData();
  };

  const treeData = ref<any>([
    {
      desc: t('全部'),
      sortingType: 'all',
      children: [],
    },
  ]);

  const getTree = async () => {
    try {
      // @ts-ignore
      const res = await getTreeApi[tableType.value]();
      treeData.value = [
        {
          desc: t('全部'),
          sortingType: 'all',
          children: res.data.map((item: any) => {
            return {
              ...item,
              disabled: item.sortingType === 0,
            };
          }),
        },
      ];
    } catch (error) {
      console.log(error);
      return Promise.reject(error);
    }
  };

  const getLists = async (params: any) => {
    const datas = {
      ...params,

      manageType: tableType.value,
      sortingType: params.sortingType == 'all' ? undefined : params.sortingType,
    };
    return await getSortingCategoryList(datas);
  };

  onMounted(async () => {
    await getTree();
  });
</script>

<style lang="less" scoped>
  :deep(.bsms-segmented) {
    .bsms-segmented-item {
      padding: 0 8px;
      flex-grow: 1;
    }
  }
</style>
