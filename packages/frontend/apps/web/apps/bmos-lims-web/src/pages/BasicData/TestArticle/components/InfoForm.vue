<template>
  <div class="edit">
    <Row class="edit-header">
      <Col :span="8">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('基础数据') }}</breadcrumb-item>
          <breadcrumb-item v-if="!props.disabled">{{ t('编辑检品') }}</breadcrumb-item>
          <breadcrumb-item v-else>{{ t('查看检品') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <Button @click="back">{{ t('返回') }}</Button>
          <Button v-if="!props.disabled" type="primary" @click="save">
            {{ t('保存') }}
          </Button>
        </Space>
      </Col>
    </Row>
    <div class="form">
      <BMTableTitle style="margin-bottom: 6px" :title="t('基本信息')"></BMTableTitle>
      <BMForm
        ref="setFormRef"
        style="width: 100%"
        v-bind="setFormProps"
        :disabled="props.disabled"></BMForm>
    </div>
    <div class="table">
      <div class="table-title">
        <BMTableTitle :title="t('实验包')"></BMTableTitle>
        <Button type="primary" v-if="!props.disabled" @click="openAddModal">{{ t('新增') }}</Button>
      </div>
      <BMTable
        ref="tableInstance"
        :data-request="loadData"
        :columns="columns"
        row-key="id"
        :showRefresh="false"
        :search="false"
        :showIndex="true"
        :pagination="false"
        :scroll="{ y: 500 }"
        :expandedRowKeys="expandedRowKeys"
        @expanded-rows-change="expandedRowsChange"
        :formProps="formProps">
        <!-- <template #toolbar>
          <Button type="primary" v-if="!props.disabled" @click="openAddModal">{{ t('新增') }}</Button>
        </template> -->
        <template #expandColumnTitle>
          {{  }}
        </template>
        <template #expandedRowRender="{ record }">
          <!-- <p>{{record.name}}</p> -->
          <div style="width: 100%; padding-left: 92px;">
            <Table
              :columns="innerColumns" :data-source="innerData" :pagination="false"
            >
            </Table>
          </div>
        </template>
      </BMTable>
    </div>
  </div>
  <AddModal 
    ref="addModalRef"
    @submit-success="handleAddSubmitSuccess"
  />
</template>

<script setup lang="tsx">
import { t } from '@bmos/i18n';
import { computed, nextTick, onMounted, reactive, Ref, ref, watch } from 'vue';
import {
  BMForm,
  BMTable,
  BMTableTitle,
  TableInstance
} from '@bmos/components';
import { useRoute } from 'vue-router';
import { useTable, useForm } from './hooks';
import AddModal from './AddModal/index.vue'
import { Rule } from 'ant-design-vue/es/form';
import { Input, message, Table } from 'ant-design-vue';
import { Key } from 'ant-design-vue/es/_util/type';
import {
  getCategoryInfo,
  getInspectionItemByPackageId,
  updateTestArticle
} from '@/services/index';

const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
  disabled: {
    type: Boolean,
    default: 'add',
  },
});

const watchStatus = computed(()=>{
  return props.disabled
})

const tableInstance = ref<TableInstance>();

const emit = defineEmits(['back']);

const loadData = async (params) => {
  return new Promise(resolve => {
    resolve({
      data: [...tableData.value],
    });
  });
};

// 展开行相关

const innerColumns = [
  { title: t('检验项目编码'), dataIndex: 'code' },
  { title: t('检验项目名称'), dataIndex: 'name' },
];

const innerData = ref([]);

const expandedRowKeys = ref<Key[]>([]);

const expandedRowsChange = async (expandedKeys: Key[]) => {
  expandedRowKeys.value = [expandedKeys[expandedKeys.length - 1]];
  const res = await getInspectionItemByPackageId(expandedRowKeys.value[0])
  innerData.value = res.data;
};

// 打开新增弹窗
const addModalRef = ref<InstanceType<typeof AddModal>>();
// const tableData = ref<any>([]);
const openAddModal = () => {
  addModalRef.value?.openModal(tableData.value?.map((item: any) => item.id));
};

const handleAddSubmitSuccess = async (list: any) => {
  tableData.value = [...tableData.value, ...list.map((item: any) => {
    return {
      ...item,
      report: false,
    }
  })];
  await tableInstance.value.fetchData()

};

const back = () => {
  emit('back');
};

const request = async (formModal: any) => {
  const params = {
    ...formModal,
    productsPackageVOList: tableInstance.value.tableData?.map((item: any) => {
      return {
        packageId: item.id,
        code: item.code,
        name: item.name,
      }
    }),
  }
  return await updateTestArticle(params)
};

const save = () => {
  setFormRef.value?.validate().then(async () => {
    try {
      await request(setFormRef.value?.formModel);
      message.success(t('编辑成功'));
      back();
    } catch (error) {
      message.error(error?.message);
    }
  })
};


const { setFormProps, setFormRef, setNodeFormData } = useForm({ 
  watchStatus: watchStatus.value, 
});
const {columns, formProps, viewReportModalOpen, tableData} = useTable({props: {watchStatus}});

onMounted(()=>{
  nextTick(async ()=>{
    setNodeFormData(props.data)
    tableData.value = props.data.productsPackageVOList.map((item: any) => {
      return {
        ...item,
        id: item.packageId,
      }
    })

    await tableInstance.value?.fetchData()
  })
})
</script>

<style lang="less" scoped>
@form-height: 170px; // 基础表单高度

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

:deep .lims-form .bmos-form-divider-container .bmos-form-divider {
  margin: 0;
}
:deep .lims-form-item {
  margin-bottom: 8px;
}
</style>