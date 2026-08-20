import { reqGetFlowConfigTreeReq } from '@/services';
import { FormProps, Recordable, formInstance } from '@bmos/components';
import { flow_STATE } from '../../enum';

export type UseFormParams = { watchStatus: Ref<flow_STATE>; isSaveFlow: Ref<boolean> };

export const useForm = (useFormContext: UseFormParams) => {
  const { watchStatus, isSaveFlow } = useFormContext;

  // form实例
  const setFormRef = ref<formInstance>();

  const setFormProps = reactive<FormProps>({
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'name',
        label: t('流程模型名称'),
        component: 'Input',
        required: true,
        componentProps: {
          disabled: watchStatus.value === flow_STATE.updateVersion || watchStatus.value === flow_STATE.editVersion,
          onChange: () => {
            isSaveFlow.value = false;
          },
        },
      },
      {
        field: 'categoryCode',
        label: t('流程类型'),
        required: true,
        component: 'TreeSelect',
        componentProps: {
          disabled: watchStatus.value !== flow_STATE.addFlow,
          fieldNames: {
            label: 'name',
            value: 'id',
            children: 'itemList',
          },
          onChange: () => {
            isSaveFlow.value = false;
          },
          request: async () => {
            try {
              const { data } = await reqGetFlowConfigTreeReq();
              return [
                {
                  id: 'all',
                  name: t('全部'),
                  selectable: false,
                  itemList: data.map((item: any) => {
                    return {
                      ...item,
                      selectable: false,
                      itemList: item.itemList.map((it: any) => {
                        return {
                          ...it,
                          selectable: true,
                        };
                      }),
                    };
                  }),
                },
              ];
            } catch (error) {
              return [];
            }
          },
        },
      },
      {
        field: 'version',
        label: t('版本号'),
        component: 'Input',
        defaultValue: 1,
        required: true,
        componentProps: {
          style: {
            width: '100%',
          },
          onChange: () => {
            isSaveFlow.value = false;
          },
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('版本描述'),
        componentProps: {
          maxLength: 50,
          rows: 4,
          class: 'add-flow-modal-textarea',
          onChange: () => {
            isSaveFlow.value = false;
          },
        },
      },
    ],
  });

  const setNodeFormData = async (formData: Recordable) => {
    try {
      await nextTick();
      Object.keys(formData).forEach(key => {
        if (key === 'label') {
          setFormRef.value?.setFormModel('name', formData[key]);
        }
        setFormRef.value?.setFormModel(key, formData[key]);
      });
    } catch (error) {
      //
    }
  };

  return {
    setFormRef,
    setFormProps,
    setNodeFormData,
  };
};
