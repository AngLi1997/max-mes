<!-- 报告审核 -->
<template>
  <div style="height: 100%">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('检验管理') }}</breadcrumb-item>
          <breadcrumb-item>{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item>{{ t('审核') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="header-btn">
        <Space :size="16">
          <!-- <div class="header-btn"> -->
          <Button type="primary" @click="submit">{{ t('报告审核') }}</Button>
          <div style="width: 1px; height: 26px; background-color: #d4d7d9"></div>
          <Button @click="back">{{ t('返回') }}</Button>
          <!-- </div> -->
        </Space>
      </Col>
    </Row>
    <LimsCard style="height: calc(100% - 64px - 182px)" :title="t('检验信息')">
      <div class="table">
        <Descriptions size="small" :column="3">
          <DescriptionsItem :label="t('检品名称')">{{ infoData.productsName }}</DescriptionsItem>
          <DescriptionsItem :label="t('规格')">{{ infoData.specification }}</DescriptionsItem>
          <DescriptionsItem :label="t('批号')">{{ infoData.batchNo }}</DescriptionsItem>
        </Descriptions>
        <BMTable
          ref="tableRef"
          :data-request="loadData"
          :columns="columns"
          :formProps="formProps"
          :scroll="{ y: 200 }"
          :showRefresh="false"
          :search="false"
          :showIndex="false"
          :pagination="false"></BMTable>
      </div>
    </LimsCard>
    <LimsCard :title="t('检验结论')">
      <Descriptions size="small" :column="3">
        <DescriptionsItem :span="3" :label="t('检验结论')">{{ reportResultInfo.result }}</DescriptionsItem>
        <DescriptionsItem :label="t('报告人')">{{ reportResultInfo.reportName }}</DescriptionsItem>
      </Descriptions>
    </LimsCard>
  </div>
  <Sign
    ref="signModalRef"
    v-model:open="signOpen"
    v-bind="signModalProps"
    :signatureDataFn="signatureDataFn"
    @signSuccess="submitSuccess"></Sign>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMTable, RenderCallbackParams } from '@bmos/components';
  import { computed, onMounted, reactive, ref } from 'vue';
  import { Descriptions, DescriptionsItem, message, RadioGroup, Radio, Alert, Row, Col, Space } from 'ant-design-vue';
  import { LimsCard } from '@/components/Card';
  import { useTable } from './hooks';
  import { getCheckOrderReportInfo, auditCheckOrderReport } from '@/services/index';
  import Sign from '@/components/Sign';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { useRouter } from 'vue-router';

  const props = defineProps({
    data: {
      type: Object,
      default: () => {
        return {};
      },
    },
  });

  const router = useRouter();

  const comRouter = computed(() => {
    return t(router.currentRoute.value.meta.id as string);
  });

  const infoData = ref<any>({});
  const tableData = ref<any>([]);
  const reportResultInfo = ref<any>({});
  const tableRef = ref<any>(null);

  const signModalProps = reactive({
    title: t('检验终止'),
    extraSchemas: [
      {
        field: 'reason',
        label: t('原因'),
        component: 'Input',
        required: true,
        componentProps: {
          maxLength: 100,
        },
      },
    ],
    signatureAction: 22,
  });

  const emit = defineEmits(['back']);

  const back = () => {
    emit('back');
  };

  const loadData = async () => {
    return new Promise(resolve => {
      resolve({
        data: [...tableData.value],
      });
    });
  };

  const { columns, formProps } = useTable();

  const signatureDataFn = (formModel: any) => {
    const data = {
      id: props.data.id,
      result: formModel.result,
      reason: formModel.reason,
    };
    return JSON.stringify(data);
  };

  const submitSuccess = async (formModel: any) => {
    try {
      const data = {
        id: props.data.id,
        result: formModel.result,
        reason: formModel.reason,
      };
      await auditCheckOrderReport(data);
      message.success(t('操作成功'));
      emit('back');
    } catch (error: any) {
      message.error(error?.message);
    }
  };

  const signModalRef = ref<InstanceType<typeof Sign>>();
  const signOpen = ref<boolean>(false);

  const tempSchema = [
    {
      field: 'label',
      component: () => (
        <Alert
          class='approval-alert'
          message={t('审核不通过将重新进行检验，请重新录入检验数据并提交')}
          type='warning'
          showIcon={true}
          icon={<ExclamationCircleOutlined />}
        />
      ),
    },
    {
      field: 'reason',
      label: t('原因'),
      component: 'Input',
      required: true,
    },
  ];

  const submit = async () => {
    (signModalProps.title = t('报告审核')),
      (signModalProps.extraSchemas = [
        {
          field: 'result',
          label: t('审核结果'),
          required: true,
          component: ({ formModel }: RenderCallbackParams) => (
            <RadioGroup
              v-model:value={formModel.result}
              onChange={(_e: any) => {
                if (formModel.result === 'PASS' && signModalProps.extraSchemas[0].field === 'label') {
                  // 删除数组第一项和最后一项
                  signModalProps.extraSchemas.splice(0, 1);
                  signModalProps.extraSchemas.splice(-1, 1);
                } else if (formModel.result === 'NOT_PASS') {
                  // 在数组第一项插入
                  signModalProps.extraSchemas.splice(0, 0, tempSchema[0]);
                  // 在数组最后一项插入
                  signModalProps.extraSchemas.push(tempSchema[1]);
                }
              }}>
              <Radio value='PASS'>{t('通过')}</Radio>
              <Radio value='NOT_PASS'>{t('不通过')}</Radio>
            </RadioGroup>
          ),
        },
      ]),
      (signModalProps.signatureAction = 25);
    signOpen.value = true;
    signModalRef.value?.setFormModel('result', 'PASS');
  };

  onMounted(async () => {
    try {
      const res = await getCheckOrderReportInfo(props.data.orderNo);
      infoData.value = {
        productsName: res.data.productsName,
        specification: res.data.specification,
        batchNo: res.data.batchNo,
      };
      res.data.reportInspectVOList.forEach((item: any) => {
        const reportName = item.reportName;
        item.reportAnalyzeVOList.forEach((i: any, index: number) => {
          tableData.value.push({
            ...i,
            reportName: index === 0 ? `【${reportName}】` : ' ',
            length: item.reportAnalyzeVOList?.length ?? 1,
          });
        });
      });
      reportResultInfo.value = res.data.reportResultInfoVO;
      tableRef.value?.fetchData();
    } catch (error: any) {
      message.error(error?.message);
    }
  });
</script>

<style lang="less" scoped>
  .mr-16 {
    margin-right: 16px;
  }

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    // background-color: #fff;
    flex-grow: 0;
    width: 100% !important;
    padding-bottom: 12px;
    // margin-bottom: var(--bmos-margin-small);
    .crumb {
      line-height: 36px;
    }
    &-btn {
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }
  }

  .table {
    // background-color: #fff;
    height: 100%;
    padding: var(--bmos-padding-small);
    // overflow: auto;
    .bmos-table {
      height: 94%;
      overflow: auto;
    }
  }
</style>
