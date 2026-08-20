<template>
  <a-table
    :columns="columns"
    :data-source="data"
    :row-selection="rowSelection"
    :locale="locale">
    <template #headerCell="{ column }">
      <template v-if="column.key === 'name'">
        <span>Name</span>
      </template>
    </template>

    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'name'">
        <span>
          {{ record.name }}
        </span>
      </template>
      <template v-else-if="column.key === 'tags'">
        <span>
          <a-tag
            v-for="tag in record.tags"
            :key="tag"
            :color="
              tag === 'loser'
                ? 'volcano'
                : tag.length > 5
                ? 'geekblue'
                : 'green'
            ">
            {{ tag.toUpperCase() }}
          </a-tag>
        </span>
      </template>
      <template v-else-if="column.key === '操作'">
        <span>
          <a>链接按钮的最大长度为100px</a>
          <a>查看</a>
          <a>历史</a>
          <a>删除</a>
          <a>删除</a>
        </span>
      </template>
    </template>
  </a-table>
</template>

<script lang="ts" setup>
  import { reactive } from 'vue';
  import type { TableColumnType, TableProps } from 'ant-design-vue';

  type TableDataType = {
    key: string;
    name: string;
    age: number;
    address: string;
  };

  const columns: TableColumnType<TableDataType>[] = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      filters: [
        {
          text: 'Joe',
          value: 'Joe',
        },
        {
          text: 'Jim',
          value: 'Jim',
        },
        {
          text: 'Submenu',
          value: 'Submenu',
          children: [
            {
              text: 'Green',
              value: 'Green',
            },
            {
              text: 'Black',
              value: 'Black',
            },
          ],
        },
      ],
      onFilter: (value: any, record: TableDataType) =>
        record.name.indexOf(value) === 0,
      sorter: (a: TableDataType, b: TableDataType) =>
        a.name.length - b.name.length,
      sortDirections: ['descend'],
    },
    {
      title: 'Age',
      dataIndex: 'age',
      key: 'age',
    },
    {
      title:
        'Address,New York No. 1 Lake Park, New York No. 1 Lake Park,New York No. 1 Lake Park',
      dataIndex: 'address',
      key: 'address',
      ellipsis: true,
    },
    {
      title: 'Tags',
      key: 'tags',
      dataIndex: 'tags',
    },
    {
      title: 'Test',
      key: 'test',
      dataIndex: 'test',
    },
    {
      title: '操作',
      key: '操作',
      ellipsis: true,
      // width: 100,
      // minWidth: 100,
      // maxWidth: 470,
    },
  ];

  const data = [
    {
      key: '1',
      name: 'John Brown',
      age: 32,
      address:
        'New York No. 1 Lake Park, New York No. 1 Lake Park,New York No. 1 Lake Park',
      tags: ['nice', 'developer'],
      test: 'test',
    },
    {
      key: '2',
      name: 'Jim Green',
      age: 42,
      address:
        'London No. 1 Lake Park, New York No. 1 Lake Park,New York No. 1 Lake Park',
      tags: ['loser'],
      test: 'test',
    },
    {
      key: '3',
      name: 'Joe Black',
      age: 32,
      address:
        'Sidney No. 1 Lake Park, New York No. 1 Lake Park,New York No. 1 Lake Park',
      tags: ['cool', 'teacher'],
      test: 'test',
    },
  ];
  const rowSelection: TableProps['rowSelection'] = {
    onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
      console.log(
        `selectedRowKeys: ${selectedRowKeys}`,
        'selectedRows: ',
        selectedRows,
      );
    },
  };
  const locale = reactive({
    cancelSort: '取消排序',
    triggerAsc: '点击升序',
    triggerDesc: '点击降序',
  });
</script>
