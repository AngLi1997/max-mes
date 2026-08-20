<template>
  <div class="edit">
    <Row class="edit-header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('基础数据') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.type=='add'">{{ t('新增检验项目') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.type=='edit'">{{ t('编辑检验项目') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.type=='view'">{{ t('查看检验项目') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <Button @click="back">{{ t('返回') }}</Button>
          <Button v-if="props.type!=='view'" type="primary" @click="save">
            {{ t('保存') }}
          </Button>
        </Space>
      </Col>
    </Row>
    <div class="form">
      <BMTableTitle style="margin-bottom: 16px" :title="t('基本信息')"></BMTableTitle>
      <BMForm
        ref="setFormRef"
        style="width: 100%"
        v-bind="setFormProps"
        :disabled="isView"></BMForm>
    </div>
    <div class="table">
      <div class="table-title">
        <BMTableTitle :title="t('分析项')"></BMTableTitle>
        <Button type="primary" v-if="!isView" @click="add">{{ t('新增') }}</Button>
      </div>
      
      <BMTable
        ref="tableInstance"
        :data-request="loadData"
        :columns="columns"
        row-key="id"
        :showRefresh="false"
        :search="false"
        :scroll="{ y: 500 }"
        :showIndex="true"
        :pagination="false">
        <!-- <template #toolbar>
          <Button type="primary" v-if="!isView" @click="add">{{ t('新增') }}</Button>
        </template> -->
      </BMTable>
    </div>
  </div>
  <AddModal
    ref="addModalref"
    @submitSuccess="fetchTableData"
  ></AddModal>
</template>

<script setup lang="tsx">
import { t } from '@bmos/i18n';
import { computed, createVNode, nextTick, onMounted, reactive, Ref, ref, watch } from 'vue';
import {
  BMForm,
  BMTable,
  BMTableTitle,
  TableInstance
} from '@bmos/components';
import { useRoute } from 'vue-router';
import { useTable, useForm } from './hooks';
import { Rule } from 'ant-design-vue/es/form';
import { 
  Input, 
  message, 
  Modal, 
  Select, 
  RadioGroup,
  Radio,
  Table,
  Textarea
 } from 'ant-design-vue';
import { MODAL_STATUS } from '../types/enum';
import AddModal from './AddModal/index.vue';
import {
  getAnalyzePage,
  saveInspectionItem,
  updateInspectionItem
} from '@/services/index';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';


const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
  type: {
    // 类型为 add/edit/view
    type: String,
    default: 'add',
  },
});

const tableInstance = ref<TableInstance>();

const watchStatus = computed<MODAL_STATUS>(()=>{
  return props.type as MODAL_STATUS
})

const emit = defineEmits(['back']);

const isView = computed(() => props.type === 'view');

const loadData = async (params: any) => {
  return new Promise(resolve => {
    resolve({
      data: tableData.value.map((item: any) => {
        return {
          ...item,
        }
      }),
    });
  });
};

const tableData = ref<any>([]);

const fetchTableData = async (list: any = []) => {
  tableData.value = [...tableInstance.value.tableData, ...list?.map((item: any) => {
    return {
      ...item,
      report: false,
    }
  })];
  await tableInstance.value.fetchData()
}


const addModalref = ref<InstanceType<typeof AddModal>>();
const add = () => {
  addModalref.value?.openModal(tableData.value?.map((item: any) => item.id));
};

const back = () => {
  emit('back');
};

const request = async (formModal: any) => {
  const params = {
    ...formModal,
    inspectAnalyzeVOList: tableInstance.value.tableData?.map((item: any) => {
      return {
        analyzeId: item.id,
        code: item.code,
        name: item.name,
        report: item.report,
        standard: item.standard,
      }
    }),
  }
  if (props.type === MODAL_STATUS.EDIT) {
    return await updateInspectionItem(params);
  } else {
    return await saveInspectionItem(params);
  }
}

const save = async () => {
  setFormRef.value?.validate().then(async () => {
    try {
      await request(setFormRef.value?.formModel);
      message.success(props.type === MODAL_STATUS.EDIT ? t('编辑成功') : t('新增成功'));
      back();
    } catch (error) {
      message.error(error?.message);
    }
  })
};

const { setFormProps, setFormRef, setNodeFormData } = useForm({ watchStatus });
// const {columns, formProps, viewReportModalOpen, rowData} = useTable({props: {watchStatus}});

const columns = [
  {
    title: t('分析项名称'),
    dataIndex: 'name',
    // resizable: true,
    // width: 190,
    formItemProps: {
      defaultValue: '',
    },
  },
  {
    title: t('分析项编码'),
    dataIndex: 'code',
    // resizable: true,
    // width: 190,
    formItemProps: {
      defaultValue: '',
    },
  },
  {
    title: t('报告项'),
    dataIndex: 'report',
    resizable: true,
    width: 350,
    customRender: ({ record }) => {
      return (
        props.type === MODAL_STATUS.VIEW ? 
        <span>{record.report ? t('是') : t('否')}</span> :
        <RadioGroup 
          style={{ width: '100px' }} 
          value={record.report}
          onChange={(value) => {
            record.report = value.target.value
          }}
        >
          <Radio value={true}>{t('是')}</Radio>
          <Radio value={false}>{t('否')}</Radio>
        </RadioGroup>
      )
    },
  },
  {
    title: t('标准规定'),
    dataIndex: 'standard',
    resizable: true,
    width: 330,
    customRender: ({ record }) => {
      return (
        props.type === MODAL_STATUS.VIEW ? 
        <span>{record.standard}</span> :
        <Input 
          v-model:value={record.standard}
          placeholder={t('请输入')}
        />
      )
    }
  },
  {
    title: t('操作'),
    fixed: 'right',
    key: 'ACTION',
    width: 100,
    hideInTable: props.type === MODAL_STATUS.VIEW,
    actions: (params, action) => [
      {
        label: t('删除'),
        ifShow: !params.record.status,
        danger: true,
        onClick: (e: any) => {
          Modal.confirm({
            title: t('操作将解绑分析项，是否继续？'),
            icon: createVNode(ExclamationCircleOutlined),
            closable: true,
            content: '',
            okText: t('确定'),
            cancelText: t('取消'),
            onOk: async () => {
              try {
                // await deleteMaterialApi(params.record.id);
                tableData.value = tableData.value.filter((item: any) => item.id !== params.record.id);
                message.success(t('解绑成功'));
                action.fetchData();
              } catch (error: any) {
                message.error(error.message);
              }
            },
          });
        },
      },
    ],
}]


onMounted(()=>{
  nextTick(async ()=>{
    setNodeFormData(props.data)
    tableData.value = props.data.inspectAnalyzeVOList?.map((item: any) => {
      return {
        ...item,
        id: item.analyzeId
      }
    }) ?? []
    await tableInstance.value.fetchData()
  })
})
</script>

<style lang="less" scoped>
.edit {
  width: 100%;
  height: 100%;
  &-header {
    padding: 4px 0 var(--bmos-padding-small) 0;
    // height: 6%;
    .crumb {
      line-height: 36px;
    }
    .action {
      text-align: right;
    }
  }
  .form {
    background-color: #fff;
    width: 100% !important;
    height: 182px;
    // display: flex;
    padding: 16px;
    margin-bottom: var(--bmos-margin-small);
    // overflow: auto;
  }
  .table {
    background-color: #fff;
    height: calc(100% - 56px - 182px - 16px);
    padding: var(--bmos-padding-small);
    .bmos-table {
      height: calc(100% - 28px);
      overflow: auto;
    }
    &-title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      // margin-bottom: 16px;
    }
  }
}
:deep .bmos-table .bmos-tool-bar-title {
  font-weight: 700;
  color: #18191a;
}
</style>