// 树形数据， 根据 fieldId 找到 祖先节点
export const findTopAncestor = (fieldId: any, nodeList: any) => {
  const findNode = (node: any, fieldId: string): any => {
    if (node.fieldId === fieldId) {
      return node;
    }
    if (node.children) {
      for (const child of node.children) {
        const result = findNode(child, fieldId);
        if (result) {
          return result;
        }
      }
    }
    return null;
  };
  for (const node of nodeList) {
    const result = findNode(node, fieldId);
    if (result) {
      return node;
    }
  }
  return null;
};

// 树形数据， 根据 叶子节点的 used 过滤 叶子节点, 保留 used 为 true 的叶子节点, 去除 used 为 false 的叶子节点
export const filterLeafNode = (tree: any) => {
  // 过滤掉 used 为 false 的叶子节点
  const filterChildren = (children: any[]): any => {
    return children
      .filter(child => child.used || (child.children && child.children.length > 0))
      .map(child => {
        if (child.children && child.children.length > 0) {
          const newChildren = filterChildren(child.children);
          if (newChildren.length > 0) {
            return { ...child, children: newChildren };
          }
          return null;
        }
        return child;
      })
      .filter(Boolean);
  };
  if (!tree) {
    return [];
  }
  // 递归过滤树形结构
  return tree
    .filter((child: { used: any; children: any[] }) => child.used || (child.children && child.children.length > 0))
    .map((node: { children: any[] }) => {
      if (node.children && node.children.length > 0) {
        const newChildren = filterChildren(node.children);
        if (newChildren.length > 0) {
          return { ...node, children: newChildren };
        }
        return null;
      }
      return node;
    })
    .filter(Boolean);
};
