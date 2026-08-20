<template>
  <div class="progress-container">
    <div class="process-list">
      <div v-for="(progress, index) in processList" :key="index" class="process-item">
        <div class="title">
          <img :src="imageCommon[`/src/assets/baiePng/processProgressTitleArrow.png`]?.default" />
          {{ progress.processName }}
          <img
            :src="imageCommon[`/src/assets/baiePng/processProgressTitleArrow.png`]?.default"
            style="transform: rotate(180deg)" />
        </div>
        <div class="process-container">
          <div
            v-for="(row, rowIndex) in getRows(progress)"
            :key="'row-' + rowIndex"
            :class="['process-row', rowIndex % 2 === 0 ? 'even' : 'odd']">
            <div v-for="(node, nodeIndex) in row" :key="node.procedureId" class="process-node">
              <!-- 连接线 -->
              <div
                v-if="nodeIndex !== row.length - 1 && rowIndex % 2 !== 0"
                class="process-line progress-line-odd"></div>

              <div :class="['process_item', node.pendingProductProcedureVOS?.length ? 'has-batch' : 'no-batch']">
                <img
                  v-if="node.pendingProductProcedureVOS?.length"
                  :src="imageCommon[`/src/assets/baiePng/processProgressActive.png`]?.default" />
                <img v-else :src="imageCommon[`/src/assets/baiePng/processProgressDeActive.png`]?.default" />
                <div :class="['item_right', node.pendingProductProcedureVOS?.length ? 'has-items' : 'no-items']">
                  <div :class="['right_top', currentLng === 'ru_RU' ? 'right_top_ru' : '']">
                    {{ node.showName }}
                  </div>
                  <div class="right_bottom">
                    <span v-for="item in node.pendingProductProcedureVOS" :key="item.batchNo">{{ item.batchNo }}</span>
                  </div>
                </div>
              </div>
              <!-- 连接线 -->
              <div v-if="nodeIndex !== row.length - 1 && rowIndex % 2 === 0" class="process-line"></div>
            </div>
            <!-- 换行转角 -->
            <div v-if="rowIndex < getRows(progress).length - 1 && rowIndex % 2 === 0" class="process-turn-even"></div>
            <div v-if="rowIndex < getRows(progress).length - 1 && rowIndex % 2 !== 0" class="process-turn-odd"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="footer-btns">
      <img
        :src="type === TypeEnum.HUMAN_ALBUMIN ? imagePaths.humanAlbuminActive : imagePaths.humanAlbumin"
        @click="() => changeType(TypeEnum.HUMAN_ALBUMIN)" />
      <img
        :src="type === TypeEnum.IMMUNOGLOBULINS ? imagePaths.immunoglobulinsActive : imagePaths.immunoglobulins"
        @click="() => changeType(TypeEnum.IMMUNOGLOBULINS)" />
    </div>
  </div>
</template>

<script setup lang="ts">
  import { reqQueryBelarusDashboardDataProcessProgress } from '@/services';
  import { TypeEnum } from './type';
  import { currentLng } from '@bmos/i18n';

  const props = withDefaults(
    defineProps<{
      typeActive: TypeEnum;
    }>(),
    {
      typeActive: TypeEnum.HUMAN_ALBUMIN,
    },
  );

  const imageCommon = import.meta.glob('@/assets/baiePng/*.png', { eager: true });
  const images = import.meta.glob('@/assets/baiePng/*/*/*.png', { eager: true });
  const type = ref<TypeEnum>(props.typeActive);

  const imagePaths = computed(() => {
    const lang = currentLng.value;
    return {
      humanAlbumin: images[`/src/assets/baiePng/${lang}/process/humanAlbumin.png`]?.default,
      humanAlbuminActive: images[`/src/assets/baiePng/${lang}/process/humanAlbuminActive.png`]?.default,
      immunoglobulins: images[`/src/assets/baiePng/${lang}/process/immunoglobulins.png`]?.default,
      immunoglobulinsActive: images[`/src/assets/baiePng/${lang}/process/immunoglobulinsActive.png`]?.default,
    };
  });

  const changeType = (t: TypeEnum) => {
    type.value = t;
    getProgress();
  };
  const processList = ref<any>([]);

  const rowLimit = 4; // 每行最多 4 个
  const getRows = (progress: any) => {
    const result = [];
    for (let i = 0; i < progress?.allProcedureVOS.length; i += rowLimit) {
      result.push(progress.allProcedureVOS.slice(i, i + rowLimit));
    }
    return result;
  };

  const getProgress = async () => {
    try {
      const { data } = await reqQueryBelarusDashboardDataProcessProgress({
        type: type.value === TypeEnum.HUMAN_ALBUMIN ? 4 : 5,
      });
      processList.value = data;
    } catch (error) {
      processList.value = [];
    }
  };

  onMounted(async () => {
    getProgress();
  });
</script>

<style lang="less" scoped>
  .progress-container {
    padding: 10px 34px 0;
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  .process-list {
    flex: 1;
    overflow-y: auto;
    overflow-x: hidden;
  }
  .process-list::-webkit-scrollbar {
    width: 0px;
    background: transparent;
  }
  .title {
    display: flex;
    width: 100%;
    height: 40px;
    padding: 8px 10px;
    justify-content: center;
    align-items: center;
    gap: 14px;
    flex-shrink: 0;
    margin-bottom: 30px;
    border-bottom: 1px solid rgba(77, 166, 255, 0);
    background: linear-gradient(
      87deg,
      rgba(107, 192, 255, 0) 2.25%,
      rgba(102, 178, 255, 0.15) 48.62%,
      rgba(107, 192, 255, 0) 95.17%
    );
  }

  .process-container {
    display: flex;
    flex-direction: column;
    position: relative;
    padding: 0 0 40px;
    width: 100%;
  }

  .process-row {
    display: flex;
    align-items: center;
    gap: 15px;
    position: relative;
  }
  .process-row.even {
    margin-bottom: 60px;
  }
  .process-row.odd {
    align-self: flex-end;
    flex-direction: row-reverse;
    margin-right: 12px;
    margin-bottom: 60px;
  }
  .process-node {
    display: flex;
    align-items: center;
    position: relative;
    gap: 15px;
  }

  .process_item {
    width: 400px;
    height: 160px;
    position: relative;
    border-radius: 4px;
    display: flex;
    gap: 10px;
    background: url('@/assets/baiePng/processProgressItemBg.png') no-repeat;
  }
  .process_item.no-batch {
    background: url('@/assets/baiePng/processProgressItemDeActiveBg.png') no-repeat;
  }
  .item_right {
    display: flex;
    flex-direction: column;
    gap: 10px;
    flex: 1;
    overflow: hidden;
  }
  .right_top {
    display: flex;
    align-items: center;
    color: #fff;
    /* 中文/16px加粗 */
    font-family: 'Source Han Sans CN';
    font-size: 16px;
    font-style: normal;
    font-weight: 500;
    line-height: 20px; /* 125% */
    padding: 4px 0px 0px 24px;
    gap: 8px;
    align-self: stretch;
    height: 56px;
    background: url('@/assets/baiePng/processProgressTitleHasBg.png') no-repeat;
  }
  .right_top_ru {
    font-size: 14px;
  }
  .right_bottom {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    flex-shrink: 0;
    flex: 1;
    overflow-y: auto;
  }
  .item_right.no-items {
    .right_top {
      color: #98bed9;
    }
  }

  .process-line {
    width: 51px;
    height: 30px;
    background-image: url('@/assets/baiePng/processProgressArrow.png');
    background-repeat: no-repeat;
    background-size: cover;
  }

  .progress-line-odd {
    transform: rotate(180deg);
  }

  .process-turn-even {
    position: absolute;
    right: 185px;
    top: 175px;
    width: 51px;
    height: 30px;
    background-image: url('@/assets/baiePng/processProgressArrow.png');
    background-repeat: no-repeat;
    background-size: cover;
    transform: rotate(90deg);
  }

  .process-turn-odd {
    position: absolute;
    left: 170px;
    top: 175px;
    width: 51px;
    height: 30px;
    background-image: url('@/assets/baiePng/processProgressArrow.png');
    background-repeat: no-repeat;
    background-size: cover;
    transform: rotate(90deg);
  }
  .footer-btns {
    height: 78px;
    background-image: url('@/assets/baiePng/processProgressFooterBg.png');
    background-repeat: no-repeat;
    background-position: bottom;
    display: flex;
    justify-content: center;
    gap: 90px;
    img {
      height: 52px;
    }
  }
</style>
