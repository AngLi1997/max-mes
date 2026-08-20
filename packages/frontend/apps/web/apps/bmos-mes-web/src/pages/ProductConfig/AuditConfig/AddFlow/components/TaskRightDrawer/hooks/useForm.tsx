import { getPlatformRoleAggregateTree } from '@/services';
import { PlusCircleOutlined } from '@ant-design/icons-vue';
import { FormProps, Recordable, RenderCallbackParams, formInstance } from '@bmos/components';
import { InputGroup, Select } from 'ant-design-vue';
import { Key } from 'ant-design-vue/es/_util/type';
import { TaskDrawerTabKeys } from '../../../types';

export type UseFormParams = {
  tabActiveKey: Ref<TaskDrawerTabKeys>;
  props: any;
};

export const useForm = (useFormContext: UseFormParams) => {
  const { tabActiveKey, props } = useFormContext;

  // form实例
  const setFormRef = ref<formInstance>();

  // 人员选择展开节点
  const reviewPersonExpandedKeys = ref<Key[]>([]);

  // 抄送人员选择展开节点
  const makePersonExpandedKeys = ref<Key[]>([]);

  // 消息通知展开节点
  const auditMegDTOListExpandedKeys = ref<Key[]>([]);

  const personTreeData = ref<any[]>([]);
  watch(
    () => props.personTreeData,
    () => {
      personTreeData.value = props.personTreeData;
      reviewPersonExpandedKeys.value = [personTreeData.value?.[0]?.id];
      makePersonExpandedKeys.value = [personTreeData.value?.[0]?.id];
      auditMegDTOListExpandedKeys.value = [personTreeData.value?.[0]?.id];
    },
    {
      immediate: true,
      deep: true,
    },
  );

  // 审核人员选择
  const reviewPersonOptions = ref<any[]>([]);
  const openSelectReviewPeople = ref<boolean>(false);
  const openSelectReviewPeopleModel = (reviewPerson: any) => {
    openSelectReviewPeople.value = true;
    reviewPersonOptions.value = reviewPerson?.map((item: any) => {
      return {
        ...item,
        ...(item?.option && {
          ...item?.option,
        }),
        ...(item?.value &&
          !item?.id && {
            id: item.value,
          }),
      };
    });
  };
  const updateReviewPeople = (reviewPerson: any) => {
    reviewPersonOptions.value = reviewPerson;
    setFormRef.value?.setFormModel(
      'reviewPerson',
      reviewPerson.map((item: any) => {
        return {
          ...item,
          label: item.name,
          key: item.id,
        };
      }),
    );
    if (reviewPerson.length) {
      setFormRef.value?.clearValidate('reviewPerson');
      setFormRef.value?.clearValidate('reviewRole');
    }
    // 校验会签策略
    setFormRef.value?.validateFields(['strategy']);
    openSelectReviewPeople.value = false;
  };

  // 抄送人员选择
  const makePersonOptions = ref<any[]>([]);
  const openSelectMakePeople = ref<boolean>(false);
  const openSelectMakePeopleModel = (makePerson: any) => {
    openSelectMakePeople.value = true;
    makePersonOptions.value = makePerson?.map((item: any) => {
      return {
        ...item,
        ...(item?.option && {
          ...item?.option,
        }),
        ...(item?.value &&
          !item?.id && {
            id: item.value,
          }),
      };
    });
  };
  const updateMakePeople = (makePerson: any) => {
    makePersonOptions.value = makePerson;
    setFormRef.value?.setFormModel(
      'makePerson',
      makePerson.map((item: any) => {
        return {
          ...item,
          label: item.name,
          key: item.id,
        };
      }),
    );
    if (makePerson.length) {
      setFormRef.value?.clearValidate('makePerson');
    }
    openSelectMakePeople.value = false;
  };

  // 消息通知选择
  const auditMegDTOListOptions = ref<any[]>([]);
  const openSelectAuditMegDTOList = ref<boolean>(false);
  const openSelectAuditMegDTOListModel = (auditMegDTOList: any) => {
    openSelectAuditMegDTOList.value = true;
    auditMegDTOListOptions.value = auditMegDTOList?.map((item: any) => {
      return {
        ...item,
        ...(item?.option && {
          ...item?.option,
        }),
        ...(item?.value &&
          !item?.id && {
            id: item.value,
          }),
      };
    });
  };
  const updateAuditMegDTOList = (auditMegDTOList: any) => {
    auditMegDTOListOptions.value = auditMegDTOList;
    setFormRef.value?.setFormModel(
      'auditMegDTOList',
      auditMegDTOList.map((item: any) => {
        return {
          ...item,
          label: item.name,
          key: item.id,
        };
      }),
    );
    if (auditMegDTOList.length) {
      setFormRef.value?.clearValidate('auditMegDTOList');
    }
    openSelectAuditMegDTOList.value = false;
  };

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
        label: t('任务名称'),
        component: 'Input',
        required: true,
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.BasicInfo;
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('描述'),
        componentProps: {
          rows: 4,
          class: 'add-flow-modal-textarea',
        },
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.BasicInfo;
        },
      },
      {
        field: 'needPwdValidate',
        component: 'RadioGroup',
        label: t('密码认证'),
        defaultValue: false,
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.BasicInfo;
        },
        componentProps: {
          options: [
            {
              label: t('是'),
              value: true,
            },
            {
              label: t('否'),
              value: false,
            },
          ],
        },
      },
      {
        field: 'field12',
        component: 'FormGroup',
        required: true,
        label: () => {
          return (
            <span>
              <span
                style={{
                  color: '#ff5633',
                  marginInlineEnd: '4px',
                  fontFamily: 'SimSun,sans-serif',
                }}>
                *
              </span>
              {t('处理人配置')}
            </span>
          );
        },
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
        },
      },
      {
        field: 'reviewPerson',
        label: t('分配审核人员'),
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <InputGroup compact>
              <Select
                style='width: 85%'
                v-model:value={formModel['reviewPerson']}
                fieldNames={{
                  label: 'name',
                  value: 'id',
                }}
                open={false}
                placeholder={t('请选择分配审核人员')}
                maxTagCount='responsive'
                mode='multiple'
                labelInValue={true}
                onChange={() => {
                  // 校验会签策略
                  setFormRef.value?.validateFields(['strategy']);
                }}
                options={reviewPersonOptions.value}></Select>
              <div
                style='width: 15%'
                class='right-add-icon-btn'
                onClick={() => openSelectReviewPeopleModel(formModel['reviewPerson'])}>
                <PlusCircleOutlined />
              </div>
            </InputGroup>
          );
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          // 如果选择了角色，就不需要选择人员
          return [
            {
              type: 'array',
              message: t('请选择分配审核人员'),
              validator: async (rule: any, value: any) => {
                if (!value?.length && !formModel.reviewRole?.length) {
                  return Promise.reject(t('请选择分配审核人员'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'reviewRole',
        label: t('分配审核角色'),
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
        },
        componentProps: {
          request: async () => {
            const { data }: any = await getPlatformRoleAggregateTree();
            return data;
          },
          fieldNames: {
            label: 'name',
            value: 'id',
          },
          onChange: (value: any) => {
            // 如果选择了角色，校验人员
            if (value?.length) {
              setFormRef.value?.clearValidate('reviewPerson');
            }
            // 校验会签策略
            setFormRef.value?.validateFields(['strategy']);
          },
          showSearch: true,
          allowClear: true,
          multiple: true,
          treeCheckable: true,
          // treeCheckStrictly: true,
          treeNodeFilterProp: 'name',
          maxTagCount: 'responsive',
        },
        component: 'TreeSelect',
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          // 如果选择了人员，就不需要选择角色
          return [
            {
              type: 'array',
              message: t('请选择分配审核角色'),
              validator: async (rule: any, value: any) => {
                if (!value?.length && !formModel.reviewPerson?.length) {
                  return Promise.reject(t('请选择分配审核角色'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'Divider',
        component: 'Divider',
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
        },
      },
      {
        field: 'buttons',
        component: 'CheckboxGroup',
        required: true,
        label: t('分配节点功能'),
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
        },
        defaultValue: ['pass', 'reject'],
        componentProps: {
          options: [
            {
              label: t('审核通过'),
              value: 'pass',
              disabled: true,
            },
            {
              label: t('审核不通过'),
              value: 'reject',
              disabled: true,
            },
            // {
            //   label: t('转交'),
            //   value: 'deliverTo',
            // },
            {
              label: t('回退'),
              value: 'returnTo',
            },
            // {
            //   label: t('抄送'),
            //   value: 'copyTo',
            // },
          ],
          onChange: (value: any) => {
            // 如果没有抄送人员，清空抄送人员
            // if (!value.includes('copyTo')) {
            //   setFormRef.value?.setFormModel('makePerson', []);
            // }
          },
        },
      },
      // {
      //   field: 'makePerson',
      //   label: t('配置抄送人员'),
      //   required: true,
      //   vShow: () => {
      //     return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
      //   },
      //   vIf: ({ formModel }: RenderCallbackParams) => {
      //     return formModel.buttons?.includes('copyTo');
      //   },
      //   dynamicRules: ({ formModel }: RenderCallbackParams) => {
      //     // 如果选择了角色，就不需要选择人员
      //     return [
      //       {
      //         type: 'array',
      //         required: true,
      //         message: t('请选择配置抄送人员'),
      //       },
      //     ];
      //   },
      //   component: ({ formModel }: RenderCallbackParams) => {
      //     return (
      //       <InputGroup compact>
      //         <Select
      //           style='width: 85%'
      //           v-model:value={formModel['makePerson']}
      //           fieldNames={{
      //             label: 'name',
      //             value: 'id',
      //           }}
      //           open={false}
      //           placeholder={t('请选择配置抄送人员')}
      //           maxTagCount='responsive'
      //           mode='multiple'
      //           labelInValue={true}
      //           options={makePersonOptions.value}></Select>

      //         <div
      //           style='width: 15%'
      //           class='right-add-icon-btn'
      //           onClick={() =>
      //             openSelectMakePeopleModel(formModel['makePerson'])
      //           }>
      //           <PlusCircleOutlined />
      //         </div>
      //       </InputGroup>
      //     );
      //   },
      // },
      {
        field: 'completeType',
        component: 'RadioGroup',
        label: t('审核规则'),
        required: true,
        defaultValue: 'or_vise',
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [
              {
                label: t('会签'),
                value: 'countersign',
              },
              {
                label: t('或签'),
                value: 'or_vise',
              },
            ],
            onChange: (e: any) => {
              // 如果选择了会签， 会签策略默认选择所有待审人
              if (formModel.completeType === 'countersign' && !formModel.strategy?.length) {
                setFormRef.value?.setFormModel('strategy', ['all_user']);
              }
            },
          };
        },
      },
      {
        field: 'strategy',
        component: 'CheckboxGroup',
        label: t('会签策略'),
        required: true,
        defaultValue: ['all_user'],
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
        },
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.completeType === 'countersign';
        },
        componentProps: {
          options: [
            {
              label: t('所有待审人'),
              value: 'all_user',
            },
            {
              label: t('所有待审角色'),
              value: 'all_role',
            },
          ],
        },
        dynamicRules: ({ formModel }: RenderCallbackParams) => {
          // 仅配置“审核人员”时，会签策略选择“所有待审角色”，提示“未配置审核角色，不可配置角色会签策略
          return [
            {
              type: 'array',
              required: true,
              message: t('请选择会签策略'),
            },
            {
              validator: async (rule: any, value: any) => {
                if (value.includes('all_role') && !formModel.reviewRole?.length) {
                  return Promise.reject(t('未配置审核角色，不可配置角色会签策略'));
                }
                if (value.includes('all_user') && !formModel.reviewPerson?.length) {
                  return Promise.reject(t('未配置审核人员，不可配置人员会签策略'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'needCommit',
        component: 'RadioGroup',
        label: t('审核意见'),
        defaultValue: false,
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
        },
        componentProps: {
          options: [
            {
              label: t('必须'),
              value: true,
            },
            {
              label: t('非必须'),
              value: false,
            },
          ],
        },
      },
      {
        field: 'needRemark',
        component: 'RadioGroup',
        label: t('备注'),
        defaultValue: false,
        vShow: () => {
          return tabActiveKey.value === TaskDrawerTabKeys.AuditConfig;
        },
        componentProps: {
          options: [
            {
              label: t('必须'),
              value: true,
            },
            {
              label: t('非必须'),
              value: false,
            },
          ],
        },
      },
      // {
      //   field: 'auditMegDTOList',
      //   label: t('节点完成消息通知'),
      //   vShow: () => {
      //     return tabActiveKey.value === TaskDrawerTabKeys.NoticeConfig;
      //   },
      //   dynamicRules: ({ formModel }: RenderCallbackParams) => {
      //     // 如果选择了角色，就不需要选择人员
      //     return [
      //       {
      //         type: 'array',
      //         required: false,
      //         message: t('请选择配置抄送人员'),
      //       },
      //     ];
      //   },
      //   component: ({ formModel }: RenderCallbackParams) => {
      //     return (
      //       <InputGroup compact>
      //         <Select
      //           style='width: 85%'
      //           v-model:value={formModel['auditMegDTOList']}
      //           fieldNames={{
      //             label: 'name',
      //             value: 'id',
      //           }}
      //           open={false}
      //           placeholder={t('请选择节点完成消息通知')}
      //           maxTagCount='responsive'
      //           mode='multiple'
      //           labelInValue={true}
      //           options={auditMegDTOListOptions.value}></Select>
      //         <div
      //           style='width: 15%'
      //           class='right-add-icon-btn'
      //           onClick={() => openSelectAuditMegDTOListModel(formModel['auditMegDTOList'])}>
      //           <PlusCircleOutlined />
      //         </div>
      //       </InputGroup>
      //     );
      //   },
      // },
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
    } catch (error) {}
  };

  return {
    setFormRef,
    setFormProps,
    setNodeFormData,

    openSelectReviewPeople,
    reviewPersonOptions,
    updateReviewPeople,

    openSelectMakePeople,
    makePersonOptions,
    updateMakePeople,

    openSelectAuditMegDTOList,
    auditMegDTOListOptions,
    updateAuditMegDTOList,
  };
};
