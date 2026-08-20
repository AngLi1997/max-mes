<template>
  <div style="height: 100%; overflow: auto">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('检验管理') }}</breadcrumb-item>
          <breadcrumb-item>{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item>{{ t('检验详情') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="header-btn">
        <Space :size="16">
          <Button v-if="props.data.processCode == CHECK_STATUS.CONFIRM" type="primary" @click="confirm">
            {{ t('确认') }}
          </Button>
          <Button v-if="props.data.processCode == CHECK_STATUS.TAKE" @click="take">{{ t('取样') }}</Button>
          <Button v-if="props.data.processCode == CHECK_STATUS.INSPECT" type="primary" @click="inspect">
            {{ t('提交') }}
          </Button>
          <Button v-if="props.data.processCode == CHECK_STATUS.INSPECT" @click="input">{{ t('录入') }}</Button>
          <Button v-if="props.data.processCode == CHECK_STATUS.REPORT" @click="generate">{{ t('报告生成') }}</Button>
          <Button v-if="props.data.processCode == CHECK_STATUS.REPORT" danger @click="recheck">
            {{ t('重新检验') }}
          </Button>
          <Button v-if="props.data.processCode == CHECK_STATUS.AUDIT_REPORT" @click="audit">{{ t('审核') }}</Button>
          <Button v-if="props.data.processCode == CHECK_STATUS.SIGN" @click="issuance">{{ t('签发') }}</Button>
          <Button @click="openLog(props.data.orderNo, true)">{{ t('日志') }}</Button>
          <Button @click="print">{{ t('打印') }}</Button>
          <Button
            v-if="![CHECK_STATUS.ALREADY_SIGN, CHECK_STATUS.ALREADY_TERMINATION].includes(props.data.processCode)"
            danger
            @click="terminate">
            {{ t('终止') }}
          </Button>
          <div style="width: 1px; height: 26px; background-color: #d4d7d9"></div>
          <Button @click="back">{{ t('返回') }}</Button>
        </Space>
      </Col>
    </Row>
    <div style="height: calc(100% - 48px); overflow: auto">
      <LimsCard style="position: sticky; top: 0; z-index: 1000; box-shadow: 0 2px 8px #f0f1f2">
        <div style="width: 100%; height: 120px; overflow: auto">
          <div class="header-title">
            <div class="header-title-info">
              <BMTableTitle :title="`${t('检验单编码')}: `" />
              <span class="header-title-info-code">{{ props.data.orderNo }}</span>
              <img class="copy-icon mr-16" :src="Copy" @click="copy" />
              <Tag :bordered="false" :class="`mr-16 tag-${props.data.processCode}`" size="small">
                {{ checkStatusMap[props.data.processCode] }}
              </Tag>
            </div>
          </div>
          <Steps v-model:current="currentStep" :responsive="false" :items="stepItems"></Steps>
        </div>
        <Alert
          v-if="endReason"
          style="margin-top: 8px"
          class="approval-alert"
          :message="endReason"
          type="warning"
          showIcon>
          <template #icon><ExclamationCircleOutlined /></template>
        </Alert>
      </LimsCard>
      <LimsCard :title="t('基本信息')" type="item">
        <Descriptions
          size="small"
          :column="3"
          style="margin-top: 16px"
          :labelStyle="{
            width: '80px',
          }">
          <DescriptionsItem v-for="item in basicItems" :key="item.label" :label="item.label">
            {{ basicData[item.field] ?? '\\' }}
          </DescriptionsItem>
        </Descriptions>
      </LimsCard>
      <LimsCard :title="t('取样信息')" type="item">
        <Descriptions
          v-if="isTake"
          size="small"
          :column="3"
          style="margin-top: 16px"
          :labelStyle="{
            width: '80px',
          }">
          <DescriptionsItem v-for="item in sampleItems" :key="item.label" :label="item.label">
            {{ sampleData[item.field] }}
          </DescriptionsItem>
        </Descriptions>
        <Empty v-else></Empty>
      </LimsCard>
      <LimsCard :title="t('检验信息')">
        <div class="table">
          <BMTable
            ref="tableInstance"
            :data-request="loadData"
            :columns="columns"
            row-key="id"
            :showRefresh="false"
            :search="false"
            :showIndex="false"
            :pagination="false"
            :scroll="{ y: 500 }"
            :formProps="formProps">
            <template #expandColumnTitle>{{}}</template>
            <template #expandedRowRender="{ record }">
              <!-- <p>{{record.name}}</p> -->
              <div style="width: 100%; padding-left: 32px">
                <Table :columns="innerColumns" :data-source="record.analyzeInfoList" :pagination="false">
                  <template #bodyCell="{ column, record: innerRecord }">
                    <template v-if="column.key === 'ACTION' && innerRecord.operatorName">
                      <Button type="link" size="small" @click="openLog(innerRecord.id, false)">
                        {{ t('日志') }}
                      </Button>
                    </template>
                  </template>
                </Table>
              </div>
            </template>
          </BMTable>
        </div>
      </LimsCard>
    </div>
  </div>
  <LogModel ref="logModelRef" />
  <Sign
    ref="signModalRef"
    v-model:open="signOpen"
    v-bind="signModalProps"
    :signatureDataFn="signatureDataFn"
    @signSuccess="submitSuccess"></Sign>
  <PrintVerify v-if="showPrint" ref="printVerifyRef"></PrintVerify>
</template>

<script setup lang="tsx">
  import { LimsCard } from '../Card';
  import { t } from '@bmos/i18n';
  import { computed, defineComponent, nextTick, onMounted, ref } from 'vue';
  import { BMTable, BMTableTitle, TableInstance } from '@bmos/components';
  import NotStarted from '@/assets/images/notStarted.png';
  import InProcess from '@/assets/images/inProcess.png';
  import Complete from '@/assets/images/complete.png';
  import Stop from '@/assets/images/stop.png';
  import Copy from '@/assets/images/copy.png';
  import { useDescriptions, useSgin, useTable } from './hooks';
  import { Steps, Tag, Table, Button, Descriptions, DescriptionsItem, message, Alert, Tooltip } from 'ant-design-vue';
  import { CHECK_STATUS, checkStatusMap } from '@/utils/enum';
  import {
    getCheckOrderInfo,
    getCheckOrderLog,
    getCheckOrderLogAnalyze,
    getCheckOrderLogOrder,
    confirmCheckOrder,
    takeCheckOrder,
    getCheckOrderAnalyzeValid,
    submitCheckOrderInspect,
    generateCheckOrderReport,
    terminateCheckOrder,
  } from '@/services/index';
  import LogModel from './logModel.vue';
  import { PrintVerify } from '@/components/PrintVerify';
  import { useRouter } from 'vue-router';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

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

  const logModelRef = ref();

  const tableInstance = ref<TableInstance>();

  const emit = defineEmits(['back', 'openInput', 'openGenerate', 'openAudit', 'openIssuance']);

  const loadData = async () => {
    return new Promise(resolve => {
      resolve({
        data: [...tableData.value],
      });
    });
  };

  // 图标组件
  const imgCom = defineComponent({
    props: {
      icon: {
        type: String,
        isRequired: true,
        default: () => {
          NotStarted;
        },
      },
    },
    setup(props) {
      return () => <img style={{ paddingBottom: '4px' }} src={props.icon} />;
    },
  });

  // 超出8位改成省略号
  const ellipsis = (text: string, num: number = 8) => {
    if (text.length > num) {
      return text.slice(0, num) + '...';
    } else {
      return text;
    }
  };

  // 步骤条内容组件
  const stepCom = defineComponent({
    props: {
      data: {
        type: Object,
        isRequired: true,
        default: () => {},
      },
    },
    setup(props) {
      return () => (
        <div class='my-step-content'>
          {props.data.operatorName.length > 8 ? (
            <Tooltip title={props.data.operatorName}>{ellipsis(props.data.operatorName)}</Tooltip>
          ) : (
            <span>{props.data.operatorName}</span>
          )}
          {/* <Tooltip title={ellipsis(props.data.operatorName)}>
          {props.data.operatorName}
        </Tooltip> */}
          {/* <span>{props.data.operatorName}</span> */}
          <span>{props.data.operateTime}</span>
        </div>
      );
    },
  });

  const stepItems = ref([]);

  const { basicData, basicItems, sampleData, sampleItems } = useDescriptions();
  const { columns, formProps, innerColumns, tableData } = useTable();

  const endReason = ref('');

  const copy = () => {
    // 复制到剪切板
    // navigator.clipboard.writeText(code.value).then(() => {
    //   message.success(t('复制成功'))
    // })

    let textarea = document.createElement('textarea');
    textarea.value = props.data.orderNo;
    document.body.appendChild(textarea);
    textarea.select();
    document.execCommand('copy');
    document.body.removeChild(textarea);
    message.success(t('复制成功'));
  };

  const back = () => {
    emit('back');
  };

  const openLog = async (data: any, flag: boolean) => {
    const request = flag ? getCheckOrderLogOrder : getCheckOrderLogAnalyze;
    const res = await request(data);
    const list = res.data.map((item: any) => {
      return {
        ...item,
        result: flag ? item.name : item.result,
      };
    });
    logModelRef.value.openModel(list);
  };

  const icomMap = {
    COMPLETE: Complete,
    PENDING: InProcess,
    TERMINATION: Stop,
    NOT_BEGIN: NotStarted,
  };

  // 签名相关
  const params = ref({});
  const signApi = ref(confirmCheckOrder);

  const { signOpen, signModalProps, signatureDataFn, signData } = useSgin({
    params,
  });

  const submitSuccess = async (_formModel: any) => {
    try {
      await signApi.value(signData.value);
      message.success(t('操作成功'));
      signOpen.value = false;
      emit('back');
    } catch (error: any) {
      message.error(error?.message);
    }
  };

  // 确认
  const confirm = () => {
    signModalProps.value = {
      title: t('请验确认'),
      extraSchemas: [],
      signatureAction: 18,
    };
    cmpOperate(CHECK_STATUS.CONFIRM);
    signOpen.value = true;
  };

  // 取样
  const take = () => {
    signModalProps.value = {
      title: t('取样'),
      extraSchemas: [
        {
          field: 'amount',
          label: t('取样量'),
          component: 'Input',
          required: true,
        },
      ],
      signatureAction: 19,
    };
    cmpOperate(CHECK_STATUS.TAKE);
    signOpen.value = true;
  };

  // 提交
  const inspect = async () => {
    const { data } = await getCheckOrderAnalyzeValid({ orderNoList: props.data.orderNo, count: 0 });
    signModalProps.value = {
      title: t('提交'),
      extraSchemas:
        data <= 0
          ? []
          : [
              {
                field: 'label',
                component: () => (
                  <Alert
                    class='approval-alert'
                    message={`${t('存在')}${data}${t('项未录入分析项，是否批量录为N/A并提交')}`}
                    type='warning'
                    showIcon={true}
                    icon={<ExclamationCircleOutlined />}
                  />
                ),
              },
            ],
      signatureAction: 20,
    } as any;
    cmpOperate(CHECK_STATUS.INSPECT);
    signOpen.value = true;
  };

  // 录入
  const input = () => {
    emit('openInput', [props.data.orderNo], false);
  };

  // 报告生成
  const generate = () => {
    emit('openGenerate', props.data, false);
  };

  // 重新检验
  const recheck = () => {
    signModalProps.value = {
      title: t('重新检验'),
      extraSchemas: [
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
          componentProps: {
            maxLength: 100,
          },
        },
      ] as any,
      signatureAction: 24,
    };
    cmpOperate(CHECK_STATUS.REPORT);
    signOpen.value = true;
  };

  // 审核
  const audit = () => {
    emit('openAudit', props.data);
  };

  // 签发
  const issuance = () => {
    emit('openIssuance', props.data);
  };

  // 终止
  const terminate = () => {
    signModalProps.value = {
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
    };
    cmpOperate(CHECK_STATUS.ALREADY_TERMINATION);
    signOpen.value = true;
  };

  const printVerifyRef = ref<InstanceType<typeof PrintVerify>>();

  // 打印
  const showPrint = ref(false);

  const print = async () => {
    showPrint.value = true;
    nextTick(() => {
      printVerifyRef.value?.printDom(basicData.value);
      setTimeout(() => {
        showPrint.value = false;
      }, 0);
    });
  };

  // 处理参数
  const cmpOperate = (processCode: string) => {
    switch (processCode) {
      case CHECK_STATUS.CONFIRM:
        params.value = {
          idList: [props.data.id],
        };
        signApi.value = confirmCheckOrder;
        break;
      case CHECK_STATUS.TAKE:
        params.value = {
          id: props.data.id,
        };
        signApi.value = takeCheckOrder;
        break;
      case CHECK_STATUS.INSPECT:
        params.value = [{ orderNo: props.data.orderNo }];
        signApi.value = submitCheckOrderInspect;
        break;
      case CHECK_STATUS.REPORT:
        params.value = {
          id: props.data.id,
        };
        signApi.value = generateCheckOrderReport;
        break;
      case CHECK_STATUS.ALREADY_TERMINATION:
        params.value = {
          id: props.data.id,
        };
        signApi.value = terminateCheckOrder;
        break;
      default:
        return () => {};
    }
  };

  // 是否取样
  const isTake = ref(false);
  // 当前节点
  const currentStep = ref(0);

  onMounted(async () => {
    try {
      const res = await getCheckOrderInfo(props.data.orderNo);
      const log = await getCheckOrderLog(props.data.orderNo);
      basicData.value = {
        ...res.data,
        inspectInfoVOList: undefined,
      };
      tableData.value = [...res.data.inspectInfoVOList];

      log.data.forEach((item: any) => {
        // 取样
        if (item.code == CHECK_STATUS.TAKE) {
          sampleData.value = {
            result: item.result,
            operatorName: item.operatorName,
            operateTime: item.operateTime,
          };
          isTake.value = !!item.result;
        }
        if (item.complete == 'TERMINATION') {
          // 已终止
          endReason.value = `${t('终止原因')}: ${item.reason} `;
        }
        stepItems.value.push({
          title: item.name,
          icon: <imgCom icon={icomMap[item.complete]} />,
          description: item.operateTime ? <stepCom data={item} /> : '',
          disabled: true,
        });
        if (item.complete == 'COMPLETE') {
          currentStep.value += 1;
        }
      });

      tableInstance.value.fetchData();
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
    position: sticky;
    top: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    // background-color: #fff;
    flex-grow: 0;
    width: 100% !important;
    padding-bottom: 12px;
    // margin-bottom: var(--bmos-margin-small);
    backdrop-filter: blur(6px);
    z-index: 1000;
    .crumb {
      // line-height: 36px;
    }
    &-btn {
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }
  }

  .header-title {
    // position: sticky;
    // top: 48px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    background-color: #fff;
    flex-grow: 0;
    width: 100% !important;
    // padding: 16px 16px;
    padding-bottom: 12px;
    margin-bottom: var(--bmos-margin-small);
    // z-index: 1000;
    // box-shadow: 0 2px 8px #f0f1f2;
    border-bottom: #e1e3e5 1px solid;
    &-info {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      &-code {
        margin: 0 16px;
        color: #18191a;
        font-size: 14px;
        line-height: 1;
      }
    }
    &-btn {
      display: flex;
      justify-content: flex-start;
      align-items: center;
    }
  }

  .lims-steps {
    max-width: 2980px;
  }
  :deep .lims-steps.lims-steps-horizontal:not(.lims-steps-label-vertical) .lims-steps-item-description {
    max-width: 300px;
    white-space: normal;
    font-size: 12px;
  }

  .copy-icon {
    cursor: pointer;
  }

  .table {
    // background-color: #fff;
    height: 100%;
    padding: 0;
    // overflow: auto;
    .bmos-table {
      height: 94%;
      overflow: auto;
    }
  }

  :deep .my-step-content {
    display: flex;
    justify-content: flex-start;
    align-items: flex-start;
    flex-direction: column;
  }
  :deep .lims-steps .lims-steps-item-disabled {
    cursor: default;
  }

  .tag {
    &-confirm,
    &-take,
    &-inspect {
      background-color: #ffecd8;
      color: #ff9a2f;
    }

    &-report,
    &-audit_report,
    &-sign {
      background-color: #d9e5ff;
      color: #2871ff;
    }

    &-already_sign {
      background-color: #dbf9ef;
      color: #59bf78;
    }

    &-terminate {
      background-color: #ffd7cf;
      color: #ff5633;
    }
  }

  :deep .lims-empty-description {
    color: #909398;
  }

  :deep .lims-steps .lims-steps-item-title::after {
    background-color: #e1e3e5;
    // height: 2px;
  }

  :deep
    .lims-steps
    .lims-steps-item-process
    > .lims-steps-item-container
    > .lims-steps-item-content
    > .lims-steps-item-title {
    color: #2871ff;
  }
  :deep
    .lims-steps
    .lims-steps-item-finish
    > .lims-steps-item-container
    > .lims-steps-item-content
    > .lims-steps-item-title::after {
    background-color: #5991ff;
  }

  :deep .bmos-table-title {
    line-height: 24px;
  }

  :deep .lims-steps .lims-steps-item-title {
    font-size: 14px;
    line-height: 35px;
  }
</style>
