<template>
  <div class="detail">
    <div class="info">
      <div class="info-flex">
        <div class="name">
          <span>{{ props.titles.title1 || t('实例业务名称') }}:</span>
          <span>{{ props.rowData.name }}</span>
        </div>
        <div class="name">
          <span>{{ props.titles.title2 || t('实例业务编号') }}:</span>
          <span>{{ props.rowData.extField }}</span>
        </div>
      </div>
      <div>
        <Button @click="back">{{ t('返回') }}</Button>
      </div>
    </div>
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      auto-height
      :autoHeightOffset="24"
      headerTitle=""
      :search="false"
      :scroll="{ x: 844 }"
      :showRefresh="false"
      :formProps="formProps"
      :extraParams="{
        processInstanceId: props.rowData.processInstanceId,
      }"
      :show-index="true">
      <template #toolbar>
        <Button @click="detailExport">{{ t('导出') }}</Button>
      </template>
    </BMTable>
  </div>
</template>
<script lang="tsx" setup>
  import type { DataRequestFn, TableInstance } from '@bmos/components';
  import { BMTable, TableColumn } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { reactive, ref, onMounted } from 'vue';
  import { Button, message } from 'ant-design-vue';
  import { ExamineDetailExport, GetTaskHistoryList } from '@/services';
  import { fileStreamDownload } from '@bmos/utils';

  const emit = defineEmits(['back']);
  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
    titles: {
      type: Object,
      default: () => {},
    },
    categoryCode: {
      type: String,
      default: '',
    },
    parentId: {
      type: String,
      default: '',
    },
  });
  const tableInstance = ref<TableInstance>();
  const formProps = reactive<any>({
    actionColOptions: {
      // span: 19,
    },
    // 是否展示更多
    showAdvancedButton: false,
    // 是否显示操作按钮
    showActionButtonGroup: true,
    baseColProps: {
      span: 6,
    },
  });
  const loadData: DataRequestFn = async (params): Promise<any> => {
    return GetTaskHistoryList(params);
  };
  // 返回按钮
  const back = () => {
    emit('back');
  };
  // 状态样式
  const style = {
    width: '7px',
    height: '7px',
    borderRadius: '50%',
    marginRight: '8px',
  };
  const colorList: any = {
    '4': {
      //通过
      color: '#59BF78',
    },
    '5': {
      //不通过
      color: '#FF5633',
    },
    '2': {
      //退回
      color: '#FF9A2F',
    },
    '1': {
      //审批中
      color: '#FFF3E5',
    },
  };
  const columns: TableColumn[] = [
    {
      title: t('流程节点名称'),
      align: 'left',
      dataIndex: 'elementName',
      hideInSearch: true,
    },
    {
      title: t('处理人'),
      align: 'left',
      dataIndex: 'completeName',
      formItemProps: {
        order: 0,
      },
    },
    {
      title: t('处理时间'),
      align: 'left',
      dataIndex: 'endTime',
    },
    {
      title: t('处理状态'),
      align: 'left',
      dataIndex: 'stateName',
      customRender: ({ record }) => (
        <div style='display: flex;align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record.stateName?.value]?.color,
            }}></div>
          <div style={{ color: colorList[record.stateName?.value]?.color }}>{record.stateName?.label}</div>
        </div>
      ),
    },
    {
      title: t('处理意见'),
      align: 'left',
      dataIndex: 'comment',
    },
    {
      title: t('备注'),
      align: 'left',
      dataIndex: 'remark',
      hideInSearch: true,
    },
  ];
  // 导出
  const detailExport = async () => {
    try {
      const data2 = {
        id: props.categoryCode, //当前树节点id
        categoryCode: props.parentId, //当前树节点父级树节点id
        processInstanceId: props.rowData.processInstanceId,
      };
      const res2: any = await ExamineDetailExport(data2);
      fileStreamDownload(res2);
    } catch (error: any) {
      message.error(error.message);
    }
  };

  onMounted(() => {});
</script>
<style scoped lang="less">
  .detail {
    padding-left: 16px;
    .info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 10px;
      .info-flex {
        display: flex;
      }
      .name {
        margin-right: 80px;
      }
    }
  }
  :deep(.action-list) {
    .plat-btn {
      padding-right: 12px;
      padding-left: 0;
    }
  }

  :deep(.plat-table-thead .plat-table-cell-fix-left .plat-table-cell-content) {
    font-weight: 700;
  }
</style>
