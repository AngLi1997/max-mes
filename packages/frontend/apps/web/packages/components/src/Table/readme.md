# Table 表格组件

Table 组件提供了高级表格展示功能，支持分页、排序、搜索等特性。  
自动请求，额外参数，flex布局table高度自适应，操作列的封装，toolbar 和 title显示。  
插槽支持自定义渲染，自定义样式，自定义操作列，自定义分页，自定义搜索，自定义排序。  
以及行的拖拽排序和 table 的纵向虚拟滚动等功能。

## 基本用法

一行代码开启表头搜索(headerSearchComponent 设为 Input 或者 Checkbox)

```js
{
    title: '价格',
    align: 'center',
    // hideInSearch: true,
    headerSearchComponent: 'Input',
    dataIndex: 'price',
    formItemProps: {
      component: 'Select',
    },
    customRender: ({ record }) => `${record.price}元`,
  },
```

```vue
<template>
  <Layout>
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      header-title="测试表格"
      :scroll="{ x: 1144, y: 400 }"
      :show-refresh="true"
      :form-props="formProps"
      :is-select="true"
      :row-selection="{
        selectedRowKeys: [2],
      }"
      :extra-params="{
        aaaa: '123',
      }"
      :show-tool-bar="true"
      show-search-border
      @change="handleTableChange"
      @handle-click-row="handleClickRow">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'name'">
          {{ record.name }}
          <a>[测试]</a>
        </template>
      </template>
      <!-- <template #expandedRowRender="{ record }">
      <p>
        {{ record.date }}
      </p>
    </template> -->
      <template #toolbar="{ reload }">
        <Button type="primary">新增</Button>
        <Button>导入</Button>
        <Button @click="reload">刷新</Button>
      </template>
    </BMTable>
    <Modal v-model:open="open" title="Basic Modal" @ok="handleOk">
      <p>Some contents...</p>
      <p>Some contents...</p>
      <p>Some contents...</p>
    </Modal>
  </Layout>
</template>

<script lang="tsx" setup>
  import { type DataRequestFn, type FormProps, BMTable, type TableColumn, type TableInstance } from '@bmos/components';
  import type { GetRowKey } from 'ant-design-vue/es/table/interface';
  import { reactive, ref } from 'vue';
  import { Button, Modal, Tag } from 'ant-design-vue';
  import { debounce } from '@bmos/utils';

  const open = ref<boolean>(false);

  const names = ['王路飞asdasdasdasdasdasdasdasdasdasdasdads', '王大蛇', '李白', '刺客伍六七'];
  const fetchStatusMapData = (keyword = '') => {
    const data = [
      {
        label: '已售罄',
        value: 0,
      },
      {
        label: '热卖中',
        value: 1,
      },
    ].filter(n => n.label.includes(keyword));
    return new Promise(resolve => {
      setTimeout(() => {
        resolve(data);
      }, 2000);
    });
  };
  const getClothesByGender = (gender: number) => {
    if (gender === 1) {
      // 男
      return [
        {
          label: '西装',
          value: 1,
        },
        {
          label: '领带',
          value: 0,
        },
      ];
    } else if (gender === 0) {
      //女
      return [
        {
          label: '裙子',
          value: 1,
        },
        {
          label: '包包',
          value: 0,
        },
      ];
    }
    return [];
  };
  const tableData = Array.from({ length: 30 }).map((_, i) => {
    const gender = ~~(Math.random() * 2);
    return {
      id: i + 1,
      date: new Date().toLocaleString(),
      name: names[~~(Math.random() * 4)],
      clothes: getClothesByGender(gender)[~~(Math.random() * 2)].label,
      price: ~~(Math.random() * 1000),
      gender,
      status: ~~(Math.random() * 2),
    };
  });

  const columns: TableColumn[] = [
    {
      title: '姓姓名名',
      align: 'center',
      dataIndex: 'name',
      fixed: 'left',
      sorter: true,
      width: 200,
      resizable: true,
      formItemProps: {
        defaultValue: '李白',
        // colProps: {
        //   span: 12,
        // },
      },
    },
    {
      title: '性别',
      align: 'center',
      dataIndex: 'gender',
      width: 100,
      resizable: true,
      formItemProps: {
        component: 'Select',
        componentProps: ({ formInstance, formModel }) => ({
          options: [
            {
              label: '男',
              value: 1,
            },
            {
              label: '女',
              value: 0,
            },
          ],
          onChange() {
            // 根据当前选择的性别，更新衣服可选项
            formInstance?.updateSchema({
              field: 'clothes',
              componentProps: {
                options: getClothesByGender(formModel.gender),
              },
            });
            formModel['clothes'] = undefined;
          },
        }),
      },
      customRender: ({ record }) => ['女', '男'][record.gender],
    },
    {
      title: '衣服',
      align: 'center',
      hideInSearch: true,
      dataIndex: 'clothes',
      formItemProps: {
        component: 'Select',
      },
    },
    {
      title: '价格',
      align: 'center',
      hideInSearch: true,
      dataIndex: 'price',
      formItemProps: {
        component: 'Select',
      },
      customRender: ({ record }) => `${record.price}元`,
    },
    {
      title: '状态',
      align: 'center',
      hideInSearch: true,
      dataIndex: 'status',
      formItemProps: {
        component: 'Select',
        componentProps: ({ formInstance, schema }) => ({
          showSearch: true,
          filterOption: false,
          request: () => {
            return fetchStatusMapData();
          },
          onSearch: debounce(async (keyword: string | undefined) => {
            schema.loading = true;
            const newSchema = {
              field: 'status',
              componentProps: {
                options: [] as any,
              },
            };
            formInstance?.updateSchema([newSchema]);
            console.log('onSearch keyword', keyword);
            const result = await fetchStatusMapData(keyword).finally(() => (schema.loading = false));
            newSchema.componentProps.options = result;
            formInstance?.updateSchema([newSchema]);
          }, 500),
          onChange(value: string) {
            console.log('onChange', value);
          },
        }),
      },
      customRender: ({ record }) => (
        <Tag color={record.status == 1 ? 'red' : 'default'}>{['已售罄', '热卖中'][record.status]}</Tag>
      ),
    },
    // {
    //   title: '操作',
    //   align: 'center',
    //   key: 'ACTION',
    //   actions: (params, action) => [
    //     {
    //       label: '编辑',
    //       onClick: ({ record }) => {
    //         console.log('编辑', params, action);
    //         console.log('编辑', record);
    //       },
    //     },
    //     {
    //       label: '删除',
    //       onClick: ({ record }) => {
    //         console.log('删除', record);
    //       },
    //     },
    //   ],
    // },
    {
      title: '操作',
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 260,
      actions: ({ record }) => [
        {
          label: '编辑',
          ifShow: Math.random() > 0.5,
          // code: '120100001000002', // 权限码传入code 会自动 使用 configProvider 的 llProps 中的 hasPermission 方法判断是否有权限
          onClick: () => {
            console.log('编辑', record);
          },
        },
        {
          label: '查看',
          onClick: () => {
            console.log('查看', record);
          },
        },
        {
          label: '删除',
          ifShow: Math.random() > 0.5,
          danger: true,
          onClick: () => {
            console.log('删除', record);
          },
        },
        {
          label: '查看详情',
          onClick: () => {
            console.log('查看详情', record);
          },
        },
        {
          label: '删除2',
          danger: true,
          onClick: () => {
            console.log('删除', record);
          },
        },
      ],
    },
  ];

  const handleOk = () => {
    open.value = false;
  };
  const tableInstance = ref<TableInstance>();

  const formProps = reactive<Partial<FormProps>>({
    actionColOptions: {
      // span: 6,
    },
    baseColProps: {
      span: 8,
    },
    showAdvancedButton: false,
    showAdvancedButtonBadge: true,
    advancedBadgeCount: 2,
  });

  const loadData: DataRequestFn = async (params, onChangeParams): Promise<any> => {
    console.log('params', params);
    console.log('onChangeParams', onChangeParams);

    return new Promise(resolve => {
      setTimeout(() => {
        resolve({
          data: {
            list: tableData,
            total: 30,
            pageNum: 1,
          },
          ...params,
        });
        // tableInstance.value?.updatePagination?.({
        //   total: 30,
        // });
        // 手动设置搜索表单的搜索项
        tableInstance.value?.getQueryFormRef()?.updateSchema?.([
          {
            field: 'price',
            componentProps: {
              options: [
                {
                  label: '0-199',
                  value: '0-199',
                },
                {
                  label: '200-999',
                  value: '200-999',
                },
              ],
            },
          },
        ]);
      }, 500);
    });
  };
  const handleTableChange = (params: any) => {
    console.log('params', params);
  };
  const handleClickRow = (record: any, key: string | GetRowKey<any>, selectedRowKeys: (string | GetRowKey<any>)[]) => {
    console.log('record', record, key, selectedRowKeys);
  };
</script>
```

## API

### Props

| 参数                         | 说明                                              | 类型                 | 默认值 |
| ---------------------------- | ------------------------------------------------- | -------------------- | ------ |
| AntV 的左右 table 参数都支持 | [参考链接](https://antdv.com/components/table-cn) | -                    | -      |
| columns                      | 表格列配置                                        | `Array<TableColumn>` | []     |
| search                       | 是否显示搜索表单                                  | Boolean              | true   |
| formProps                    | 表单属性配置                                      | FormProps            | {}     |
| dataRequest                  | 表格数据请求函数                                  | Function             |        |
| showIndex                    | 是否显示索引号                                    | Boolean              | false  |
| indexColumnProps             | 索引列属性配置                                    | TableColumn          | {}     |
| showToolBar                  | 是否显示表格工具栏                                | Boolean              | true   |
| showTableSetting             | 是否显示表格设置                                  | Boolean              | true   |
| headerTitle                  | 表格标题                                          | String               | -      |
| titleTooltip                 | 表格标题提示信息                                  | String               | -      |
| showRefresh                  | 是否显示刷新按钮                                  | Boolean              | false  |
| isSelect                     | / 是否支持选中                                    | Boolean              | false  |
| isMultipleSelect             | 是否支持多选                                      | Boolean              | false  |
| showSearchBorder             | 是否显示表格搜索下的 border                       | Boolean              | true   |
| isExtraParamsChangeQuery     | 额外参数改变是否立刻触发查询                      | Boolean              | true   |
| extraParams                  | 表格额外参数                                      | Object               | {}     |
| maxActionCount               | 操作列最大按钮数， 超出用 ... 显示                | Number               | 4      |
| pageSizeChangeToFirst        | 切换 pageSize 是否回到第一页                      | Boolean              | false  |
| virtualScroll                | 是否开启纵向虚拟滚动                              | Boolean              | false  |
| dragSort                     | 是否开启拖拽排序功能                              | Boolean              | false  |

### TableColumn 参数

| 参数                       | 说明                                           | 类型                                                                       | 默认值 |
| -------------------------- | ---------------------------------------------- | -------------------------------------------------------------------------- | ------ |
| dataIndex                  | 和 AntV 一样                                   | -                                                                          | -      |
| searchField                | 指定搜索的字段                                 | string                                                                     | -      |
| hideInSearch               | 在查询表单中不展示此项                         | boolean                                                                    | -      |
| hideInTable                | 在 Table 中不展示此列                          | boolean                                                                    | -      |
| formItemProps              | 传递给搜索表单 Form.Item 的配置,可以配置 rules | `Partial<FormSchema<T>>`                                                   | -      |
| headerSearchComponent      | 表头搜索组件                                   | string                                                                     | -      |
| headerSearchComponentProps | 表头搜索组件属性                               | Recordable                                                                 | -      |
| actions                    | 操作列，一般用于对表格某一行数据进行操作       | `(params: CustomRenderParams<T>, action: TableActionType) => ActionItem[]` | -      |
| children                   | children                                       | `TableColumn<T>[]`                                                         | -      |

### Methods

| 方法            | 说明                                                                          | 参数                                                    | 返回值       |
| --------------- | ----------------------------------------------------------------------------- | ------------------------------------------------------- | ------------ |
| fetchData       | 拉取表格数据                                                                  | params, reset(table change时间的参数一致)               | Promise      |
| refresh         | 刷新表格数据                                                                  | void                                                    | void         |
| getQueryFormRef | 获取查询表单实例                                                              | void                                                    | FormInstance |
| reRenderTable   | 重新渲染表格                                                                  | void                                                    | void         |
| getTableData    | 获取表格数据                                                                  | void                                                    | T[]          |
| updateTableData | 更新表格数据                                                                  | data: T[]                                               | void         |
| addColumn       | 添加 column, 在 dataIndex 之前 columns 添加， 如果没有 dataIndex 则添加到最后 | column: TableColumn \| TableColumn[], dataIndex: string | void         |
| removeColumn    | 移除 column                                                                   | dataIndex: string \| string[]                           | void         |
| updateColumn    | 更新 column                                                                   | column: TableColumn \| TableColumn[]                    | void         |
| replaceColumn   | 替换 column                                                                   | column: TableColumn \| TableColumn[]                    | void         |

### 插槽

支持自定义渲染的插槽，允许用户根据需求自定义表格的某些部分。

| 名称        | 说明                |
| ----------- | ------------------- |
| Form的slot  | Form 组件支持的插槽 |
| headerTitle | 表格标题            |
| toolbar     | 表格工具栏          |
