<!-- 报告生成 -->
<template>
  <div style="height: 100%">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('检验管理') }}</breadcrumb-item>
          <breadcrumb-item>{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item>{{ t('生成') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="header-btn">
        <Space :size="16">
          <!-- <div class="header-btn"> -->
          <Button type="primary" @click="submit">{{ t('报告生成') }}</Button>
          <Button danger @click="reInspection">{{ t('重新检测') }}</Button>
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
      <BMForm ref="setFormRef" v-bind="setFormProps"></BMForm>
    </LimsCard>
  </div>
  <Sign
    ref="signModalRef"
    v-model:open="signOpen"
    v-bind="signModalProps"
    :signatureDataFn="signatureDataFn"
    @signSuccess="submitSuccess" />
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMTable, BMForm } from '@bmos/components';
  import { onMounted, reactive, ref, computed } from 'vue';
  import { Alert, Descriptions, DescriptionsItem, message } from 'ant-design-vue';
  import { LimsCard } from '@/components/Card';
  import { useTable, useForm } from './hooks';
  import { generateCheckOrderReport, getCheckOrderReportInfo } from '@/services/index';
  import { Sign } from '@/components/Sign';
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

  const tableRef = ref<InstanceType<typeof BMTable>>();
  const signModalRef = ref<InstanceType<typeof Sign>>();

  const signOpen = ref(false);

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
  // 基本信息
  const infoData = ref<any>({});
  // 列表信息
  const tableData = ref<any>([]);
  // 结论信息
  const reportResultInfo = ref<any>({});

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
  const { setFormProps, setFormRef, setNodeFormData } = useForm();

  const submit = async () => {
    await setFormRef.value?.submit();
    generate();
  };

  // 报告生成
  const generate = () => {
    signModalProps.title = t('报告生成');
    signModalProps.extraSchemas = [];
    signModalProps.signatureAction = 23;
    // signModalRef.value?.openModal(props.data, true)
    signOpen.value = true;
  };

  // 重新检测
  const reInspection = () => {
    signModalProps.title = t('重新检测');
    signModalProps.extraSchemas = [
      {
        field: 'label',
        component: () => (
          <Alert
            class='approval-alert'
            message={t('检验任务数据将重新录入，当前检验报告作废，是否继续？')}
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
    signModalProps.signatureAction = 24;
    // signModalRef.value?.openModal(props.data, true)
    signOpen.value = true;
  };

  const signatureDataFn = (formModal: any) => {
    const data = {
      id: props.data.id,
      result: signModalProps.signatureAction == 23 ? setFormRef.value?.formModel.result : undefined,
      reason: formModal.reason,
    };
    return JSON.stringify(data);
  };

  const submitSuccess = async (formModal: any) => {
    console.log('formModal', formModal);
    try {
      const data = {
        id: props.data.id,
        result: setFormRef.value?.formModel.result,
        reason: formModal.reason,
      };
      await generateCheckOrderReport(data);
      message.success(t('操作成功'));
      emit('back');
    } catch (error: any) {
      message.error(error?.message);
    }
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
        item.reportAnalyzeVOList.forEach((i: any, index) => {
          tableData.value.push({
            ...i,
            reportName: index === 0 ? `【${reportName}】` : ' ',
            length: item.reportAnalyzeVOList?.length ?? 1,
          });
        });
      });
      reportResultInfo.value = res.data.reportResultInfoVO;
      setNodeFormData(reportResultInfo.value);
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
