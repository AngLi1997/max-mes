// 传入 treeData, 平级化树形结构数据
export const flatMenuTreeData = (treeData: any[]) => {
  const res: any = {};
  const loop = (data: any) => {
    data.forEach((item: any) => {
      if (item.children) {
        loop(item.children);
      }
      if (item?.id) {
        res[item?.id] = {
          ...item,
          children: [],
        };
      }
    });
  };
  loop(treeData);
  return res;
};
