<template>
  <div class="environmental-monitoring-container">
    <div class="filter">
      <!-- 四个角图标 -->
      <div class="filterLeftTop"></div>
      <div class="filterRightTop"></div>
      <div class="filterLeftBottom"></div>
      <div class="filterRightBottom"></div>
      <div class="filter-container">
        <BMForm ref="filterRef" v-bind="filterFormProps" @submit="filterSubmit" @reset="filterRet" />
      </div>
    </div>
    <BMTable
      v-if="mqttUrl"
      ref="tableRef"
      showIndex
      :columns="columns"
      :dataRequest="renderTable"
      :pagination="{
        pageSize: 20,
        showQuickJumper: false,
      }"
      :search="false"
      :showToolBar="false"
      :scroll="{ x: 400, y: 300 }" />
  </div>
</template>
<script setup lang="tsx">
  import { BMForm, BMTable, Recordable, TableColumn } from '@bmos/components';
  import { useForm, useTable } from './hooks';
  import { getParameter, getPlatformEquipmentMqttAccredit, postPlatformFactoryRoomDashboardPage } from '@/services';
  import MqttClient from '@/hooks/useMqtt';

  const { filterRef, filterFormProps, cleanLevelDict } = useForm();
  const { tableRef, columns, baseColumns } = useTable({
    cleanLevelDict,
  });

  const mqttUrl = ref<string>('');
  const hubUrl = ref<string>('');
  const hubUserInfo = ref<Recordable>({});
  const getMqttUrl = async () => {
    try {
      const { data } = await getParameter('platform.sys.acquisition-address');
      const mqttConfig = JSON.parse(data?.value || '{}').supCon;
      mqttUrl.value = mqttConfig?.mqttAddress || '172.30.1.103:8083';
      const { data: userInfo } = await getPlatformEquipmentMqttAccredit();
      hubUserInfo.value = userInfo;
      const { data: hubConfig } = await getParameter('platform.sys.acquisition-address');
      hubUrl.value = JSON.parse(hubConfig.value || '{}')?.hub?.mqttAddress || '172.30.1.167:38080';
      return Promise.resolve();
    } catch (error) {
      return Promise.reject();
    }
  };

  const mqttMap = new Map();
  const mqttEquipmentCode = new Map();
  const hubMap = new Map();
  const hubDataPointName = new Map();

  const closeMqtt = () => {
    // 遍历 mqttMap 清除订阅
    mqttMap.forEach((value: any) => {
      value.endMqtt();
    });
    mqttMap.clear();
    mqttEquipmentCode.clear();
    hubMap.forEach((value: any) => {
      value.endMqtt();
    });
    hubMap.clear();
    hubDataPointName.clear();
  };

  const setHubDataPointName = (data: any) => {
    if (hubDataPointName.has(data.dataPointName)) {
      const arr = mqttEquipmentCode.get(data.dataPointName);
      arr.push(data); // 直接在数组上操作
      hubDataPointName.set(data.dataPointName, arr);
    } else {
      hubDataPointName.set(data.dataPointName, [data]);
    }
  };

  const setMqttEquipmentCode = (data: any) => {
    if (mqttEquipmentCode.has(data.equipmentCode)) {
      const arr = mqttEquipmentCode.get(data.equipmentCode);
      arr.push(data); // 直接在数组上操作
      mqttEquipmentCode.set(data.equipmentCode, arr); // 更新 map
    } else {
      mqttEquipmentCode.set(data.equipmentCode, [data]);
    }
  };

  const myMqttCreate = (env: any) => {
    if (env.acquisitionPlatform?.value === 'hub') {
      if (!hubMap.has(env.dataPointName)) {
        hubMap.set(
          env.dataPointName,
          new MqttClient(
            `mqtt://${hubUrl.value}/ws/dmcMQTT/`,
            `nup/system/tagValue/${env.dataPointName}`,
            (topic, data, envData) => {
              console.log('hub消息', topic, data, envData);
              hubDataPointName.get(data.name).forEach((envItem: any) => {
                tableRef.value?.updateTableData('id', envItem.roomId, envItem.dataPointName, data.value);
              });
            },
            hubUserInfo.value,
            env,
          ),
        );
      }
      setHubDataPointName(env);
    } else if (env.acquisitionPlatform?.value === 'supCon') {
      if (!mqttMap.has(env.equipmentCode)) {
        mqttMap.set(
          env.equipmentCode,
          new MqttClient(
            `ws://${mqttUrl?.value}/mqtt`,
            env.equipmentCode || 'rtdvalue/report',
            (topic, data, envData) => {
              data.RTValue.forEach((item: any) => {
                if (item.name === envData.dataPointName) {
                  mqttEquipmentCode.get(envData.equipmentCode).forEach((envItem: any) => {
                    tableRef.value?.updateTableData('id', envItem.roomId, envItem.dataPointName, item.value);
                  });
                }
              });
            },
            {},
            env,
          ),
        );
      }
      setMqttEquipmentCode(env);
    }
  };

  const renderTable = async (params: any) => {
    try {
      let columns: TableColumn[] = [...baseColumns];
      closeMqtt();
      const formData = filterRef.value?.getFormValues();
      const res = await postPlatformFactoryRoomDashboardPage({
        ...formData,
        ...params,
      });
      res?.data?.list?.forEach((item: any) => {
        item.roomEnvPropertyDTOList?.forEach((env: any) => {
          //  if (env.equipmentCode === 'mqtt') {
          // env.dataPointName = 'A000683';
          // env.equipmentCode = 'FTT2002206';
          //  }
          myMqttCreate(env);
          if (columns.findIndex(column => column.dataIndex === env.equipmentDataPropertyCode) === -1) {
            columns.push({
              title: env.equipmentDataPropertyName,
              dataIndex: env.dataPointName,
            });
          }
        });
      });
      tableRef.value?.replaceColumn(columns);
      return Promise.resolve({
        ...res,
        data: {
          ...res.data,
          list: res.data?.list,
        },
      });
    } catch (error) {
      console.error(error);
    }
  };

  const filterSubmit = () => {
    tableRef.value?.fetchData();
  };
  const filterRet = () => {
    tableRef.value?.fetchData();
  };

  onMounted(async () => {
    await getMqttUrl();
  });
</script>
<style lang="less">
  .dc-content {
    background: radial-gradient(61.36% 50% at 50% 50%, #346 0%, #292c33 100%);
  }
  .environmental-monitoring-container {
    .bmos-table {
      flex: 1;
      overflow: auto;
      width: 100%;
    }
    .bmos-table .dc-table-body {
      border-bottom: none !important;
    }
    .dc-table-wrapper .dc-table {
      background: transparent;
      color: #fff;
    }
    .dc-table-wrapper .dc-table-thead > tr > th {
      border-bottom: 1px solid rgba(153, 204, 255, 0.2);
      background: rgb(46, 70, 108);
      color: #b9e8ff;
    }
    .dc-table-wrapper
      .dc-table-thead
      > tr
      > th:not(:last-child):not(.dc-table-selection-column):not(.dc-table-row-expand-icon-cell):not([colspan])::before {
      background-color: #3b78b5;
    }
    .dc-table-wrapper .dc-table-cell-scrollbar:not([rowspan]) {
      box-shadow: none;
    }
    .dc-table-wrapper .dc-table:not(.dc-table-bordered) .dc-table-tbody > tr > td {
      border-top: 1px solid rgb(46, 70, 108);
    }
    .dc-table-wrapper .dc-table:not(.dc-table-bordered) .dc-table-tbody > tr:nth-child(even) {
      background: rgba(45, 166, 255, 0.05);
    }
    .dc-table-wrapper .dc-table:not(.dc-table-bordered) .dc-table-tbody > tr:last-child > td {
      border-bottom: 1px solid rgba(153, 204, 255, 0.2);
    }
    .dc-table-wrapper .dc-table-tbody > tr > td {
      transition: background 2s;
    }
    .dc-table-wrapper .dc-table-tbody > tr.dc-table-row:hover > td {
      background: rgba(51, 170, 255, 0.1);
    }
    .dc-table-wrapper .dc-table-cell-fix-left {
      background: rgb(42, 48, 60);
    }
    // .dc-table-wrapper .dc-table-cell-fix-left 的 偶数行 背景色
    .dc-table-wrapper .dc-table-cell-fix-left:nth-child(even) {
      background: rgba(221, 137, 11, 0.05);
    }
    .dc-table-wrapper .dc-table-tbody > tr.dc-table-placeholder:hover > td {
      background: transparent;
    }
    .bmos-table .dc-table-wrapper .dc-table-pagination.dc-pagination {
      color: #fff;
    }
    .dc-pagination .dc-pagination-item a {
      color: #fff;
    }
    .dc-pagination .dc-pagination-item-active {
      background-color: rgba(51, 170, 255, 0.1);
    }
    .anticon {
      color: #fff;
    }
    ::-webkit-scrollbar {
      width: 0;
    }
  }
</style>
<style lang="less" scoped>
  .environmental-monitoring-container {
    color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    height: 100%;
    width: 100%;
    gap: 20px;
  }
  .filter {
    width: 100%;
    position: relative;
    padding: 20px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
    border-radius: 4px;
    border: 1px solid rgba(72, 177, 255, 0.1);
    background: linear-gradient(
        90deg,
        rgba(51, 170, 255, 0) 0%,
        rgba(51, 170, 255, 0.1) 50.5%,
        rgba(51, 170, 255, 0) 100%
      ),
      rgba(48, 53, 64, 0.2);
  }
  /* 在 filter-container 四个角 显示 filterLeftTop.svg 其他三个旋转 */
  .filterLeftTop,
  .filterRightTop,
  .filterLeftBottom,
  .filterRightBottom {
    position: absolute;
    width: 20px;
    height: 20px;
    background: url(./assets/corners.svg) no-repeat;
  }

  .filterLeftTop {
    top: 0;
    left: 0;
  }

  .filterLeftBottom {
    bottom: 0;
    left: 0;
    transform: rotate(270deg);
  }

  .filterRightTop {
    top: 0;
    right: 0;
    transform: rotate(90deg);
  }

  .filterRightBottom {
    bottom: 0;
    right: 0;
    transform: rotate(180deg);
  }
  :deep(.dc-form-item) {
    margin-bottom: 0;
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
    background: url(./assets/normalBtn.svg) no-repeat;
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
    background: url(./assets/normalBtn.svg) no-repeat;
    background-size: cover;
    transition: background 0.3s ease;
  }
  :deep(.submit-btn:hover) {
    color: #fff;
    background: url(./assets/hoverBtn.svg);
    background-size: cover;
  }
  :deep(.reset-btn:hover) {
    color: #fff;
    background: url(./assets/hoverBtn.svg);
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
