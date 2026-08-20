// 树选时候去查到对应的整个node节点
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
