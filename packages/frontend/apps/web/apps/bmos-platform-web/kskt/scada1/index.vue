<template>
  <div v-if="showSetting" class="setting-box">
    <div class="save-button">
      <Button type="primary" @click="save">{{t('保存')}}</Button>
    </div>
    <BMTableTitle :title="t('生产进度配置')" />
    <BMTable
      :columns="columns"
      :dataSource="tableData"
      :pagination="false"
      :search="false"
      :showToolBar="false"
      :scroll="{ x: 400, y: 300 }" />
    <BMModalForm
      ref="modalFormRef"
      v-model:open="open"
      :title="t('关联工艺')"
      :formProps="formProps"
      wrapClassName="modalSizeMedium"
      @okModal="okModal"></BMModalForm>
  </div>
  <div v-else class="scada1_wrap">
    <div class="button-box">
      <div class="button1"></div>
      <div class="button2" @click="changeScada"></div>
      <div class="button3" @click="viewDevice"></div>
    </div>
    <div class="header"></div>
    <div class="item-box">
      <div
        class="item item1"
        :class="{ active1: scadaList[0]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[0] = true">
        <div v-if="tipsList[0]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[0]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[0]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('粗制间一')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[0] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item2"
        :class="{ active2: scadaList[1]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[1] = true">
        <div v-if="tipsList[1]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[1]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[1]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('粗制间二')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[1] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item3"
        :class="{ active3: scadaList[2]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[2] = true">
        <div v-if="tipsList[2]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[2]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[2]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('生化间')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[2] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item4"
        :class="{ active4: scadaList[3]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[3] = true">
        <div v-if="tipsList[3]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[3]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[3]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('纯化间')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[3] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item5"
        :class="{ active5: scadaList[4]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[4] = true">
        <div v-if="tipsList[4]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[4]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[4]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('精制间')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[4] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item6"
        :class="{ active6: scadaList[5]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[5] = true">
        <div v-if="tipsList[5]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[5]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[5]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('过滤间')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[5] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item7"
        :class="{ active7: scadaList[6]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[6] = true">
        <div v-if="tipsList[6]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[6]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[6]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('精制间')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[6] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item8"
        :class="{ active8: scadaList[7]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[7] = true">
        <div v-if="tipsList[7]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[7]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[7]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('过滤间')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[7] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item9"
        :class="{ active9: scadaList[8]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[8] = true">
        <div v-if="tipsList[8]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[8]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[8]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('预灌装间')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[8] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item10"
        :class="{ active10: scadaList[9]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[9] = true">
        <div v-if="tipsList[9]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[9]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[9]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('灯检间')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[9] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
      <div
        class="item item11"
        :class="{ active11: scadaList[10]?.inProductionBatchNoList.length > 0 }"
        @click.self="tipsList[10] = true">
        <div v-if="tipsList[10]" class="tips tips1">
          <img src="../assets//scada1/tips.png" class="tips-img" />
          <div class="tips-text">
            <div class="line-box color1">
              <div style="width: 75px">{{t('生产批号')}}：</div>
              <div>
                <div v-for="(item, index) in scadaList[10]?.inProductionBatchNoList || []" :key="index">{{ item }}</div>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('工序名称')}}：</div>
              <div>
                <span>{{ scadaList[10]?.procedureName || '-' }}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间名称')}}：</div>
              <div>
                <span>{{t('外包装间')}}</span>
              </div>
            </div>
            <div class="line-box color2">
              <div>{{t('房间编号')}}：</div>
              <div>
                <span>1002</span>
              </div>
            </div>
          </div>
          <div class="tips-close" @click="tipsList[10] = false">
            <img src="../assets/scada1/close.png" class="close-img" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { BMTable, BMTableTitle, BMModalForm, FormProps } from '@bmos/components';
  import { loopSelectableNotValueTree } from '@bmos/utils';
  import { t } from '@bmos/i18n';
  import {
    reqProductScheduleProcedureList,
    reqProductScheduleProcedureConfig,
    reqProductScheduleList,
    getProcessListTreeReq,
    reqProcedureHistoricListGET,
  } from '../api/index.ts';
  const showSetting = ref(false);
  const tipsList = ref([false, false, false, false, false, false, false, false, false, false, false]);
  const emit = defineEmits(['change']);
  const changeScada = () => {
    emit('change');
  };
  // 生产设备
  const viewDevice = () => {
    window.open(
      'http://172.28.100.11:38080/hub-app/system/1860933391017340928/1859051263719858176.html?_=1736301955239',
    );
  };

  const scadaList = ref([]);

  const clickItem = ref({});
  const modalFormRef = ref();
  const columns = [
    {
      title: t('序号'),
      dataIndex: 'seq',
      width: 60,
    },
    {
      title: t('显示工序/房间名称'),
      dataIndex: 'name',
      width: 200,
    },
    {
      title: t('关联工序'),
      dataIndex: 'relatedProcesses',
      width: 200,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      width: 100,
      actions: ({ record }: any) => [
        {
          label: t('关联工序'),
          onClick: () => {
            open.value = true;
            clickItem.value = record;
            clickItem.value.newProcessName = record.processName;
            clickItem.value.newProcedureName = record.procedureName;
            nextTick(() => {
              modalFormRef.value?.formRef.setFieldsValue({
                processId: record.processId,
                procedureId: record.procedureId,
              });
              record.processId && getProcedureHistory(record.processId);
            });
          },
        },
      ],
    },
  ];

  const tableData = ref([
    { seq: 1, name: t('前处理一 (粗制间一)') },
    { seq: 2, name: t('前处理二 (粗制间二)') },
    { seq: 3, name: t('生化反应 (生化间)') },
    { seq: 4, name: t('超滤 (纯化间)') },
    { seq: 5, name: t('浓缩 (精制间)') },
    { seq: 6, name: t('离心 (过滤间)') },
    { seq: 7, name: t('均质 (精制间)') },
    { seq: 8, name: t('配置、出泡 (过滤间)') },
    { seq: 9, name: t('罐装 (预灌装间)') },
    { seq: 10, name: t('灯检 (灯检间)') },
    { seq: 11, name: t('包装 (外包装间)') },
  ]);
  const open = ref(false);
  const formProps = reactive<FormProps>({
    schemas: [
      {
        field: 'processId',
        component: 'TreeSelect',
        label: t('关联工艺'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            treeData: [],
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            request: async () => {
              try {
                const { data } = await getProcessListTreeReq();
                return loopSelectableNotValueTree(data, 'isFlag', true);
              } catch (error) {}
            },
            onChange: (val, option) => {
              clickItem.value.newProcessName = option[0] || '';
              clickItem.value.newProcedureName = '';
              formModel.procedureId = undefined;
              val && getProcedureHistory(val);
            },
          };
        },
      },
      {
        field: 'procedureId',
        component: 'Select',
        label: t('关联工序'),
        required: true,
        componentProps: {
          showSearch: true,
          fieldNames: {
            label: 'name',
            value: 'id',
          },
          options: [],
          onChange: (val, option) => {
            clickItem.value.newProcedureName = option?.name || '';
          },
        },
      },
    ],
  });
  const okModal = async () => {
    const values = await modalFormRef.value?.validate();
    tableData.value[clickItem.value.seq - 1] = {
      ...clickItem.value,
      ...values,
      relatedProcesses: `${clickItem.value.newProcessName}-${clickItem.value.newProcedureName}`,
      processName: clickItem.value.newProcessName,
      procedureName: clickItem.value.newProcedureName,
    };
    open.value = false;
  };

  const save = async () => {
    try {
      const params = tableData.value.map((item: any) => {
        return {
          seq: item.seq,
          processId: item.processId,
          procedureId: item.procedureId,
        };
      });
      const { data } = await saveProductScheduleProcedureConfig(params);
      showSetting.value = false;
    } catch (error) {
      console.log(error);
    }
  };

  // 查询关联工序
  async function getProcedureHistory(processId: string) {
    try {
      const { data } = await reqProcedureHistoricListGET(processId);
      modalFormRef.value?.formRef?.updateSchema({
        field: 'procedureId',
        componentProps: {
          options: data,
        },
      });
    } catch (error) {}
  }

  // 查询生产进度
  const getProductScheduleList = async () => {
    try {
      const { data } = await reqProductScheduleList();
      scadaList.value = data || [];
    } catch (error) {
      console.log(error);
    }
  };

  // 查询生产进度设置工序-康盛科泰
  const getProductScheduleProcedureList = async () => {
    try {
      const { data } = await reqProductScheduleProcedureList();
      data.forEach((item: any) => {
        tableData.value[item.seq - 1] = {
          ...tableData.value[item.seq - 1],
          ...item,
          relatedProcesses: `${item.processName}-${item.procedureName}`,
        };
      });
    } catch (error) {
      console.log(error);
    }
  };
  // 保存配置
  async function saveProductScheduleProcedureConfig() {
    try {
      const params = tableData.value.map((item: any) => {
        return {
          seq: item.seq,
          processId: item.processId,
          procedureId: item.procedureId,
        };
      });
      await reqProductScheduleProcedureConfig(params);
      await getProductScheduleList();
      showSetting.value = false;
    } catch (error) {
      console.log(error);
    }
  }
  // 快捷键出发点
  const quickEntry = (event: KeyboardEvent) => {
    if (event.ctrlKey && event.key === 'i') {
      // 这里处理快捷键按下的逻辑
      showSetting.value = !showSetting.value;
      showSetting.value && getProductScheduleProcedureList();
    }
  };

  const timer = ref<any>(null);
  onMounted(() => {
    // 获取数据
    document.addEventListener('keydown', quickEntry);
    getProductScheduleList();
    timer.value = setInterval(() => {
      getProductScheduleList();
    }, 5000);
  });
  onUnmounted(() => {
    document.removeEventListener('keydown', quickEntry);
    if (timer.value) {
      clearInterval(timer.value);
    }
  });
</script>

<style lang="less" scoped>
  .setting-box {
    margin: 0 auto;
    padding: 40px;
    width: 1280px;
    height: 900px;
    .save-button {
      width: 100%;
      text-align: right;
    }
  }
  @media only screen and (max-width: 1900px) {
    .scada1_wrap {
      width: 1920px !important;
      height: 1080px !important;
    }
  }
  .scada1_wrap {
    background: url('../assets/scada1/bg.png') repeat-y center/cover;
    background-size: 100% 100%;

    overflow-x: hidden;
    box-sizing: border-box;
    min-height: 1080px;
    height: 100%;
    // 右上角按钮
    .button-box {
      position: absolute;
      z-index: 999;
      display: flex;
      top: 45px;
      right: 80px;
      .button1 {
        width: 140px;
        height: 46px;
        background: url('../assets/scada1/button1.png') no-repeat center/cover;
        cursor: pointer;
        margin-right: 30px;
      }
      .button2 {
        width: 140px;
        height: 46px;
        background: url('../assets/scada1/button2.png') no-repeat center/cover;
        cursor: pointer;
        margin-right: 30px;
      }
      .button3 {
        width: 140px;
        height: 46px;
        background: url('../assets/scada1/button3.png') no-repeat center/cover;
        cursor: pointer;
      }
    }
    // 头部
    .header {
      background: url('../assets/scada2/header.png') repeat-y center/cover;
      width: 1878.67px;
      height: 86px;
      position: relative;
      left: 50%;
      transform: translateX(-50%);
    }

    .item-box {
      position: relative;
      left: calc(50% - 960px);
      width: 1920px;
      height: 994px;
      background: url('../assets/scada1/line.gif') repeat-y center/cover;
      .item {
        position: absolute;
        width: 361px;
        height: 240px;
        .tips {
          width: 240px;
          height: 150px;
          position: relative;
          .tips-img {
            width: 100%;
            height: 100%;
          }
          .tips-text {
            position: absolute;
            top: 0%;
            display: block;
            width: 209px;
            height: 110px;
            overflow-x: hidden;
            overflow-y: auto;
            padding-top: 10px;
            padding-left: 11px;
            box-sizing: content-box;
            .line-box {
              display: flex;
              margin-bottom: 2px;
              font-size: 14px;
            }
            .color1 {
              color: #ffcb3e;
            }
            .color2 {
              color: #ffffff;
            }
          }
          .tips-text::-webkit-scrollbar {
            display: none;
          }
          .tips-close {
            width: 20px;
            height: 20px;
            position: absolute;
            left: 217px;
            top: 9px;
          }
        }
      }
      .item1 {
        background: url('../assets/scada1/item1.png') no-repeat center/cover;
        top: 205px;
        left: 49px;
      }
      .item1:hover {
        background: url('../assets/scada1/hover1.png') no-repeat center/cover;
      }
      .item2 {
        background: url('../assets/scada1/item2.png') no-repeat center/cover;
        top: 13px;
        left: 379px;
      }
      .item2:hover {
        background: url('../assets/scada1/hover2.png') no-repeat center/cover;
      }
      .item3 {
        height: 249px;
        background: url('../assets/scada1/item3.png') no-repeat center/cover;
        top: 63px;
        left: 979px;
      }
      .item3:hover {
        background: url('../assets/scada1/hover3.png') no-repeat center/cover;
      }
      .item4 {
        width: 363px;
        height: 216px;
        background: url('../assets/scada1/item4.png') no-repeat center/cover;
        top: 268px;
        left: 682px;
      }
      .item4:hover {
        background: url('../assets/scada1/hover4.png') no-repeat center/cover;
      }
      .item5 {
        height: 242px;
        background: url('../assets/scada1/item5.png') no-repeat center/cover;
        top: 424px;
        left: 370px;
      }
      .item5:hover {
        background: url('../assets/scada1/hover5.png') no-repeat center/cover;
      }
      .item6 {
        height: 291px;
        background: url('../assets/scada1/item6.png') no-repeat center/cover;
        top: 558px;
        left: 52px;
      }
      .item6:hover {
        background: url('../assets/scada1/hover6.png') no-repeat center/cover;
      }
      .item7 {
        height: 222px;
        background: url('../assets/scada1/item7.png') no-repeat center/cover;
        top: 742px;
        left: 520px;
      }
      .item7:hover {
        background: url('../assets/scada1/hover7.png') no-repeat center/cover;
      }
      .item8 {
        height: 282px;
        background: url('../assets/scada1/item8.png') no-repeat center/cover;
        top: 488px;
        left: 866px;
      }
      .item8:hover {
        background: url('../assets/scada1/hover8.png') no-repeat center/cover;
      }
      .item9 {
        height: 267px;
        background: url('../assets/scada1/item9.png') no-repeat center/cover;
        top: 319px;
        left: 1171px;
      }
      .item9:hover {
        background: url('../assets/scada1/hover9.png') no-repeat center/cover;
      }
      .item10 {
        height: 227px;
        background: url('../assets/scada1/item10.png') no-repeat center/cover;
        top: 191px;
        left: 1461px;
      }
      .item10:hover {
        background: url('../assets/scada1/hover10.png') no-repeat center/cover;
      }
      .item11 {
        height: 270px;
        background: url('../assets/scada1/item11.png') no-repeat center/cover;
        top: 582px;
        left: 1390px;
      }
      .item11:hover {
        background: url('../assets/scada1/hover11.png') no-repeat center/cover;
      }
      .active1 {
        background: url('../assets/scada1/activeItem1.png') no-repeat center/cover;
      }
      .active2 {
        background: url('../assets/scada1/activeItem2.png') no-repeat center/cover;
      }
      .active3 {
        background: url('../assets/scada1/activeItem3.png') no-repeat center/cover;
      }
      .active4 {
        background: url('../assets/scada1/activeItem4.png') no-repeat center/cover;
      }
      .active5 {
        background: url('../assets/scada1/activeItem5.png') no-repeat center/cover;
      }
      .active6 {
        background: url('../assets/scada1/activeItem6.png') no-repeat center/cover;
      }
      .active7 {
        background: url('../assets/scada1/activeItem7.png') no-repeat center/cover;
      }
      .active8 {
        background: url('../assets/scada1/activeItem8.png') no-repeat center/cover;
      }
      .active9 {
        background: url('../assets/scada1/activeItem9.png') no-repeat center/cover;
      }
      .active10 {
        background: url('../assets/scada1/activeItem10.png') no-repeat center/cover;
      }
      .active11 {
        background: url('../assets/scada1/activeItem11.png') no-repeat center/cover;
      }
      .tips1 {
        left: -20px;
        top: -97px;
      }
      .tips2 {
        left: -74px;
        top: -71px;
      }
      .tips3 {
        left: 10px;
        top: -106px;
      }
      .tips4 {
        left: -27px;
        top: -142px;
      }
      .tips5 {
        left: -20px;
        top: -118px;
      }
      .tips6 {
        left: -23px;
        top: -72px;
      }
      .tips7 {
        left: -41px;
        top: -123px;
      }
      .tips8 {
        left: -71px;
        top: -85px;
      }
      .tips9 {
        left: -63px;
        top: -78px;
      }
      .tips10 {
        left: -53px;
        top: -105px;
      }
    }
  }
</style>
