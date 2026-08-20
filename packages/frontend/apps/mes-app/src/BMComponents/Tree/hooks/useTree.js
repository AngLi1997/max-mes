import { computed, ref } from 'vue';
export function useTree({ props, customFieldNames }) {
  // 搜索关键字
  const searchValue = ref('');

  // 树数据Map
  const treeDataMap = ref(new Map());
  // 搜索时展示的节点keys
  const showKeys = computed(() => {
    let value = searchValue.value;
    if (value) {
      let expanded = [];
      // 获取树节点的所有上级节点
      treeDataMap.value.forEach((item) => {
        if (item[customFieldNames.value.name].indexOf(value) > -1) {
          expanded = [
            item[customFieldNames.value.key],
            ...getParentKeys(
              treeDataMap.value,
              item[customFieldNames.value.parentId]
            ),
            ...expanded
          ];
        }
      });
      expanded = Array.from(new Set(expanded));
      return expanded;
    }
    return [];
  });
  /*
   * @description: 铺平树数据
   * @param {DataNode[]} treeData: 树数据
   */
  function generateMap(treeData) {
    function generate(data) {
      if (!data) {
        return;
      }
      for (let i = 0; i < data.length; i++) {
        const node = data[i];
        const key = node[customFieldNames.value.key];
        if (!treeDataMap.value) {
          treeDataMap.value = new Map();
        }
        treeDataMap.value.set(key, node);
        if (node.children) {
          generate(node.children);
        }
      }
    }
    generate(treeData);
  }

  /*
   * @description: 获取父节点的key
   * @param key: 当父节点的key
   * @param treeDataMap: 平铺树Map数据
   */
  function getParentKeys(treeDataMap, parentId) {
    let keys = [];

    function loop(dataMap, id) {
      const parentNode = dataMap.get(id);
      if (!keys.includes(id) && parentNode) {
        keys.push(id);
        if (parentNode[customFieldNames.value.parentId] !== '0') {
          loop(dataMap, parentNode[customFieldNames.value.parentId]);
        }
      }
    }
    loop(treeDataMap, parentId);
    return keys;
  }

  return {
    searchValue,
    treeDataMap,
    showKeys,
    generateMap,
    getParentKeys
  };
}
