// 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
export const loopTree = (data: any) => {
  return data.map((item: any) => {
    item.showName = item.name; //树展示为showName
    if (item.children) {
      loopTree(item.children);
    }
    return item;
  });
};
// 树展示为code-name
export const loopTree2 = (data: any) => {
  return data.map((item: any) => {
    // item.showName = item.code + '-' + item.name;
    item.showName = item.name;
    if (item.children) {
      loopTree2(item.children);
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
