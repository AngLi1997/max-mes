<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('绑定工位')"
    wrapClassName="modalSizeMedium"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @okModal="ok">
    <Segmented v-model:value="choiceTypeValue" :options="choiceType" block style="width: 100%" />
    <!-- 产线 -->
    <div v-show="choiceTypeValue === t('产线')" class="tree-container">
      <BMSearchTree
        v-if="tree.treeData[0]?.children.length > 0"
        v-model:checked-keys="tree.CHECKED_KEYS"
        v-model:expanded-keys="tree.EXPANDED_KEYS"
        :showAllAddIcon="false"
        :showAction="false"
        :tree-data="tree.treeData"
        :checkable="true"
        :fieldNames="{ title: 'showName', key: 'uniqueId' }"
        @check="check"></BMSearchTree>
      <Empty v-else></Empty>
    </div>
    <!-- 房间 -->
    <div v-show="choiceTypeValue === t('房间')" class="tree-container">
      <BMSearchTree
        v-if="tree2.treeData[0]?.children.length > 0"
        v-model:checked-keys="tree2.CHECKED_KEYS"
        v-model:expanded-keys="tree2.EXPANDED_KEYS"
        :showAllAddIcon="false"
        :showAction="false"
        :tree-data="tree2.treeData"
        :checkable="true"
        :fieldNames="{ title: 'showName', key: 'id' }"
        @check="check2"></BMSearchTree>
      <Empty v-else></Empty>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message, Segmented } from 'ant-design-vue';
  import { reactive, watch } from 'vue';
  import {
    reqEquipmentStationUserBindStation,
    reqEquipmentStationUserStationList,
    reqFactoryLineUserLine,
  } from '@/api';
  const open = ref<boolean>(false);
  const props = withDefaults(
    defineProps<{
      rowId: string;
    }>(),
    {
      rowId: '',
    },
  );
  const choiceType = ref<any>([t('产线'), t('房间')]); //分段选择器
  const choiceTypeValue = ref<any>();
  const allStationIdList = ref<any>();
  const productionLineIds = ref<any>([]); //存初始时树里的所有产线id
  const roomIds = ref<any>(); //存初始时树里的所有房间id
  const lineStationList = ref<any>(); //存产线下的工位树
  const flatTreeList = ref<any>(); //存扁平的产线下的工位树

  // 产线
  const tree = reactive<any>({
    treeData: [],
    CHECKED_KEYS: [],
    EXPANDED_KEYS: [],
  });
  // 房间
  const tree2 = reactive<any>({
    treeData: [],
    CHECKED_KEYS: [],
    EXPANDED_KEYS: [],
  });
  const checkedIds = ref<any>([]); //产线id集合
  const checkedIds2 = ref<any>([]); //房间id集合
  const loopTree = (data: any) => {
    return data.map((item: any) => {
      item.showName = item.code + '-' + item.name;
      if (item.children) {
        loopTree(item.children);
      }
      return item;
    });
  };
  // 扁平化id
  const addId = (data: any) => {
    let ids: any = [];
    data?.forEach((item: any) => {
      ids.push(item.id);
      if (item.children) {
        ids = ids.concat(addId(item.children));
      }
    });
    return ids;
  };
  // 扁平化tab产线树
  const flatTree = (data: any) => {
    let ids: any = [];
    data?.forEach((item: any) => {
      ids.push(item);
      if (item.children) {
        ids = ids.concat(flatTree(item.children));
      }
    });
    return ids;
  };
  // 通过产线下的工位数组去查他对应的唯一键
  const addUniqueId = (data: any, data2: any) => {
    let ids: any = [];
    data?.forEach((item: any) => {
      if (data2?.includes(item.id)) {
        ids.push(item.uniqueId);
      }
      if (item.children) {
        ids = ids.concat(addUniqueId(item.children, data2));
      }
    });
    return ids;
  };

  // 通过uniqueId组成的数组去树里查成id数组
  const uniqueIdFindId = (data: any, data2: any) => {
    let ids: any = [];
    data?.forEach((item: any) => {
      if (data2?.includes(item.uniqueId) && item.type === 4) {
        ids.push(item.id);
      }
      if (item.children) {
        ids = ids.concat(uniqueIdFindId(item.children, data2));
      }
    });
    return ids;
  };

  // 获取工位树
  const getTreeData = async () => {
    try {
      const res = await reqFactoryLineUserLine({ userId: props.rowId });
      tree.treeData = [
        {
          showName: t('全部'),
          id: '0',
          uniqueId: '0',
          children: loopTree(res.data?.lineStationList),
        },
      ];
      lineStationList.value = loopTree(res.data?.lineStationList);
      flatTreeList.value = flatTree(lineStationList.value);
      tree2.treeData = [
        {
          showName: t('全部'),
          id: '0',
          children: loopTree(res.data?.roomStationList),
        },
      ];
      allStationIdList.value = res.data?.allStationIdList;
      productionLineIds.value = addId(res.data?.lineStationList);
      roomIds.value = addId(res.data?.roomStationList);
    } catch (error: any) {
      tree.treeData = [];
      tree2.treeData = [];
      message.error(error.message);
    } finally {
      tree.EXPANDED_KEYS = ['0'];
      tree2.EXPANDED_KEYS = ['0'];
    }
  };
  // 产线tab选中复选框触发
  const check = (
    selectedKeys: any[],
    info: {
      checked: boolean;
      node: any;
      checkedNodes: any;
    },
  ) => {
    checkedIds.value = [];
    const hh = info.checkedNodes?.map((item: any) => item.uniqueId?.slice(-19));
    if (info.checked) {
      // 勾选的时候就把相同的都勾选上
      const temp2 = flatTreeList.value
        ?.filter((item: any) => hh?.includes(item.id))
        ?.map((item2: any) => item2.uniqueId);
      tree.CHECKED_KEYS = [...temp2];
    } else {
      const mapItem = flatTreeList.value.filter((item: any) => info.node.id == item.id);
      let newKeys: any[];
      if (info.node.type == 4) {
        //取消勾选工位
        newKeys = mapItem.reduce((prev: any[], cur: any) => {
          prev.push(cur.uniqueId?.substring(0, 19), cur.uniqueId?.substring(0, cur.uniqueId.length - 20), cur.uniqueId); //依次去除相同的产线级、房间级、工位级
          return prev;
        }, []);
      }
      if (info.node.type == 3) {
        //取消勾选房间
        const temp4 = mapItem //找到相同房间下面的所有工位级
          ?.map((item: any) => item.children)
          ?.flat()
          ?.map((item2: any) => item2.uniqueId);
        const temp5 = mapItem.reduce((prev: any[], cur: any) => {
          prev.push(cur.uniqueId?.substring(0, 19), cur.uniqueId); //去除相同的产线级、房间级
          return prev;
        }, []);
        newKeys = [...temp4, ...temp5];
      }
      if (info.node.type == 2) {
        //取消勾选产线
        const temp6 = flatTree(mapItem)?.map((item: any) => item.id);
        const temp7 = flatTreeList.value
          ?.filter((item: any) => temp6?.includes(item.id))
          ?.map((item2: any) => item2.uniqueId);
        const temp8 = flatTreeList.value?.filter((item: any) => item.type === 2)?.map((item2: any) => item2.uniqueId); //去除所有产线级
        newKeys = [...temp7, ...temp8];
      }
      tree.CHECKED_KEYS = tree.CHECKED_KEYS.filter((item: any) => !newKeys.includes(item));
    }
  };
  // 房间tab选中复选框触发
  const check2 = (
    selectedKeys: any[],
    info: {
      checkedNodes: any;
    },
  ) => {
    checkedIds2.value = [];
    const temp = info.checkedNodes.filter((item: any) => item.type && item.type === 4);
    temp.forEach((item: any) => {
      checkedIds2.value.push(item.id);
    });
  };
  // 确定弹框
  const ok = async () => {
    if (tree.treeData[0]?.children.length === 0 && tree2.treeData[0]?.children.length === 0) {
      open.value = false;
      return;
    }
    checkedIds.value = [...new Set(uniqueIdFindId(lineStationList.value, tree.CHECKED_KEYS))];
    try {
      const data = {
        userId: props.rowId,
        stationIdList: [...checkedIds.value, ...checkedIds2.value],
        allStationIdList: allStationIdList.value,
      };
      await reqEquipmentStationUserBindStation(data);
      message.success(t('绑定成功'));
      open.value = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        choiceTypeValue.value = choiceType.value[0];
        await getTreeData();
        const { data } = await reqEquipmentStationUserStationList({ userId: props.rowId });
        const data1 = data.filter((item: any) => productionLineIds.value?.includes(item));
        const data2 = data.filter((item: any) => roomIds.value?.includes(item));
        const temp = addUniqueId(lineStationList.value, data1);
        tree.CHECKED_KEYS = temp;
        checkedIds.value = temp;
        tree2.CHECKED_KEYS = data2;
        checkedIds2.value = data2;
      } else {
        tree.CHECKED_KEYS = [];
        tree2.CHECKED_KEYS = [];
      }
    },
    {
      immediate: true,
    },
  );
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
  }
</style>
