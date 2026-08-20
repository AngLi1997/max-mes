export const loopTree = (data: any) => {
  return data.map((item: any) => {
    // item.showName = item.code + '-' + item.name;
    item.showName = item.name;
    if (item.children) {
      loopTree(item.children);
    }
    return item;
  });
};
// 工厂建模树展示位code-name
export const loopTree2 = (data: any) => {
  return data.map((item: any) => {
    item.showName = item.code + '-' + item.name;
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

// 通过编辑的id重新从树中找 然后刷新描述列表
export const findItemsById = (items: any, id: any) => {
  const result: any = [];
  function findByIdRecursive(items: any) {
    items.forEach((item: any) => {
      if (item.id === id) {
        result.push(item);
      }
      if (item.children && item.children.length > 0) {
        findByIdRecursive(item.children);
      }
    });
  }
  findByIdRecursive(items);
  return result[0];
};
