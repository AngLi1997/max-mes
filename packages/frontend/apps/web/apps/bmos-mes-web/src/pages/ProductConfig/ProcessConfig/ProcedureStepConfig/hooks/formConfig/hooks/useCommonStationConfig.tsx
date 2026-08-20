import { getProcedureRooms } from '@/services';
import { FormSchema } from '@bmos/components';
import { t } from '@bmos/i18n';
import { loopSelectableNotValueTree } from '@bmos/utils';
import { ConfigFormProps } from '../../../types';

export type UseCommonStationConfigParams = {
  props: ConfigFormProps;
  hasChange: Ref<boolean>;
  showStationTitle?: boolean;
};
export const useCommonStationConfig = ({ props, hasChange, showStationTitle = true }: UseCommonStationConfigParams) => {
  const stationConfig: FormSchema[] = [
    {
      field: 'stationConfig',
      label: t('工位信息'),
      component: 'TableTitle',
      vIf: showStationTitle,
    },
    {
      field: 'stationShow',
      component: 'TreeSelect',
      label: t('工位'),
      componentProps: ({ formModel }) => {
        return {
          multiple: true,
          showSearch: true,
          treeNodeFilterProp: 'showName',
          fieldNames: {
            label: 'showName',
            value: 'roomIdPath',
            children: 'children',
          },
          request: async () => {
            try {
              const { data } = await getProcedureRooms(props.procedureId);
              return loopSelectableNotValueTree(data, 'stationFlag', true);
            } catch (error) {
              return [];
            }
          },
          onChange: (val: string[]) => {
            formModel['station'] = val.map(item => item.split('-').pop());
            hasChange.value = true;
          },
        };
      },
    },
  ];
  return {
    stationConfig,
  };
};
