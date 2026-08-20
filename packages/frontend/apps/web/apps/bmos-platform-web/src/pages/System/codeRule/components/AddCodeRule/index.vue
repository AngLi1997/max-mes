<template>
  <div class="add-code-rule">
    <Row class="add-code-rule-header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item @click="back">{{ t('编号规则') }}</breadcrumb-item>
          <breadcrumb-item>{{ t('新增编号规则') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <Button @click="back">{{ t('返回') }}</Button>
          <Button type="primary" :disabled="isView" @click="save">
            {{ t('保存') }}
          </Button>
        </Space>
      </Col>
    </Row>
    <div class="code-rule-form">
      <BMForm ref="addCodeRuleFormRef" v-bind="formProps"></BMForm>
    </div>
    <div class="code-rule-data-table">
      <DataTable ref="dataTableRef" :currentStatus="currentStatus" :selectDictId="selectDictId" :codeObj="codeObj" />
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import DataTable from './components/index.vue';
  import { message } from 'ant-design-vue';
  import { FormProps, BMForm, Recordable, formInstance } from '@bmos/components';
  import {
    reqPlatformCodeRuleDetailGET,
    reqPlatformCodeRuleSavePOST,
    reqPlatformCodeRuleUpdatePUT,
    reqPlatformCodeRuleVersionSavePOST,
    reqPlatformDictListDownGET,
  } from '@/api';
  import { AddRuleDataStatus } from '../../types';

  const emit = defineEmits<{
    (e: 'back'): void;
  }>();
  const codeObj = computed(() => {
    if (!dictIdSelect.value || !selectDictId.value) return void 0;
    return dictIdSelect.value.find((item: any) => item.id === selectDictId.value);
  });
  const props = withDefaults(
    defineProps<{
      currentStatus?: AddRuleDataStatus;
      selectCodeRuleVersion?: Recordable;
      selectCodeRule?: Recordable;
    }>(),
    {
      currentStatus: AddRuleDataStatus.ADD,
    },
  );

  const isView = computed(() => {
    return props.currentStatus === AddRuleDataStatus.VIEW;
  });
  const dictIdSelect = ref([]);
  const selectDictId = ref<string>('');
  const formProps = reactive<FormProps>({
    disabled: isView.value,
    schemas: [
      {
        field: 'name',
        label: t('规则名称'),
        component: 'Input',
        componentProps: {
          disabled:
            props.currentStatus === AddRuleDataStatus.ADD_VERSION ||
            props.currentStatus === AddRuleDataStatus.EDIT_VERSION,
        },
        required: true,
        // rules: [
        //   {
        //     min: 1,
        //     max: 100,
        //     message: t('规则名称不能超过100长度字符'),
        //     trigger: 'blur',
        //   },
        // ],
      },
      {
        field: 'code',
        label: t('规则编号'),
        component: 'Input',
        componentProps: {
          disabled:
            props.currentStatus === AddRuleDataStatus.ADD_VERSION ||
            props.currentStatus === AddRuleDataStatus.EDIT_VERSION,
        },
        required: true,
      },
      {
        field: 'version',
        label: t('版本号'),
        component: 'Input',
        required: true,
      },
      {
        field: 'dictId',
        label: t('数据字典'),
        component: 'Select',
        componentProps: {
          showSearch: true,
          filterOption: (input: string, option: any) => {
            return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          onChange: (value: string) => {
            selectDictId.value = value;
            dataTableRef.value.setDataSource([]);
          },
          request: async () => {
            try {
              const { data }: any = await reqPlatformDictListDownGET('1730513339114741760');
              return data.map((item: any) => ({
                label: `${item.label}-${item.value}`,
                value: item.id,
              }));
            } catch (error: any) {
              return [];
            }
          },
        },
      },
      {
        field: 'description',
        label: t('版本描述'),
        component: 'Input',
      },
    ],
    labelWidth: 120,
    baseColProps: {
      span: 8,
    },
    autoAdvancedLine: 3,
    alwaysShowLines: 3,
    showActionButtonGroup: false,
  });

  // 返回
  const back = () => {
    emit('back');
  };

  const getDictIds = async () => {
    try {
      const { data }: any = await reqPlatformDictListDownGET('1730513339114741760');
      dictIdSelect.value = data;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const dataTableRef = ref<any>();
  const addCodeRuleFormRef = ref<formInstance>();
  // 保存
  const save = async () => {
    try {
      const values = await addCodeRuleFormRef.value?.validate();
      const dataSource = await dataTableRef.value.getDataSource();
      if (!dataSource?.length) {
        message.error(t('请配置编号规则属性'));
        return;
      }
      const resetRule = dataTableRef.value.getResetRule();
      const params = {
        name: values?.name || '',
        code: values?.code || '',
        version: values?.version || '',
        dictId: values?.dictId || '',
        description: values?.description || '',
        ...(resetRule?.length ? { resetRule } : { resetRule: [] }),
        codeRuleVersionDetails: dataSource,
      };
      if (props.currentStatus === AddRuleDataStatus.EDIT_VERSION) {
        const { name, code, ...rest } = params;
        await reqPlatformCodeRuleUpdatePUT({
          id: props.selectCodeRule?.id,
          versionId: props.selectCodeRuleVersion?.id,
          ...rest,
        });
        message.success(t('编辑成功'));
      } else if (props.currentStatus === AddRuleDataStatus.ADD_VERSION) {
        const { name, ...rest } = params;
        await reqPlatformCodeRuleVersionSavePOST({
          ...rest,
        });
        message.success(t('新增版本成功'));
      } else {
        await reqPlatformCodeRuleSavePOST({
          ...params,
        });
        message.success(t('保存成功'));
      }
      back();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  onMounted(async () => {
    try {
      await getDictIds();
      if (
        (props.currentStatus === AddRuleDataStatus.VIEW ||
          props.currentStatus === AddRuleDataStatus.EDIT_VERSION ||
          props.currentStatus === AddRuleDataStatus.ADD_VERSION) &&
        props.selectCodeRuleVersion?.id
      ) {
        const { data }: any = await reqPlatformCodeRuleDetailGET(props.selectCodeRuleVersion?.id);
        addCodeRuleFormRef.value?.setFieldsValue({
          name: data.name,
          code: data.code,
          version: data.version,
          dictId: data.dictId,
          description: data.description,
        });
        if (data.dictId) {
          selectDictId.value = data?.dictId;
        }
        dataTableRef.value.setResetRule(data.resetRules);
        data.details.forEach((item: any) => (item.sort = Number(item.sort)));
        dataTableRef.value.setDataSource(
          data.details.map((item: any) => {
            return {
              ...item,
              type: item.type.value,
              id: item.type.value + new Date().getTime() + item.sort,
            };
          }),
        );
      }
      if (props.currentStatus === AddRuleDataStatus.EDIT_VERSION) {
        addCodeRuleFormRef.value?.updateSchema([
          {
            field: 'name',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'code',
            componentProps: {
              disabled: true,
            },
          },
        ]);
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  });
</script>
<style lang="less" scoped>
  .add-code-rule {
    width: 100%;
    height: 100%;
    .add-code-rule-header {
      padding: 4px 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
        li {
          cursor: pointer;
        }
      }
      .action {
        text-align: right;
      }
    }
    .code-rule-form {
      background-color: #fff;
      padding: var(--bmos-padding-small) var(--bmos-padding-small) 0 var(--bmos-padding-small);
      margin-bottom: var(--bmos-margin-small);
    }
    .code-rule-data-table {
      background-color: #fff;
      height: calc(100% - 68px - 36px - 76px);
      padding: var(--bmos-padding-small);
    }
  }
</style>
