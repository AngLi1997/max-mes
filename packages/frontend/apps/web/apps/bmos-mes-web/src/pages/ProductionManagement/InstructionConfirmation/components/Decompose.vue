<template>
  <div style="height: 100%; overflow: auto">
    <div class="header">
      <Breadcrumb>
        <BreadcrumbItem>{{ t('指令单确认') }}</BreadcrumbItem>
        <BreadcrumbItem>{{ t('确认') }}</BreadcrumbItem>
      </Breadcrumb>
      <div>
        <Button style="margin-right: 10px" @click="goBack">
          {{ t('返回') }}
        </Button>
        <Button type="primary" :loading="submitLoading" @click="saveTeam(true)">{{ t('确认指令单') }}</Button>
      </div>
    </div>
    <div class="main">
      <div class="des_box">
        <Descriptions :column="4" :labelStyle="{ color: '#606266' }">
          <DescriptionsItem v-for="item in descriptionsDom" :key="item.key" :label="item.label">
            {{ descriptionsData?.[item.key] }}
          </DescriptionsItem>
        </Descriptions>
      </div>
      <div class="msg-box">
        <div class="msg-card">
          <div class="msg-card-header">{{ t('工序信息') }}</div>
          <Descriptions :column="3" :labelStyle="{ color: '#606266' }">
            <DescriptionsItem :label="t('工序名称')">
              {{ nodeMsg.name }}
            </DescriptionsItem>
            <DescriptionsItem :label="t('工序阶段编码')">
              {{ nodeMsg.code }}
            </DescriptionsItem>
          </Descriptions>
        </div>
        <div class="msg-card msg-main">
          <div class="msg-card-header">{{ t('班组配置') }}</div>
          <div class="msg-team-config">
            <BMTable
              ref="teamTable"
              row-key="id"
              :columns="columns"
              :autoHeightOffset="24"
              :search="false"
              :showToolBar="false"
              :dataSource="tableData"
              bordered
              :scroll="{ x: 0, y: tableHeight }"
              :pagination="false"
              showSearchBorder></BMTable>
          </div>
        </div>
        <Button class="msg-btn" type="primary" :loading="saveLoading" @click="saveTeam(false)">
          {{ t('保存') }}
        </Button>
      </div>
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import { ref, onMounted } from 'vue';
  import {
    Breadcrumb,
    BreadcrumbItem,
    Button,
    Descriptions,
    DescriptionsItem,
    Select,
    SelectOption,
  } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { Modal, message } from 'ant-design-vue';
  import { BMTable, TableInstance } from '@bmos/components';
  import { descriptionsDataType, descriptionsDomType } from '../types/index';
  import type { TableColumn } from '@bmos/components';
  import {
    getProcedureDetailById,
    instructionTeamDetail,
    getPlanTeamListByProductPlanId,
    instructionTeamSave,
    instructionTeamConfirm,
    reqFactoryLineListByProcessVersion,
  } from '@/services';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => ({}),
    },
  });
  const emit = defineEmits(['close']);
  const descriptionsDom = ref<descriptionsDomType[]>([
    {
      label: t('产品名称'),
      key: 'productName',
    },
    {
      label: t('产品编码'),
      key: 'productMergeCode',
    },
    {
      label: t('产品规格'),
      key: 'productSpecification',
    },
    {
      label: t('生产工艺'),
      key: 'processName',
    },
    {
      label: t('产线'),
      key: 'productLineName',
    },
    {
      label: t('指令单类型'),
      key: 'type',
    },
    {
      label: t('指令单编号'),
      key: 'planNo',
    },
    {
      label: t('生产批号'),
      key: 'batchNo',
    },
    {
      label: t('生产批量'),
      key: 'productionBatch',
    },
    {
      label: t('计划生产时间'),
      key: 'productDate',
    },
  ]); //信息展示DOM
  const descriptionsData = ref<descriptionsDataType>(); //信息展示数据
  const nodeMsg = ref({
    name: '',
    code: '',
  }); //当前工序数据
  const tableHeight = ref<number | string>(); //表格滑动高度
  const teamTable = ref<TableInstance>();
  const tableData = ref<any>([]);
  const teamOptions = ref<any>([]);
  const timeUnit = ref<any>({
    hour: t('小时'),
    day: t('天'),
    minute: t('分钟'),
  });
  const saveLoading = ref(false); //保存loading
  const allNodeData = ref<any>({}); //所有信息
  const submitLoading = ref(false); //确认指令单
  const columns: TableColumn[] = [
    {
      title: t('步骤/任务'),
      align: 'center',
      dataIndex: 'procedureStepModelName',
    },
    {
      title: t('执行班组'),
      align: 'center',
      dataIndex: 'teamIds',
      customRender: ({ record }) => (
        <div>
          <Select
            v-model:value={record.teamIds}
            mode='multiple'
            style='width: 100%'
            placeholder={t('请选择')}
            optionFilterProp='label'
            showSearch
            getPopupContainer={triggerNode => triggerNode.parentNode}>
            {teamOptions.value.map((item: any) => {
              return (
                <SelectOption value={item.id} label={item.name}>
                  {item.name}-{item.code}
                </SelectOption>
              );
            })}
          </Select>
        </div>
      ),
    },
    {
      title: t('执行时长'),
      align: 'center',
      dataIndex: 'date',
      customRender: ({ record }) => (
        <div>
          {!!record.procedureStepTime && (
            <span>
              {record.procedureStepTime}
              {timeUnit.value[record.procedureStepTimeUnit]}
            </span>
          )}
        </div>
      ),
    },
  ];
  onMounted(async () => {
    // 给表格加滑动高度
    tableHeight.value = (document.getElementsByClassName('msg-main')[0] as HTMLDivElement)?.offsetHeight - 100 || 270;
    nextTick(() => {
      (document.getElementsByClassName('mes-table-body')[1] as HTMLDivElement).style.height = `${tableHeight.value}px`;
    });
    // 指令单确认详情
    const { data } = await instructionTeamDetail(props.rowData.id);
    const { data: productLineList } = await reqFactoryLineListByProcessVersion({
      id: data.planDetailVO.processId,
      version: data.planDetailVO.processVersion,
    });
    const productLineName = productLineList?.find((item: any) => data.planDetailVO.productionLineId === item.id)?.name;
    // 回显数据
    descriptionsData.value = {
      ...data.planDetailVO, //TODO 回显生产批量信息待对接等后端加
      productionBatch: data.planDetailVO.batchQuantity + data.planDetailVO.unitName, //生产批量
      productLineName, //产线
      type: data.planDetailVO.type.label, //指令单类型
    } as descriptionsDataType;
    nodeMsg.value = {
      name: data.instructionVO.procedureModelName,
      code: data.instructionVO.procedureModelCode,
    };
    // 可选班组集合为
    const { data: team_options } = await getPlanTeamListByProductPlanId({
      productPlanId: data.instructionVO.productPlanId,
    });
    teamOptions.value = team_options;
    // 回显表格
    tableData.value = data.instructionVO.teams.map((item: any) => {
      if (!item.teamIds) {
        item.teamIds = [];
      }
      return item;
    });
    allNodeData.value = data.instructionVO;
  });
  /*
      事件
    */
  //  点击返回按钮
  const goBack = () => {
    emit('close');
  };
  // 保存班组
  const saveTeam = async (type = false) => {
    let flag = false;
    const details = tableData.value.map((item: any, index: number) => {
      if (item.teamIds?.length == 0) {
        flag = true;
      }
      return {
        ...item,
        nodeStepId: item.nodeStepId,
        teamIds: item.teamIds,
        nodeId: allNodeData.value.nodeId,
        sort: index + 1,
      };
    });
    if (!flag) {
      // 暂存
      try {
        saveLoading.value = true;
        const data = {
          details,
          instructionId: props.rowData.id, //指令单id
          nodeId: allNodeData.value.nodeId, //生产工序节点id
          procedureId: allNodeData.value.procedureId,
          procedureModelId: allNodeData.value.procedureModelId,
          productPlanId: allNodeData.value.productPlanId, //生产计划id
        };
        if (type) {
          // 确认指令单
          await instructionTeamConfirm(data);
          goBack();
          sendMessage(MessageType.UpdateMessageCount);
          message.success(t('确认指令单成功'));
        } else {
          // 保存指令单
          await instructionTeamSave(data);
          message.success(t('指令单保存成功'));
        }
      } catch (error: any) {
        error.message && message.error(error.message);
      } finally {
        saveLoading.value = false;
        submitLoading.value = false;
      }
    } else {
      // 含有未选择班组框,提示
      Modal.error({
        title: t('警告'),
        content: t('工序步骤需配置班组'),
        okText: t('确定'),
      });
    }
  };
</script>
<style lang="less" scoped>
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 10px;
    height: 38px;
    margin-bottom: 10px;
  }
  .main {
    background-color: white;
    overflow: hidden;
    height: calc(100% - 48px);
    .msg-box {
      width: 100%;
      height: calc(100% - 90px);
      background-color: white;
      padding: 10px;
      .msg-card {
        margin-bottom: 10px;
        .msg-card-header {
          padding: 10px 20px;
          border-bottom: 1px solid #e1e3e5;
          margin-bottom: 10px;
          position: relative;
          &::before {
            content: '';
            position: absolute;
            width: 4px;
            height: 16px;
            left: 10px;
            top: 13px;
            background-color: #2871ff;
          }
        }
        .msg-select-box {
          padding-top: 10px;
          & > span {
            margin: 0 15px;
          }
        }
        .msg-btn {
          margin: 40px 0 0 10px;
        }
        .msg-team-config {
          height: calc(100% - 40px);
        }
      }
      .msg-main {
        height: calc(100% - 185px);
      }
    }
  }
  .des_box {
    height: 125px;
    padding: 10px 15px;
    box-sizing: border-box;
    border-bottom: 4px solid #f2f3f5;
  }
  #graph-container {
    width: 100%;
    height: calc(100% - 125px);
  }
  :deep(.mes-table-cell) {
    overflow: visible;
  }
  :deep(.bmos-table) {
  }
</style>
