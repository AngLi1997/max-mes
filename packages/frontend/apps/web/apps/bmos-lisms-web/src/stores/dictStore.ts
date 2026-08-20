import { getMaterialSupplierList, getStaticDataConfigList } from '@/services';
import { cloneDeep } from '@bmos/utils';
import { defineStore } from 'pinia';
import { ref } from 'vue';

const staticConfigEnum = {
  标本管理: 'GM001',
  拒收原因: 'GM002',
  物料库管理: 'GM003',
  供应商类型: 'GM004',
  关键物料类型: 'GM005',
  物料单位: 'GM006',
  物料规格: 'GM007',
  仓库地址: 'GM008',
  领用原因: 'GM009',
  报废原因: 'GM010',
  退货原因: 'GM011',
  实验室资源管理: 'GM012',
  消耗原因: 'GM013',
  设备类型: 'GM014',
  检验管理: 'GM015',
  检验方式: 'GM016',
  质控品类型: 'GM017',
  报告管理: 'GM018',
  文件类型: 'GM019',
  全局参数: 'GM020',
  检验参数: 'GM021',
  领用库: 'GM022',
  物料参数: 'GM023',
  修约规则: 'GM025',
  修约参数: 'GM026',
  免疫类型: 'GM028',
  蛋白电泳检验文件: 'GM029',
  质控品含量: 'GM030',
  供应商: 'GYS',
};

export type StaticConfigEnumKeys = keyof typeof staticConfigEnum;

/**
 * 获取供应商列表
 * @returns data
 */
const getSupplierList = async () => {
  const { data } = await getMaterialSupplierList();
  return { data: data.map((item: any) => ({ label: item.supplierName, value: item.identify })) };
};

export const useDict = defineStore('dictStore', () => {
  // 字典集合
  const dictMap = ref<Map<string, any>>(new Map());

  // 更新字典集合
  const setDict = async (menuIdentify: StaticConfigEnumKeys) => {
    try {
      let dict: any[] = [];
      switch (menuIdentify) {
        case '供应商': {
          const { data } = await getSupplierList();
          dict = data ?? [];
          break;
        }
        case '领用库': {
          const { data } = await getStaticDataConfigList(staticConfigEnum[menuIdentify]);
          if (data && data.length) {
            dict = data.map((item: any) => ({ label: `${item.value}-${item.label}`, value: item.value }));
          }
          break;
        }
        default: {
          const { data } = await getStaticDataConfigList(staticConfigEnum[menuIdentify]);
          dict = data ?? [];
          break;
        }
      }
      // const { data } =
      //   menuIdentify === '供应商'
      //     ? await getSupplierList()
      //     : await getStaticDataConfigList(staticConfigEnum[menuIdentify]);
      if (dict && dict.length) {
        dictMap.value.set(staticConfigEnum[menuIdentify], dict);
      }
    } catch (err) {
      console.log(err);
    }
  };

  const getDict = async (menuIdentify: StaticConfigEnumKeys): Promise<any[]> => {
    const dict = dictMap.value.get(staticConfigEnum[menuIdentify]);
    if (!dict || !dict.length) {
      await setDict(menuIdentify);
    }
    return cloneDeep(dictMap.value.get(staticConfigEnum[menuIdentify]));
  };

  // 查找字典
  const findDictItem = (menuIdentify: StaticConfigEnumKeys, id: string) => {
    return dictMap.value.get(staticConfigEnum[menuIdentify])?.find((item: any) => item.value === id);
  };

  return { dictMap, getDict, findDictItem, setDict };
});
