<!-- 预警消息 -->
<template>
  <Drawer
    v-model:open="open"
    :title="t('预警消息')"
    placement="right"
    width="900px"
    destroyOnClose
    @after-open-change="afterOpenChange">
    <BMTable
      ref="tableRef"
      :data-request="reqPlasmaNoticePage"
      :search="false"
      :columns="columns"
      row-key="id"
      headerTitle=""
      :extraParams="{
        noticeType: 1,
      }"
      :scroll="{ x: 844, y: 400 }"
      :pagination="{
        pageSize: 20,
      }"
      :showRefresh="false">
      <template #toolbar>
        <Button type="link" :loading="loading" @click="readAll">{{ t('全部标记为已读') }}</Button>
      </template>
    </BMTable>
  </Drawer>
</template>

<script setup lang="tsx">
  import type { TableColumn } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { reqPlasmaNoticeAllRead, reqPlasmaNoticePage, reqPlasmaNoticeRead } from '../../api/info';
  import { BMTable } from '@bmos/components';

  const emits = defineEmits(['toWarningPage', 'readSuccess']);

  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('通知标题'),
      dataIndex: 'title',
      hideInSearch: true,
      width: 170,
    },
    {
      title: t('预警内容'),
      dataIndex: 'content',
      hideInSearch: true,
      width: 250,
    },
    {
      title: t('状态'),
      dataIndex: 'readFlag',
      hideInSearch: true,
      width: 100,
      customRender: ({ record }) => {
        return (
          <div
            style='display: flex;
       align-items: center;'>
            <div
              style={{
                width: '7px',
                height: '7px',
                borderRadius: '50%',
                backgroundColor: record.readFlag ? '#59BF78' : '#FF9A2F',
                marginRight: '8px',
              }}></div>
            <div style={{ color: record.readFlag ? '#59BF78' : '#FF9A2F' }}>
              {record.readFlag ? t('已读') : t('未读')}
            </div>
          </div>
        );
      },
    },
    {
      title: t('类型'),
      dataIndex: 'type',
      hideInSearch: true,
      width: 100,
      customRender: ({ record }) => {
        const typeEnum: any = {
          WARNING_INFORMATION: t('预警信息'),
          AUDIT_INFORMATION: t('审核信息'),
        };
        return typeEnum[record?.type] ?? '-';
      },
    },
    {
      title: t('日期'),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 170,
      sorter: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          onClick: async () => {
            // look(record);
            try {
              await reqPlasmaNoticeRead({
                serviceName: record.serviceName,
                noticeId: record.id,
              });
              emits('toWarningPage', {
                ...record,
                categoryCode: record.identifierId,
              });
              emits('readSuccess');
              open.value = false;
            } catch (error: any) {
              error.message && message.error(error.message);
            }
          },
        },
      ],
    },
  ];

  const loading = ref<boolean>(false);

  const readAll = async () => {
    try {
      loading.value = true;
      await reqPlasmaNoticeAllRead();
      message.success(t('操作成功'));
      emits('readSuccess');
      tableRef.value?.fetchData();
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      loading.value = false;
    }
  };

  const open = ref<boolean>(false);

  const afterOpenChange = (bool: boolean) => {
    console.log('open', bool);
  };

  const showDrawer = () => {
    open.value = true;
  };

  defineExpose({ showDrawer });
</script>

<style lang="less" scoped>
  // :deep(.bmos-table .bsms-table-wrapper .bsms-table) {
  //   flex: 0;
  // }
</style>
