import { getStorageMaterialInfoByNo, getStorageMaterialPrintTag } from '@/api';
import { usePermissionStore } from '@/stores/permission.js';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useDetails = () => {
  const { showNotify } = useNotify();

  const { hasPermission } = usePermissionStore();
  const bmosPrinterInstance = ref(null);
  // 详情api数据
  const detailsApiList = ref({});
  // 物料详情列表
  const details = ref([
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
    },
    {
      title: t('物料编码'),
      dataIndex: 'mergeCode',
    },
    {
      title: t('物料批次'),
      dataIndex: 'materialBatchNo',
    },
    {
      title: t('物料件号'),
      dataIndex: 'materialNo',
    },
    {
      title: t('可用量'),
      dataIndex: 'availableQuantity',
      isUnit: true,
      unitName: 'unit',
    },
    {
      title: t('预定量'),
      dataIndex: 'reserveQuantity',
      isUnit: true,
      unitName: 'unit',
    },
    {
      title: t('容器'),
      dataIndex: 'container',
    },
    {
      title: t('货位'),
      dataIndex: 'materialIsName',
    },
    {
      title: t('有效期至'),
      dataIndex: 'expiredDate',
    },
    {
      title: t('预定批次'),
      dataIndex: 'batchNo',
    },
    {
      title: t('预定人员'),
      dataIndex: 'reserveUserName',
    },
    {
      title: t('预定时间'),
      dataIndex: 'reserveTime',
    },
    {
      title: t('原始编码'),
      dataIndex: 'originalCode',
    },
    {
      title: t('原厂批号'),
      dataIndex: 'factoryBatchNo',
    },
    {
      title: t('生产商'),
      dataIndex: 'producer',
    },
    {
      title: t('供应商'),
      dataIndex: 'supplier',
    },
  ]);
    // 打印
  const printing = async () => {
    const res = await bmosPrinterInstance.value.print();
    try {
      uni.showLoading({
        title: t('打印中...'),
        mask: true,
      });
      if (res) {
        const data = {
          body: {
            no: detailsApiList.value.materialNo,
          },
          deviceId: res.id,
          sceneId: 121001003,
        };
        await getStorageMaterialPrintTag(data);
        setTimeout(() => {
          uni.hideLoading();
        }, 2000);
      }
      uni.hideLoading();
    }
    catch (error) {
      error.message && showNotify({ type: 'warning', message: error.message });
    }
  };
  // 跳转
  const toUrl = (url) => {
    // 页面A 跳转到 页面B，并传递参数
    if (url.to) {
      const data = {
        ...detailsApiList.value,
        categoryType: detailsApiList.value.categoryType.value,
      };
      const query = Object.keys(data)
        .map(
          key =>
            `${encodeURIComponent(key)}=${encodeURIComponent(
              data[key],
            )}`,
        )
        .join('&');
      uni.navigateTo({
        url: `${url.to}?${query}`,
      });
    }
  };
  // 标签按钮
  const tagKeys = reactive([
    {
      title: t('拆包'),
      type: 'success',
      plain: false,
      to: '/pages/inventoryManagement/materialOperation/unpacking/index',
      onClick: res => toUrl(res),
      isShow: hasPermission('121020002000006'),
    },
    {
      title: t('使用'),
      type: 'success',
      plain: false,
      to: '/pages/inventoryManagement/materialOperation/use/index',
      onClick: res => toUrl(res),
      isShow: hasPermission('121020002000015'),
    },
    {
      title: t('出库'),
      type: 'primary',
      plain: false,
      to: '/pages/inventoryManagement/materialOperation/materialOutbound/index',
      onClick: res => toUrl(res),
      isShow: hasPermission('121020002000001'),
    },
    {
      title: t('退库'),
      type: 'info',
      plain: true,
      to: '/pages/inventoryManagement/materialOperation/return/index',
      onClick: res => toUrl(res),
      isShow: hasPermission('121020002000013'),
    },
    {
      title: t('盘点'),
      type: 'info',
      plain: true,
      to: '/pages/inventoryManagement/materialOperation/inventory/index',
      onClick: res => toUrl(res),
      isShow: hasPermission('121020002000002'),
    },
    {
      title: t('预定'),
      type: 'info',
      plain: true,
      to: '/pages/inventoryManagement/materialOperation/reserve/index',
      onClick: res => toUrl(res),
      isShow: hasPermission('121020002000004'),
    },
    {
      title: t('销毁'),
      type: 'info',
      plain: true,
      to: '/pages/inventoryManagement/materialOperation/destruction/index',
      onClick: res => toUrl(res),
      isShow: hasPermission('121020002000014'),
    },
    {
      title: t('移库'),
      type: 'info',
      plain: true,
      to: '/pages/inventoryManagement/materialOperation/transfer/index',
      onClick: res => toUrl(res),
      isShow: hasPermission('121020002000003'),
    },
    {
      title: t('打印'),
      type: 'info',
      plain: true,
      to: '',
      onClick: () => printing(),
      isShow: hasPermission('121020002000007'),
    },
  ]);
  // 判断是否有值
  const isObjectEmpty = (obj) => {
    return Object.values(obj).every((value) => {
      return (
        value === null
        || value === undefined
        || (typeof value === 'string' && value.trim() === '')
        || (Array.isArray(value) && value.length === 0)
      );
    });
  };
  // 物料详情Api
  const detailsApi = async (materialNo) => {
    try {
      const res = await getStorageMaterialInfoByNo({
        materialNo,
      });
      const customFields = [...res.data.materialCustomFields, ...res.data.materialBatchCustomFields, ...res.data.materialPieceCustomFields];
      const arrAll = customFields?.map((item) => {
        return {
          title: item.fieldName,
          dataIndex: `${item.fieldType}-${item.field}`,
          value: item.fieldValue,
        };
      });
      const keyField = 'dataIndex';
      const objTemp = arrAll?.reduce((obj, item) => {
        obj[item[keyField]] = item.value;
        return obj;
      }, {});
      details.value = details.value.concat(arrAll);
      detailsApiList.value = {
        ...res.data,
        materialIsName: `${res.data?.materialPositionCode}-${
          res?.data?.materialPositionName
        }`,
        ...objTemp,
      };
      // 预定值
      const batchId = {
        batchId: res.data?.batchId,
      };
      if (!isObjectEmpty(batchId)) {
        tagKeys[4].isShow = false; // 盘点
        tagKeys[5].title = t('取消预定'); // 预定
        tagKeys[5].isShow = hasPermission('121020002000005');
        tagKeys[5].to = '/pages/inventoryManagement/materialOperation/cancelReserve/index';
      }
    }
    catch (error) {
      error.message && showNotify({ type: 'warning', message: error.message });
    }
  };

  return {
    details,
    tagKeys,
    detailsApi,
    detailsApiList,
    bmosPrinterInstance,
    toUrl,
  };
};
