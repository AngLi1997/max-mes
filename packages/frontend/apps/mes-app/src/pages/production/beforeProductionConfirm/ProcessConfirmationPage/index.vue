<template>
  <BMLayout>
    <BMBasicPage
      :title="t('生产前确认')"
      :default-padding="false"
      :loading="loading"
      @left-click="toBack"
      @confirm="confirm"
      @cancel="cancel"
    >
      <view class="tabTitle">
        <wd-tabs v-model="tabSub.index" class="type-tab" @change="({ index }) => tabChange(index)">
          <wd-tab v-for="item in tabSub.list" :key="item.name" :title="item.name" />
        </wd-tabs>
        <view style="width: 100%;height: 1px; margin-bottom:9.38rpx; border-top: 1px solid #E1E3E5;" />
      </view>
      <view class="tabContent">
        <view v-if="tabSub.index === 0" class="container">
          <BMInfoDisplay
            icon="xinxi"
            :title="t('生产信息')"
            :basic-items="basicItems"
            :info-data="planDetailVO"
            background="var(--bmos-bg-form)"
          />
          <view class="section-title">
            {{ t("关联批次") }}
          </view>
          <AssociatedBatchesComponent
            v-if="relatedList.length > 0"
            v-model:data-list="relatedList"
          />
          <view v-else class="no-data">
            {{ t("无关联工艺") }}
          </view>
        </view>
        <view v-if="tabSub.index === 1">
          <view class="sticky-box">
            <wd-sticky :offset-top="offsetTop" :z-index="3">
              <view class="sticky-item">
                <wd-radio-group v-model="activeIndex" shape="button">
                  <wd-radio
                    v-for="(item, index) in instructions"
                    :key="index"
                    :value="index"
                  >
                    {{ item.procedureModelName }}
                  </wd-radio>
                </wd-radio-group>
              </view>
            </wd-sticky>
          </view>
          <view class="container">
            <BMInfoDisplay
              :basic-items="[
                {
                  label: t('工序名称'),
                  field: 'procedureModelName',
                },
                {
                  label: t('工序阶段编码'),
                  field: 'procedureModelCode',
                },
              ]"
              :info-data="activeItem"
              background="#F7F8FA"
            >
              <template #title>
                <view class="info-display-title">
                  {{ t("工序信息") }}
                </view>
              </template>
            </BMInfoDisplay>

            <view class="table-team">
              <view class="table-title">
                {{ t("班组配置") }}
              </view>
              <BMTable v-bind="tableProps" />
            </view>
          </view>
        </view>
        <view v-if="tabSub.index === 2">
          <MaterialReservation :confirm-before="true" />
        </view>
        <view v-if="tabSub.index === 3">
          <ManagementPage :confirm-before="true" :production-line-id="productionLineId" />
        </view>
        <view v-if="tabSub.index === 4">
          <EquipmentPage :confirm-before="true" :production-line-id="productionLineId" />
        </view>
      </view>
    </BMBasicPage>
    <BMCheckboxModal
      v-model="selectValue"
      v-model:open="teamShow"
      :title="t('多选弹窗')"
      :options="teamsList"
      :field-names="{
        label: 'name_code',
        value: 'id',
      }"
      @confirm="executiveTeamConfirm"
    />
  </BMLayout>
</template>

<script setup lang="jsx">
import {
  getMesPlanRelationList,
  getProductionInstructionDetailApi,
  getProductionPlanTeamApi,
  saveBeforeProductionConfirmApi,
} from '@/api/productionApi.js';
import { BMBasicPage, BMCheckboxModal, BMInfoDisplay, BMLayout, BMTable } from '@/BMComponents';

import MaterialReservation from '@/pages/businessComponents/materialReservation/index.vue';// 物料预定
import EquipmentPage from '@/pages/equipment/useInfo/index.vue';
import ManagementPage from '@/pages/roomManagement/managementPage/index.vue';
import { debounce } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdIcon from 'wot-design-uni/components/wd-icon/wd-icon.vue';
import AssociatedBatchesComponent from '../../components/associatedBatchesComponent/index.vue';

const props = defineProps({
  id: String,
});

const { showNotify } = useNotify();
// tab选项卡
const tabSub = reactive({
  list: [{
    name: t('关联批次'),
  }, {
    name: t('执行班组'),
  }, {
    name: t('物料预定'),
  }, {
    name: t('房间确认'),
  }, {
    name: t('设备确认'),
  }],
  index: 0,
});

const basicItems = ref([
  {
    label: t('产品名称'),
    field: 'productName',
  },
  {
    label: t('产品编码'),
    field: 'productMergeCode',
  },
  {
    label: t('产品批号'),
    field: 'batchNo',
  },
  {
    label: t('生产工艺'),
    field: 'processName',
  },
]);

const timeTypes = {
  day: t('日'),
  hour: t('时'),
  minute: t('分'),
};
const planDetailVO = ref({});
const instructions = ref([]);
const productionLineId = ref();
const offsetTop = ref(148);
const loading = ref(false);
// #ifdef H5
offsetTop.value = 106;
// #endif
// 关联批次列表
const relatedList = ref([]);
const teamObj = ref({});
const teamsList = ref([]);
const teamShow = ref(false);
const selectValue = ref([]);
const teamKey = ref('');
const activeIndex = ref(0);
const activeItem = computed(() => {
  return instructions.value[activeIndex.value] || {};
});
const tabChange = (val) => {
  tabSub.index = val;
};
const getShowName = (row) => {
  if (row.teamIds.length === 0) {
    return '';
  }
  return row.teamIds.map(item => teamObj.value[item]).join(',');
};
const openTeamRef = (team) => {
  selectValue.value = team.teamIds;
  teamKey.value = team.procedureStepModelId;
  teamShow.value = true;
};
const tableProps = reactive({
  noDataText: t('暂无关联'),
  pagination: false,
  data: [],
  border: true,
  showNoData: true,
  tableColProps: [
    {
      label: t('步骤名称'),
      prop: 'procedureStepModelName',
    },
    {
      label: t('执行时长'),
      prop: 'procedureStepTime',
      customRender: ({ row }) => {
        return (
          <view>
            {row.procedureStepTime}
            {timeTypes[row.procedureStepTimeUnit]}
          </view>
        );
      },
    },
    {
      label: t('执行班组'),
      prop: '',
      customRender: ({ row }) => {
        return (
          <view class="select-box" onClick={() => openTeamRef(row)}>
            <view class="bmos-ellipsis-1">{ getShowName(row) }</view>
            <WdIcon
              name="jiantou-you"
              size="14.06rpx"
              color="#2871ff"
              style="margin-right: 9.38rpx"
              class-prefix="bmos-app-icon"
            />
          </view>
        );
      },
    },
  ],
});

const toBack = () => {
  uni.navigateBack();
};
  // 执行班组弹框确定
const executiveTeamConfirm = () => {
  activeItem.value.teams.forEach((item) => {
    if (item.procedureStepModelId === teamKey.value) {
      item.teamIds = selectValue.value;
    }
  });
};

const cancel = () => {
  uni.navigateBack();
};
  // 确认是否所有工序下所有工序步骤班组都已选择
const checkTeam = () => {
  let flag = true;
  instructions.value.forEach((item) => {
    if (item.teams.length > 0) {
      item.teams.forEach((team) => {
        if (team.teamIds.length === 0) {
          flag = false;
        }
      });
    }
  });
  return flag;
};
  // 构造confirm的数据
const getConfirmData = () => {
  const teamConfirmDTO = [];
  instructions.value.forEach((item) => {
    const data = {
      details: item.teams.map((team) => {
        return {
          procedureStepId: team.procedureStepId,
          nodeStepId: team.nodeStepId,
          procedureStepModelId: team.procedureStepModelId,
          procedureStepModelName: team.procedureStepModelName,
          procedureStepTime: team.procedureStepTime,
          procedureStepTimeUnit: team.procedureStepTimeUnit,
          sort: team.sort,
          teamIds: team.teamIds,
        };
      }),
      instructionId: item.id,
      nodeId: item.nodeId,
      procedureId: item.procedureId,
      procedureModelId: item.procedureModelId,
      productPlanId: item.productPlanId,
    };
    teamConfirmDTO.push(data);
  });

  const relationPlan = [];
  relatedList.value.forEach((item) => {
    item.batchNos
        = item.relationBatchList.map(item2 => item2.planBatchNo) || [];
    item.planIds = item.relationBatchList.map(item2 => item2.planId) || [];
    relationPlan.push({
      processId: item.id,
      planIds: item.planIds,
      batchNos: item.batchNos,
    });
  });
  return {
    planId: planDetailVO.value.id,
    relationPlan,
    teamConfirmDTO,
  };
};
const confirm = debounce(async () => {
  if (!checkTeam()) {
    showNotify({
      type: 'danger',
      message: t('请先选择班组'),
    });
    return;
  }
  const data = getConfirmData();
  loading.value = true;
  try {
    await saveBeforeProductionConfirmApi(data);
    loading.value = false;
    uni.navigateBack();
  }
  catch (err) {
    loading.value = false;
    err.message && showNotify({
      type: 'danger',
      message: err.message,
    });
  }
}, 500);

// 获取生产指令单详情
const getProductionInstructionDetail = async () => {
  const res = await getProductionInstructionDetailApi(props.id);
  const temp = teamsList.value.map(item => item.id);
  planDetailVO.value = res.data.planDetailVO || {};
  productionLineId.value = res.data.planDetailVO?.productionLineId;
  // 过滤掉不在执行班组里的数据
  instructions.value = res.data.instructions.map((item) => {
    const teams = item?.teams.map((item2) => {
      return {
        ...item2,
        teamIds: item2?.teamIds?.filter(item3 => temp.includes(item3)),
      };
    });
    return {
      ...item,
      teams,
    };
  });
  // 获取关联的工艺集合、回显关联批次
  const res2 = await getMesPlanRelationList({ planId: props.id });
  res2.data.forEach((item) => {
    item.name = item.processName;
    item.id = item.processId;
    item.checkedNodes2 = item.relationBatchList
      ?.filter(item => item.related)
      ?.map((item2) => {
        return {
          id: item2.planId,
          batchNo: item2.planBatchNo,
        };
      });
  });
  relatedList.value = res2.data || [];
};
  // 生成班组对象
const createTeamObj = (data) => {
  teamObj.value = {};
  data.forEach((item) => {
    teamObj.value[item.id] = `${item.name}-${item.code}`;
  });
};
  // 获取班组列表
const getProductionPlanTeam = async () => {
  const res = await getProductionPlanTeamApi({
    productPlanId: props.id,
  });
  teamsList.value = ([...res.data] || []).map(item => ({
    ...item,
    name_code: `${item.name}-${item.code}`,
  }));
  createTeamObj(res.data);
};
  // 获取详情相关数据
const getDetailData = async () => {
  await getProductionPlanTeam();
  await getProductionInstructionDetail();
};

watch(() => activeItem.value, () => {
  tableProps.data = activeItem.value.teams || [];
}, { immediate: true });
onMounted(() => {
  getDetailData();
});
</script>

<style lang="scss" scoped>
.section-title {
  margin: 9.38rpx 0;
  font-size: 14.06rpx;
  color: var(--bmos-color-text-title);
  font-weight: 400;
}
.container {
  width: 100%;
  padding: 0 9.38rpx;
  box-sizing: border-box;

  :deep(.table-box) {
    height: unset;
  }

  .no-data {
    width: 100%;
    height: 37.5rpx;
    line-height: 37.5rpx;
    text-align: center;
    font-size: 12.89rpx;
    color: var(--bmos-color-text-sub);
  }

  .info-display-title {
    font-size: 12.89rpx;
    color: var(--bmos-color-text-main);
  }

  .table-team {
    margin-top: 9.38rpx;
    border-radius: 4.69rpx;
    padding: 9.38rpx;
    background-color: #fff;
    box-sizing: border-box;
    .table-title {
      height: 37.5rpx;
      font-size: 12.89rpx;
      color: var(--bmos-color-text-title);
    }
    :deep(.select-box) {
      display: flex;
      justify-content: space-between;
    }
  }
}
.sticky-box {
  width: 100%;
  padding: 9.38rpx 0;
  box-sizing: border-box;
  margin-top: -9.38rpx;
  .sticky-item {
    width: 100vw;
    background-color: #fff;
    padding: 0 9.38rpx;
    box-sizing: border-box;
  }
}
:deep(.wd-tabs__nav-container) {
  border-top: 1px solid #e1e3e5;
}
.tabTitle {
  width: 100%;
  position: fixed;
  top: 46.88rpx;
  z-index: 9;
}
.tabContent {
  padding-top: 50.39rpx;
}
</style>
