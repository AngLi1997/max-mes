<!-- 大屏 -->
<template>
  <div class="bg">
    <div class="content">
      <div class="filter">
        <BMForm ref="formRef" v-bind="filterFormProps" @submit="filterSubmit" @reset="filterRet" />
      </div>
      <!-- 生产进度 -->
      <div v-show="showProgress">
        <!-- 工艺流程 -->
        <div class="filterContent-process">
          <div class="processFlow"></div>
          <div class="processFlow-content">
            <!-- 第一行 -->
            <div class="firstLine">
              <div v-for="(item, index) in firstLineProcedure?.concat(firstLineProcedure)?.slice(0, -1)" :key="index">
                <div :class="handleFirst(index)">
                  {{
                    index % 2 === 0
                      ? firstLineProcedure[index / 2]?.customName || firstLineProcedure[index / 2]?.procedureName
                      : ''
                  }}
                </div>
              </div>
            </div>
            <!-- 第二行 -->
            <div v-if="secondLineProcedure?.length > 0">
              <div style="display: flex; flex-direction: row-reverse">
                <div class="up-arrow"></div>
              </div>
              <div class="secondLine" style="flex-direction: row-reverse; margin-top: 15px">
                <div v-for="(item, index) in 27" :key="index">
                  <div :class="handleSecond(index)">
                    {{
                      index % 2 == 0
                        ? secondLineProcedure?.[index / 2]?.customName ||
                          secondLineProcedure?.[index / 2]?.procedureName
                        : ''
                    }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 生产批次 -->
        <div class="filterContent-batch">
          <div class="productionBatch"></div>
          <div
            class="scroll"
            :style="{ height: secondLineProcedure?.length > 0 ? 'calc(100vh - 485px)' : 'calc(100vh - 410px)' }">
            <div v-for="(item, index) in planStatisticList" :key="index" class="productionBatch-content">
              <div class="batchOutline">{{ item?.batchNo }}</div>
              <!-- 第一行 -->
              <div class="firstLine">
                <div
                  v-for="(stepItem, stepIndex) in item.stateList
                    ?.slice(0, 14)
                    .concat(item.stateList?.slice(0, 14))
                    ?.slice(0, -1)"
                  :key="stepIndex"
                  :class="
                    handleBatchFirst(
                      item.stateList?.slice(0, 14).concat(item.stateList?.slice(0, 14))?.slice(0, -1), //第一行数据
                      stepIndex,
                    )
                  "></div>
              </div>
              <!-- 第二行 -->
              <div v-if="item.stateList?.slice(14, 28)?.length > 0">
                <div style="display: flex; flex-direction: row-reverse">
                  <div
                    :class="[
                      'up-arrow',
                      planStatisticList[index].stateList[13]?.value === 2 &&
                      planStatisticList[index].stateList[14]?.value === 2
                        ? 'blue'
                        : '',
                    ]"></div>
                </div>
                <div class="secondLine" style="flex-direction: row-reverse; margin-top: 26px">
                  <div
                    v-for="(stepItem, stepIndex) in 27"
                    :key="stepIndex"
                    :class="[
                      handleBatchSecond(stepIndex, item.stateList?.slice(14, 28)),
                      handleColor(stepIndex, item.stateList?.slice(14, 28)),
                    ]"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 生产工序 -->
      <div v-show="!showProgress">
        <div class="filterContent-procedure">
          <div class="productionProcedure"></div>
          <div class="procedure-box">
            <RowProcedure
              v-for="(item, index) in lineNumber"
              :key="index"
              :lineIndex="`${index}`"
              :reverse="index % 2 === 0 ? false : true"
              :rowData="procedureStatisticList?.slice(index * 3, (index + 1) * 3)"></RowProcedure>
          </div>
        </div>
      </div>
    </div>
    <!-- 切换 -->
    <div class="button-box">
      <div :class="['button', showProgress ? 'activeButton1' : 'button1']" @click="viewProgress"></div>
      <div :class="[!showProgress ? 'activeButton2' : 'button2', 'button']" @click="viewProcedure"></div>
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { onMounted, ref } from 'vue';
  import RowProcedure from './components/RowProcedure.vue';
  import { getProcessListTreeReq, getFactoryLineListByProcessVersion, postProgressDashboardDetail } from '@/services';
  import { loopSelectableNotValueTree } from '@bmos/utils';
  import { findNodeByValue } from './utils';
  import { BMForm, FormProps, RenderCallbackParams } from '@bmos/components';
  import { message } from 'ant-design-vue';
  const route = useRoute();

  // 筛选表单
  const filterFormProps: Ref<FormProps> = ref({
    showAdvancedButton: false,
    baseColProps: {
      span: 4,
    },
    actionColOptions: {
      span: 16,
    },
    resetButtonOptions: {
      class: 'reset-btn',
    },
    submitButtonOptions: {
      class: 'submit-btn',
    },
    schemas: [
      {
        field: 'processId',
        component: 'TreeSelect',
        label: t('工艺'),
        noFormItemMarginBottom: true,
        formItemProps: {
          hasFeedback: false,
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            maxTagCount: 'responsive',
            treeNodeFilterProp: 'showName',
            treeData: [],
            onChange: async (val: any) => {
              formModel.lineIds = [];
              if (!val) {
                formRef.value?.updateSchema({
                  field: 'lineIds',
                  componentProps: {
                    options: [],
                  },
                });
                return;
              }
              await getLineList(val);
            },
          };
        },
      },
      {
        field: 'lineIds',
        component: 'Select',
        label: t('产线'),
        componentProps: () => {
          return {
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            maxTagCount: 'responsive',
            mode: 'multiple',
            treeNodeFilterProp: 'name',
            options: [],
          };
        },
      },
    ],
  });
  const formRef = ref<any>();
  const treeData = ref<any>(); //工艺树
  const lineNumber = ref<any>(); //展示多少行工序

  // 获取数据
  const getData = async () => {
    try {
      const res = await formRef.value?.validate();
      if (!res.processId) return message.error(t('请先选择工艺'));
      const { data } = await postProgressDashboardDetail({ ...res });
      procedureStatisticList.value = data?.procedureStatisticList;
      lineNumber.value = Math.ceil(procedureStatisticList.value?.length / 3);
      firstLineProcedure.value = procedureStatisticList.value?.slice(0, 14);
      secondLineProcedure.value = procedureStatisticList.value?.slice(14, 28);
      planStatisticList.value = data?.planStatisticList;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 获取工艺
  const getProcessList = async () => {
    try {
      const { data } = await getProcessListTreeReq({ activeProcess: true });
      treeData.value = data;
      formRef.value?.updateSchema({
        field: 'processId',
        componentProps: {
          treeData: loopSelectableNotValueTree(data, 'isFlag', true),
        },
      });
    } catch (error) {}
  };

  //通过工艺获取对应的产线
  const getLineList = async (processId: any) => {
    const foundNode = findNodeByValue(treeData.value, processId);
    const { data } = await getFactoryLineListByProcessVersion({
      id: foundNode.id,
      version: foundNode.activeVersion,
    });
    formRef.value?.updateSchema({
      field: 'lineIds',
      componentProps: {
        options: data,
      },
    });
  };
  // 筛选
  const filterSubmit = () => {
    getData();
  };
  // 重置
  const filterRet = async () => {
    formRef.value?.updateSchema({
      field: 'lineIds',
      componentProps: {
        options: [],
      },
    });
    // getData();
  };
  const showProgress = ref<boolean>(true);
  //测试数据
  const _testData = ref<any>({
    //总数居
    processId: '1873970901419167744',
    processName: '数据看板数据点测试工艺',
    lineStatistics: [
      {
        lineId: '1809164887079915520',
        lineName: '老王的办公室',
        inProgressCount: '3',
        completedCount: '1',
      },
    ],
    procedureStatisticList: [
      {
        procedureId: '1873970902031536128',
        procedureName: '工序节点1',
        customName: '第一个工序',
        batchNoList: [
          '改革春风吹满地中国人民真整齐',
          '齐心协力跨世纪',
          '一场大水没咋地',
          '改革春风吹满地',
          '齐心协力跨世纪',
          '一场大水没咋地',
          '改革春风吹满地',
          '齐心协力跨世纪',
          '一场大水没咋地',
        ],
      },
      {
        procedureId: '1875061229622530050',
        procedureName: '工序节点2',
        customName: '第二个工序',
        batchNoList: ['批号一', '批号二', '批号三'],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点3',
        customName: '第三个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点4',
        customName: '第四个工序',
        batchNoList: ['齐心协力跨世纪', '一场大水没咋地'],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点5',
        customName: '第五个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点6',
        customName: '第六个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点7',
        customName: '第七个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点8',
        customName: '第八个工序',
        batchNoList: ['床前明月光', '疑是地上霜', '巨头望明月', '低头思故乡'],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点9',
        customName: '第九个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点10',
        customName: '第10个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点11',
        customName: '第11个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点12',
        customName: '第12个工序',
        batchNoList: ['举头望明月发动飞洒飞洒发飞洒飞洒发', '低头思故乡'],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点13',
        customName: '第13个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点14',
        customName: '第14个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点15',
        customName: '第15个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点16',
        customName: '第16个工序',
        batchNoList: [],
      },
      {
        procedureId: '33333',
        procedureName: '工序节点17',
        customName: '第17个工序',
        batchNoList: [],
      },
    ],
    planStatisticList: [
      {
        planId: '1875062426538479616',
        batchNo: '改革春风吹满地',
        stateList: [
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 1,
            label: '进行中',
            name: '进行中',
          },
          {
            value: 0,
            label: '未激活',
            name: '未激活',
          },
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 3,
            label: '已结束',
            name: '已结束',
          },
          {
            value: 0,
            label: '未激活',
            name: '未激活',
          },
          {
            value: 0,
            label: '未激活',
            name: '未激活',
          },
          {
            value: 0,
            label: '未激活',
            name: '未激活',
          },
          {
            value: 0,
            label: '未激活',
            name: '未激活',
          },
          {
            value: 0,
            label: '未激活',
            name: '未激活',
          },
          {
            value: 0,
            label: '未激活',
            name: '未激活',
          },
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 1,
            label: '进行中',
            name: '进行中',
          },
        ],
      },
      {
        planId: '1875069406095216640',
        batchNo: '齐心协力跨世纪',
        stateList: [
          {
            value: 1,
            label: '进行中',
            name: '进行中',
          },
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
        ],
      },
      {
        planId: '1875069406409789440',
        batchNo: '一场大水没咋地',
        stateList: [
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 1,
            label: '进行中',
            name: '进行中',
          },
        ],
      },
      {
        planId: '1875069406409789440',
        batchNo: '一场大水没咋地',
        stateList: [
          {
            value: 2,
            label: '进行中',
            name: '进行中',
          },
          {
            value: 2,
            label: '未激活',
            name: '未激活',
          },
        ],
      },
      {
        planId: '1875069406409789440',
        batchNo: '一场大水没咋地',
        stateList: [
          {
            value: 2,
            label: '已完成',
            name: '已完成',
          },
          {
            value: 0,
            label: '未激活',
            name: '未激活',
          },
        ],
      },
      {
        planId: '1875069406409789440',
        batchNo: '一场大水没咋地',
        stateList: [
          {
            value: 2,
            label: '进行中',
            name: '进行中',
          },
          {
            value: 2,
            label: '未激活',
            name: '未激活',
          },
        ],
      },
      {
        planId: '1875069406409789440',
        batchNo: '一场大水没咋地',
        stateList: [
          {
            value: 2,
            label: '进行中',
            name: '进行中',
          },
          {
            value: 2,
            label: '未激活',
            name: '未激活',
          },
        ],
      },
      {
        planId: '1875069406409789440',
        batchNo: '一场大水没咋地',
        stateList: [
          {
            value: 2,
            label: '进行中',
            name: '进行中',
          },
          {
            value: 3,
            label: '已结束',
            name: '已结束',
          },
        ],
      },
    ],
  });
  //工艺流程数据
  const procedureStatisticList = ref<any>();
  const firstLineProcedure = ref<any>();
  const secondLineProcedure = ref<any>();
  //生产批次数据
  const planStatisticList = ref<any>();
  const handleFirst = (index: any) => {
    return index % 2 === 0 ? 'procedureName' : 'arrow';
  };
  const handleSecond = (index: any) => {
    if (index > (secondLineProcedure.value?.length - 1) * 2 && index % 2 !== 0) {
      return 'opposite-arrow hidden';
    }
    if (index > (secondLineProcedure.value?.length - 1) * 2 && index % 2 === 0) {
      return 'procedureName hidden';
    }
    return index % 2 === 0 ? 'procedureName' : 'opposite-arrow';
  };
  // 每个批次的第一行
  const handleBatchFirst = (firstLineData: any, stepIndex: any) => {
    if (stepIndex % 2 === 0) {
      // 点样式
      switch (firstLineData?.[stepIndex / 2]?.value) {
        case 0:
          return 'dot';
        case 1:
          return 'dot green';
        case 2:
          return 'dot blue';
        case 3:
          return 'dot yellow';
        default:
          break;
      }
    } else {
      // 线样式
      if (firstLineData[(stepIndex - 1) / 2]?.value === 2 && firstLineData[(stepIndex + 1) / 2]?.value === 2) {
        return 'line blue';
      }
    }
    return stepIndex % 2 === 0 ? 'dot' : 'line';
  };
  const handleBatchSecond = (stepIndex: any, secondLineData: any) => {
    if (stepIndex > (secondLineData?.length - 1) * 2 && stepIndex % 2 !== 0) {
      return 'line hidden';
    }
    if (stepIndex > (secondLineData?.length - 1) * 2 && stepIndex % 2 === 0) {
      return 'dot hidden';
    }
    return stepIndex % 2 === 0 ? 'dot' : 'line';
  };
  const handleColor = (stepIndex: any, secondLineData: any) => {
    if (stepIndex % 2 === 0) {
      // 点样式
      switch (secondLineData?.[stepIndex / 2]?.value) {
        case 0:
          return 'dot';
        case 1:
          return 'dot green';
        case 2:
          return 'dot blue';
        case 3:
          return 'dot yellow';
        default:
          break;
      }
    } else {
      // 线样式
      if (secondLineData[(stepIndex - 1) / 2]?.value === 2 && secondLineData[(stepIndex + 1) / 2]?.value === 2) {
        return 'line blue';
      }
    }
  };
  // 生产进度按钮
  const viewProgress = () => {
    showProgress.value = true;
  };
  // 生产工序按钮
  const viewProcedure = () => {
    showProgress.value = false;
  };
  onMounted(async () => {
    // //测试展示数据
    // procedureStatisticList.value = testData.value.procedureStatisticList;
    // lineNumber.value = Math.ceil(procedureStatisticList.value?.length / 3);
    // firstLineProcedure.value = procedureStatisticList.value?.slice(0, 14);
    // secondLineProcedure.value = procedureStatisticList.value?.slice(14, 28);
    // planStatisticList.value = testData.value.planStatisticList;
    //初始化展示默认值
    await getProcessList();
    const data = route.query; //上个页面带过来的值
    try {
      data.lineIds = JSON.parse(data.lineIds as string);
    } catch (error) {
      data.lineIds = [];
    }
    formRef.value?.setFormModels({ processId: data.processId || '', lineIds: data.lineIds || [] });
    if (data.processId) {
      await getLineList(data.processId);
      await getData();
    }
  });
</script>
<style lang="less">
  .dc-content {
    padding: 0px !important;
    background: radial-gradient(61.36% 50% at 50% 50%, #346 0%, #292c33 100%);
  }
</style>
<style scoped lang="less">
  .bg {
    width: 100%;
    overflow-x: scroll;
    height: 100%;
    background: url('/src/assets/ProductionSchedule/bg.png') no-repeat center/cover;
    background-size: 100% 100%;
    .content {
      width: 100%;
      padding: 20px;
      box-sizing: border-box;
      .filter {
        width: 100%;
        height: 80px;
        padding: 20px;
        margin-bottom: 20px;
        background: url('/src/assets/ProductionSchedule/filter.png') repeat-y center/cover;
        background-size: 100% 100%;
      }
      // 工艺流程筛选内容
      .filterContent-process {
        width: 100%;
        padding: 0px 10px;
        box-sizing: border-box;
        .processFlow {
          width: 134px;
          height: 22px;
          margin-bottom: 20px;
          background: url('/src/assets/ProductionSchedule/processFlow.png') repeat-y center/cover;
        }
        .processFlow-content {
          width: 100%;
          // 向下箭头
          .up-arrow {
            width: 16px;
            height: 16px;
            background: url('/src/assets/ProductionSchedule/arrow.png') repeat-y center/cover;
            transform: rotate(90deg) translateX(8px) translateY(40px);
          }
          .firstLine,
          .secondLine {
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;
            .procedureName {
              width: 96px;
              height: 46px;
              line-height: 46px;
              padding: 0px 10px;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
              color: white;
              text-align: center;
              box-sizing: border-box;
              background: url('/src/assets/ProductionSchedule/procedureOutline.png') repeat-y center/cover;
            }
            .arrow {
              width: 16px;
              height: 16px;
              margin: 0px 11.9px;
              background: url('/src/assets/ProductionSchedule/arrow.png') repeat-y center/cover;
            }

            // 反箭头
            .opposite-arrow {
              width: 16px;
              height: 16px;
              margin: 0px 11.9px;
              background: url('/src/assets/ProductionSchedule/arrow.png') repeat-y center/cover;
              transform: rotate(180deg);
            }
            //隐藏
            .hidden {
              visibility: hidden;
            }
          }
        }
      }
      // 生产批次筛选内容
      .filterContent-batch {
        width: 100%;
        padding: 0px 10px;
        box-sizing: border-box;
        .productionBatch {
          width: 134px;
          height: 22px;
          margin-top: 20px;
          margin-bottom: 10px;
          background: url('@/assets/ProductionSchedule/productionBatch.png') repeat-y center/cover;
        }
        .scroll {
          overflow-y: scroll;
        }
        .productionBatch-content {
          width: 100%;
          margin-top: 15px;
          .batchOutline {
            width: 160px;
            height: 40px;
            line-height: 40px;
            padding: 0px 10px;
            margin-bottom: 10px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            color: white;
            text-align: left;
            box-sizing: border-box;
            background: url('/src/assets/ProductionSchedule/batchOutline.png') repeat-y center/cover;
          }
          .up-arrow {
            width: 30px;
            height: 4px;
            background-color: #405a80;
            transform: rotate(90deg) translateX(13px) translateY(28px);
          }
          .blue {
            background: linear-gradient(323.29deg, #81caff -11.89%, #44a2ff 81.82%);
          }
          .firstLine,
          .secondLine {
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0px 36px;
            .dot {
              width: 16px;
              height: 16px;
              min-width: 16px;
              border-radius: 50%;
              background: #4d6280;
            }
            //已完成
            .blue {
              background: linear-gradient(323.29deg, #81caff -11.89%, #44a2ff 81.82%);
            }
            //进行中
            .green {
              background: linear-gradient(180deg, #2d9533 0%, #60ba65 100%);
            }
            // 已结束
            .yellow {
              background: linear-gradient(344.65deg, #e1b975 7.87%, #dfac56 102.96%);
            }

            .line {
              width: 100%;
              height: 4px;
              background-color: #405a80;
              overflow: hidden;
            }
            //隐藏
            .hidden {
              visibility: hidden;
            }
          }
        }
      }
      // 生产工序筛选内容
      .filterContent-procedure {
        width: 100%;
        padding: 0px 10px;
        box-sizing: border-box;
        .productionProcedure {
          width: 134px;
          height: 22px;
          margin-top: 20px;
          margin-bottom: 10px;
          background: url('/src/assets/ProductionSchedule/productionProcedure.png') repeat-y center/cover;
        }
        .procedure-box {
          padding: 0px 30px;
          height: calc(100vh - 300px);
          overflow-y: scroll;
        }
      }
    }

    // 切换
    .button-box {
      display: flex;
      position: fixed;
      left: 42%;
      bottom: 20px;
      .button {
        width: 130px;
        height: 32px;
        cursor: pointer;
        margin-right: 30px;
      }
      .activeButton1 {
        background: url('/src/assets/ProductionSchedule/activeButton1.png') no-repeat center/cover;
      }
      .button1 {
        background: url('/src/assets/ProductionSchedule/button1.png') no-repeat center/cover;
      }
      .activeButton2 {
        background: url('/src/assets/ProductionSchedule/activeButton2.png') no-repeat center/cover;
      }
      .button2 {
        background: url('/src/assets/ProductionSchedule/button2.png') no-repeat center/cover;
      }
    }
  }
  :deep(.reset-btn) {
    border: none;
    color: #86a7bf;
    padding: 8px 16px;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
    width: 80px;
    height: 36px;
    flex-shrink: 0;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    text-decoration: none;
    font-size: 16px;
    background: url(../environmentalMonitoring/assets/normalBtn.svg) no-repeat;
    background-size: cover;
    transition: background 0.3s ease;
  }
  :deep(.submit-btn) {
    border: none;
    color: #86a7bf;
    padding: 8px 16px;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
    width: 80px;
    height: 36px;
    flex-shrink: 0;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    text-decoration: none;
    font-size: 16px;
    background: url(../environmentalMonitoring/assets/normalBtn.svg) no-repeat;
    background-size: cover;
    transition: background 0.3s ease;
  }
  :deep(.submit-btn:hover) {
    color: #fff;
    background: url(../environmentalMonitoring/assets/hoverBtn.svg);
    background-size: cover;
  }
  :deep(.reset-btn:hover) {
    color: #fff;
    background: url(../environmentalMonitoring/assets/hoverBtn.svg);
    background-size: cover;
  }
  :deep(.dc-form-item .dc-form-item-label > label) {
    color: #fff;
  }
  :deep(.dc-select:not(.dc-select-customize-input) .dc-select-selector) {
    border-radius: 4px;
    border: 1px solid rgba(65, 159, 255, 0.3);
    background: rgba(204, 229, 255, 0.15);
  }
  :deep(.dc-select-single .dc-select-selector) {
    color: #fff;
  }
  :deep(.dc-select) {
    color: #fff;
  }
  :deep(.dc-select-selection-item-remove .anticon-close) {
    color: #fff;
  }
  :deep(.dc-select .dc-select-clear) {
    background: transparent;
  }
  :deep(.dc-select .dc-select-clear:hover) {
    color: #fff;
  }
</style>
