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

// 通过树节点value去查到其对应的整个node节点（传整个树及某个节点的value）
export const findNodeByValue = (nodes: any, value: any) => {
  for (let i = 0; i < nodes?.length; i++) {
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

// 扁平化树
export const flatTree = (data: any) => {
  let tree: any = [];
  data?.forEach((item: any) => {
    tree.push(item);
    if (item.children) {
      tree = tree.concat(flatTree(item.children));
    }
  });
  return tree;
};

// 同层级按物料合并编码升序排序
export const sortTreeNodes = (nodes: any) => {
  nodes?.sort((a: any, b: any) => a?.mergeCode.localeCompare(b?.mergeCode));
  nodes?.forEach((node: any) => {
    if (node.children) {
      sortTreeNodes(node.children);
    }
  });
  return nodes;
};
