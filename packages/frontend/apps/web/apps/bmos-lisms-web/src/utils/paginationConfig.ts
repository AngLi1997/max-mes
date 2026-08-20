export const paginationBig = reactive({
  current: 1,
  pageSize: 50,
  total: 0,
  pageSizeOptions: ['20', '30', '50', '100'],
  showQuickJumper: true,
  showSizeChanger: true, // 显示可改变每页数量
  showTotal: (total: number) => `${t('共')} ${total} ${t('条')}`, // 显示总数
});

export const paginationSmall = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  pageSizeOptions: ['10', '20', '50', '100'],
  showQuickJumper: true,
  showSizeChanger: true, // 显示可改变每页数量
  showTotal: (total: number) => `${t('共')} ${total} ${t('条')}`, // 显示总数
});
