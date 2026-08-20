export const getProductCategoryId = (productId: string, treeData: any[]) => {
  let categoryId = '';
  const findCategory = (data: any[]) => {
    for (let i = 0; i < data.length; i++) {
      if (data[i].id === productId) {
        categoryId = data[i].parentId;
        break;
      }
      if (data[i].children && data[i].children.length) {
        findCategory(data[i].children);
      }
    }
  };
  findCategory(treeData);
  return categoryId;
}