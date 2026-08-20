<!-- 新增编辑页面 -->
<template>
  <div class="edit">
    <Row class="edit-header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('基础数据') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.type=='add'">{{ t('新增实验包') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.type=='edit'">{{ t('编辑实验包') }}</breadcrumb-item>
          <breadcrumb-item v-if="props.type=='view'">{{ t('查看实验包') }}</breadcrumb-item>
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
      <!-- <div class="form-title">{{ t('基本信息') }}</div> -->
      <BMForm
        ref="setFormRef"
        style="width: 100%"
        v-bind="setFormProps"
        :disabled="isView"></BMForm>
    </div>
    <div class="table">
      <div class="table-title">
        <BMTableTitle :title="t('检验项目')"></BMTableTitle>
        <Button type="primary" v-if="!isView" @click="add">{{ t('新增') }}</Button>
      </div>
      <BMTable
        ref="tableInstance"
        :data-request="loadData"
        :columns="columns"
        row-key="id"
        :showRefresh="false"
        :scroll="{ y: 500 }"
        :search="false"
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
  TableInstance,
  BMTableTitle
} from '@bmos/components';
import AddModal from '../AddModal/index.vue';
import { useTable, useForm } from './hooks';
import { Rule } from 'ant-design-vue/es/form';
import { Input, message, Modal, Select, SelectOption, Textarea } from 'ant-design-vue';
import { MODAL_STATUS } from '../../types'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { saveExperimentalPackage, updateExperimentalPackage } from '@/services';


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

const watchStatus = computed<MODAL_STATUS>(()=>{
  return props.type as MODAL_STATUS
})

const emit = defineEmits(['back']);

const isView = computed(() => props.type === 'view');
const tableInstance = ref<TableInstance>();
const tableData = ref<any>([]);

const loadData = async (params) => {
  return new Promise(resolve => {
    resolve({
      data: tableData.value,
    });
  });
};

const columns = [
  {
    title: t('检验项目名称'),
    dataIndex: 'name',
    resizable: true,
  },
  {
    title: t('检验项目编码'),
    dataIndex: 'code',
    resizable: true,
    // componentProps: {
    //   disabled: watchStatus.value !== MODAL_STATUS.ADD,
    // },
  },
  {
    title: t('操作'),
    fixed: 'right',
    key: 'ACTION',
    hideInTable: watchStatus.value === MODAL_STATUS.VIEW,
    width: 100,
    actions: (params, action) => [
      {
        label: t('删除'),
        danger: true,
        ifShow: !params.record.status,
        onClick: (e: any) => {
          Modal.confirm({
            title: t('操作将解绑检验项目，是否继续？'),
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
    packageInspectVOList: tableData.value?.map((item: any) => {
      return {
        inspectId: item.id,
        code: item.code,
        name: item.name,
      }
    }),
  }
  if (props.type === MODAL_STATUS.EDIT) {
    return await updateExperimentalPackage(params);
  } else {
    return await saveExperimentalPackage(params);
  }
}

const fetchTableData = async (list: any) => {
  tableData.value = [...tableData.value, ...list.map((item: any) => {
    return {
      ...item,
      report: false,
    }
  })];
  await tableInstance.value.fetchData()
}

const save = async () => {
  setFormRef.value?.validate().then(async () => {
    try {
      console.log(setFormRef.value?.formModel)
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

onMounted(()=>{
  nextTick(async ()=>{
    setNodeFormData(props.data)
    tableData.value = props.data.packageInspectVOList.map((item: any) => {
      return {
        ...item,
        id: item.inspectId,
      }
    })

    await tableInstance.value.fetchData()
  })
})
</script>

<style lang="less" scoped>
@form-height: 128px;

.edit {
  width: 100%;
  height: 100%;
  &-header {
    padding: 4px 0 var(--bmos-padding-small) 0;
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
    height: @form-height;
    // display: flex;
    padding: 16px;
    margin-bottom: var(--bmos-margin-small);
    .form-title {
      width: 100%;
      color: #18191A;
      font-weight: 600;
      font-size: 16px;
      line-height: 1.5;
      margin-bottom: 16px;
    }
  }
  .table {
    background-color: #fff;
    height: calc(100% - 56px - @form-height - 16px);
    padding: var(--bmos-padding-small);
    .bmos-table {
      height: calc(100% - 28px);
      overflow: auto;
    }
    &-title {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}
:deep .bmos-table .bmos-tool-bar-title {
  font-weight: 700;
  color: #18191a;
}
</style>