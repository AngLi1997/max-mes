import { getProcedureRoomsListReq } from '@/services';
import { FormSchema } from '@bmos/components';
import { t } from '@bmos/i18n';
import { loopSelectableNotValueTree } from '@bmos/utils';
import { ConfigFormProps } from '../../../../types';
import { useCommonStationConfig } from '../../hooks';

export type UseCleanCheckConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
};
// 清场执行
export const useCleanCheckConfig = ({ props, hasChange }: UseCleanCheckConfigParams) => {
  const { stationConfig } = useCommonStationConfig({ props, hasChange });
  const cleanCheckConfig = ref<FormSchema[]>([
    {
      field: 'roomIdListShow',
      component: 'TreeSelect',
      label: t('房间配置'),
      componentProps: ({ formModel }) => {
        return {
          multiple: true,
          showSearch: true,
          fieldNames: {
            label: 'showName',
            value: 'roomIdPath',
            children: 'children',
          },
          treeNodeFilterProp: 'showName',
          request: async () => {
            try {
              const { data } = await getProcedureRoomsListReq(props.procedureId);
              return loopSelectableNotValueTree(data, 'roomFlag', true);
            } catch (error) {
              return [];
            }
          },
          onChange: (val: string[]) => {
            formModel.roomIdList = val?.map(item => item.split('-').pop()) || [];
            hasChange.value = true;
          },
        };
      },
    },
    ...stationConfig
  ]);
  return {
    cleanCheckConfig,
  };
};
