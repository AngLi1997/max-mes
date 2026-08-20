<!-- 新增计划页面 -->
<template>
  <div v-show="showAdd" class="addManage">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="back">
            {{ t('生产计划管理') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ type === 'view' ? t('查看生产计划') : t('新建生产计划') }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
        <Button v-if="type !== 'view'" type="primary" @click="save">{{ t('下发') }}</Button>
      </template>
      <BMForm ref="formRef" v-bind="formProps" @formModelChange="formModelChange"></BMForm>
      <div class="btns">
        <BMTableTitle :title="t('指令单批次信息')" />
        <div>
          <Space :size="15">
            <Button :disabled="props.type === 'view'" @click="generateNumber">
              {{ t('生成编号') }}
            </Button>
            <Button :disabled="props.type === 'view'" @click="planCalendarAdjustment">
              {{ t('计划日历调整') }}
            </Button>
            <Button danger :disabled="props.type === 'view'" @click="resetPlan">
              {{ t('重置生产计划') }}
            </Button>
            <Button type="primary" :disabled="props.type === 'view'" @click="generatePlan">
              {{ t('生成生产计划') }}
            </Button>
          </Space>
        </div>
      </div>
      <!-- tab标签切换 -->
      <Tabs v-model:activeKey="activeKey" :tab-position="'top'" type="card" class="tabs">
        <TabPane v-for="(item, index) in tableDataList.length" :key="index" :tab="t('生产计划') + (index + 1)">
          <div class="table">
            <BMTable
              row-key="key"
              :dataSource="tableDataList[index]"
              :columns="columns"
              :search="false"
              :scroll="{ x: 1044, y: 400 }"
              :showRefresh="false"
              :defaultExpandAllRows="true"
              :showSearchBorder="true"
              :showToolBar="false"
              :showIndex="true"
              :row-selection="{
                hideSelectAll: true,
                selectedRowKeys: selectedRowKeys,
                onSelect: onSelect,
                getCheckboxProps: (record: any) => ({
    disabled: props.type === 'view'
  })
              }"
              :pagination="false">
              <template #expandedRowRender="{ record }">
                <BMTable
                  :columns="columns2"
                  :dataSource="record.procedureListDetail"
                  :pagination="false"
                  :search="false"
                  :showToolBar="false"
                  :showIndex="true"
                  :scroll="{ x: 400, y: 300 }" />
              </template>
            </BMTable>
          </div>
        </TabPane>
      </Tabs>
    </BreadcrumbButton>
  </div>
  <!-- 编辑指令单批次信息的页面 -->
  <EditBatchInfo
    v-if="!showAdd && showEdit"
    :batchInfoRowData="batchInfoRowData"
    :relatedPlanBatch="relatedPlanBatch"
    @backAdd="backAdd"
    @updateBatchInfo="updateBatchInfo"></EditBatchInfo>
  <!-- 计划日历调整 -->
  <PlanCalendarPage
    v-if="!showAdd && showCalendar"
    :source="source"
    :tableDataList="tableDataList"
    @backAdd="backAdd"></PlanCalendarPage>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMForm, formInstance, BMTable, TableColumn, BMTableTitle } from '@bmos/components';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import EditBatchInfo from './components/editBatchInfo.vue';
  import PlanCalendarPage from './components/planCalendarPage.vue';
  import {
    reqPlanTemplateList,
    reqProductionBuildPlan,
    reqProductionListPlanDetail,
    getParameter,
    reqProductionBuildBatchNo,
    reqProductionPlanIssue, //下发
  } from '@/services';
  import { Modal, message, Space, Tabs, TabPane } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import dayjs from 'dayjs';
  import { dataTool } from 'echarts';

  const router = useRouter();

  const formRef = ref<formInstance>();
  const emits = defineEmits(['back']);
  const activeKey = ref<any>(0); //对应TabPane的key
  const showAdd = ref<boolean>(true);
  const batchInfoRowData = ref<any>(); //指令单批次信息的行数据
  const planTypeList = ref<any>(); //存脚本而来的指令单类型（可获取指令单编码productPlanType）
  const groupNumber = ref<any>(); //存第几组
  const groupNumberIndex = ref<any>(); //存该组对应的第几行
  const generatePlanParams = ref<any>(); //存上一次点生成生产计划时的参数
  const productPlanType = ref<any>();
  const relatedPlanBatch = ref<any>([]); //存关联的计划批次
  const selectedRowKeys = ref<any>([]);
  const showEdit = ref<any>(false);
  const showCalendar = ref<any>(false);
  const source = ref<any>('sourcePlan');
  const showPlanRed = ref<any>([]); //下发之后判断指令单编号重复的给标红
  const showBatchRed = ref<any>([]); //下发之后判断生产批号重复的给标红
  const formNoPlanName = ref<any>(); //计划名称改变的时候不清空下面表格
  const onSelect = (record: any, selected: any) => {
    if (!selected) {
      // 取消勾选
      selectedRowKeys.value = selectedRowKeys.value.filter((item: any) => item !== record.key);
      record.checked = 'false';
    } else {
      selectedRowKeys.value.push(record.key);
      record.checked = 'true';
    }
  };
  const tableDataList = ref<any>([]); //存分组所有信息(选完生产计划数量点生产生产计划后得到)
  const props = withDefaults(
    defineProps<{
      type: string;
      rowData: Object;
    }>(),
    {},
  );
  const formProps = reactive<any>({
    initialValues: {},
    disabled: false,
    baseColProps: {
      span: 8,
    },
    showActionButtonGroup: false,
    schemas: [
      {
        label: t('计划名称'),
        field: 'planName',
        component: 'Input',
        required: true,
      },
      {
        label: t('生产计划模板'),
        field: 'planTemplateId',
        component: 'Select',
        required: true,
        componentProps: ({ formModel }: any) => {
          return {
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            onChange: (val: any, option: any) => {
              if (!option.confirmed) {
                Modal.confirm({
                  title: t('提示'),
                  icon: h(ExclamationCircleOutlined),
                  closable: true,
                  content: t('该生产计划模版中有工艺版本与当前生效版本不一致，请确认'),
                  onOk: () => {
                    router.push({
                      name: 'plan-template',
                    });
                  },
                  onCancel() {
                    formModel.planTemplateId = undefined;
                  },
                });
              }
            },
            request: async () => {
              try {
                const { data } = await reqPlanTemplateList();
                return data || [];
              } catch (error) {
                return [];
              }
            },
          };
        },
      },
      {
        field: 'planType',
        component: 'Select',
        label: t('指令单类型'),
        required: true,
        componentProps: () => {
          return {
            options: [
              {
                label: t('生产批次'),
                value: 'PRODUCT',
              },
              {
                label: t('实验批次'),
                value: 'EXPERIMENT',
              },
              {
                label: t('验证批次'),
                value: 'VERIFY',
              },
            ],
            onChange: async (val: any) => {
              if (!val) {
                productPlanType.value = undefined;
                return;
              }
              productPlanType.value = planTypeList.value.find((item: any) => item?.label === val)?.value; //A B C
            },
          };
        },
      },
      {
        field: 'planNumber',
        component: 'InputNumber',
        label: t('生产计划数量'),
        defaultValue: 1,
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: string) => {
                if (!value && value !== '0') {
                  return Promise.reject(t('请输入小于100的正整数!'));
                } else if (!/^([1-9][0-9]{0,1}|99)$/.test(value)) {
                  return Promise.reject(t('请输入小于100的正整数!'));
                } else {
                  return Promise.resolve();
                }
              },
              trigger: 'blur',
            },
          ];
        },
        componentProps: {
          placeholder: t('请输入'),
        },
      },
      {
        field: 'planFirstDate',
        component: 'DatePicker',
        label: t('首批生产日期'),
        required: true,
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
          placeholder: t('请选择日期'),
        },
      },
      {
        field: 'duration',
        component: 'InputNumber',
        label: t('间隔时长'),
        required: true,
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (_rule: any, value: any) => {
                if (value == 0) {
                  return Promise.resolve();
                }
                if (!value) {
                  return Promise.reject(t('请输入间隔时长'));
                } else if (!/^([1-9][0-9]{0,1}|99)$/.test(value)) {
                  return Promise.reject(t('请输入小于100的正整数!'));
                } else {
                  return Promise.resolve();
                }
              },
              trigger: 'blur',
            },
          ];
        },
        componentSlots: () => {
          return {
            addonAfter: () => <div style='min-width:40px'>{t('天')}</div>,
          };
        },
      },
    ],
  });
  // 主表格列
  const columns: TableColumn[] = [
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 180,
      resizable: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 150,
      resizable: true,
    },
    {
      title: t('产品规格'),
      dataIndex: 'productSpecification',
      width: 130,
      resizable: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 150,
      resizable: true,
    },
    {
      title: t('计划开始日期'),
      dataIndex: 'startTime',
      width: 140,
      resizable: true,
    },
    {
      title: t('计划结束日期'),
      dataIndex: 'endTime',
      width: 140,
      resizable: true,
    },
    {
      title: t('产线'),
      dataIndex: 'productionLineName',
      width: 150,
      resizable: true,
    },
    {
      title: t('指令单编号'),
      dataIndex: 'planNo',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return <div style={showPlanRed.value?.includes(record.planNo) ? 'color: red' : ''}>{record.planNo || '-'}</div>;
      },
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <div style={showBatchRed.value?.includes(record.batchNo + '-' + record.processId) ? 'color: red' : ''}>
            {record.batchNo || '-'}
          </div>
        );
      },
    },
    {
      title: t('生产批量'),
      dataIndex: 'batchQuantity',
      width: 120,
      resizable: true,
    },
    {
      title: t('关联批次信息'),
      dataIndex: 'relatedBatchInfo', //string类型
      width: 400,
      resizable: true,
      customRender: ({ record }) => {
        return <div class='relatedBatchInfo'>{record.relatedBatchInfo}</div>;
      },
    },
    {
      title: t('生产批号沿用'),
      dataIndex: 'reuseBatchNumber',
      width: 110,
      resizable: true,
      customRender: ({ record }) => {
        return <div>{record.reuseBatchNumber === true ? t('是') : t('否')}</div>;
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 150,
      actions: ({ record, index }: any) => [
        {
          label: t('编辑'),
          disabled: props.type === 'view',
          onClick: e => {
            batchInfoRowData.value = record;
            groupNumber.value = record.groupNumber; //存分组几(生产计划几)
            groupNumberIndex.value = index; //存该分组的第几行
            relatedPlanBatch.value = tableDataList.value[record.groupNumber].filter(
              (item: any, index: any) => index !== e.index,
            ); //计划批次
            showAdd.value = false;
            showEdit.value = true;
            showCalendar.value = false;
          },
        },
      ],
    },
  ];
  // 子表格列
  const columns2: TableColumn[] = [
    {
      title: t('工序名称'),
      dataIndex: 'procedureName',
      width: 120,
    },
    {
      title: t('计划开始日期'),
      dataIndex: 'startTime',
      width: 120,
      resizable: true,
    },
    {
      title: t('计划结束日期'),
      dataIndex: 'endTime',
      width: 120,
      resizable: true,
    },
  ];
  // 生成编号
  const generateNumber = async () => {
    await formRef.value?.validate();
    if (tableDataList.value.length === 0) return message.error(t('请生成生产计划'));
    tableDataList.value?.forEach((item: any) => {
      item?.forEach((item2: any) => {
        item2.productPlanType = productPlanType.value;
      });
    });
    try {
      let temp: any = cloneDeep(tableDataList.value);
      temp = temp?.map((item: any) => {
        return item.filter((item2: any) => item2.checked === 'true');
      });
      const { data } = await reqProductionBuildBatchNo(temp);
      if (data.meg) {
        const msgList = data?.meg?.split(';');
        message.warning({
          content: h(
            'div',
            { style: 'display:inline-table;text-align:left;vertical-align: top;' },
            msgList.map((item: any, index: any) => {
              return [item, index < msgList.length - 1 ? <br /> : null];
            }),
          ),
        });
      } else {
        message.success(t('生成成功'));
      }
      showPlanRed.value = [];
      showBatchRed.value = [];
      data.list?.forEach((item: any) => {
        item.key = item.groupNumber + '' + item.sort; //唯一键
      });

      tableDataList.value.forEach((item: any) => {
        item.forEach((item2: any) => {
          const Index = data.list.findIndex((listItem: any) => listItem.key === item2.key);
          if (Index > -1) {
            const temp = data.list.find((listItem: any) => listItem.key === item2.key);
            item2.batchNo = temp?.batchNo || '';
            item2.planNo = temp?.planNo || '';
            item2.batchNoCode = temp?.batchNoCode || '';
            item2.planNoCode = temp?.planNoCode || '';
            item2.relatedBatchInfo = temp?.productionBatchList || '';
            item2.relationBatchSortList = temp?.relationBatchSortList || [];
            item2.planNoCodeApplyTime = temp?.planNoCodeApplyTime;
          } else {
            //没勾选的
            item2.batchNo = '';
            item2.planNo = '';
            item2.batchNoCode = '';
            item2.planNoCode = '';
            item2.relatedBatchInfo = '';
            item2.relationTable = []; //未勾选的需要清空关联批次信息
          }
        });
      });
      tableDataList.value.forEach((item: any) => {
        item.forEach((item2: any) => {
          if (item2.checked === 'false') {
            item2.relationBatchSortListAO = []; //未勾选的需要清空关联批次信息
          } else {
            item2.relationBatchSortListAO = item2.relationBatchSortList?.map((item3: any) => {
              return {
                value: item3,
                processId: item[item3]?.processId,
                batchNo: item[item3]?.batchNo,
              };
            }); //计划模板那的关联批次转换为对象数组
          }
        });
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 监听表单改变时清空下方表格
  const formModelChange = (value: any) => {
    if (props.type === 'add' && formNoPlanName.value) {
      const { planName: planName1, ...obj1 } = value;
      const { planName: planName2, ...obj2 } = formNoPlanName.value;
      console.log(planName1, planName2);
      if (JSON.stringify(obj1) === JSON.stringify(obj2)) {
        // 除了计划名称以外的表单未改变
      } else {
        tableDataList.value = [];
        generatePlanParams.value = {};
      }
    }
  };
  // 计划日历调整
  const planCalendarAdjustment = () => {
    if (tableDataList.value?.length === 0) {
      message.error(t('暂无计划日历'));
      return;
    }
    showAdd.value = false;
    showCalendar.value = true;
    showEdit.value = false;
  };
  // 重置生产计划
  const resetPlan = () => {
    Modal.confirm({
      title: t('重置确认'),
      icon: h(ExclamationCircleOutlined),
      closable: true,
      content: t('确认后根据生产信息配置重新生成指令单批次信息'),
      onOk: async () => {
        const res = await formRef.value?.validate();
        try {
          const params = {
            templateId: res.planTemplateId,
            planNumber: res.planNumber,
            planFirstDate: res.planFirstDate,
            duration: res.duration,
            planTemplateId: undefined,
            confirmed: true,
          };
          const { data } = await reqProductionBuildPlan(params);
          message.success(t('重置成功'));
          formNoPlanName.value = res;
          selectedRowKeys.value = [];
          data.forEach((item: any) => {
            item.forEach((item2: any) => {
              item2.key = item2.groupNumber + '' + item2.sort;
              selectedRowKeys.value.push(item2.key); //默认勾选所有
              item2.checked = 'true';
              item2.relatedBatchInfo = item2.productionBatchList; //回显生产计划模板那的关联批次
              item2.relationBatchSortListAO = item2.relationBatchSortList?.map((item3: any) => {
                return {
                  value: item3,
                  processId: item[item3]?.processId,
                  batchNo: item[item3]?.batchNo,
                };
              }); //计划模板那的关联批次转换为对象数组
              // 生成生成计划初始时把currentRelationList加上
              item2.currentRelationList = item2.relationBatchSortList?.map((item3: any) => {
                return {
                  planIds: [item3],
                  processId: item[item3]?.processId,
                };
              });
              item2.processIdItemInterval = dayjs(item2.endTime).diff(dayjs(item2.startTime), 'day'); //算该工艺的开始间隔时长(天)
              item2.processIdItemDuration = dayjs(item2.endTime).diff(dayjs(item2.startTime), 'day') + 1; //算该工艺的执行时长
              item2.startTimeSources = item2.startTime; //存最初时的开始时间 用于后续计划调整时算提前还是延后了多少天
              item2.procedureListDetail?.forEach((item4: any) => {
                item4.procedureItemInterval = dayjs(item4.startTime).diff(dayjs(item2.startTime), 'day'); //算该工艺的每个工序的开始间隔时长(天) 0926-0927算间隔一天
                item4.procedureItemDuration = dayjs(item4.endTime).diff(dayjs(item4.startTime), 'day') + 1; //算该工艺的每个工序的执行时长 例0926-0927算共两天
                item4.startTimeSources = item4.startTime; //存最初时的开始时间 用于后续计划调整时算提前还是延后了多少天
              });
            });
          });
          tableDataList.value = data;
        } catch (error: any) {
          message.error(error.message);
        }
      },
    });
  };
  // 生成生产计划
  const generatePlan = async () => {
    const res = await formRef.value?.validate();
    try {
      const params = {
        templateId: res.planTemplateId,
        planNumber: res.planNumber,
        planFirstDate: res.planFirstDate,
        duration: res.duration,
        planTemplateId: undefined,
        confirmed: true,
      };
      if (JSON.stringify(params) === JSON.stringify(generatePlanParams.value)) {
        message.warning(t('生产信息配置和上次一致'));
        return;
      }
      const { data } = await reqProductionBuildPlan(params);
      message.success(t('生成成功'));
      formNoPlanName.value = res;
      selectedRowKeys.value = [];
      generatePlanParams.value = params;
      data.forEach((item: any) => {
        item.forEach((item2: any) => {
          item2.key = item2.groupNumber + '' + item2.sort; //唯一键
          selectedRowKeys.value.push(item2.key); //默认勾选所有
          item2.checked = 'true';
          item2.relatedBatchInfo = item2.productionBatchList; //回显生产计划模板那的关联批次
          item2.relationBatchSortListAO = item2.relationBatchSortList?.map((item3: any) => {
            return {
              value: item3,
              processId: item[item3]?.processId,
              batchNo: item[item3]?.batchNo,
            };
          }); //计划模板那的关联批次转换为对象数组
          // 生成生成计划初始时把currentRelationList加上
          item2.currentRelationList = item2.relationBatchSortList?.map((item3: any) => {
            return {
              planIds: [item3],
              processId: item[item3]?.processId,
            };
          });
          item2.processIdItemInterval = dayjs(item2.endTime).diff(dayjs(item2.startTime), 'day'); //算该工艺的开始间隔时长(天)
          item2.processIdItemDuration = dayjs(item2.endTime).diff(dayjs(item2.startTime), 'day') + 1; //算该工艺的执行时长
          item2.startTimeSources = item2.startTime; //存最初时的开始时间 用于后续计划调整时算提前还是延后了多少天
          item2.procedureListDetail?.forEach((item4: any) => {
            item4.procedureItemInterval = dayjs(item4.startTime).diff(dayjs(item2.startTime), 'day'); //算该工艺的每个工序的开始间隔时长(天) 0926-0927算间隔一天
            item4.procedureItemDuration = dayjs(item4.endTime).diff(dayjs(item4.startTime), 'day') + 1; //算该工艺的每个工序的执行时长 例0926-0927算共两天
            item4.startTimeSources = item4.startTime; //存最初时的开始时间 用于后续计划调整时算提前还是延后了多少天
          });
        });
      });
      tableDataList.value = data;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 编辑之后更新前端指令单批次信息表格
  const updateBatchInfo = (val: any) => {
    showAdd.value = true;
    showPlanRed.value = [];
    showBatchRed.value = [];
    // 刷新前端表格
    tableDataList.value[groupNumber.value][groupNumberIndex.value].productionLineId = val.productionLineId;
    tableDataList.value[groupNumber.value][groupNumberIndex.value].productionLineCode = val.productionLineCode;
    tableDataList.value[groupNumber.value][groupNumberIndex.value].productionLineName = val.productionLineName;
    tableDataList.value[groupNumber.value][groupNumberIndex.value].planNo = val.planNo;
    tableDataList.value[groupNumber.value][groupNumberIndex.value].batchNo = val.batchNo;
    tableDataList.value[groupNumber.value][groupNumberIndex.value].batchQuantity = val.batchQuantity;
    tableDataList.value[groupNumber.value][groupNumberIndex.value].relatedBatchInfo = val.relatedBatchInfo;
    tableDataList.value[groupNumber.value][groupNumberIndex.value].relationList = val.relationTable?.map(item => {
      return {
        processId: item.processId,
        planIds: item.planIds || [],
      };
    });
    tableDataList.value[groupNumber.value][groupNumberIndex.value].currentRelationList = val.relationTable?.map(
      (item: any) => {
        return {
          processId: item.processId,
          planIds: item.sorts,
        };
      },
    );
    // 如果计划批次被改了 那么relationBatchSortList也要对应修改
    tableDataList.value[groupNumber.value][groupNumberIndex.value].relationBatchSortList = val.relationTable
      ?.map((item: any) => item.sorts)
      .flat();
    tableDataList.value[groupNumber.value][groupNumberIndex.value].batchNoList = val.relationTable
      ?.map((item: any) => item?.batchNoList)
      ?.flat()
      .filter((item2: any) => item2); //过滤为null的数据

    tableDataList.value[groupNumber.value][groupNumberIndex.value].relationTable = val.relationTable; //暂存编辑之后的关联批次表格
    tableDataList.value[groupNumber.value].forEach((item: any) => {
      // 修改需沿用的批号
      if (
        item.relationBatchSortList?.length > 0 &&
        item.relationBatchSortList?.includes(val.updateContinueBatchNoSort) &&
        item.reuseBatchNumber
      ) {
        item.relatedBatchInfo = item.relatedBatchInfo.replace(val.oldBatchNo, val.batchNo);
        item.batchNo = val.batchNo;
        handleContinue(val.batchNo, item.sort, val.oldBatchNo);
      }
    });
    tableDataList.value[groupNumber.value].forEach((item: any) => {
      item.relationBatchSortListAO = item.relationBatchSortList?.map((item2: any) => {
        //计划模板那的关联批次转换为对象数组
        return {
          value: item2,
          processId: tableDataList.value[groupNumber.value][item2]?.processId,
          batchNo: tableDataList.value[groupNumber.value][item2]?.batchNo,
        };
      });
    });

    //同步更新非编辑行的关联批次信息
    tableDataList.value[groupNumber.value].forEach((item: any) => {
      if (item.relationBatchSortListAO?.length > 0) {
        const temp = item?.relationBatchSortListAO?.map((item2: any) => {
          return {
            ...item2,
            name: tableDataList.value[groupNumber.value][item2.value].processName,
            showName:
              tableDataList.value[groupNumber.value][item2.value].processName +
              '-' +
              (item2.batchNo ? item2.batchNo : ''),
          };
        });
        const temp2 = temp.map((hh: any) => hh.showName);
        item.relatedBatchInfo = temp2?.join(';');
      }
    });
    tableDataList.value[groupNumber.value][groupNumberIndex.value].relatedBatchInfo = val.relatedBatchInfo;
  };
  // 连续沿用处理
  const handleContinue = (batchNo: any, sort: any, oldBatchNo: any) => {
    tableDataList.value[groupNumber.value].forEach((item: any) => {
      if (
        item.relationBatchSortList?.length > 0 &&
        item.relationBatchSortList?.includes(sort) &&
        item.reuseBatchNumber
      ) {
        item.relatedBatchInfo = item.relatedBatchInfo.replace(oldBatchNo, batchNo);
        item.batchNo = batchNo;
        handleContinue(batchNo, item.sort, oldBatchNo);
      }
    });
  };

  // 指令单类型(脚本而来)
  const getPlanTypeList = async () => {
    try {
      const { data } = await getParameter('mes.ProductionPlanType');
      planTypeList.value = Object.keys(JSON.parse(data.value)).map((item: any) => {
        return {
          label: item,
          value: JSON.parse(data.value)[item],
        };
      });
    } catch (error) {
      planTypeList.value = [];
    }
  };
  // 下发按钮(保存)
  const save = async () => {
    const res = await formRef.value?.validate();
    let flag = true;
    const temp = tableDataList.value.map((item: any) => {
      return [
        ...item.filter(function (item2: any, index: any, arr: any) {
          if (item2.currentRelationList) {
            const temp2 = [...new Set(item2.currentRelationList?.map((item: any) => item?.planIds)?.flat())];
            if (temp2?.length >= arr?.filter((item3: any) => item3.checked === 'true')?.length) {
              flag = false;
            }
          }
          return selectedRowKeys.value.includes(item2.key);
        }),
      ];
    });
    if (!flag) {
      message.error(t('对应关联工艺生产批次有未勾选'));
      return;
    }
    try {
      const data = { ...res, itemList: temp };
      const resData = await reqProductionPlanIssue(data);
      if (resData.data.success == false) {
        showPlanRed.value = [];
        showBatchRed.value = [];
        const planArr = resData.data?.planNoList?.map((item: any) => t('指令单编号') + item); //重复了的编号提醒
        const batchArr = resData.data?.batchNoList?.map(
          (item: any) => t('生产批号') + item?.batchNo + '(' + item?.processName + ')',
        );
        showPlanRed.value = resData.data?.planNoList;
        showBatchRed.value = resData.data?.batchNoList?.map((item: any) => item.batchNo + '-' + item.processId);
        const tipString = [...planArr, ...batchArr].join('、');
        message.error(tipString + t('已存在'));
        return;
      }
      message.success(t('下发成功'));
      back();
    } catch (error: any) {
      message.error(error.message);
      if (error.code == 8224004) {
        Modal.confirm({
          title: t('提示'),
          icon: h(ExclamationCircleOutlined),
          closable: true,
          content: t('生效版本不一致'),
          onOk: async () => {
            router.push({
              name: 'plan-template',
            });
          },
        });
      }
    }
  };
  // 返回到管理页面
  const back = () => {
    emits('back');
  };
  // 返回到新增页面
  const backAdd = () => {
    showAdd.value = true;
  };

  // 查看时回显数据
  const echoData = async () => {
    const { data } = await reqProductionListPlanDetail({ id: props.rowData.id });
    formRef.value?.setFormModels({
      planName: data.planName,
      planTemplateId: data.planTemplateName,
      planType: data.planType,
      planNumber: data.planNumber,
      planFirstDate: data.planFirstDate,
      duration: data.duration,
    });
    formRef.value?.setFormProps({
      disabled: true,
    });
    data.planDetailVOList?.forEach((item: any) => {
      item.forEach((item2: any) => {
        item2.key = item2.groupNumber + '' + item2.sort;
        selectedRowKeys.value.push(item2.key); //默认勾选所有
      });
    });
    tableDataList.value = data.planDetailVOList;
  };

  onMounted(() => {
    switch (props.type) {
      case 'view':
        echoData();
        break;
      case 'add':
        getPlanTypeList();
        break;
    }
  });
</script>
<style lang="less" scoped>
  .addManage {
    width: 100%;
    height: 100%;
    .container {
      padding: 0px;
    }
  }
  :deep(.mes-input-number) {
    width: 100%;
  }
  :deep(.mes-input-number-group-wrapper) {
    width: 100%;
  }
  .btns {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15px;
  }
  .table {
    height: calc(100vh - 350px);
  }
  :deep(.relatedBatchInfo) {
    max-width: 400px;
    /* 超出部分用省略号表示 */
    text-overflow: ellipsis;
    overflow: hidden;
  }
</style>
