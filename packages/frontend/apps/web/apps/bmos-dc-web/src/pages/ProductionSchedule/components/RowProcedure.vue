<!-- 行工序 -->
<template>
  <div
    v-if="lineIndex !== '0' && rowData?.length > 0"
    class="down-arrow-box"
    :style="{ flexDirection: reverse ? 'row-reverse' : '' }">
    <div :class="['arrow', 'down']"></div>
  </div>
  <div class="rowContent" :style="{ flexDirection: reverse ? 'row-reverse' : '' }">
    <div v-for="(item, index) in 5" :key="index" :class="handleClass(index, rowData[index / 2]?.batchNoList)">
      <div v-if="index % 2 === 0" class="procedureName">
        {{ rowData?.[index / 2]?.customName || rowData?.[index / 2]?.procedureName }}
      </div>
      <div v-if="index % 2 === 0" class="batchNoBox">
        <div
          v-for="(batchNoItem, batchNoIndex) in rowData[index / 2]?.batchNoList"
          :key="batchNoIndex"
          class="batchList">
          {{ batchNoItem }}
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="tsx" setup>
  const props = withDefaults(
    defineProps<{
      rowData: Array<any>;
      lineIndex: string;
      reverse: boolean; //是否反向布局
    }>(),
    {
      rowData: () => [],
      lineIndex: '1',
      reverse: false,
    },
  );
  const handleClass = (index: any, batchNoList: any) => {
    if (index % 2 === 0) {
      // 工序框样式
      if (batchNoList?.length === undefined) return 'procedureBox hidden';
      if (batchNoList?.length !== 0) {
        return 'procedureBox bright';
      } else {
        return 'procedureBox dark ';
      }
    } else {
      if (index > (props.rowData?.length - 1) * 2) {
        return 'arrow hidden';
      } else {
        return props.reverse ? 'reverse arrow' : 'arrow';
      }
    }
  };
</script>

<style scoped lang="less">
  .rowContent {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 5px;
    .procedureBox {
      position: relative;
      width: 29%;
      min-width: 320px;
      height: 130px;
      .procedureName {
        position: absolute;
        top: 20px;
        left: 30%;
      }
      .batchNoBox {
        width: 70%;
        min-width: 200px;
        max-height: 60px;
        overflow-y: scroll;
        display: flex;
        flex-wrap: wrap;
        position: absolute;
        top: 60px;
        left: 30%;
        color: white;
        > div:nth-child(2n) {
          margin-left: 3%;
        }
        .batchList {
          width: 46%;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
      }
    }
    //暗盒子
    .dark {
      color: #86a7bf;
      background: url('/src/assets/ProductionSchedule/darkProcedure.png');
      background-size: 100% 100%;
    }
    .bright {
      color: white;
      background: url('/src/assets/ProductionSchedule/brightProcedure.png');
      background-size: 100% 100%;
    }
    .reverse {
      transform: rotate(180deg);
    }
    //隐藏
    .hidden {
      visibility: hidden;
    }
  }
  .down-arrow-box {
    display: flex;
    padding: 0px 12%;
  }
  .arrow {
    width: 74px;
    height: 60px;
    background: url('/src/assets/ProductionSchedule/arrow2.png') repeat-y center/cover;
  }

  // 向下箭头
  .down {
    transform: rotate(90deg);
    margin-bottom: 10px;
  }
</style>
