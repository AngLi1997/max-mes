<template>
  <NormalModalForm
    v-model:open="open"
    :title="t('绑定物料')"
    destroyOnClose
    wrap-class-name="modalSizeMedium inspection-bind-material-modal"
    @okModal="okModal">
    <Tabs v-model:activeKey="activeKey" @change="handleTabChange">
      <TabPane :key="MaterialTypeMap.RawMaterial" :tab="t('绑定原辅包')">
        <div class="tree-content">
          <div class="title">{{ t('物料列表') }}</div>
          <BMSearchTree
            v-model:expandedKeys="rawMaterialExpandedKeys"
            v-model:checkedKeys="rawMaterialCheckedKeys"
            :showAllAddIcon="false"
            :showAction="false"
            checkable
            :field-names="{
              title: 'showName',
              key: 'id',
            }"
            :tree-data="rawMaterialTreeData"
            @check="checkHandle" />
        </div>
      </TabPane>
      <TabPane :key="MaterialTypeMap.MiddleProduct" :tab="t('绑定中间品')">
        <div class="tree-content">
          <div class="title">{{ t('物料列表') }}</div>
          <BMSearchTree
            v-if="activeKey === MaterialTypeMap.MiddleProduct"
            v-model:expandedKeys="middleProductExpandedKeys"
            v-model:checkedKeys="middleProductCheckedKeys"
            :showAllAddIcon="false"
            :showAction="false"
            checkable
            :field-names="{
              title: 'showName',
              key: 'id',
            }"
            :tree-data="middleProductTreeData"
            @check="checkHandle" />
        </div>
      </TabPane>
      <TabPane :key="MaterialTypeMap.Product" :tab="t('绑定成品')">
        <div class="tree-content">
          <div class="title">{{ t('物料列表') }}</div>
          <BMSearchTree
            v-if="activeKey === MaterialTypeMap.Product"
            v-model:expandedKeys="productExpandedKeys"
            v-model:checkedKeys="productCheckedKeys"
            :showAllAddIcon="false"
            :showAction="false"
            checkable
            :field-names="{
              title: 'showName',
              key: 'id',
            }"
            :tree-data="productTreeData"
            @check="checkHandle" />
        </div>
      </TabPane>
    </Tabs>
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { NormalModalForm, BMSearchTree } from '@bmos/components';
  import { MaterialTypeMap, MaterialTypeValue } from '@/pages/ProductionMaterials/PageComponentNew/const';
  import { Key } from 'ant-design-vue/es/_util/type';
  import {
    reqInspectConfigBindMaterial,
    reqProductMaterialProductTreeReq,
    reqInspectConfigQueryMaterial,
    getProductMaterialFinishProductTree,
  } from '@/services';
  import { Tabs, TabPane, message } from 'ant-design-vue';
  import { DataNode } from 'ant-design-vue/es/tree';
  import { findItemByAttr } from '@bmos/utils';

  const open = defineModel<boolean>('open', {
    default: false,
  });
  const props = defineProps({
    rowData: {
      type: Object as PropType<any>,
      default: () => ({}),
    },
  });

  const activeKey = ref<MaterialTypeValue>(MaterialTypeMap.RawMaterial);
  const tabKeyArr = [
    {
      key: MaterialTypeMap.RawMaterial,
      title: t('绑定原辅包'),
    },
    {
      key: MaterialTypeMap.MiddleProduct,
      title: t('绑定中间品'),
    },
    {
      key: MaterialTypeMap.Product,
      title: t('绑定成品'),
    },
  ];

  const handleTabChange = (key: Key) => {
    activeKey.value = key as MaterialTypeValue;
  };

  const rawMaterialExpandedKeys = ref<string[]>([]);
  const rawMaterialCheckedKeys = ref<string[]>([]);
  const rawMaterialCheckedNodes = ref<DataNode[]>([]);
  const rawMaterialTreeData = ref<any[]>([]);

  const middleProductExpandedKeys = ref<string[]>([]);
  const middleProductCheckedKeys = ref<string[]>([]);
  const middleProductCheckedNodes = ref<DataNode[]>([]);
  const middleProductTreeData = ref<any[]>([]);

  const productExpandedKeys = ref<string[]>([]);
  const productCheckedKeys = ref<string[]>([]);
  const productCheckedNodes = ref<DataNode[]>([]);
  const productTreeData = ref<any[]>([]);

  const fetchRawMaterialTreeData = async (type: MaterialTypeValue) => {
    try {
      let data: any[] = [];
      if (type === MaterialTypeMap.Product) {
        const { data: productData } = await getProductMaterialFinishProductTree();
        data = productData;
      } else {
        const { data: materialData } = await reqProductMaterialProductTreeReq(type);
        data = materialData;
      }

      switch (type) {
        case MaterialTypeMap.RawMaterial:
          rawMaterialTreeData.value = [
            {
              id: 'all',
              showName: t('全部'),
              categoryFlag: true,
              children: data,
            },
          ];
          rawMaterialExpandedKeys.value = ['all'];
          rawMaterialCheckedKeys.value = rawMaterialCheckedKeys.value.filter((key: string) => {
            return findItemByAttr(data, 'id', key);
          });
          break;
        case MaterialTypeMap.MiddleProduct:
          middleProductTreeData.value = [
            {
              id: 'all',
              showName: t('全部'),
              categoryFlag: true,
              children: data,
            },
          ];
          middleProductExpandedKeys.value = ['all'];

          middleProductCheckedKeys.value = middleProductCheckedKeys.value.filter((key: string) => {
            return findItemByAttr(data, 'id', key);
          });
          break;
        case MaterialTypeMap.Product:
          productTreeData.value = [
            {
              id: 'all',
              showName: t('全部'),
              categoryFlag: true,
              children: data,
            },
          ];
          productExpandedKeys.value = ['all'];
          productCheckedKeys.value = productCheckedKeys.value.filter((key: string) => {
            return findItemByAttr(data, 'id', key);
          });
          break;
      }
    } catch (error) {
      console.error(error);
    }
  };

  watch(
    () => open.value,
    async newVal => {
      rawMaterialCheckedNodes.value = [];
      middleProductCheckedNodes.value = [];
      productCheckedNodes.value = [];
      if (newVal) {
        await nextTick();
        const { data } = await reqInspectConfigQueryMaterial(props.rowData.id);
        rawMaterialCheckedKeys.value = data;
        middleProductCheckedKeys.value = data;
        productCheckedKeys.value = data;
        tabKeyArr.forEach(tab => {
          fetchRawMaterialTreeData(tab.key);
        });
      }
    },
    {
      immediate: true,
    },
  );

  const getIdsByNodes = (nodes: any[]) => {
    return nodes.filter((node: any) => !node.categoryFlag).map((node: any) => node.id);
  };

  const okModal = async () => {
    try {
      const rowMaterialIds = rawMaterialCheckedNodes.value.length
        ? getIdsByNodes(rawMaterialCheckedNodes.value)
        : rawMaterialCheckedKeys.value;
      const middleProductIds = middleProductCheckedNodes.value.length
        ? getIdsByNodes(middleProductCheckedNodes.value)
        : middleProductCheckedKeys.value;
      const productIds = productCheckedNodes.value.length
        ? getIdsByNodes(productCheckedNodes.value)
        : productCheckedKeys.value;
      await reqInspectConfigBindMaterial({
        id: props.rowData.id,
        materialIdList: [...new Set([...rowMaterialIds, ...middleProductIds, ...productIds])],
      });
      message.success(t('绑定成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const checkHandle = (_checkedKeys: any, e: any) => {
    switch (activeKey.value) {
      case MaterialTypeMap.RawMaterial:
        rawMaterialCheckedNodes.value = e.checkedNodes;
        break;
      case MaterialTypeMap.MiddleProduct:
        middleProductCheckedNodes.value = e.checkedNodes;
        break;
      case MaterialTypeMap.Product:
        productCheckedNodes.value = e.checkedNodes;
        break;
    }
  };
</script>

<style lang="less">
  .inspection-bind-material-modal {
    .mes-modal-body {
      margin-top: 0;
      max-height: calc(100vh - 150px - 128px + 16px);
      height: calc(100vh - 150px - 128px + 16px);
      overflow: hidden;
    }
    .tree-content {
      height: 100%;
      border-radius: 5px;
      border: 1px solid #d4d6d9;
      padding-bottom: 6px;
      .title {
        display: flex;
        height: 40px;
        padding: 10px 16px;
        align-items: center;
        gap: 16px;
        flex-shrink: 0;
        border-radius: 5px 0px 0px 0px;
        background: var(--bmos-disable-color);
      }
      .bmos-search-tree {
        height: calc(100% - 40px);
      }
    }
    .mes-form {
      height: 100%;
      .mes-tabs {
        height: 100%;
        .mes-tabs-content {
          height: 100%;
        }
      }
    }
  }
</style>
