import { ref, reactive, onMounted } from 'vue';
import { t } from '@/utils/useBmosI18n.js';
import { BMFormSelect } from '@/BMComponents';
import { useToast } from 'wot-design-uni';
import { getListByProductionLineIds, getInstructionTeamApi, flowChangeTeamApi } from '@/api/productionApi.js';

export const useTable = (queryInfo) => {
  const toast = useToast();
  const tableRef = ref();
  const showChangeTeamModal = ref(false);
  const orderTeamValue = ref([]);
  const stepList = ref([]);
  const clickStepData = ref({});
  const topInfoData = ref({
    name: '',
    code: ''
  });
  const tableData = ref([]);
  const unitOptions = {
    day: t('日'),
    hour: t('时'),
    minute: t('分')
  };
  // 切换工序节点
  const stepItemClick = async(item) => {
    if (!item) {
      return;
    }
    clickStepData.value = item;
    topInfoData.value = {
      name: item.procedureModelName,
      code: item.procedureModelCode
    };
    orderTeamValue.value = [];
    // 获取当前工序可选班组
    const { data } = await getListByProductionLineIds({ lineIds: item.lineIds });
    orderTeamOption.value = data.map((item) => {
      item.name = item.name + '-' + item.code;
      return item;
    });
    tableData.value = [...item.teams];
  };
  // 所有工序步骤切换班组
  const allTeamChange = (val) => {
    const newTeams = [];
    val?.map((item) => {
      newTeams.push(item.id);
    });
    tableData.value = tableData.value.map((item) => {
      if (!item.isFlay) {
        item.teamIds = [...newTeams];
      }
      return item;
    });
  };
  // 确定换班
  const changeTeams = async() => {
    try {
      const changeTeamList = [];
      let flag = false;
      // 遍历每个工序
      stepList.value.map((item) => {
        // 遍历每个工序步骤,将班组信息放置工艺中
        item.teams.map((team) => {
          if (team.teamIds.length !== 0) {
            changeTeamList.push({
              productInstructionTeamId: team.id,
              teamIds: team.teamIds
            });
          } else {
            flag = true;
          }
        });
      });
      if (flag) {
        // 判断执行班组是否都填写了
        toast.error(t('请选择班组'));
        return;
      }
      const params = {
        changeTeamList,
        executionId: queryInfo.value.executionId,
        nodeFunction: queryInfo.value.nodeFunction,
        nodeId: queryInfo.value.nodeId,
        planId: queryInfo.value.planId,
        procedureModelId: queryInfo.value.procedureModelId,
        procedureStepModelId: queryInfo.value.procedureStepModelId,
        processInstanceId: queryInfo.value.processInstanceId,
        procedureChangeNumber: queryInfo.value.procedureChangeNumber,
        processChangeNumber: queryInfo.value.processChangeNumber
      };
      await flowChangeTeamApi(params);
      uni.navigateBack();
    } catch (error) {
      error.message && toast.error(error.message);
    }
  };
  const orderTeamOption = ref([]);
  const tableProps = {
    pagination: false,
    data: [],
    border: true,
    tableColProps: [
      {
        prop: 'procedureStepModelName',
        label: t('步骤/任务'),
        width: 400,
        thProps: {
          align: 'left'
        }
      },
      {
        prop: 'procedureStepTime',
        label: t('执行时长'),
        width: 200,
        thProps: {
          align: 'left'
        },
        customRender: ({ row }) => {
          return <view>{`${row.procedureStepTime || ''}${unitOptions[row.procedureStepTimeUnit] || ''}`}</view>;
        }
      },
      {
        prop: 'teamIds',
        label: t('执行班组'),
        width: 400,
        thProps: {
          align: 'left'
        },
        customRender: ({ row }) => {
          return <BMFormSelect
          v-model={row.teamIds}
          title={t('执行班组')}
          type="checkbox"
          style="width: 100%;"
          options={orderTeamOption.value}
          field-names={{
            label: 'name',
            value: 'id'
          }}
        />;
        }
      }
    ]
  };
  onMounted(async() => {
    // const { data } = await getRoomProcedureList({
    //   processId: queryInfo.value.processId,
    //   version: queryInfo.value.processVersion
    // });
    // stepList.value = data;
    const { data } = await getInstructionTeamApi({
      changeTeamNumber: queryInfo.value.nodeFunction === '3' ? queryInfo.value.procedureChangeNumber : queryInfo.value.processChangeNumber,
      nodeFunction: queryInfo.value.nodeFunction,
      planId: queryInfo.value.planId,
      procedureModelId: queryInfo.value.procedureModelId
    });
    stepList.value = data;
    stepItemClick(data[0]);
  });
  return {
    tableRef,
    tableProps,
    showChangeTeamModal,
    orderTeamValue,
    orderTeamOption,
    stepList,
    clickStepData,
    topInfoData,
    tableData,
    changeTeams,
    stepItemClick,
    allTeamChange
  };
};
