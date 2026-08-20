import {
  isObject,
  omit,
} from 'lodash-es';
import {
  computed,
  ref,
  unref,
  watch,
} from 'vue';

export const useTableState = ({
  props,
}) => {
  /** 表格实例 */
  const tableRef = ref();
  /** 表格数据 */
  const tableData = ref([]);
  /** 内部属性 */
  const innerPropsRef = ref();
  /** 分页配置参数 */
  const paginationRef = ref(false);
  /** 选中列 */
  const selectedRows = ref([]);
  /** 表格加载 */
  const loadingRef = ref(!!props.loading);
  const showPopover = ref(false);

  const triggered = ref(false);
  const loadMoreStatus = ref('');
  const requestTimer = ref(null);
  // 如果 props.pagination 为 true，那就默认使用分页
  watch(
    () => props.pagination,
    () => {
      if (props.pagination === true) {
        paginationRef.value = {
          current: 1,
          pageSize: 10,
          total: 0,
          hideIfOnePage: false,
        };
      }
      else if (isObject(props.pagination)) {
        paginationRef.value = {
          current: 1,
          pageSize: 10,
          total: props.data?.length,
          hideIfOnePage: false,
        };
        paginationRef.value = {
          ...paginationRef.value,
          ...props.pagination,
        };
      }
      else {
        paginationRef.value = false;
      }
    },
    {
      immediate: true,
    },
  );
  /**
   * @description 获取props
   */
  const getProps = computed(() => {
    return {
      ...props,
      ...unref(innerPropsRef),
    };
  });

  const getBindValues = computed(() => {
    const props = unref(getProps);
    let propsData = {
      ...props,
      height: '100%',
    };

    propsData = omit(propsData, ['onChange', 'tableColProps']);
    return propsData;
  });

  const getThBindValues = computed(() => {
    const props = unref(getProps);
    // 如果其中某项设置了 width，那就不需要设置 width, 没有设置就默认平分所有没有设置的宽度， width可以是 number 或者 string 或者 百分比
    const {
      tableColProps,
    } = props;
    const widthList = tableColProps
      .filter(item => item.width)
      .map((item) => {
        return `${item.width}%`;
      });
    const hasWidth = widthList.length > 0;
    if (!hasWidth) {
      const width = `${100 / tableColProps.length}%`;
      return tableColProps.map((item) => {
        if (!item.width) {
          item.width = width;
        }
        return {
          prop: item.prop,
          label: item.label,
          fixed: item.fixed,
          align: 'left',
          ...item.thProps,
          width,
        };
      });
    }
    else {
      return tableColProps.map((item) => {
        if (item.width) {
          return {
            prop: item.prop,
            label: item.label,
            fixed: item.fixed,
            align: 'left',
            ...item.thProps,
            width: `${item.width}rpx`,
          };
        }
        return {
          prop: item.prop,
          label: item.label,
          fixed: item.fixed,
          align: 'left',
          ...item.thProps,
          // width: 'calc((100% - ' + widthList.join(' - ') + ') / ' + (tableColProps.length - widthList.length) + ')'
        };
      });
    }
  });

  const getTdBindValues = computed(() => {
    const props = unref(getProps);
    // 如果其中某项设置了 width，那就不需要设置 width, 没有设置就默认平分所有没有设置的宽度， width可以是 number 或者 string 或者 百分比
    const {
      tableColProps,
    } = props;
    return tableColProps;
  });

  // 如果外界设置了dataSource，那就直接用外界提供的数据
  watch(
    () => props.data,
    (val) => {
      if (val) {
        if (props.pagination) {
          tableData.value = val.slice(
            (paginationRef.value.current - 1) * paginationRef.value.pageSize,
            paginationRef.value.current * paginationRef.value.pageSize,
          );
          paginationRef.value.total = val.length;
        }
        else {
          tableData.value = val;
        }
      }
    },
    {
      immediate: true,
      deep: true,
    },
  );

  return {
    tableRef,
    loadingRef,
    tableData,
    innerPropsRef,
    getProps,
    getBindValues,
    getThBindValues,
    getTdBindValues,
    paginationRef,
    selectedRows,
    showPopover,
    triggered,
    loadMoreStatus,
    requestTimer,
  };
};
