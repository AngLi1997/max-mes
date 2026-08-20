interface rowSelectConfig {
  hideSelectAll: Boolean;
  getCheckboxProps: Function | Object;
}

export function useRowSelection({ hideSelectAll, getCheckboxProps }: rowSelectConfig): any {
  const selectedRows = ref<any[]>([]);
  const selectedRow = ref<any>({});

  const rowSelection = reactive({
    type: 'checkbox',
    hideSelectAll,
    columnWidth: 50,
    fixed: true,
    selectedRowKeys: [] as any[],
    preserveSelectedRowKeys: true,
    getCheckboxProps,
    onChange: (rowKeys: any[], rows: any[]) => {
      if (hideSelectAll) {
        // 单选
        rowSelection.selectedRowKeys = rowKeys?.length ? [rowKeys[rowKeys.length - 1]] : [];
        selectedRow.value = rows[rows.length - 1] ?? {};
      } else {
        // 多选
        rowSelection.selectedRowKeys = rowKeys ?? [];
        selectedRows.value = rows ?? [];
      }
    },
  });

  /**
   * @description: 清空选中
   */
  const clearSelect = () => {
    rowSelection.selectedRowKeys = [];
    selectedRows.value = [];
    selectedRow.value = {};
  };

  return {
    selectedRow,
    selectedRows,
    rowSelection,
    clearSelect,
  };
}
