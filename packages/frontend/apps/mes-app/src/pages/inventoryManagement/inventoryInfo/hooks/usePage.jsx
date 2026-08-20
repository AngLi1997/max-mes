import {
  getStorageConfigTreeApi,
  getStorageMaterialBatchPageApi,
  getStorageMaterialInfoByNo,
  getStorageMaterialPageApi,
} from '@/api';
import { usePermissionStore } from '@/stores/permission.js';
import { t } from '@/utils/useBmosI18n.js';
import { useMathJs } from '@/utils/useMathJs.js';
import { computed, onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import { tableColProps } from './tableColProps.jsx';

export const usePage = () => {
  const { showNotify } = useNotify();
  const { math } = useMathJs();
  const cargoSpaceOpen = ref(false);
  const { hasPermission } = usePermissionStore();
  const infoItems = [
    {
      label: t('货位信息'),
      field: 'cargoSpaceName',
      type: 'text',
    },
    {
      label: t('选择货位'),
      type: 'button',
      click: () => (cargoSpaceOpen.value = true),
    },
  ];
  const initId = ref('');
  const cargoSpaceNameObj = ref({});
  const treeModalData = ref([]); // 货位树
  const goodsLocation = ref(); // 当前选择货位
  const showList = ref(true);
  const listData = ref([
    {
      label: t('出库'),
      url: 'materialOutbound',
      isShow: hasPermission('121020002000001'),
    },
    {
      label: t('入库'),
      url: 'toWarehouse',
      isShow: hasPermission('121020002000020'),
    },
    {
      label: t('移库'),
      url: 'transfer',
      isShow: hasPermission('121020002000003'),
    },
    {
      label: t('退库'),
      url: 'return',
      isShow: hasPermission('121020002000013'),
    },
    {
      label: t('使用'),
      url: 'use',
      isShow: hasPermission('121020002000015'),
    },
    {
      label: t('销毁'),
      url: 'destruction',
      isShow: hasPermission('121020002000014'),
    },
    {
      label: t('库存物料'),
      url: 'table1',
      isShow: true,
    },
    {
      label: t('批次统计'),
      url: 'table2',
      isShow: true,
    },
  ]);

  const infoData = computed(() => {
    return {
      cargoSpaceName: cargoSpaceNameObj.value[goodsLocation.value],
    };
  });

  const tableType = ref(1); // 表格显示类型 物料件信息 1 批次同级 2
  const materialScan = ref(); // 当前扫描物料件号

  //   // 表格配置
  const { msg_tableColProps, statistics_tableColProps } = tableColProps(
    goodsLocation,
  );

  const toUrl = (url) => {
    if (url === 'table1' || url === 'table2') {
      tableType.value = url === 'table1' ? 1 : 2;
      showTableTypeChange();
      showList.value = false;
      return;
    }
    const newQuery = {
      materialPositionId: goodsLocation.value,
    };
    const query = Object.keys(newQuery)
      .map(
        key =>
          `${encodeURIComponent(key)}=${encodeURIComponent(
            newQuery[key],
          )}`,
      )
      .join('&');
    uni.navigateTo({
      url: `/pages/inventoryManagement/materialOperation/${url}/index?${query}`,
    });
  };

  // 取消选择货位
  const goodsLocationCancel = () => {
    // 没有选择货位不能进行操作
    if (!goodsLocation.value) {
      uni.reLaunch({
        url: `/pages/home/index`,
      });
    }
  };
  // 手动输入物料件/容器编号
  const onScanConfirm = () => {
    if (materialScan.value) {
      handleScan(materialScan.value);
    }
  };
  // 扫描物料件成功
  const onScanSuccess = (res) => {
    // 判断res的前两位是否为01 02 04,如果是则去掉前两位
    if (res.startsWith('01') || res.startsWith('02') || res.startsWith('04')) {
      const code = res.substring(2);
      code && handleScan(code);
    }
    else {
      showNotify({
        type: 'danger',
        message: t('请扫描的物料件/容器标签'),
      });
    }
  };
  const handleScan = async (code) => {
    try {
      const { data } = await getStorageMaterialInfoByNo({
        materialNo: code,
      });
      if (plusTotal(data.availableQuantity, data.reserveQuantity) === '0') {
        showNotify({ type: 'warning', message: t('物料件未生效') });
        return;
      }
      if (data.materialPositionId) {
        const newQuery = {
          ...data,
          goodsLocation: goodsLocation.value,
        };
        const query = Object.keys(newQuery)
          .map(
            key =>
              `${encodeURIComponent(key)}=${encodeURIComponent(
                newQuery[key],
              )}`,
          )
          .join('&');
        uni.navigateTo({
          url: `/pages/inventoryManagement/materialDetails/index?${query}`,
        });
      }
      else {
        const newQuery = {
          ...data,
          ...{
            materialPositionId: goodsLocation.value,
          },
          goodsLocation: goodsLocation.value,
        };
        const url = Object.keys(newQuery)
          .map(
            key =>
              `${encodeURIComponent(key)}=${encodeURIComponent(
                newQuery[key],
              )}`,
          )
          .join('&');
        uni.navigateTo({
          url: `/pages/inventoryManagement/materialOperation/toWarehouse/index?${url}`,
        });
      }
    }
    catch (error) {
      error.message && showNotify({ type: 'warning', message: error.message });
    }
  };
  const showTableTypeChange = () => {
    if (tableType.value === 1) {
      tableProps.tableColProps = [...msg_tableColProps];
    }
    else {
      tableProps.tableColProps = [...statistics_tableColProps];
    }
  };
  const tableProps = reactive({
    border: false,
    pagination: {
      pageSize: 20,
    },
    showNoData: true,
    noDataText: t('暂无库存信息'),
    tableColProps: [...msg_tableColProps],
  });
  // 获取表格数据
  const getTableList = async (params) => {
    if (!goodsLocation.value) {
      return [];
    }
    return await (tableType.value === 1
      ? getStorageMaterialPageApi
      : getStorageMaterialBatchPageApi)({
      ...params,
    });
  };

  const getShowName = (arr, parentName = '') => {
    arr.map((item) => {
      let name = '';
      if (parentName) {
        name = `${parentName}/`;
      }
      name += item.name;
      cargoSpaceNameObj.value[item.id] = name;
      if (item.children.length) {
        getShowName(item.children, name);
      }
    });
  };
  onMounted(async () => {
    // 获取暂存树
    const { data } = await getStorageConfigTreeApi();
    treeModalData.value = data;
    getShowName(treeModalData.value);
    if (initId.value) {
      goodsLocation.value = initId.value;
    }
    else {
      cargoSpaceOpen.value = true;
    }
  });
  const plusTotal = (a, e) => {
    const a1 = math.bignumber(a);
    const e1 = math.bignumber(e);
    return math.add(a1, e1).toString();
  };

  const toBack = () => {
    if (!showList.value) {
      showList.value = true;
      return;
    }
    uni.reLaunch({
      url: `/pages/home/index`,
    });
  };
  return {
    infoItems,
    infoData,
    cargoSpaceOpen,
    goodsLocation,
    cargoSpaceNameObj,
    treeModalData,
    tableType,
    materialScan,
    tableProps,
    initId,
    showList,
    listData,
    toUrl,
    goodsLocationCancel,
    onScanSuccess,
    onScanConfirm,
    showTableTypeChange,
    getTableList,
    toBack,
  };
};
