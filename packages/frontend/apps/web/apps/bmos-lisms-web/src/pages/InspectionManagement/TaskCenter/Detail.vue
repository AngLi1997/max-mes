<!-- 任务中心 - 详情 -->
<template>
  <div class="task-center-details">
    <div class="header">
      {{ t('检验数据信息') }}
    </div>
    <div class="content">
      <div class="details-table">
        <BMTable :search="false" :data-source="basicDataSource" :columns="basicColumns" :pagination="false">
          <template #headerTitle>
            <BMTableTitle :title="t('基础信息')" />
          </template>
        </BMTable>
        <BMTable
          v-for="item in inspectItemList"
          :key="item.code"
          :search="false"
          :data-source="[item]"
          :columns="columns"
          :pagination="false">
          <template #headerTitle>
            <BMTableTitle :title="InspectionProjectDict.find((it: any) => it.value === item.inspectItemCode)?.label" />
          </template>
        </BMTable>
      </div>
    </div>
  </div>
</template>

<script setup lang="tsx">
  import { postInspectAlldataDetail, postInspectTaskDetail } from '@/services';
  import { useConfig } from '@/stores';
  import { SpecimenTypeEnum } from '@/types';
  import { BMTable, TableColumn, BMTableTitle } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';

  defineOptions({
    name: 'TaskCenterDetail',
    inheritAttrs: false,
  });

  const { InspectionProjectDict } = getDicts();
  const { getConfigEnumsValueByParamId } = useConfig();
  const basicColumns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 220,
    },
    {
      title: t('标本类型'),
      dataIndex: ['sampleType', 'label'],
      width: 150,
    },
    {
      title: t('标本分类'),
      dataIndex: 'sampleClassification',
      width: 170,
      customRender: ({ record }) => {
        if (record.sampleClassification?.value === SpecimenTypeEnum.SERUM_SPECIMEN) {
          return (
            <span
              style={{
                color: getConfigEnumsValueByParamId('血清标本颜色'),
              }}>
              {record.sampleClassification?.label}
            </span>
          );
        }
        return record.sampleClassification?.label ?? '-';
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'donorTime',
      width: 200,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'donorNo',
      width: 170,
    },
    {
      title: t('姓名'),
      dataIndex: 'donorName',
      width: 100,
    },
    {
      title: t('性别'),
      dataIndex: 'immunityType',
      width: 100,
    },
    {
      title: t('血型'),
      dataIndex: 'donorBloodType',
      width: 100,
    },
    {
      title: t('检验状态'),
      dataIndex: ['inspectStatus', 'label'],
      width: 170,
    },
  ];
  const columns: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'inspectItemCode',
      width: 160,
      customRender: ({ record }) => {
        return InspectionProjectDict.find((item: any) => item.value === record.inspectItemCode)?.label;
      },
    },
    {
      title: t('结果值'),
      dataIndex: 'inspectValue',
      width: 100,
    },
    {
      title: t('检验结果'),
      dataIndex: ['inspectResult', 'label'],
      width: 170,
    },
    {
      title: t('试剂批号'),
      dataIndex: 'reagentBatchNo',
      width: 140,
    },
    {
      title: t('质控品批号'),
      dataIndex: 'qcBatchNo',
      width: 170,
    },
    {
      title: t('检验人'),
      dataIndex: 'inspector',
      width: 170,
    },
    {
      title: t('检验日期'),
      dataIndex: 'inspectTime',
      width: 170,
    },
    {
      title: t('复核人'),
      dataIndex: 'checkBy',
      width: 140,
    },
    {
      title: t('复核日期'),
      dataIndex: 'checkTime',
      width: 170,
    },
  ];

  const basicDataSource = ref<any>([]);
  const inspectItemList = ref<any>([]);

  const route = useRoute();
  const { sampleNo } = route.query;
  onMounted(async () => {
    try {
      const { data } = await postInspectTaskDetail({
        sampleNo,
        fetchInspectDataDetail: true,
        fetchSampleDetail: true,
      });
      const { data: allData } = await postInspectAlldataDetail({
        sampleNo,
      });
      basicDataSource.value = [data];
      inspectItemList.value = allData;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  });
</script>

<style scoped lang="less">
  .task-center-details {
    background-color: #fff;
    height: 100%;
    display: flex;
    flex-direction: column;
    .header {
      padding: 16px;
      font-size: 16px;
      font-weight: bold;
      border-bottom: 1px solid #f0f0f0;
    }
    .content {
      padding: 16px;
      flex: 1;
      overflow: hidden auto;
    }
  }
  .task-center-details .details-table {
    height: 160px;
  }
  :deep(.bmos-table .lisms-table-body) {
    border-bottom: none !important;
  }
</style>
