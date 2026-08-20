<template>
  <div class="add-dict">
    <Row class="add-dict-header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('字典管理') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.type1 == 'add'">{{ t('新增字典') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.type1 == 'edit'">{{ t('编辑字典') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.type1 == 'look'">{{ t('查看字典') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <Button @click="back">{{ t('返回') }}</Button>
          <Button v-if="props.type1 !== 'look'" type="primary" :disabled="isView" @click="save">
            {{ t('保存') }}
          </Button>
        </Space>
      </Col>
    </Row>
    <div class="dict-form">
      <Form ref="dictFormRef" :model="dictForm" :label-width="100" :rules="dictFormRules" layout="inline">
        <FormItem :label="t('字典名称')" name="dictName">
          <Input v-model:value="dictForm.dictName" maxlength="100" :disabled="isView || props.type1 == 'look'" />
        </FormItem>
        <FormItem :label="t('字典编码')" name="dictCode">
          <Input v-model:value="dictForm.dictCode" maxlength="100" :disabled="isView || props.type1 == 'look'" />
        </FormItem>
      </Form>
    </div>
    <div class="dict-data-table">
      <DataTable ref="dataTableRef" :isView="isView" :type1="type1" :dictRowData="rowData" />
    </div>
  </div>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { DictFormModel } from '../../types';
  import { Rule } from 'ant-design-vue/es/form';
  import DataTable from './components/index.vue';
  import { reqPlatformDictSavePOST, reqPlatformDictUpdatePOST } from '@/api';
  import { message } from 'ant-design-vue';
  import { UnwrapRef, ref, reactive } from 'vue';

  const emit = defineEmits<{
    (e: 'back'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      isView?: boolean;
      type1?: any;
      rowData?: any;
    }>(),
    {
      isView: false,
      type1: '',
      rowData: {},
    },
  );

  // 字典表单
  const dictForm: UnwrapRef<DictFormModel> = reactive({});

  const dictFormRules: Record<string, Rule[]> = {
    dictName: [{ required: true, message: t('请输入字典名称'), trigger: 'blur' }],
    dictCode: [{ required: true, message: t('请输入字典编码'), trigger: 'blur' }],
  };

  // 返回
  const back = () => {
    emit('back');
  };

  const dataTableRef = ref<any>();
  const dictFormRef = ref<any>();
  // 保存
  const save = () => {
    dictFormRef.value.validate().then(async () => {
      try {
        const dataSource = dataTableRef.value.getDataSource();
        const params = {
          ...dictForm,
          detailList: dataSource,
        };
        if (props.type1 !== 'edit') {
          await reqPlatformDictSavePOST(params);
        } else {
          console.log(dataTableRef.value.deleteIds, 'dataTableRef.value.deleteIds');
          await reqPlatformDictUpdatePOST({
            ...params,
            id: props.rowData.id,
            dictIdList: dataTableRef.value.deleteIds,
          });
        }
        message.success(t('保存成功'));
        back();
      } catch (error: any) {
        error.message && message.error(error.message);
      }
    });
  };
  onMounted(() => {
    nextTick(() => {
      if (props.type1 == 'look' || props.type1 == 'edit') {
        dictForm.dictName = props.rowData.dictName;
        dictForm.dictCode = props.rowData.dictCode;
      }
      // 回显表格待做..
    });
  });
</script>
<style lang="less" scoped>
  .add-dict {
    width: 100%;
    height: 100%;
    .add-dict-header {
      padding: 4px 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
      }
      .action {
        text-align: right;
      }
    }
    .dict-form {
      background-color: #fff;
      height: 68px;
      display: flex;
      padding: var(--bmos-padding-small);
      margin-bottom: var(--bmos-margin-small);
      .plat-input {
        width: 400px;
      }
    }
    .dict-data-table {
      background-color: #fff;
      height: calc(100% - 68px - 36px - 32px);
      padding: var(--bmos-padding-small);
    }
  }
</style>
