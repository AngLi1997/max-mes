<template>
  <div class="process-container">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('工艺审核填报') }}</breadcrumb-item>
          <breadcrumb-item>
            {{ t('工序审核') }}
          </breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="action">
        <Space>
          <Button @click="back">{{ t('返回') }}</Button>
        </Space>
      </Col>
    </Row>
    <div class="processInfo">
      <BMTableTitle style="margin-bottom: 10px" :title="t('工艺信息')"></BMTableTitle>
      <Descriptions :column="4">
        <DescriptionsItem v-for="item in basicItems" :key="item.label" :label="item.label">
          <div v-if="item?.field === 'confirmOpinion'">{{ props.rowData[item?.field]?.label ?? '' }}</div>
          <div v-else>{{ props.rowData[item?.field] ?? '-' }}</div>
        </DescriptionsItem>
      </Descriptions>
    </div>
    <div class="processNodes">
      <BMTableTitle style="margin-bottom: 10px" :title="t('工序节点')"></BMTableTitle>
      <div class="table">
        <BMTable
          ref="tableInstance"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          auto-height
          :autoHeightOffset="24"
          headerTitle=""
          :scroll="{ x: 844, y: 200 }"
          :showRefresh="false"
          :pagination="{
            pageSize: 20,
          }"
          :formProps="{
            showAdvancedButtonBadge: false,
            showAdvancedButton: false,
            showActionButtonGroup: false,
          }"></BMTable>
      </div>
    </div>
  </div>
  <!-- 审核结论弹框 -->
  <conclusionModal
    ref="conclusionModalRef"
    :rowData="rowData1"
    :type="type"
    @updateTable="updateTable"></conclusionModal>
</template>
<script lang="tsx" setup>
  import { Row, Col, Breadcrumb, BreadcrumbItem, Space, Button, Descriptions, DescriptionsItem } from 'ant-design-vue';
  import { BMTableTitle, BMTable, TableColumn } from '@bmos/components';
  import type { DataRequestFn, TableInstance } from '@bmos/components';
  import conclusionModal from './conclusionModal.vue';
  import StateTag from '@/components/StateTag/index.vue';
  import { usePermissionStore } from '@/stores/permission';
  import { t } from '@bmos/i18n';
  import { getProcedurePage } from '@/services';

  const { hasPermission } = usePermissionStore();
  const emit = defineEmits(['back']);
  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
  });
  const type = ref('procedure'); //type为工序
  const conclusionModalRef = ref<any>();
  const rowData1 = ref<any>();
  const tableInstance = ref<TableInstance>();

  const basicItems = reactive<any>([
    {
      label: t('产品名称'),
      field: 'productName',
    },
    {
      label: t('产品编码'),
      field: 'productCode',
    },
    {
      label: t('产品规格'),
      field: 'productSpecification',
    },
    {
      label: t('工艺名称'),
      field: 'processName',
    },
    {
      label: t('生产批号'),
      field: 'planBatchNo',
    },
    {
      label: t('生产开始时间'),
      field: 'startTime',
    },
    {
      label: t('生产结束时间'),
      field: 'endTime',
    },
    {
      label: t('审核结论'),
      field: 'confirmOpinion',
    },
  ]);
  // 表格列
  const columns: TableColumn[] = [
    {
      title: t('工序名称'),
      align: 'left',
      dataIndex: 'procedureName',
      fixed: 'left',
      hideInSearch: true,
    },
    {
      title: t('工序完成时间'),
      align: 'left',
      dataIndex: 'procedureTime',
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('审核结论'),
      align: 'left',
      dataIndex: 'confirmOpinion',
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return record.confirmOpinion ? (
          <StateTag
            type={
              record.confirmOpinion?.value === 'ELIGIBLE'
                ? 'success'
                : record.confirmOpinion?.value === 'NOT_ELIGIBLE'
                ? 'danger'
                : record.confirmOpinion?.value === 'RESTS'
                ? 'default'
                : ''
            }>
            {record.confirmOpinion?.label || '-'}
          </StateTag>
        ) : (
          '-'
        );
      },
    },
    {
      title: t('备注'),
      align: 'left',
      dataIndex: 'remark',
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }: any) => [
        {
          label: t('审核结论'),
          ifShow: hasPermission('120030009000001'),
          onClick: () => {
            rowData1.value = record;
            conclusionModalRef.value.openModal();
          },
        },
      ],
    },
  ];
  // 表格数据来源
  const loadData: DataRequestFn = async (reqData: any) => {
    const data = {
      ...reqData,
      processConfirmId: props.rowData?.id,
    };
    return getProcedurePage(data);
  };
  // 返回
  const back = () => {
    emit('back');
  };
  const updateTable = () => {
    tableInstance.value?.fetchData();
  };
</script>
<style lang="less" scoped>
  .process-container {
    width: 100%;
    height: 100%;
    position: relative;
    .header {
      padding: 4px 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
      }
    }
    .action {
      text-align: right;
    }
    .processInfo {
      width: 100%;
      height: 140px;
      background-color: white;
      padding: 12px 16px;
      margin-bottom: 10px;
    }
    .processNodes {
      width: 100%;
      height: calc(100% - 210px);
      background-color: white;
      padding: 16px 16px 0 16px;
      display: flex;
      flex-direction: column;
      .table {
        flex: 1;
        overflow-y: hidden;
      }
    }
  }
</style>
