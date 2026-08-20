import { getListEquipmentPropertyReq } from '@/services';
import { FormSchema } from '@bmos/components';
import { t } from '@bmos/i18n';
import { findItemByAttr } from '@bmos/utils';
import { ConfigFormProps } from '../../../../types';

export type UseEquipmentInfoBasicConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 设备信息 - 基础配置
export const useEquipmentInfoBasicConfig = ({ props, hasChange }: UseEquipmentInfoBasicConfigParams) => {
  const equipmentInfoBasicConfig = ref<FormSchema[]>([
    {
      field: 'equipmentAttr',
      component: 'TreeSelect',
      label: t('设备属性配置'),
      required: true,
      componentProps: () => {
        return {
          showSearch: true,
          treeNodeFilterProp: 'name',
          fieldNames: {
            label: 'name',
            value: 'propertyCode',
            children: 'children',
          },
          request: async () => {
            try {
              const equipmentInfoItem = findItemByAttr(props.nodeList, 'id', props.activeNodeData?.parentId);
              const equipmentInfoConfig = props.configList.find(item => item.fieldId === equipmentInfoItem?.fieldId);
              if (equipmentInfoConfig) {
                const station = JSON.parse(equipmentInfoConfig.configInfo).station;
                if (station) {
                  const { data } = await getListEquipmentPropertyReq(station.join(','));
                  return [
                    {
                      name: t('基础属性'),
                      propertyCode: 'basic',
                      selectable: false,
                      children: [
                        {
                          name: t('设备编码'),
                          propertyCode: 'code',
                        },
                        {
                          name: t('设备名称'),
                          propertyCode: 'name',
                        },
                        {
                          name: t('设备标签'),
                          propertyCode: 'tagNames',
                        },
                        {
                          name: t('规格型号'),
                          propertyCode: 'specifications',
                        },
                        {
                          name: t('设备地点'),
                          propertyCode: 'position',
                        },
                        {
                          name: t('设备厂商'),
                          propertyCode: 'manufacturer',
                        },
                        {
                          name: t('购置日期'),
                          propertyCode: 'purchase_date',
                        },
                      ],
                    },
                    {
                      name: t('静态属性'),
                      propertyCode: 'static',
                      selectable: false,
                      children: data,
                    },
                  ];
                }
              }
              return [];
            } catch (error) {
              return [];
            }
          },
          onChange: () => {
            hasChange.value = true;
          },
        };
      },
    },
  ]);
  return {
    equipmentInfoBasicConfig,
  };
};
