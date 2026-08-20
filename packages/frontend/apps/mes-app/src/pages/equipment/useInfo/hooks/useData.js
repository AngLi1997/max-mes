import { getEquipmentAppByLinePage, getEquipmentAppPage, getEquipmentTagTree, postScanScanDeviceCode } from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { useScan } from '@/utils/useScan.js';
import { onShow } from '@dcloudio/uni-app';
import { onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

const { showNotify } = useNotify();

export const useData = ({ props }) => {
  const showForm = ref(false);
  const liquidScan = ref(); // 设备扫描值
  const equipmentList = ref();
  const isWindows = ref(false);
  const itemValue = ref();
  const { bmosScanCode } = useScan();
  const params = reactive({
    pageNum: 1,
    pageSize: 24,
  });
  const loadMoreStatus = ref('loadmore');
  const total = ref(0);
  const triggered = ref(false);
  const filterData = ref();

  // 是否是deviceStatus页面
  const isDeviceStatus = props.deviceStatus === 'true';
  // 设备扫码失败
  const onScanFail = () => {
    showNotify({ type: 'danger', message: t('请扫描设备标签') });
  };
  // 打开日志填报页/ 跳转设备详情页
  const openAddInfo = (data) => {
    if (isDeviceStatus || props.confirmBefore) {
      const query = Object.keys(data)
        .map(
          key => `${encodeURIComponent(key)}=${encodeURIComponent(data[key])}`,
        )
        .join('&');
      uni.navigateTo({
        url: `/pages/deviceStatus/deviceDetails/index?${query}`,
      });
      return;
    }
    showForm.value = true;
    itemValue.value = data;
  };

  // 设备扫描成功
  const onScanSuccess = async (code) => {
    if (!code) {
      uni.showToast({
        title: t('扫码失败'),
        icon: 'error',
        duration: 2000,
        mask: true,
      });
      return;
    }
    try {
      const { data } = await postScanScanDeviceCode({ deviceCode: code });
      openAddInfo({
        id: data.deviceId,
        code: data.deviceCode,
        name: data.deviceName,
      });
    }
    catch (error) {
      error.message && showNotify({ type: 'warning', message: error.message });
    }
  };

  // 打开扫码
  const openScan = () => {
    bmosScanCode({
      success: (data) => {
        const type = data.result.slice(0, 2);
        if (type !== '04') {
          showNotify({ type: 'warning', message: t('请扫描设备标签') });
          return;
        }
        const code = data.result.slice(2);
        onScanSuccess(code);
      },
      fail: () => {
        showNotify({ type: 'warning', message: t('扫码失败') });
      },
      complete: () => {},
    });
  };
  // 关闭日志填报页
  const closeAdduseInfo = () => {
    showForm.value = false;
    itemValue.value = {};
  };

  const getAllList = async () => {
    const { data } = props?.confirmBefore ? await getEquipmentAppByLinePage({ ...params, ...filterData.value, productionLineId: props.productionLineId }) : await getEquipmentAppPage({ ...params, ...filterData.value });
    if (params.pageNum === 1) {
      equipmentList.value = data.list;
    }
    else {
      equipmentList.value = equipmentList.value.concat(data.list);
    }
    total.value = data.total;
    triggered.value = false;
    loadMoreStatus.value
      = total.value > equipmentList.value.length ? 'loadmore' : 'nomore';
  };
  // 筛选设备
  const filterConfirm = async () => {
    params.pageNum = 1;
    getAllList();
  };

  const getChildren = (arr) => {
    const newArr = arr.map((item) => {
      if (item.children?.length === 0) {
        item.categoryFlag = true;
      }
      else {
        item.categoryFlag = false;
        item.children = getChildren(item.children);
      }
      return item;
    });
    return newArr;
  };
  const formProps = reactive({
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('设备名称'),
        colProps: {
          span: 24,
        },
      },
      {
        field: 'code',
        component: 'Input',
        label: t('设备编码'),
        colProps: {
          span: 24,
        },
      },
      {
        field: 'ids',
        component: 'BMFormSelect',
        label: t('设备类型'),
        colProps: {
          span: 24,
        },
        componentProps: () => {
          return {
            request: async () => {
              // 获取设备类型树
              const { data } = await getEquipmentTagTree();
              return getChildren(data);
            },
            title: t('设备类型'),
            type: 'tree',
            placeholder: t('请选择'),
            fieldNames: {
              key: 'id',
              checkKey: 'id',
              children: 'children',
            },
            mode: 'multiple',
            treeData: [],
          };
        },
      },
    ],
  });
  // 下拉刷新触发
  const onRefresh = async () => {
    params.pageNum = 1;
    triggered.value = true;
    getAllList();
  };
  // 上拉触底
  const onScrolltolower = () => {
    console.log('上拉触底');
    if (
      params.pageNum * params.pageSize < total.value
      && triggered.value === false
    ) {
      params.pageNum++;
      loadMoreStatus.value = 'loading';
      getAllList();
    }
  };
  onMounted(async () => {
    // #ifdef APP-PLUS
    isWindows.value = false;
    // #endif
    // #ifdef H5
    isWindows.value = true;
    // #endif
    getAllList();
  });

  onShow(() => {
    params.pageNum = 1;
    getAllList();
  });
  return {
    showForm,
    liquidScan,
    formProps,
    equipmentList,
    isWindows,
    itemValue,
    params,
    triggered,
    isDeviceStatus,
    loadMoreStatus,
    filterData,
    onScanSuccess,
    onScanFail,
    filterConfirm,
    openAddInfo,
    closeAdduseInfo,
    openScan,
    onRefresh,
    onScrolltolower,
  };
};
