<template>
  <div class="header">
    <Breadcrumb>
      <BreadcrumbItem>{{ t('指令单分解') }}</BreadcrumbItem>
      <BreadcrumbItem v-if="showType == 'view'">
        {{ t('查看详情') }}
      </BreadcrumbItem>
      <BreadcrumbItem v-else>{{ t('分解') }}</BreadcrumbItem>
    </Breadcrumb>
    <div>
      <Button style="margin-right: 10px" @click="goBack">
        {{ t('返回') }}
      </Button>
      <Button v-if="showType == 'decompose'" type="primary" @click="planGenerate">
        {{ t('指令单生成') }}
      </Button>
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
    <Flow
      ref="flowInstance"
      :modalJson="flowJson"
      isView
      class="flow"
      :isShowLeftToolBar="false"
      :isShowTopToolBar="false"
      :isTransform="false"
      :mouseenter="() => {}" />
    <PromptBox></PromptBox>
    <div :class="{ 'msg-box': true, 'show-box': isClick }">
      <div class="msg-card">
        <div class="msg-card-header">{{ t('工序信息') }}</div>
        <Descriptions :column="3" :labelStyle="{ color: '#606266' }">
          <DescriptionsItem :label="t('工序名称')">
            {{ clickNodeMsg.label }}
          </DescriptionsItem>
          <DescriptionsItem :label="t('工序阶段编码')">
            {{ clickNodeDetail.stageCode }}
          </DescriptionsItem>
        </Descriptions>
      </div>
      <div class="msg-card">
        <div class="msg-card-header">{{ t('工序负责人') }}</div>
        <div class="msg-select-box">
          <div>
            <span class="label">{{ t('工序负责人') }}</span>
            <Input v-model:value="clickNodeDetail.principalName" :disabled="true" style="width: 200px" />
          </div>
          <div v-if="clickNodeMsg.status?.value == 'CONFIRM'">
            <span class="label">{{ t('工序确认人') }}</span>
            <Input v-model:value="clickNodeMsg.confirmUserName" :disabled="true" style="width: 200px" />
          </div>
        </div>
      </div>
      <div v-if="clickNodeMsg.status?.value == 'CONFIRM'" class="msg-card msg-main">
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
            :pagination="false"
            :scroll="{ x: 400, y: 400 }"
            showSearchBorder></BMTable>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import { ref } from 'vue';
  import { Breadcrumb, BreadcrumbItem, Button, Descriptions, DescriptionsItem } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { BMTable, TableInstance } from '@bmos/components';
  import { descriptionsDataType, descriptionsDomType, procedureListItemType } from '../types/index';
  import ItemNode from './components/ItemNode.vue';
  import PromptBox from './components/PromptBox.vue';
  import type { TableColumn } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import Flow from '@/components/Flow';
  import { FlowInstanceType } from '@/components/Flow/type';
  import {
    planInstructionGenerate,
    getProcedureList,
    planTeamList,
    reqPlatformRoleDetail,
    reqFactoryLineListByProcessVersion,
  } from '@/services';

  const emit = defineEmits(['close']);
  const props = defineProps({
    showType: {
      type: String,
      default: '',
    },
    modalJson: {
      type: Array,
      default: () => [],
    },
    rowData: {
      type: Object,
      default: () => ({}),
    },
  });
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
  // 流程图
  const flowInstance = ref<FlowInstanceType>();
  const lastNode = ref<any>(''); //上一个点击节点
  const isClick = ref(false); //是否已经有点击节点
  const clickNodeMsg = ref<any>({}); //点前点击节点数据
  const teamTable = ref<TableInstance>();
  const tableData = ref<any>([]);
  // 班组列表
  const teamOptions = ref<any>({});
  const columns: TableColumn[] = [
    {
      title: t('步骤名称'),
      align: 'center',
      dataIndex: 'procedureStepModelName',
    },
    {
      title: t('执行班组'),
      align: 'center',
      dataIndex: 'teamIds',
      customRender: ({ record }) => (
        <div class='table-team'>
          {record.teamIds.map((item: any, index: number) => {
            return (
              <span>
                {index == 0 ? '' : '，'}
                {teamOptions.value[item]}
              </span>
            );
          })}
        </div>
      ),
    },
    {
      title: t('执行时长'),
      align: 'center',
      dataIndex: 'procedureStepTime',
      customRender: ({ record }) => (
        <div>
          {record.procedureStepTime}
          {timeTypes[record.procedureStepTimeUnit]}
        </div>
      ),
    },
  ];
  // 流程图数据
  const flowJson = ref<any>();
  // 工序集合
  const procedureList = ref<procedureListItemType[]>([]);
  // 当前点击节点详情
  const clickNodeDetail = ref<any>({});
  const timeTypes: any = {
    hour: t('小时'),
    day: t('天'),
    minute: t('分钟'),
  };
  onMounted(async () => {
    flowJson.value = props.modalJson;
    // 流程图节点
    flowInstance.value?.register({
      shape: 'custom-vue-item-node',
      component: ItemNode,
    });
    const res = await reqFactoryLineListByProcessVersion({
      id: props.rowData?.processId,
      version: props.rowData?.processVersion,
    });
    const productLineName = res.data?.find((item: any) => props.rowData.productionLineId === item.id)?.name;
    descriptionsData.value = {
      ...props.rowData,
      type: props.rowData.type.label, //指令单类型
      productionBatch: props.rowData.batchQuantity + props.rowData.unitName, //生产批量
      productLineName, //产线
    } as descriptionsDataType;
    // 给表格加滑动高度
    const { data } = await getProcedureList({
      processId: props.rowData.processId,
      version: props.rowData.processVersion,
    });
    procedureList.value = data;
    // 添加节点点击事件
    flowInstance.value?.graph?.on('node:click', async (e: any) => {
      if (isClick.value) {
        // 清空上一个焦点\详情弹窗数据
        clickNode();
      }
      if (e.node.port.ports[0]?.id == 'end-port' || e.node.port.ports[0]?.id == 'start-port') {
        // 开始或结束不展示
        return;
      }
      // 暂存当前点击节点
      lastNode.value = e.node;
      // 焦点入焦
      e.node.setData({
        isClick: true,
      });
      // 详情窗展示数据
      // 回显名称等
      clickNodeMsg.value = e.node.data;
      // 查找该节点下负责人可选角色
      clickNodeDetail.value = procedureList.value.find(item => item.nodeId == e.node.id);
      const { data: roleDetail } = await reqPlatformRoleDetail(clickNodeDetail.value.principal);
      clickNodeDetail.value.principalName = roleDetail.roleName;
      if (e.node.data.status?.value == 'CONFIRM') {
        // 已确认回显班组
        tableData.value = [...e.node.data.teams];
      }
      setTimeout(() => {
        isClick.value = true;
      }, 200);
    });
    // 可选班组集合为
    const { data: teamData } = await planTeamList({
      status: 'TRUE',
    });
    teamData.map((item: any) => {
      teamOptions.value[item.id] = item.name + '-' + item.code;
    });
  });
  /*
      事件
    */
  //  点击返回按钮
  const goBack = () => {
    emit('close');
  };
  // 点击节点焦点入焦
  const clickNode = () => {
    // 清空上一次点击焦点数据
    lastNode.value.setData({
      isClick: false,
    });
    // 弹窗关闭
    isClick.value = false;
  };
  // 指令单生成
  const planGenerate = async () => {
    try {
      await planInstructionGenerate(props.rowData.planDetailVO.id);
      message.success(t('指令单生成成功'));
      emit('close');
      sendMessage(MessageType.UpdateMessageCount);
    } catch (error: any) {
      message.error(error.message);
    }
  };
</script>
<style lang="less" scoped>
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 10px;
    height: 30px;
    margin-bottom: 10px;
  }
  .main {
    height: calc(100% - 40px);
    background-color: white;
    position: relative;
    overflow: hidden;
    .msg-box {
      position: absolute;
      width: 50%;
      height: calc(100% - 125px);
      right: 0;
      bottom: 0;
      background-color: white;
      transition: all 0.3s;
      opacity: 0;
      visibility: hidden;
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
        .msg-team-config {
          height: calc(100% - 52px);
        }
        .msg-select-box {
          display: flex;
          flex-wrap: wrap;
          & > div {
            padding-top: 10px;
          }
          .label {
            margin: 0 15px;
          }
        }
        .msg-btn {
          margin: 40px 0 0 10px;
        }
      }
      .msg-main {
        height: calc(100% - 200px);
      }
    }
    .show-box {
      opacity: 1;
      visibility: visible;
    }
  }
  .des_box {
    height: 125px;
    padding: 10px 15px;
    box-sizing: border-box;
    border-bottom: 4px solid #f2f3f5;
  }
  .graph-container {
    width: 100%;
    height: calc(100% - 125px);
  }
  :deep(.table-team) {
    width: 100%;
    text-wrap: wrap;
    text-align: center;
  }
</style>
