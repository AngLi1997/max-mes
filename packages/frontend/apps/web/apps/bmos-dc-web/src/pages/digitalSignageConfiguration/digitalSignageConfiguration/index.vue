<template>
  <div class="data_config_box">
    <div class="item_list_box">
      <div class="board_box">
        <BoardItem
          v-for="item in boardList"
          :key="item.filedId"
          :data="item"
          :clickId="clickFiledId"
          @click="clickBoardChange(item)"
          @delete="boardDelete"></BoardItem>
      </div>
      <Button class="add_btn" @click="addBoard">{{ t('新增看板') }}</Button>
    </div>
    <div class="form_box">
      <div class="box">
        <BMTableTitle :title="t('看板配置')" />
        <Tabs v-show="clickFiledId != ''" v-model:activeKey="activeKey" @change="typeChange">
          <TabPane key="data" :tab="t('数据')"></TabPane>
          <TabPane key="style" :tab="t('样式')"></TabPane>
        </Tabs>
        <BMForm v-show="clickFiledId != ''" ref="formRef" v-bind="formProps"></BMForm>
      </div>
      <Button v-if="clickFiledId != ''" class="add_btn" type="primary" :loading="loading" @click="formSubmit">
        {{ t('更新') }}
      </Button>
    </div>
    <div class="echarts_box">
      <BMTableTitle :title="t('看板预览')" />
      <div v-if="showChart" class="chart_box">
        <Echarts :options="eChartOptions" :height="700" />
        <div v-if="peiData.length > 0" class="pei_box">
          <div v-for="(item, index) in peiData" :key="index" class="pei">
            <div class="pei_color" :style="{ backgroundColor: item.color }"></div>
            <div class="pei_name">{{ item.name }}</div>
            <div class="pei_line"></div>
            <div class="pei_value">{{ item.value }}</div>
          </div>
        </div>
      </div>
      <div v-else class="no_box">
        <Empty class="msg_box" :icon="'NoChart'" :emptyName="t('暂无图表')" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useData } from './hooks/useData';
  import { Button } from 'ant-design-vue';
  import BoardItem from './component/BoardItem.vue';
  import { BMTableTitle, BMForm } from '@bmos/components';
  import { Tabs, TabPane } from 'ant-design-vue';
  import Empty from '@/components/Empty/index.vue';
  import Echarts from '@/components/Echarts/index.vue';
  const {
    boardList,
    clickFiledId,
    activeKey,
    formRef,
    formProps,
    eChartOptions,
    addBoard,
    boardDelete,
    clickBoardChange,
    typeChange,
    formSubmit,
    showChart,
    loading,
    peiData,
  } = useData();
</script>
<style scoped lang="less">
  .data_config_box {
    width: 100%;
    height: 100%;
    background-color: #fff;
    overflow: hidden;
    display: flex;
    align-items: center;
    .item_list_box {
      height: 100%;
      padding: 8px;
      width: 15%;
      box-sizing: border-box;
      border-right: 1px solid #e1e3e5;
      position: relative;
      padding-bottom: 40px;
      .board_box {
        height: 100%;
        overflow-y: auto;
      }
    }
    .form_box {
      width: 30%;
      height: 100%;
      padding: 8px;
      position: relative;
      box-sizing: border-box;
      border-right: 1px solid #e1e3e5;
      padding-bottom: 50px;
      .box {
        height: 100%;
        overflow-y: auto;
      }
      :deep(.dc-tabs-nav-wrap) {
        padding-left: 20px;
      }
    }
    .add_btn {
      position: absolute;
      left: 8px;
      bottom: 8px;
      width: calc(100% - 16px);
    }
    .echarts_box {
      width: 55%;
      height: 100%;
      padding: 8px;
      .no_box {
        width: 100%;
        height: 90%;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .chart_box {
        height: 90%;
        display: flex;
        align-items: center;
        justify-content: center;
        position: relative;
        .pei_box {
          width: 70%;
          position: absolute;
          bottom: 50px;
          right: 0;
          left: 0;
          margin: auto;
          display: flex;
          flex-wrap: wrap;
          gap: 30px;
          .pei {
            width: calc(50% - 30px);
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 10px;
            .pei_color {
              width: 10px;
              height: 10px;
              flex-shrink: 0;
            }
            .pei_name {
              white-space: nowrap;
            }
            .pei_line {
              border-top: 2px dashed rgba(185, 232, 255, 0.25);
              width: 100%;
            }
          }
        }
      }
    }
  }
</style>
