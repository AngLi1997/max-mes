/*
 * @description: 获取父节点的key
 * @param key: 当父节点的key
 * @param treeDataMap: 平铺树Map数据
 */
export function getParentKeys(
	treeDataMap,
	parentId
) {
	let keys = [];

	function loop(data, id) {
		const parentNode = treeDataMap.get(id);
		if (!keys.includes(id)) {
			keys.push(id);
			if (parentNode?.parentId !== '0') {
				loop(treeDataMap, parentNode?.parentId);
			}
		}
	}
	loop(treeDataMap, parentId);
	return keys;
}

/*
 * @description: 铺平树数据
 * @param {DataNode[]} treeData: 树数据
 */
export function generateMap(treeData) {
	let dataListMap = new Map();

	function generate(data) {
		if (!data) {
			return;
		}
		for (let i = 0; i < data.length; i++) {
			const node = data[i];
			const key = node.id;
			if (!dataListMap) {
				dataListMap = new Map();
			}
			dataListMap.set(key, node);
			if (node.children) {
				generate(node.children);
			}
		}
	}
	generate(treeData);
	return dataListMap;
}

/**
 * @description: 获取所有的父节点Ids
 */
export function getParentIds(key, treeData) {
	let parentIds = [];

	function loop(data) {
		for (let i = 0; i < data.length; i++) {
			const node = data[i];
			if (node.children && node.children.length > 0) {
				if (
					node.children.some(
						(item) => item.id === key
					)
				) {
					parentIds.push(node.id);
				} else {
					loop(node.children);
				}
			}
		}
	}
	loop(treeData);
	return parentIds;
}

/*
 * @description: 获取父节点的name
 * @param Name: 当父节点的Name
 * @param treeDataMap: 平铺树Map数据
 */
export function getParentName(
	treeDataMap,
	parentId
) {
	let keys = [];
	let name = [];

	function loop(data, id) {
		const parentNode = treeDataMap.get(id);
		console.log(parentNode);
		if (!keys.includes(id)) {
			keys.push(id);
			name.push({
				id: id,
				name: parentNode?.name
			});
			if (parentNode?.parentId !== '0') {
				loop(treeDataMap, parentNode?.parentId);
			}
		}
	}
	loop(treeDataMap, parentId);
	return name;
}

/**
 * @description: 传入 treeData, 平级化树形结构数据
 * @param {*} treeData 
 * @returns {Object} [key: id, value: { ...item, children: [] }
 */
export const flatMenuTreeData = (treeData) => {
  const res = {};
  const loop = (data) => {
    data.forEach((item) => {
      if (item.children) {
        loop(item.children);
      }
      if (item?.id) {
        res[item?.id] = {
          ...item,
          children: []
        };
      }
    });
  };
  loop(treeData);
  return res;
};
