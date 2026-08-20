import { isAsyncFunction, isBoolean, isFunction, isObject } from '@/utils/is.js';
import { nextTick, unref } from 'vue';

export const useTableMethods = ({ props, tableState, emit }) => {
  const {
    tableRef,
    triggered,
    loadMoreStatus,
    requestTimer,
    tableData,
    paginationRef,
    loadingRef,
    selectedRows,
  } = tableState;

  /**
   * @description 更新表格分页信息
   * @param {Pagination} info 分页信息
   */
  const updatePagination = (info = paginationRef.value) => {
    if (isBoolean(info)) {
      paginationRef.value = info;
    }
    else if (isObject(paginationRef.value)) {
      paginationRef.value = {
        ...paginationRef.value,
        ...info,
      };
      triggered.value = false;
      loadMoreStatus.value
        = paginationRef.value.total > tableData.value.length ? '' : 'finished';
    }
  };

  /**
   * @description 选择项变化
   * @param {Row[]} _selectedRows 选中的行
   */
  const selectionChange = (_selectedRows) => {
    const { detail } = _selectedRows;
    const rows = detail.index?.map(index => tableData.value[index]);
    selectedRows.value = rows;
    emit('selection-change', rows);
  };

  /**
   * @description 获取表格数据
   * @returns {Row[]} 表格数据
   */
  const getTableData = () => {
    return tableData.value;
  };

  /**
   * @description 更新table 数据
   * @param {string | number} value 值
   * @param {string} key 键
   * @param {number} index 索引
   */
  const updateRowChange = (value, key, index) => {
    tableData.value = tableData.value.map((item, i) => {
      if (i === index) {
        emit('updateRow', {
          row: {
            ...item,
            [key]: value,
          },
          value,
          key,
          index,
        });
        return {
          ...item,
          [key]: value,
        };
      }
      return item;
    });
  };

  /**
   * @description 用于多选表格，切换某一行的选中状态，如果使用了第二个参数，则是设置这一行选中与否（selected 为 true 则选中）
   * @param {Array} index 行索引数组
   * @param {boolean} selected 是否选中
   */
  const toggleRowSelection = (index, selected) => {
    tableRef.value?.toggleRowSelection(index, selected);
  };

  /**
   * @param {object} params 表格查询参数
   * @param {boolean} rest
   * @description 获取表格数据
   */
  const fetchData = async (params = {}, rest) => {
    try {
      const { pagination } = rest || {};
      // 如果用户没有提供dataSource并且dataRequest是一个函数，那就进行接口请求
      if (
        Object.is(props.data, undefined)
        && (isFunction(props.dataRequest) || isAsyncFunction(props.dataRequest))
      ) {
        await nextTick();
        const _pagination = unref(paginationRef);
        // 是否启用了分页
        const enablePagination = isObject(_pagination);
        const queryParams = {
          ...params,
        };
        // 如果有额外的参数，将额外的参数添加到请求参数中
        if (props.extraParams) {
          Object.assign(queryParams, {
            ...props.extraParams,
          });
        }
        // 如果启用了分页，将分页信息添加到请求参数中
        if (enablePagination) {
          Object.assign(queryParams, {
          // @ts-ignore
            pageNum: _pagination.current,
            // @ts-ignore
            pageSize: _pagination.pageSize,
          });
        }
        loadingRef.value = true;
        const data = await props?.dataRequest?.(queryParams, rest).finally(() => (loadingRef.value = false));
        const _data = data?.data;
        if (_data?.pageNum && _data?.total) {
          const { pageNum, total } = _data;

          // @ts-ignore
          // if (enablePagination && _pagination?.current && retryFetchCount-- > 0) {
          if (enablePagination && _pagination?.current) {
          // 有分页时,删除当前页最后一条数据时 自动往前一页查询
            if (_data?.list.length === 0 && total > 0 && pageNum > 1) {
            // @ts-ignore
              _pagination.current--;

              return reload(true);
            }
          }
          updatePagination({
            ...(pagination || {}),
            current: pageNum || 1,
            total,
          });
        }
        else if (_data?.total === 0) {
          updatePagination({
            ...(pagination || {}),
            total: 0,
            current: 1,
          });
        }
        else {
          updatePagination(pagination);
        }
        if (Array.isArray(_data?.list)) {
          if (paginationRef.value.bottomOutRefresh && queryParams.pageNum > 1) {
          // 根据 props.rowKey 去重
            tableData.value = Array.from(new Map([...tableData.value, ..._data.list].map(item => [item[props.rowKey], item])).values());
          }
          else {
            tableData.value = _data.list;
          }
        }
        else if (Array.isArray(_data)) {
          if (paginationRef.value.bottomOutRefresh && queryParams.pageNum > 1) {
          // 根据 props.rowKey 去重
            tableData.value = Array.from(new Map([...tableData.value, ..._data].map(item => [item[props.rowKey], item])).values());
          }
          else {
            tableData.value = _data;
          }
        }
        else {
          tableData.value = [];
        }
      }
      else {
        updatePagination(pagination);
        // 根据 pagination 中的 current, 以及 paginationRef 的 pageSize 分页
        if (isObject(pagination) && pagination.current) {
          tableData.value = props.data.slice(
            (pagination.current - 1) * paginationRef.value.pageSize,
            pagination.current * paginationRef.value.pageSize,
          );
        }
      }
      return tableData;
    }
    catch (_error) {
      if (paginationRef.value.bottomOutRefresh) {
        tableData.value = [];
        clearTimer();
      }
    }
  };

  /**
   * @description 下拉刷新
   */
  const onRefresh = () => {
    paginationRef.value.current = 1;
    triggered.value = true;
    fetchData();
  };

  /**
   * @description 触底刷新
   */
  const onScrollToLower = () => {
    if (
      paginationRef.value.current * paginationRef.value.pageSize < paginationRef.value.total
      && triggered.value === false
    ) {
      paginationRef.value.current++;
      loadMoreStatus.value = 'loading';
      fetchData();
    }
  };

  /**
   * @description 分页菜单点击
   */
  const pageMenuClick = (e) => {
    if (e.item.value) {
      paginationRef.value.pageSize = Number.parseInt(e.item.value, 10);
      fetchData();
    }
  };

  /**
   * @description 重置表格数据
   * @param {object} params 表格查询参数
   * @param {number} page 页码
   */
  const handleReset = (params, page = 1) => {
    params.pageNum = page;
    fetchData(params);
  };

  /**
   * @description 分页切换
   * @param {number} value 当前页
   */
  const paginationChange = ({ value }) => {
    updatePagination({
      current: value,
    });
    fetchData({}, {
      pagination: {
        current: value,
      },
    });
    emit('change', { value });
  };

  /**
   * @description 刷新表格
   * @param {boolean} resetPageIndex 是否重置页码
   */
  const reload = async (resetPageIndex = false) => {
    const pagination = unref(paginationRef);
    if (Object.is(resetPageIndex, true) && isObject(pagination)) {
      // @ts-ignore
      pagination.current = 1;
    }
    if (props.intervalRequest) {
      clearTimer();
      requestTimer.value = setInterval(() => {
        fetchData();
      }, 10000);
    }
    return await fetchData();
  };

  /**
   * @description 清除定时器
   */
  const clearTimer = () => {
    if (requestTimer.value) {
      clearInterval(requestTimer.value);
      requestTimer.value = null;
    }
  };

  return {
    pageMenuClick,
    getTableData,
    fetchData,
    reload,
    updatePagination,
    handleReset,
    paginationChange,
    selectionChange,
    updateRowChange,
    toggleRowSelection,
    onRefresh,
    onScrollToLower,
    clearTimer,
  };
};
