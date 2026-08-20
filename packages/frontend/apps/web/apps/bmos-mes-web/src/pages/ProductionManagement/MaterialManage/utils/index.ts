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
