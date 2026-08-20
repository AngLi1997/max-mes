<template>
  <BMLayout>
    <BMBasicPage
      :title="showList ? t('库存管理') : t('库存查询')"
      :show-buttons="false"
      @left-click="toBack"
    >
      <view class="content">
        <view class="title_box">
          <Info :basic-items="infoItems" :info-data="infoData" />
        </view>
        <view class="tag_box">
          <view class="segmented_box">
            <wd-segmented
              v-if="!showList"
              v-model:value="tableType"
              :options="[
                {
                  label: t('物料件信息'),
                  value: 1,
                },
                {
                  label: t('批次统计'),
                  value: 2,
                },
              ]"
              @change="showTableTypeChange"
            >
              <template #label="{ option }">
                {{ option.label }}
              </template>
            </wd-segmented>
          </view>
          <BMScanInput
            v-model="materialScan"
            :placeholder="t('物料件号/容器编号')"
            suffix-icon="search"
            @confirm="onScanConfirm"
            @clicksuffixicon="onScanConfirm"
          />
        </view>
        <view v-if="showList" class="url_box">
          <view
            v-for="item in listData" :key="item.url" class="url_item" :style="`background: url('./static/images/${item.url}.png') 100%;background-size: 100% 100%;`" @click="toUrl(item.url)"
          >
            {{ item.label }}
          </view>
        </view>
        <view v-else class="table_box">
          <BMTable
            :key="tableType"
            v-bind="tableProps"
            :extra-params="{
              materialPositionId: goodsLocation,
              dir: 'asc',
              orderBy: tableType === 1 ? 'materialNo' : 'expiredDate',
            }"
            :data-request="getTableList"
          />
        </view>
      </view>
    </BMBasicPage>
    <BMTreeModal
      v-model="goodsLocation"
      v-model:open="cargoSpaceOpen"
      :title="t('选择货位')"
      :tree-data="treeModalData"
      :field-names="{
        name: 'name',
        key: 'id',
        checkKey: 'level.value',
        checkKeyValue: 4,
      }"
      required
      @cancel="goodsLocationCancel"
    />
    <!-- 扫码 -->
    <BMScanNew @success="onScanSuccess" />
  </BMLayout>
</template>

<script lang="ts" setup>
import { BMBasicPage, BMLayout, BMScanInput, BMScanNew, BMTable, BMTreeModal } from '@/BMComponents';
import Info from '@/pages/weighingComponents/info';
import { t } from '@/utils/useBmosI18n.js';
import { onLoad } from '@dcloudio/uni-app';
import { usePage } from './hooks/usePage.jsx';

const {
  infoItems,
  infoData,
  goodsLocation,
  cargoSpaceOpen,
  treeModalData,
  tableType,
  materialScan,
  tableProps,
  initId,
  showList,
  listData,
  getTableList,
  goodsLocationCancel,
  onScanSuccess,
  onScanConfirm,
  showTableTypeChange,
  toBack,
  toUrl,
} = usePage();

onLoad((e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(
    Object.keys(e || '').map(key => [
      decodeURIComponent(key),
      decodeURIComponent(e?.[key]),
    ]),
  );
  initId.value = query?.materialPositionId;
  // #endif
  // #ifdef H5
  initId.value = e?.materialPositionId;
  // #endif
});
</script>

<style lang="scss" scoped>
.content {
  .title_box {
    margin-bottom: 9.38rpx;
  }
  .tag_box {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 9.38rpx;
    .segmented_box {
      width: 50%;
    }
  }
  .table_box {
    height: 329.3rpx;
  }
  .url_box {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    gap: 10.55rpx;
    .url_item {
      width: calc(33% - 7.03rpx);
      height: 93.75rpx;
      line-height: 93.75rpx;
      text-align: center;
      box-sizing: border-box;
      padding-right: 58.59rpx;
      font-size: 15.23rpx;
      color: #242526;
      border-radius: 4.69rpx;
    }
  }
}
</style>
