<template>
  <BMModal
    v-model="showFilterModal"
    :default-padding="false"
    :title="t('筛选')"
    size="small"
    position="right"
    closable
    :cancel-text="t('重置')"
    @confirm="confirm"
    @cancel="reset"
  >
    <view class="filter_form_box">
      <BMForm
        ref="formRef"
        v-bind="formProps"
      />
    </view>
  </BMModal>
</template>
<script setup>
  import { ref, computed, reactive } from 'vue';
  import { t } from '@/utils/useBmosI18n.js';
  import { BMModal, BMForm } from '@/BMComponents';
  import { getProductTreeApi, getProcedureLineApi } from '@/api/productionApi.js';

  const props = defineProps({
    open: {
      type: Boolean,
      default: false
    }
  });
  const emit = defineEmits([
    'update:open',
    'confirm',
    'reset'
  ]);
  const showFilterModal = computed({
    get: () => props.open,
    set: (val) => {
      emit('update:open', val);
    }
  });

  const formRef = ref();

  const confirm = async() => {
    const data = await formRef.value?.validate();
    showFilterModal.value = false;
    emit('confirm', data);
  };
  const reset = () => {
    formRef.value?.resetForm();
    showFilterModal.value = false;
    emit('reset');
  };
  
  const getChildrenData = (arr) => {
    let newArr = [];
    arr.map((item) => {
      item.categoryFlag = !item.categoryFlag;
      if (item.children.length > 0) {
        item.children = getChildrenData(item.children);
      }
      newArr.push(item);
    });
    return newArr;
  };
  // 筛选表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'productId',
        component: 'BMFormSelect',
        label: t('产品名称'),
        colProps: {
          span: 24
        },
        defaultValue: [],
        componentProps: ({ formModel }) => {
          return {
            request: async() => {
              const { data } = await getProductTreeApi({ categoryType: 2 });
              return getChildrenData(data);
            },
            title: t('产品名称'),
            type: 'tree',
            mode: 'multiple',
            fieldNames: {
              name: 'showName',
              key: 'id',
              checkKey: 'categoryFlag',
              checkKeyValue: true,
              parentId: 'parentId',
              children: 'children'
            },
            treeData: []
          };
        }
      },
      {
        field: 'batchNo',
        component: 'Input',
        label: t('批号'),
        colProps: {
          span: 24
        }
      },
      {
        field: 'lineId',
        component: 'BMFormSelect',
        label: t('产线'),
        defaultValue: [],
        colProps: {
          span: 24
        },
        componentProps: ({ formModel }) => {
          return {
            request: async() => {
              const { data } = await getProcedureLineApi();
              const options = getChildrenList(data);
              return options;
            },
            title: t('产线名称'),
            type: 'tree',
            mode: 'multiple',
            fieldNames: {
              key: 'id',
              checkKey: 'categoryFlag',
              checkKeyValue: true,
              parentId: 'parentId',
              children: 'children'
            },
            treeData: []
          };
        }
      }
    ]
  });
  const getChildrenList = (list, parentId) => {
    if (!list) {
      return [];
    }
    let newChildren = [];
    list.map((item) => {
      const children = getChildrenList(item.children, item.id);
      item.name = item.code + '-' + item.name;
      item.categoryFlag = !item.parentId;
      item.parentId = item.parentId ?? parentId;
      if (item.infoList) {
        item.infoList.map((infoItem) => {
          infoItem.name = `${infoItem.code}-${infoItem.name}`;
          infoItem.categoryFlag = !infoItem.parentId;
          infoItem.parentId = infoItem.parentId ?? item.id;
        });
        item.children = [...children, ...item.infoList];
      } else {
        item.children = [...children];
      }
      newChildren.push(item);
    });
    return newChildren;
  };
  </script>
  <style scoped>
  .filter_form_box{
    margin-right: -5.72rpx;
    padding: 11.72rpx 0 0 5rpx;
    width: 260.53rpx;
  }
</style>
