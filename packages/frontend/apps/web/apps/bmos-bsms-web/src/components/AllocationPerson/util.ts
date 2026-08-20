export const flat_children = (arr: any[]): any[] => {
  const data = [];
  for (let index = 0; index < arr.length; index++) {
    const element = arr[index];
    data.push(element);
    if (element.deptFlag) {
      if (element?.children?.length > 0) {
        const list = flat_children(element?.children);
        data.push(...list);
      }
    }
  }
  return data;
};

export const filterArrayToArray = (
  sources: Array<any>,
  filters: Array<any>,
  callback?: (source: any, filter: any) => boolean,
) => {
  return sources.filter(source => {
    return (
      filters.findIndex(filter => {
        if (callback) return callback(source, filter);
        return source.id === filter.id;
      }) < 0
    );
  });
};

// 遍历树， 找到所有的人员 deptFlag = false
export const flatTree = (tree: any[]): any[] => {
  const data = [];
  for (let index = 0; index < tree.length; index++) {
    const element = tree[index];
    if (element.deptFlag) {
      if (element?.children?.length > 0) {
        const list = flatTree(element?.children);
        data.push(...list);
      }
    } else {
      data.push(element);
    }
  }
  return data;
};
