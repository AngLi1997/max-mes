<template>
  <BMLayout>
    <BMBasicPage :title="title" :show-buttons="false" background-color="#F2F3F5" @left-click="toBack">
      <template #titleRight>
        <view class="right-content">
          <uv-row justify="space-between" gutter="10">
            <uv-col span="6">
              <BMFilter v-model="filterData" :form-props="formProps" @confirm="filterDetail" @reset="filterDetail" />
            </uv-col>
          </uv-row>
        </view>
      </template>
      <view class="top-scan">
        <div style="width: 50%">
          <BMScan
            v-model="scanValue"
            type="input"
            :allow-types="['04']"
            :error-type-placeholder="t('设备码无法识别，可选择设备')"
            @success="onScanSuccess"
            @fail="onScanFail"
            @confirm="onScanSuccess"
          />
        </div>
      </view>
      <scroll-view class="content" scroll-y="true">
        <view class="equipment-list">
          <equipmentItem
            v-for="item in showEquipmentList"
            :key="item.id"
            :item="item"
            @click="toNext(item.id, item.code)"
          />
          <BMNoData v-if="showEquipmentList?.length == 0" type="emptyData" :text="t('暂无可用设备')" />
        </view>
      </scroll-view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
  import { reactive, ref } from 'vue';
  import { t } from '@/utils/useBmosI18n.js';
  import equipmentItem from './components/equipmentItem.vue';
  import { onLoad } from '@dcloudio/uni-app';
  import { urlQueryRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import {
    reqGetConfigByStationIdListApi,
    reqPlatformEquipmentAppGetConfigByProductionLineIdApi,
    getEquipmentTagTree,
    postScanScanDeviceCode
  } from '@/api';
  import { useToast } from 'wot-design-uni';
  import { BMBasicPage, BMScan, BMLayout, BMNoData, BMFilter } from '@/BMComponents/index.js';
  import { isEmpty } from 'lodash-es';

  const toast = useToast();

  const queryInfo = ref({});

  const equipmentList = ref([]);
  const showEquipmentList = ref([]);
  const filterData = ref({
    name: '',
    code: '',
    ids: []
  });

  const formProps = reactive({
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('设备名称'),
        colProps: {
          span: 24
        }
      },
      {
        field: 'code',
        component: 'Input',
        label: t('设备编码'),
        colProps: {
          span: 24
        }
      },
      {
        field: 'ids',
        component: 'BMFormSelect',
        label: t('设备类型'),
        defaultValue: [],
        colProps: {
          span: 24
        },
        componentProps: ({ formModel }) => {
          return {
            title: t('设备类型'),
            type: 'tree',
            mode: 'multiple',
            fieldNames: {
              name: 'name',
              key: 'id',
              checkKey: 'id',
              children: 'children'
            },
            request: async() => {
              try {
                const { data } = await getEquipmentTagTree();
                console.log(getChildren(data));
                return getChildren(data);
              } catch (error) {
                return [];
              }
            }
          };
        }
      }
    ]
  });
  // 返回
  const toBack = () => {
    uni.navigateBack();
  };
  // 跳转到数据采集页面
  const toNext = (equipmentId, code) => {
    const { componentType } = queryInfo.value;
    const params = {
      equipmentId,
      code: code,
      ...queryInfo.value
    };
    const query = Object.keys(params)
      .map((key) => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
      .join('&');
    if (componentType === 'EQUIPMENT_INFO') {
      uni.navigateTo({
        url: `/pages/businessComponents/equipmentInfo/index?${query}`
      });
    } else if (componentType === 'EQUIPMENT_DATA_ACQUISITION') {
      uni.navigateTo({
        url: `/pages/businessComponents/equipmentDataAcquisition/dataAcquisition/index?${query}`
      });
    }
  };

  const filterDetail = () => {
    const { name, code, ids } = filterData.value;
    let filteredList = equipmentList.value;
    if (!isEmpty(name)) {
      filteredList = filteredList.filter(item => item.name.includes(name));
    }
    if (!isEmpty(code)) {
      filteredList = filteredList.filter(item => item.code.includes(code));
    }
    if (ids && ids.length > 0) {
      filteredList = filteredList.filter(item => 
        item.tagIdList.some(tag => ids.includes(tag.id))
      );
    }
    showEquipmentList.value = filteredList;
  };

  const scanValue = ref('');
  const onScanSuccess = async(code) => {
    try {
      const { data } = await postScanScanDeviceCode({ deviceCode: code });
      if (data) {
        toNext(data.deviceId, code);
      } else {
        toast.error(t('设备码无法识别，可选择设备'));
      }
    } catch (error) {
      error.message && toast.error(error.message);
    }
  };
  const onScanFail = (err) => {
    toast.error(t('设备码无法识别，可选择设备'));
  };

  const title = ref(t('设备数采'));

  const getDetail = async(query) => {
    try {
      queryInfo.value = query;
      const { configInfo, componentType } = query;
      if (componentType === 'EQUIPMENT_INFO') {
        title.value = t('设备信息');
      } else if (componentType === 'EQUIPMENT_DATA_ACQUISITION') {
        title.value = t('设备数采');
      }
      if (configInfo && configInfo !== 'null' && JSON.parse(configInfo).station) {
        const res = await reqGetConfigByStationIdListApi(
          urlQueryRef.value.productionLineId || '',
          JSON.parse(configInfo).station
        );
        equipmentList.value = res.data;
        filterDetail();
      } else {
        const res = await reqPlatformEquipmentAppGetConfigByProductionLineIdApi({
          productionLineId: urlQueryRef.value.productionLineId
        });
        equipmentList.value = res.data;
        filterDetail();
      }
    } catch (error) {
      //
    }
  };
  const getChildren = (arr) => {
    let newArr = arr.map((item) => {
      if (item.children?.length === 0) {
        item.categoryFlag = true;
      } else {
        item.categoryFlag = false;
        item.children = getChildren(item.children);
      }
      return item;
    });
    return newArr;
  };

  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e).map((key) => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    getDetail(query);
    // #endif
    // #ifdef H5
    getDetail(e);
  // #endif
  });
</script>

<style lang="scss" scoped>
.form-container {
  padding: 20px 8px;
  :deep(.bm-form) {
    width: 100%;
  }
}
.right-content {
  display: flex;

  :deep .uv-button {
    border: none;
  }
}
.top-scan {
  padding-top: 9.38rpx;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 9.38rpx;
  .wd-input {
    width: 50%;
  }
}
.content {
  width: 100%;
  height: calc(100vh - 46.88rpx - 9.38rpx - 46.88rpx - 9.38rpx - 9.38rpx);
  font-size: 14rpx;
  font-weight: normal;
  .equipment-list {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    padding-bottom: 9.38rpx;
    gap: 9.38rpx;
    grid-row: 9.38rpx;
  }
}
</style>
