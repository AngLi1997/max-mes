<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('关联批次')"
    wrapClassName="modalSizeMedium"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @okModal="ok">
    <div class="tree-container">
      <Segmented v-model:value="segmentedValue" :options="data" block @change="changeTab" />
      <!-- 计划批次 -->
      <div v-show="segmentedValue === t('计划批次')">
        <InputSearch v-model:value="searchKey2" class="input" :placeholder="t('请输入')" />
        <CheckboxGroup v-model:value="checkboxValue2" class="group">
          <div
            v-for="item in list2"
            v-show="item.batchNo.indexOf(searchKey2) > -1"
            :key="item"
            style="width: 100%; margin-bottom: 10px">
            <Checkbox :value="item.id">{{ item.batchNo }}</Checkbox>
          </div>
        </CheckboxGroup>
        <Empty v-if="list2.length === 0"></Empty>
      </div>
      <!-- 关联批次 -->
      <div v-show="segmentedValue === t('关联批次')">
        <InputSearch v-model:value="searchKey0" class="input" :placeholder="t('请输入')" />
        <CheckboxGroup v-model:value="checkboxValue0" class="group">
          <div
            v-for="item in list0"
            v-show="item.batchNo.indexOf(searchKey0) > -1"
            :key="item"
            style="width: 100%; margin-bottom: 10px">
            <Checkbox :value="item.id">{{ item.batchNo }}</Checkbox>
          </div>
        </CheckboxGroup>
        <Empty v-if="list0.length === 0"></Empty>
      </div>
      <!--历史批次-->
      <div v-show="segmentedValue === t('历史批次')">
        <InputSearch v-model:value="searchKey1" class="input" :placeholder="t('请输入')" @search="onSearch" />
        <CheckboxGroup v-model:value="checkboxValue1" class="group">
          <div v-for="item in list1" :key="item" style="width: 100%; margin-bottom: 10px">
            <Checkbox :value="item.id" @change="checkboxChange(item.id, $event)">{{ item.batchNo }}</Checkbox>
          </div>
        </CheckboxGroup>
        <Empty v-if="list1.length === 0"></Empty>
      </div>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Segmented, InputSearch, CheckboxGroup, Checkbox } from 'ant-design-vue';
  import { ref, reactive, watch } from 'vue';
  import { reqPlanInfoStartPage } from '@/services';
  const emit = defineEmits(['modalOk']);
  const props = withDefaults(
    defineProps<{
      rowData?: any;
    }>(),
    {
      rowData: () => {},
    },
  );
  const open = ref<boolean>(false);
  const data = reactive<any>([t('计划批次'), t('关联批次'), t('历史批次')]); //分段选择器
  const segmentedValue = ref<any>(data[0]);
  const searchKey0 = ref<any>(''); //输入框值
  const searchKey1 = ref<any>('');
  const searchKey2 = ref<any>(''); //计划批次输入框的值
  const checkboxValue0 = ref<any>([]); //勾选的框
  const checkboxValue1 = ref<any>([]); //勾选的id集合数组
  const checkboxValue2 = ref<any>([]); //计划批次勾选的id集合数组

  const list0 = ref<any>([]); //关联批次list
  const list1 = ref<any>([]);
  const list2 = ref<any>([]); //计划批次list
  // 关联批次勾选的数组对象
  const checkedNodes0 = computed(() => {
    return list0.value.filter((item: any) => checkboxValue0.value.includes(item.id));
  });
  // 历史批次勾选的数组对象
  const checkedNodes1 = ref<any>([]);
  // 计划批次勾选的数组对象
  const checkedNodes2 = computed(() => {
    return list2.value.filter((item: any) => checkboxValue2.value.includes(item.id));
  });

  const ok = () => {
    const planIds: any = [];
    const batchNos: any = [];
    const checkedNodes = [...checkedNodes2.value, ...checkedNodes0.value, ...checkedNodes1.value];
    checkedNodes.forEach(item => {
      planIds.push(item.id);
      batchNos.push(item.batchNo);
    });
    emit('modalOk', planIds, batchNos, props.rowData?.id, checkedNodes1.value);
    open.value = false;
  };
  const changeTab = async (val: any) => {
    switch (val) {
      case t('计划批次'):
        break;
      case t('关联批次'):
        break;
      case t('历史批次'):
        break;
      default:
        break;
    }
  };
  // 获取对应的关联批次并回显
  const getList0 = async (val: any) => {
    const { data } = await reqPlanInfoStartPage({ processId: val, relation: 'FALSE' });
    list0.value = data;
    const echo0 = list0.value
      .filter((item: any) => props.rowData?.planIds?.includes(item.id))
      ?.map((item2: any) => item2.id);
    checkboxValue0.value = echo0;
    // 关联批次的id集合(需展示计划批次)
    const ids0 = list0.value.map((item: any) => item.id);
    const temp = props.rowData?.relationBatchList?.filter((item: any) => !item.related && !ids0.includes(item.planId));
    list2.value = temp.map((item: any) => {
      return {
        batchNo: item.planBatchNo,
        id: item.planId,
      };
    });
    const echo2 = list2.value
      .filter((item: any) => props.rowData?.planIds?.includes(item.id))
      ?.map((item2: any) => item2.id);
    checkboxValue2.value = echo2;
  };

  // 历史批次搜素
  const onSearch = async () => {
    checkboxValue1.value = checkedNodes1.value.map((item: any) => item.id);
    if (searchKey1.value === '') {
      list1.value = checkedNodes1.value;
      return;
    }
    const { data } = await reqPlanInfoStartPage({
      processId: props.rowData?.processId,
      relation: 'TRUE',
      batchNo: searchKey1.value,
    });
    list1.value = searchKey1.value ? data : checkedNodes1.value;
  };
  // 历史批次复选框勾选与取消
  const checkboxChange = (id: any, e: any) => {
    if (e.target.checked) {
      list1.value.forEach((item: any) => {
        if (item.id === id) {
          checkedNodes1.value.push(item);
        }
      });
    } else {
      checkedNodes1.value = checkedNodes1.value.filter((item: any) => item.id !== id);
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        segmentedValue.value = data[0];
        searchKey0.value = '';
        searchKey1.value = '';
        searchKey2.value = '';
        checkboxValue0.value = [];
        checkboxValue1.value = [];
        checkboxValue2.value = [];
        getList0(props.rowData.id); //回显计划批次及关联批次
        list1.value = props.rowData.checkedNodes1 || [];
        checkedNodes1.value = props.rowData.checkedNodes1 || [];
        checkboxValue1.value = props.rowData.planIds;
      }
    },
    {
      immediate: true,
    },
  );
  // 打开弹窗
  const openModal = () => {
    open.value = true;
  };
  defineExpose({
    openModal,
  });
</script>

<style scoped lang="less">
  .tree-container {
    height: 400px;
    overflow-y: auto;
    .input {
      margin: 16px 0px;
    }
    .group {
      width: 100%;
      max-height: 290px;
      overflow-y: auto;
    }
  }
</style>
