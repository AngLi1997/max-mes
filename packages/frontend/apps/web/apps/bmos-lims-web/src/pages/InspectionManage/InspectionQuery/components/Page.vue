<template>
  <BMPageComponent
    ref="pageRef"
    :showAllAddIcon="false"
    :showAction="false"
    :showAddChildren="false"
    :rowKeys="['id']"
    :treeData="treeData"
    :autoExpandParent="true"
    :defaultExpandParent="true"
    :search="[true]"
    :formProps="[formFirstProps]"
    :fieldNames="{
      title: 'showName',
      key: 'id',
    }"
    :treeField="{
      field: {
        categoryId: 'id',
        categoryFlag: 'categoryFlag',
      },
    }"
    :showHeader="[false]"
    :show-tool-bars="[true]"
    :requests="[getPage]"
    :columns="[columnsFirst]"
  >
    <template #tableHeaderTitle0="{ instance }">
      <div style="margin: 0; padding: 0;" class="header-flex">
        <!-- <BMTableTitle :title="t('检验查询')"></BMTableTitle> -->
        <!-- <Tabs
          v-model:activeKey="activeKey"
          @change="(key) => {tabChange(key, instance)}"
        >
          <TabPane key="1" :tab="t('进行中')"></TabPane>
          <TabPane key="2" :tab="t('已完成')"></TabPane>
        </Tabs> -->
        <RadioGroup
          v-model:value="activeKey"
          size="small"
          @change="(_e) => {tabChange(instance)}"
        >
          <RadioButton
            value="1"
          >{{ t('进行中') }}</RadioButton>
          <RadioButton
            value="2"
          >{{ t('已完成') }}</RadioButton>
        </RadioGroup>
      </div>
    </template>
  </BMPageComponent>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, nextTick } from 'vue';
import { t } from '@bmos/i18n';
import { useTable } from '../hooks';
import { DataNode } from 'ant-design-vue/es/tree';
import { BMPageComponent, BMTableTitle } from '@bmos/components';
import SyncForm from './SyncForm.vue';
import {
  Tabs,
  TabPane,
  RadioGroup,
  RadioButton,
  message,
} from 'ant-design-vue';
import { 
  getLimsTree,
  getCheckOrderFinishPage,
  getCheckOrderPage
} from '@/services/index';

const treeData = ref<DataNode[]>([]);

const emit = defineEmits(['open']);

const tableApi = computed(() => {
  if (activeKey.value === '1') {
    return getCheckOrderPage;
  }
  return getCheckOrderFinishPage;
})

// 获取当前日期和30天之前的日期
const getNowDate = () => {
  // 获取当前日期
  const today = new Date();
  // 获取30天前的日期
  const thirtyDaysAgo = new Date(today);
  thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);

  return {
    verifyBeginTime: thirtyDaysAgo.toISOString().split('T')[0],
    verifyEndTime: today.toISOString().split('T')[0],
  }
}

// 请求数据
const getPage = async (params: any) => {
  try {
    const data = {
      // ...getNowDate(),
      ...params
    }
    if (!params.categoryId || params.categoryId === 'all') {
      return await tableApi.value({
        ...data,
        categoryId: 0,
        categoryFlag: true,
      });
    }

    return await tableApi.value(data);
  } catch(error: any) {
    message.error(error?.message);
  }
};

// 获取树
const getTreeData = async () => {
  try {
    const { data } = await getLimsTree({});
    treeData.value = [
      {
        id: 'all',
        name: t('全部'),
        showName: t('全部'),
        key: 'all',
        categoryFlag: true,
        children: data,
      },
    ];
  } catch (error) {
    message.error(error.message);
  }
};
const watchEditInfo = (row: any, disabled: boolean) => {
  emit('open', row, disabled);
}

const activeKey = ref('1');
const tabChange = (instance: any) => {
  // activeKey.value = key;
  chnageFileter(activeKey.value);
  instance.updateHeaderSearchData({processCode: undefined});
  pageRef.value.fetchData();
}


const { pageRef, columnsFirst, formFirstProps, rowData, chnageFileter } =useTable({
  watchEditInfo,
});


onMounted(() => {
  getTreeData();
  // dayjs 获取当前日期和30天之前
  // const data = getNowDate()

  // pageRef.value.getQueryFormRef(0).setFieldsValue({verifyTime: [data.verifyBeginTime, data.verifyEndTime]});
});

</script>

<style lang="less" scoped>
@bg-color: #F5F6F7;

:deep .bmos-tool-bar {
  justify-content: flex-start;
}

.header-flex {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-direction: column;
  margin-bottom: 16px;
}

:deep .lims-radio-group {
  background-color: @bg-color;
  padding: 4px;
  border-radius: 3px;
}
:deep .lims-radio-button-wrapper {
  border: 0;
  background-color: @bg-color;
  border-radius: 3px;
  font-family: Source Han Sans CN;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0em;
  text-align: center;
  color: #606266;
  width: 124px;
  height: 28px;
  line-height: 28px;
  &::before {
    background-color: @bg-color;
  }
  &-checked {
    background-color: #fff;
    box-shadow: 0px 0px 5px 0px #0000001A;
    color: #2871FF;
    &::before {
      background-color: @bg-color;
    }
    &:hover::before {
      background-color: @bg-color;
    }
    &:active {
      background-color: @bg-color;
    }
  }
}
</style>