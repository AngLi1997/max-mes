import { EmitFn } from '@bmos/components';
import { DetailListItemType, DetailListPropsType } from '../types';
const propertyId = 'itemId';

export const useDetailList = (emit: EmitFn, props: DetailListPropsType) => {
  const itemMap = new Map();
  const nodeList = computed(() => {
    itemMap.clear();

    return props.list.map(item => {
      const listItem: DetailListItemType = {
        ...item,
        node: Object.freeze(item),
      };
      itemMap.set(item.id, listItem);
      return listItem;
    });
  });

  const handleItemClick = (e: MouseEvent) => {
    const target = e.target as HTMLElement;
    if (target.dataset.hasOwnProperty(propertyId)) {
      const itemId = target.dataset[propertyId];
      let itemData;
      if (itemMap.has(itemId)) {
        itemData = itemMap.get(itemId);
      }
      emit('icon-click', itemId, itemData);
    }
  };
  return { handleItemClick, nodeList };
};
