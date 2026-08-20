<!-- 房间清场日志 -->
<template>
  <BMPageComponent
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[true]"
    :formProps="[formFirstProps as FormProps]"
    :requests="[roomCleanupList as any]"
    :columns="[columns as TableColumn[]]">
    <template #tableHeaderToolbar0="{ instance }:any">
      <Dropdown :trigger="['click']">
        <Button type="primary">
          {{ t('导出') }}
        </Button>
        <template #overlay>
          <Menu>
            <MenuItem key="1" @click="exportTable(instance, 'screen')">{{ t('导出筛选数据') }}</MenuItem>
            <MenuItem key="2" @click="exportTable(instance, 'currentPage')">{{ t('导出当前页数据') }}</MenuItem>
          </Menu>
        </template>
      </Dropdown>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('房间清场日志')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>
<script lang="tsx" setup>
  import { getPlatformFactoryRoomLogPage } from '@/services';
  import { t } from '@bmos/i18n';
  import { useColumns, useParams } from './hooks';
  import { TableColumn, BMPageComponent, BMTableTitle, FormProps } from '@bmos/components';
  import { Dropdown, Menu, MenuItem } from 'ant-design-vue';
  const UseParams = useParams();
  const { queryParams } = UseParams;
  const { columns, formFirstProps, exportTable } = useColumns({ UseParams });
  //房间清场日志列表
  const roomCleanupList = async (params: any) => {
    queryParams.value = params;
    return await getPlatformFactoryRoomLogPage(queryParams.value);
  };
</script>
