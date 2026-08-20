<template>
  <a-card title="基础组件样式">
    <a-row>
      <a-col :span="24">
        <a-space wrap>
          <a-button type="primary">按钮</a-button>
          <a-button>按钮</a-button>
          <a-button type="dashed">按钮</a-button>
          <a-button type="text">按钮</a-button>
          <a-button type="link">按钮</a-button>
          <a-button type="primary" danger>危险按钮</a-button>
          <a-button danger>危险按钮</a-button>
          <a-button type="dashed" danger>危险按钮</a-button>
          <a-button type="text" danger>危险按钮</a-button>
          <a-button type="link" danger>危险按钮</a-button>
          <a-button type="primary" :icon="h(SearchOutlined)">按钮</a-button>
          <a-button :icon="h(SearchOutlined)">按钮</a-button>
          <!-- 下拉菜单更多 -->
          <div>
            <a-dropdown>
              <template #overlay>
                <a-menu @click="handleMenuClick">
                  <a-menu-item key="1">
                    选择效果
                  </a-menu-item>
                  <a-menu-item key="2">
                    功能名称
                  </a-menu-item>
                  <a-menu-item key="3">
                    功能名称
                  </a-menu-item>
                </a-menu>
              </template>
              <a-button>
                更多
                <DownOutlined />
              </a-button>
            </a-dropdown>
          </div>

          <!-- 输入框 -->
          <a-input
            v-model:value="value"
            class="test-inp"
            allow-clear
            placeholder="请输入11111" />
          <!-- 文本框 -->
          <a-textarea v-model:value="value" placeholder="文本框默认高度64px" />
          <!-- 滚动条 -->
          <div
            style="
              width: 90px;
              height: 50px;
              overflow: auto;
              border: 1px solid rgb(212 215 217);
">
            滚动条样式测试1111111111111111111
          </div>
          <!-- 搜索框 -->
          <a-input-search
            v-model:value="value"
            allow-clear
            placeholder="搜索框"
            style="width: 200px;" />
          <!-- 数字输入框 -->
          <a-input-number
            id="inputNumber"
            v-model:value="value"
            placeholder="数字"
            :min="1"
            :max="10" />
          <!-- 数字输入框 -->
          <a-input-number
            id="inputNumber"
            v-model:value="value"
            status="error"
            placeholder="错误色"
            :min="1"
            :max="10" />
          <!-- 多选框 -->
          <a-checkbox v-model:checked="checked">复选</a-checkbox>
          <!-- 单选框 -->
          <a-radio v-model:checked="checked1">单选</a-radio>
          <!-- 选择器 -->
          <a-select
            ref="select"
            placeholder="请选择"
            v-model:value="value1"
            style="width: 120px;">
            <a-select-option value="jack">Jack</a-select-option>
            <a-select-option value="lucy">Lucy</a-select-option>
            <a-select-option value="disabled" disabled>
              Disabled
            </a-select-option>
            <a-select-option value="Yiminghe">yiminghe</a-select-option>
          </a-select>
          <!-- 多选 -->
          <a-select
            ref="select"
            placeholder="请选择"
            mode="multiple"
            :max-tag-count="maxTagCount"
            v-model:value="value2"
            style="width: 120px;">
            <a-select-option value="jack">Jack</a-select-option>
            <a-select-option value="lucy">Lucy</a-select-option>
            <a-select-option value="disabled">abled</a-select-option>
            <a-select-option value="Yiminghe">yiminghe</a-select-option>
          </a-select>
          <!-- 级联选择器 -->
          <a-cascader
            v-model:value="value3"
            :options="options"
            multiple
            :show-search="{ filter }"
            placeholder="请选择" />

          <!-- 开关 -->
          <a-switch v-model:checked="checked" disabled />
          <a-switch v-model:checked="checked" size="small" />
          <!-- 日期选择器 -->
          <a-date-picker v-model:value="value4" placeholder="选择日期" />
          <a-range-picker v-model:value="value4" />
          <!-- 时间选择 -->
          <a-time-picker v-model:value="value5" />
          <a-time-range-picker />
          <!-- 上传 -->
          <upLoad></upLoad>
          <!-- 分页 -->
          <a-pagination
            v-model:current="current1"
            show-quick-jumper
            :total="500"
            :show-total="total => `共 ${total} 条`" />
          <!-- 标签页 -->
          <a-tabs v-model:activeKey="activeKey">
            <a-tab-pane key="1" tab="标签一">Content of Tab Pane 1</a-tab-pane>
            <a-tab-pane key="2" tab="标签二" force-render>
              Content of Tab Pane 2
            </a-tab-pane>
            <a-tab-pane key="3" tab="标签三">Content of Tab Pane 3</a-tab-pane>
          </a-tabs>
          <a-tabs v-model:activeKey="activeKey" type="card">
            <a-tab-pane key="1" tab="Tab 1">选择一</a-tab-pane>
            <a-tab-pane key="2" tab="Tab 2">选择二</a-tab-pane>
            <a-tab-pane key="3" tab="Tab 3">选择三</a-tab-pane>
          </a-tabs>
          <a-tabs v-model:activeKey="activeKey" tab-position="left" animated>
            <a-tab-pane key="1" tab="Tab 1">选择类型1</a-tab-pane>
            <a-tab-pane key="2" tab="Tab 2">选择类型2</a-tab-pane>
            <a-tab-pane key="3" tab="Tab 3">选择类型3</a-tab-pane>
          </a-tabs>
          <!-- 分段控制器 -->
          <a-segmented v-model:value="value6" :options="data" size="large" />
          <a-segmented v-model:value="value6" :options="data" />
          <!-- 消息提示 -->
          <a-button @click="info">Info</a-button>
          <a-button @click="success">Success</a-button>
          <a-button @click="error">Error</a-button>
          <a-button @click="warning">Warning</a-button>
          <a-button @click="loadingTip">进行提示</a-button>

          <!-- 通知提醒框 -->
          <a-button @click="() => openNotificationWithIcon('info')">
            Info
          </a-button>
          <a-button @click="() => openNotificationWithIcon('success')">
            Success
          </a-button>
          <a-button @click="() => openNotificationWithIcon('warning')">
            Warning
          </a-button>
          <a-button @click="() => openNotificationWithIcon('error')">
            Error
          </a-button>
          <!-- 对话框 -->
          <a-button type="primary" @click="showModal">Modal</a-button>
          <a-modal
            v-model:open="open"
            title="Modal"
            ok-text="确认"
            cancel-text="取消"
            @ok="hideModal">
            <p>Bla bla ...</p>
            <p>Bla bla ...</p>
            <p>Bla bla ...</p>
          </a-modal>
          <a-button @click="confirm">Confirm</a-button>
        </a-space>
        <a-alert message="Informational Notes" type="info" show-icon />
      </a-col>
    </a-row>
  </a-card>
</template>
<script setup lang="ts">
  // 测试基础组件样式
  import { h, ref, reactive, createVNode } from 'vue';
  import type { MenuProps } from 'ant-design-vue';
  import {
    SearchOutlined,
    DownOutlined,
    // DeleteOutlined,
  } from '@ant-design/icons-vue';
  import type { CascaderProps } from 'ant-design-vue';
  import { message } from 'ant-design-vue';
  import {
    ExclamationCircleOutlined,
  } from '@ant-design/icons-vue';
  import { notification } from 'ant-design-vue';
  import { Modal } from 'ant-design-vue';
  import upLoad from './upLoad.vue'
  const handleMenuClick: MenuProps['onClick'] = e => {
    console.log('click', e);
  };
  const value = ref<string>('');
  const checked = ref(false);
  const checked1 = ref<boolean>(false);
  const value1 = ref(undefined);
  const value2 = ref(undefined);
  const value3 = ref<string[]>([]); //级联
  const value4 = ref(); //日期选择
  const value5 = ref(''); //时间选择
  const maxTagCount = ref(1); //select多选限数
  const current1 = ref<number>(1); //分页当前页
  const activeKey = ref('1'); //标签页当前页
  const data = reactive(['Daily', 'Weekly', 'Monthly', 'Quarterly', 'Yearly']); //分段选择器
  const value6 = ref(data[0]);
  const open = ref<boolean>(false); //对话框
  const showModal = () => {
    open.value = true;
  };
  const hideModal = () => {
    open.value = false;
  };
  const info = () => {
    message.info('这是一条普通提示');
  };
  const success = () => {
    message.success('这是一条成功提示');
  };
  const error = () => {
    message.error('这是一条错误提示');
  };
  const warning = () => {
    message.warning('这是一条警示提示');
  };
  const loadingTip = () => {
    const hide = message.loading('这是一条上传/进行中提示', 0);
    setTimeout(hide, 2500);
  };
  const openNotificationWithIcon = (type: string) => {
    notification[type]({
      message: '提示标题',
      description:
        '普通内容普通内容普通内容普通内容普通内容普通内容普通内容普通内容普通内容普通内容普通内容普通内容',
      duration: 0,
      // icon: ()=> h(DeleteOutlined, { style: 'color : #108ee9'}),
    });
  };
  const filter = (inputValue, path) => {
    return path.some(
      option =>
        option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1,
    );
  };
  const options: CascaderProps['options'] = [
    {
      value: 'zhejiang',
      label: 'Zhejiang',
      children: [
        {
          value: 'hangzhou',
          label: 'Hangzhou',
          children: [
            {
              value: 'xihu',
              label: 'West Lake',
            },
            {
              value: 'xihu2',
              label: 'West Lake2',
            },
          ],
        },
      ],
    },
    {
      value: 'jiangsu',
      label: 'Jiangsu',
      children: [
        {
          value: 'nanjing',
          label: 'Nanjing',
          children: [
            {
              value: 'zhonghuamen',
              label: 'Zhong Hua Men',
            },
          ],
        },
      ],
    },
  ];

  // 对话框
  const confirm = () => {
    Modal.confirm({
      title: '删除确认',
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: '您确认要删除这条数据?',
      okText: '确认',
      cancelText: '取消',
    });
  };
</script>
<style lang="less" scoped>
</style>
