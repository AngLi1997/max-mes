import { getConfigAll } from '@/services';
import { Recordable } from '@bmos/components';
import { formatToDate, isEmpty } from '@bmos/utils';
import { message } from 'ant-design-vue';
import { defineStore } from 'pinia';

export const ConfigEnum = {
  实验室名称: 'CF0001',
  日期格式: 'CF0002',
  冷藏超期开关: 'CF0003',
  标本冷藏超期时间: 'CF0004',
  血清标本颜色: 'CF0005',
  蛋白电泳超期开关: 'CF0006',
  冷冻超期开关: 'CF0007',
  标本冷栋超期时间: 'CF0008',
  质控品最大数量: 'CT0001',
  合格数据颜色: 'CT0002',
  不合格数据颜色: 'CT0003',
  物料有效期预警阈值: 'CW001',
  供应商有效期预警阈值: 'CW002',
  默认最低库存量: 'CW003',
  无修约规则: 'RR001',
  'HLJZ/2-09-006': 'RR001',
  内质控值: 'RT0001',
  标本类型使用: 'ORG_TEST_SAMPLE',
  普通: 'NORMAL',
  甲肝: 'HA',
  乙肝: 'HB',
  狂犬: 'RABIES',
  破伤风: 'TETANUS',
  炭疽: 'ANTHRAX',
};

export type ConfigEnumKeys = keyof typeof ConfigEnum;

export const useConfig = defineStore('configStore', () => {
  const configs = ref<Recordable[]>([]);

  const refreshConfig = async () => {
    try {
      const { data } = await getConfigAll();
      configs.value = data;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const getConfigEnumsValueByParamId = (paramId: ConfigEnumKeys) => {
    const item = configs.value.find((config: Recordable) => config.paramId === ConfigEnum[paramId]);
    if (item) {
      return item.enumsValue;
    } else {
      return '';
    }
  };

  const getDateFormat = (date: string) => {
    try {
      if (isEmpty(date)) return '-';
      const dateFormat = getConfigEnumsValueByParamId('日期格式');
      return isEmpty(dateFormat) ? date : formatToDate(date, dateFormat);
    } catch (error) {
      return '-';
    }
  };

  return { refreshConfig, configs, getConfigEnumsValueByParamId, getDateFormat };
});
