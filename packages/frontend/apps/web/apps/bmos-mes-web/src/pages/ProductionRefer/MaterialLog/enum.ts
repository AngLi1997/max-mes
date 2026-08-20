//操作类型
export const Animation: Record<string | number, string> = {
  0: t('入库'),
  1: t('退库'),
  2: t('出库'),
  3: t('盘点'),
};

//具体操作
export const Operation: Record<string | number, string> = {
  1: t('物料入库-递交'),
  2: t('物料入库-接收'),
  3: t('物料退库-递交'),
  4: t('物料退库-接收'),
  5: t('物料出库-发放'),
  6: t('物料出库-领用'),
  7: t('盘点-盘点'),
  8: t('盘点-复核'),
};
