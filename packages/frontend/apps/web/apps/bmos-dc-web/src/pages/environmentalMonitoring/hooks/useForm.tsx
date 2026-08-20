import {
  getPlatformTenementTree,
  getProcessProductLineReq,
  postPlatformFactoryRoomDashboardList,
  postPlatformTenementFloorTree,
  reqListDictCode,
} from '@/services';
import { FormProps, RenderCallbackParams } from '@bmos/components';
import { loopSelectableNotValueTree } from '@bmos/utils';

export const useForm = () => {
  const filterRef = ref<any>(null);
  const cleanLevelDict = ref<any[]>([]);
  const filterFormProps: Ref<FormProps> = ref({
    showAdvancedButton: false,
    baseColProps: {
      span: 4,
    },
    actionColOptions: {
      span: 4,
    },
    resetButtonOptions: {
      class: 'reset-btn',
    },
    submitButtonOptions: {
      class: 'submit-btn',
    },
    schemas: [
      {
        field: 'productLineIds',
        component: 'TreeSelect',
        label: t('产线'),
        noFormItemMarginBottom: true,
        formItemProps: {
          hasFeedback: false,
        },
        componentProps: () => {
          return {
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            maxTagCount: 'responsive',
            multiple: true,
            treeNodeFilterProp: 'showName',
            request: async () => {
              try {
                const { data } = await getProcessProductLineReq();
                return loopSelectableNotValueTree(data, 'lineFlag', true);
              } catch (error) {}
            },
          };
        },
      },
      {
        field: 'tenementIds',
        component: 'TreeSelect',
        label: t('楼栋'),
        noFormItemMarginBottom: true,
        formItemProps: {
          hasFeedback: false,
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            maxTagCount: 'responsive',
            multiple: true,
            treeNodeFilterProp: 'name',
            request: async () => {
              try {
                const { data } = await getPlatformTenementTree();
                return data;
              } catch (error) {}
            },
            onChange: () => {
              formModel.tenementFloorIds = [];
              formModel.roomId = [];
            },
          };
        },
      },
      {
        field: 'tenementFloorIds',
        component: 'Select',
        label: t('楼层'),
        noFormItemMarginBottom: true,
        formItemProps: {
          hasFeedback: false,
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            maxTagCount: 'responsive',
            mode: 'multiple',
            request: {
              watchFields: ['tenementIds'],
              options: {
                immediate: true,
              },
              callback: async () => {
                try {
                  if (!formModel.tenementIds?.length) {
                    return [];
                  }
                  const { data } = await postPlatformTenementFloorTree({
                    status: 'ENABLE',
                    tenementIds: formModel.tenementIds,
                  });
                  return data;
                } catch (error) {
                  return [];
                }
              },
            },
            onChange: () => {
              formModel.roomId = [];
            },
          };
        },
      },
      {
        field: 'cleanLevel',
        component: 'Select',
        label: t('清洁等级'),
        formItemProps: {
          hasFeedback: false,
        },
        noFormItemMarginBottom: true,
        componentProps: {
          fieldNames: {
            label: 'label',
            value: 'value',
          },
          maxTagCount: 'responsive',
          mode: 'multiple',
          request: async () => {
            const res = await reqListDictCode({
              code: 'CleanroomClassifications',
            });
            cleanLevelDict.value = res.data;
            return res.data;
          },
        },
      },
      {
        field: 'roomIds',
        component: 'Select',
        label: t('房间'),
        noFormItemMarginBottom: true,
        formItemProps: {
          hasFeedback: false,
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            maxTagCount: 'responsive',
            mode: 'multiple',
            request: {
              watchFields: ['tenementFloorIds'],
              options: {
                immediate: true,
              },
              callback: async () => {
                try {
                  if (!formModel.tenementFloorIds?.length) {
                    return [];
                  }
                  const { data } = await postPlatformFactoryRoomDashboardList({
                    cleanLevels: formModel.cleanLevel,
                    tenementFloorIds: formModel.tenementFloorIds,
                  });
                  return data;
                } catch (error) {
                  return [];
                }
              },
            },
          };
        },
      },
    ],
  });
  return {
    filterRef,
    filterFormProps,
    cleanLevelDict,
  };
};
