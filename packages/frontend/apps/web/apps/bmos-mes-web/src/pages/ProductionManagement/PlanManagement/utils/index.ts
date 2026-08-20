// 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
export const loopTree = (data: any) => {
  return data.map((item: any) => {
    if (item.categoryFlag) {
      item.selectable = false;
    } else {
      item.selectable = true;
    }
    item.label = item.mergeCode + '-' + item.name;
    if (item.children) {
      loopTree(item.children);
    }
    return item;
  });
};

// 循环树形结构数据 data, 根据 level 为 POSITION 添加属性 selectable false
export const loopMaterialTree = (data: any) => {
  return data.map((item: any) => {
    if (item.level !== 'POSITION') {
      //货位
      item.selectable = false;
    } else {
      item.selectable = true;
    }
    item.label = item.name;
    if (item.children) {
      loopMaterialTree(item.children);
    }
    return item;
  });
};

// 树多选时候去查到对应的整个node节点
export const findNodeByValue = (nodes: any, value: any) => {
  for (let i = 0; i < nodes.length; i++) {
    const node = nodes[i];
    if (node.id === value) {
      return node;
    }
    if (node.children) {
      const foundNode: any = findNodeByValue(node.children, value);
      if (foundNode) {
        return foundNode;
      }
    }
  }
  return null;
};
