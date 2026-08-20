export const loopTree = (data: any) => {
  return data.map((item: any) => {
    item.showName = item.code + '-' + item.name;
    if (item.children) {
      loopTree(item.children);
    }
    return item;
  });
};
