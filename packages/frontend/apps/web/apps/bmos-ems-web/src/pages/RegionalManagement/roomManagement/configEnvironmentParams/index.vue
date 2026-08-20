<!-- 配置环境参数 -->
<template>
  <div class="config-environment-params">
    <BreadcrumbButton>
      <template #breadcrumb>
        <Breadcrumb>
          <breadcrumb-item @click="back">
            {{ t('房间管理') }}
          </breadcrumb-item>
          <breadcrumb-item>{{ t('配置环境参数') }}</breadcrumb-item>
        </Breadcrumb>
      </template>
      <template #btns>
        <Button @click="back">{{ t('返回') }}</Button>
        <Button type="primary" @click="save">{{ t('保存') }}</Button>
      </template>
      <BMTableTitle :title="t('房间信息')"></BMTableTitle>
      <BMDescriptions :list="detailList" :column="4" :showBottomBorder="true"></BMDescriptions>
      <div class="flex-between mtb-16">
        <BMTableTitle :title="t('参数配置')"></BMTableTitle>
        <Button @click="addEnvironmentParams">{{ t('新增环境参数') }}</Button>
      </div>
      <div class="config-environment-params">
        <div v-for="item in configEnvironmentParamsList" :key="item.itemIndex" class="config-environment-params-item">
          <BMForm :ref="el => getFormRefs(el, item)" v-bind="formProps" />
        </div>
      </div>
    </BreadcrumbButton>
  </div>
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import {
    BMTableTitle,
    BMDescriptions,
    DescriptionsItemProps,
    FormProps,
    BMForm,
    Recordable,
    RenderCallbackParams,
  } from '@bmos/components';
  import { message, Button } from 'ant-design-vue';
  import TableTitleItem from './components/TableTitleItem.vue';
  import TableData from './components/TableData.vue';
  import { reqFactoryRoomInfo, reqListDictCode, reqSaveRoomEnvProperty } from '@/services';

  const route = useRoute();
  const router = useRouter();

  const itemIndex = ref(0);

  // 房间详情
  const roomDetail = ref<Recordable>({
    name: '',
    code: '',
  });
  // 环境参数options
  const environmentParamsOptions = ref<any[]>([]);

  const detailList = computed((): DescriptionsItemProps[] => {
    return [
      {
        label: t('房间名称'),
        value: roomDetail.value.name as string,
      },
      {
        label: t('房间编号'),
        value: roomDetail.value.code as string,
      },
    ];
  });

  const configEnvironmentParamsList = ref([
    {
      envPropertyCode: '',
      itemIndex: `a${itemIndex.value}`,
      equipmentDataList: [
        {
          equipmentDataPropertyCode: '',
          equipmentId: '',
          itemIndex: `b0`,
        },
      ],
    },
  ]);
  const formRefs = ref<Recordable>({});
  const getFormRefs = (el: any, item: any) => {
    if (el) {
      formRefs.value[item.itemIndex] = el;
    }
  };
  const formProps: Ref<FormProps> = ref({
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'envPropertyCode',
        showLabel: false,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <TableTitleItem
              modelValue={formModel.envPropertyCode}
              onChange={value => {
                formModel.envPropertyCode = value;
                configEnvironmentParamsList.value.forEach(item => {
                  if (item.itemIndex === formModel.itemIndex) {
                    item.envPropertyCode = value;
                  }
                });
              }}
              itemIndex={formModel.itemIndex}
              options={environmentParamsOptions.value}
              onDeleteEnvProperty={key => {
                configEnvironmentParamsList.value = configEnvironmentParamsList.value.filter(
                  item => item.itemIndex !== key,
                );
              }}
            />
          );
        },
        dynamicRules: ({ formInstance }: RenderCallbackParams) => {
          return [
            {
              validator: async () => {
                try {
                  const formRef = formInstance?.compRefMap.get('envPropertyCode');
                  await formRef?.validateForm();
                  return Promise.resolve();
                } catch (error) {
                  return Promise.reject(error);
                }
              },
            },
          ];
        },
      },
      {
        field: 'equipmentDataList',
        showLabel: false,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <TableData
              equipmentDataList={formModel.equipmentDataList}
              onChange={value => {
                formModel.equipmentDataList = value;
                configEnvironmentParamsList.value.forEach(item => {
                  if (item.itemIndex === formModel.itemIndex) {
                    item.equipmentDataList = value;
                  }
                });
              }}
            />
          );
        },
        dynamicRules: ({ formInstance }: RenderCallbackParams) => {
          return [
            {
              validator: async () => {
                try {
                  const formRef = formInstance?.compRefMap.get('equipmentDataList');
                  await formRef?.validateForm();
                  return Promise.resolve();
                } catch (error) {
                  return Promise.reject();
                }
              },
            },
          ];
        },
      },
    ],
  });

  // 新增环境参数
  const addEnvironmentParams = () => {
    itemIndex.value++;
    configEnvironmentParamsList.value.push({
      envPropertyCode: '',
      itemIndex: `a${itemIndex.value}`,
      equipmentDataList: [
        {
          equipmentDataPropertyCode: '',
          equipmentId: '',
          itemIndex: `b0`,
        },
      ],
    });
  };
  // 返回管理页面
  const back = () => {
    router.go(-1);
  };

  // 回显环境参数
  const echoEnvironmentParams = () => {
    if (roomDetail.value.roomEnvPropertyDTOList) {
      const obj: any = {};
      roomDetail.value.roomEnvPropertyDTOList.forEach((item: any) => {
        if (obj[item.envPropertyCode]) {
          obj[item.envPropertyCode].equipmentDataList.push({
            equipmentDataPropertyCode: item.equipmentDataPropertyCode,
            equipmentId: item.equipmentId,
            itemIndex: `b${obj[item.envPropertyCode].equipmentDataList.length}`,
          });
        } else {
          obj[item.envPropertyCode] = {
            envPropertyCode: item.envPropertyCode,
            itemIndex: `a${Object.values(obj).length}`,
            equipmentDataList: [
              {
                equipmentDataPropertyCode: item.equipmentDataPropertyCode,
                equipmentId: item.equipmentId,
                itemIndex: `b0`,
              },
            ],
          };
        }
      });
      configEnvironmentParamsList.value = Object.values(obj);
    }
  };

  // 获取房间详情
  const getRoomInfo = async () => {
    try {
      const res = await reqFactoryRoomInfo(route.query.roomId as string);
      roomDetail.value = res.data || {};
      echoEnvironmentParams();
    } catch (error: any) {
      message.error(error.message as string);
    }
  };
  // 获取字典
  const getDictCode = async () => {
    try {
      const res = await reqListDictCode({
        code: 'EnvironmentalParameters',
      });
      environmentParamsOptions.value = res.data || [];
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const validateForm = async () => {
    try {
      const formRefList = Object.values(formRefs.value);
      const validateResult = await Promise.all(formRefList.map((formInstance: any) => formInstance?.validate()));
      return Promise.resolve(validateResult);
    } catch (error) {
      return Promise.reject(error);
    }
  };
  // 保存
  const save = async () => {
    try {
      const validateResult = await validateForm();
      const params: {
        envPropertyCode: string;
        equipmentDataPropertyCode: string;
        equipmentId: string;
        roomId: string;
      }[] = [];
      validateResult.forEach(item => {
        item.equipmentDataList.forEach((equipment: any) => {
          params.push({
            envPropertyCode: item.envPropertyCode,
            equipmentDataPropertyCode: equipment.equipmentDataPropertyCode,
            equipmentId: equipment.equipmentId,
            roomId: route.query.roomId as string,
          });
        });
      });
      await reqSaveRoomEnvProperty(params);
      message.success(t('保存成功'));
      router.go(-1);
    } catch (error) {}
  };
  onMounted(async () => {
    getDictCode();
    getRoomInfo();
  });

  watch(
    () => configEnvironmentParamsList.value,
    () => {
      nextTick(() => {
        configEnvironmentParamsList.value.forEach(item => {
          formRefs.value[item.itemIndex]?.setFormModels({
            ...item,
          });
        });
      });
    },
    { deep: true, immediate: true },
  );
</script>

<style lang="less" scoped>
  .mtb-16 {
    margin-top: 16px;
    margin-bottom: 16px;
  }
  .config-environment-params {
    height: 100%;
    overflow-y: auto;
    .config-environment-params-item {
      border: 1px solid #e1e3e5;
      border-radius: 4px;
    }
    .config-environment-params-item > :deep(.ems-form) > .ems-row > .ems-col {
      padding-right: 0 !important;
    }
    .config-environment-params-item
      > :deep(.ems-form)
      > .ems-row
      > .ems-col:first-child
      > .ems-form-item
      > .ems-row
      > .ems-col:first-child {
      width: 0;
    }
  }
</style>
