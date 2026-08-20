# Form 组件

表单组件，组件内置很多方法以及参数，比如联动，接口请求以及时间的处理等等。

## 基础用法

```vue
<template>
  <BMForm ref="myFormRef" v-bind="formProps" @submit="handleSubmit" @reset="handleSet">
    <template #selectA="{ formModel, field }">
      <Select
        v-model:value="formModel[field]"
        :options="optionsA"
        mode="multiple"
        allow-clear
        @change="valueSelectA = formModel[field]" />
    </template>
    <template #selectB="{ formModel, field }">
      <Select
        v-model:value="formModel[field]"
        :options="optionsB"
        mode="multiple"
        allow-clear
        @change="valueSelectB = formModel[field]" />
    </template>
    <template #resetBefore>
      <Button type="primary" shape="round" @click="returnClick">自定义</Button>
    </template>
  </BMForm>
</template>

<script lang="tsx" setup>
  import { UploadOutlined } from '@ant-design/icons-vue';
  import { cloneDeep } from '@bmos/utils';
  import { Button, Select, message } from 'ant-design-vue';
  import { computed, ref, unref } from 'vue';
  import { type FormInstance, type FormProps, type FormSchema, BMForm } from '@bmos/components';

  defineOptions({
    name: 'DemosFormBasicForm',
  });

  const provincesOptions = [
    {
      id: 'guangdong',
      label: '广东省',
      value: '1',
      key: '1',
    },
    {
      id: 'jiangsu',
      label: '江苏省',
      value: '2',
      key: '2',
    },
  ];
  const citiesOptionsData = {
    guangdong: [
      {
        label: '珠海市',
        value: '1',
        key: '1',
      },
      {
        label: '深圳市',
        value: '2',
        key: '2',
      },
      {
        label: '广州市',
        value: '3',
        key: '3',
      },
    ],
    jiangsu: [
      {
        label: '南京市',
        value: '1',
        key: '1',
      },
      {
        label: '无锡市',
        value: '2',
        key: '2',
      },
      {
        label: '苏州市',
        value: '3',
        key: '3',
      },
    ],
  };

  const fetchOptionList = async () => {
    return new Promise(resolve => {
      setTimeout(() => {
        resolve(
          Array.from({ length: 10 }).map((_, i) => ({
            label: `选项${i}`,
            value: `${i}`,
          })),
        );
      }, 3000);
    });
  };

  const schemas: FormSchema[] = [
    {
      field: 'field1',
      component: 'Input',
      label: '字段字段1字段1字段1字段1字段1字段11',
      componentProps: () => {
        return {
          placeholder: '自定义placeholder',
          onChange: (e: any) => {
            console.log(e);
          },
        };
      },
      componentSlots: () => {
        return {
          prefix: () => '前缀',
          suffix: () => '后缀',
        };
      },
    },
    {
      field: 'field2',
      component: 'Input',
      label: '带后缀',
      defaultValue: '111',
      componentProps: {
        onChange: (e: any) => {
          console.log(e);
        },
      },
      suffix: '天',
    },
    {
      field: 'field3',
      component: 'DatePicker',
      label: '字段3',
    },
    {
      field: 'field4',
      component: 'Select',
      label: '字段4',
      componentProps: {
        options: [
          {
            label: '选项1',
            value: '1',
            key: '1',
          },
          {
            label: '选项2',
            value: '2',
            key: '2',
          },
        ],
      },
    },
    {
      field: 'field5',
      component: 'CheckboxGroup',
      label: '字段5',
      componentProps: {
        options: [
          {
            label: '选项1',
            value: '1',
          },
          {
            label: '选项2',
            value: '2',
          },
        ],
      },
    },
    {
      field: 'field7',
      component: 'RadioGroup',
      label: '字段7',
      componentProps: {
        options: [
          {
            label: '选项1',
            value: '1',
          },
          {
            label: '选项2',
            value: '2',
          },
        ],
      },
    },
    {
      field: 'field8',
      component: 'Checkbox',
      label: '字段8',
      componentSlots: 'Check',
    },
    {
      field: 'field9',
      component: 'Switch',
      label: '字段9',
    },
    {
      field: 'field10',
      component: 'RadioGroup',
      label: '字段10',
      componentProps: {
        options: [
          {
            label: '选项1',
            value: '1',
          },
          {
            label: '选项2',
            value: '2',
          },
        ],
      },
    },
    {
      field: 'field11',
      component: 'Cascader',
      label: '字段11',
      componentProps: {
        options: [
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
        ],
      },
    },
    {
      field: 'field30',
      component: 'Select',
      label: '懒加载远程下拉',
      required: true,
      componentProps: {
        request: async () => {
          return await fetchOptionList();
        },
        onChange: (e: any) => {
          console.log('selected:', e);
        },
      },
      defaultValue: '1',
    },
    {
      field: 'field31',
      component: 'Select',
      label: '下拉本地搜索',
      helpMessage: ['Select组件', '远程数据源本地搜索', '只发起一次请求获取所有选项'],
      componentProps: {
        showSearch: true,
        filterOption: (input: string, option: any) => {
          return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
        },
        request: async () => {
          return await fetchOptionList();
        },
        onChange: (e: any) => {
          console.log('selected:', e);
        },
      },
      required: true,
      defaultValue: '0',
    },
    {
      field: 'field33',
      component: 'TreeSelect',
      label: '远程下拉树',
      helpMessage: ['TreeSelect组件', '使用接口提供的数据生成选项'],
      required: true,
      componentProps: {
        request: () => {
          return new Promise(resolve => {
            const treeData = Array.from({ length: 5 }).map((_, i) => ({
              title: `选项 ${i}`,
              value: `选项 ${i}`,
              children: Array.from({ length: 3 }).map((_, j) => ({
                title: `选项 ${i}-${j}`,
                value: `选项 ${i}-${j}`,
              })),
            }));
            setTimeout(() => resolve(treeData), 2000);
          });
        },
      },
    },
    // {
    //   field: 'field34',
    //   component: ({ schema }: any) => {
    //     const options = schema.componentProps?.requestResult || [];
    //     return <RadioGroup options={options.slice(0, 2)}></RadioGroup>;
    //   },
    //   label: '远程RG',
    //   helpMessage: ['RadioGroup组件', '使用接口提供的数据生成选项'],
    //   required: true,
    //   componentProps: {
    //     optionType: 'button',
    //     request: async () => {
    //       const data = await fetchOptionList();
    //       return data;
    //     },
    //   },
    //   defaultValue: '0',
    // },
    // {
    //   field: 'field35',
    //   component: 'RadioGroup',
    //   label: '远程Radio',
    //   helpMessage: ['RadioGroup组件', '使用接口提供的数据生成选项'],
    //   required: true,
    //   componentProps: {
    //     optionType: 'button',
    //     request: async () => {
    //       const data: any = await fetchOptionList();
    //       return data.slice(0, 2);
    //     },
    //   },
    //   defaultValue: '1',
    // },
    {
      field: 'province',
      component: 'Select',
      label: '省份',
      componentProps: ({ formModel, formInstance }: any) => {
        return {
          options: provincesOptions,
          placeholder: '省份与城市联动',
          onChange: (e: any) => {
            // console.log(e)
            let citiesOptions =
              e == 1
                ? // @ts-expect-error
                  citiesOptionsData[provincesOptions[0].id]
                : // @ts-expect-error
                  citiesOptionsData[provincesOptions[1].id];
            // console.log(citiesOptions)
            if (e === undefined) {
              citiesOptions = [];
            }
            formModel.city = undefined; //  reset city value
            const { updateSchema } = formInstance;
            updateSchema({
              field: 'city',
              componentProps: {
                options: citiesOptions,
              },
            });
          },
        };
      },
    },
    {
      field: 'city',
      component: 'Select',
      label: '城市',
      componentProps: {
        options: [], // defalut []
        placeholder: '省份与城市联动',
      },
    },
    {
      field: 'selectA',
      component: 'Select',
      label: '互斥SelectA',
      slot: 'selectA',
      defaultValue: [],
    },
    {
      field: 'selectB',
      component: 'Select',
      label: '互斥SelectB',
      slot: 'selectB',
      defaultValue: [],
    },
    {
      field: 'field20',
      component: 'InputNumber',
      label: '字段20',
      required: true,
    },
    {
      field: 'field21',
      component: 'Slider',
      label: '字段21',
      componentProps: {
        min: 0,
        max: 100,
        range: true,
        marks: {
          20: '20°C',
          60: '60°C',
        },
      },
    },
    {
      field: 'field22',
      component: 'Rate',
      label: '字段22',
      defaultValue: 3,
      componentProps: {
        disabled: false,
        allowHalf: true,
      },
    },
    {
      field: 'field23',
      component: 'Upload',
      label: '字段23',
      componentProps: {
        action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
      },
      componentSlots: {
        default: () => (
          <Button>
            <UploadOutlined /> Click to Upload
          </Button>
        ),
      },
    },
  ];

  const formProps: FormProps = {
    schemas,
    labelWidth: 120,
    baseColProps: {
      span: 12,
    },
    autoAdvancedLine: 6,
    alwaysShowLines: 6,
    actionColOptions: {
      span: 12,
      style: {
        textAlign: 'center',
      },
    },
    showAdvancedButton: true,
  };

  const valueSelectA = ref<string[]>([]);
  const valueSelectB = ref<string[]>([]);
  const options = ref<any>([]);
  options.value = Array.from({ length: 10 }).map((_, i) => ({
    label: `选项${i}`,
    value: `${i}`,
  }));

  const optionsA = computed(() => {
    // @ts-expect-error
    return cloneDeep(unref(options)).map(op => {
      op.disabled = unref(valueSelectB).indexOf(op.value) !== -1;
      return op;
    });
  });
  const optionsB = computed(() => {
    // @ts-expect-error
    return cloneDeep(unref(options)).map((op: { disabled: boolean; value: string }) => {
      op.disabled = unref(valueSelectA).indexOf(op.value) !== -1;
      return op;
    });
  });

  // 点击提交
  function handleSubmit(values: any) {
    message.success(
      <div>
        验证通过！<pre class='text-left'>{JSON.stringify(values, null, 2)}</pre>
      </div>,
      3,
    );
  }

  const myFormRef = ref<FormInstance>();
  const handleSet = () => {
    console.log('myFormRef', myFormRef.value?.formModel);
  };

  const returnClick = () => {
    console.log('返回');
  };
</script>
```

## API

### Form Props

| 属性                    | 说明                                  | 类型                                                       | 默认值                                                                     |
| ----------------------- | ------------------------------------- | ---------------------------------------------------------- | -------------------------------------------------------------------------- |
| AntdV From              | AntdV From 组件的属性                 | `FormProps`                                                | `{ labelAlign: 'right', layout: 'horizontal', colon: false }`              |
| initialValues           | 预置字段默认值                        | `Object`                                                   | `{}`                                                                       |
| labelWidth              | 标签宽度 固定宽度                     | `[Number, String]`                                         | `100`                                                                      |
| FormSchema              | 表单配置规则                          | `FormSchema[]`                                             | `[]`                                                                       |
| mergeDynamicData        | 默认数据，合并进 formData             | `Object`                                                   | `null`                                                                     |
| baseRowStyle            | 默认 row style                        | `CSSProperties`                                            | `null`                                                                     |
| rowProps                | 栅栏Row配置                           | `RowProps`                                                 | `null`                                                                     |
| baseColProps            | 默认 col style                        | `Partial<ColEx>`                                           | `{ span: 6 }`                                                              |
| autoSubmitOnEnter       | 在INPUT组件上单击回车时，是否自动提交 | `Boolean`                                                  | `true`                                                                     |
| disabled                | 禁用表单                              | `Boolean`                                                  | `false`                                                                    |
| emptySpan               | 表单结束后空 span 占位符              | `Number`                                                   | `0`                                                                        |
| fieldMapToTime          | 处理时间段字段                        | `Object`                                                   | `null`                                                                     |
| transformDateFunc       | 转化时间字段                          | `Function`                                                 | `(date: any) => { return date?.format?.('YYYY-MM-DD HH:mm:ss') ?? date; }` |
| rulesMessageJoinLabel   | 是否把 label 添加进校验信息           | `Boolean`                                                  | `true`                                                                     |
| autoAdvancedLine        | 超过 1 行自动折叠                     | `Number`                                                   | `1`                                                                        |
| alwaysShowLines         | 不受折叠影响的行数                    | `Number`                                                   | `1`                                                                        |
| showActionButtonGroup   | 是否显示操作按钮                      | `Boolean`                                                  | `true`                                                                     |
| actionColOptions        | 操作列Col配置                         | `Partial<ColEx>`                                           | `null`                                                                     |
| showResetButton         | 显示重置按钮                          | `Boolean`                                                  | `true`                                                                     |
| resetButtonOptions      | 重置按钮配置                          | `同确认按钮`                                               | `null`                                                                     |
| showSubmitButton        | 显示确认按钮                          | `Boolean`                                                  | `true`                                                                     |
| submitButtonOptions     | 确认按钮配置                          | `Partial<ButtonProps> & { text?: string; class?: string }` | `null`                                                                     |
| showAdvancedButton      | 是否显示收起展开按钮                  | `Boolean`                                                  | `false`                                                                    |
| resetFunc               | resetFunc                             | `Function`                                                 | `null`                                                                     |
| submitFunc              | 自定义提交函数                        | `Function`                                                 | `null`                                                                     |
| showAdvancedButtonBadge | 是否显示更多后的 徽标数               | `Boolean`                                                  | `false`                                                                    |
| advancedBadgeCount      | 徽标数数字                            | `Number`                                                   | `0`                                                                        |
| useMaxLengthRule        | 是否使用默认的长度校验                | `Boolean`                                                  | `true`                                                                     |
| formKey                 | formKey                               | `Number、String`                                           | `formKey`                                                                  |
| scrollToError           | 是否滑动到错误项                      | `Boolean`                                                  | `false`                                                                    |

### FormSchema 参数

| 属性                   | 说明                                                                                         | 类型                                                                                               | 默认值   |
| ---------------------- | -------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------- | -------- |
| field                  | 字段名                                                                                       | string                                                                                             | -        |
| changeEvent            | 字段值改变触发方法                                                                           | string                                                                                             | `change` |
| label                  | 标签名                                                                                       | string                                                                                             | -        |
| helpMessage            | 帮助信息                                                                                     | string                                                                                             | -        |
| helpComponentProps     | 帮助信息组件配置                                                                             | `Partial<TooltipProps>`                                                                            | -        |
| labelWidth             | 标签宽度                                                                                     | string                                                                                             | -        |
| componentProps         | 组件属性配置                                                                                 | `Partial<ComponentProps> \| (opt: RenderCallbackParams<T>): ComponentProps`                        | -        |
| componentSlots         | 组件插槽                                                                                     | `(opt: RenderCallbackParams<T>) => Recordable<CustomRenderFn<T>> \| Recordable<CustomRenderFn<T>>` | -        |
| required               | 是否必填                                                                                     | `boolean \| ((renderCallbackParams: RenderCallbackParams<T>) => boolean)`                          | false    |
| rules                  | 校验规则                                                                                     | `Rule[]`                                                                                           | -        |
| rulesMessageJoinLabel  | 是否把 label 添加进校验信息                                                                  | `boolean`                                                                                          | `true`   |
| loading                | 是否加载中                                                                                   | `boolean`                                                                                          | false    |
| formItemProps          | Form.Item 属性                                                                               | `Partial<FormItemProps>`                                                                           | -        |
| colProps               | Col 属性                                                                                     | `Partial<ColProps>`                                                                                | -        |
| order                  | 排序                                                                                         | `number`                                                                                           | -        |
| defaultValue           | 默认值                                                                                       | `any`                                                                                              | -        |
| isAdvanced             | 是否显示展开收起按钮                                                                         | `boolean`                                                                                          | true     |
| labelFullWidth         | 标签是否占满整行                                                                             | `boolean`                                                                                          | false    |
| span                   | 栅格占位格数                                                                                 | `number`                                                                                           | -        |
| vShow                  | 是否显示(作用同v-show)                                                                       | `boolean \| ((renderCallbackParams: RenderCallbackParams<T>) => boolean)`                          | -        |
| VIf                    | 是否显示(作用同v-if)                                                                         | `boolean \| ((renderCallbackParams: RenderCallbackParams<T>) => boolean)`                          | -        |
| renderColContent       | 自定义渲染 col 内容                                                                          | `CustomRenderFn<T>`                                                                                | -        |
| noFormItemMarginBottom | 是否去掉 FormItem 的 margin-bottom                                                           | `boolean`                                                                                          | `false`  |
| noLabelTip             | 是否去掉 label 的 tooltip                                                                    | `boolean`                                                                                          | `false`  |
| slot                   | 自定义渲染slot, 如 slot为 name, 在form的template标签中加入插槽 name ，就可以显示为这项的内容 | `string`                                                                                           | -        |
| colSlot                | 自定义渲染col slot                                                                           | `string`                                                                                           | -        |
| noLabel                | 是否不显示label                                                                              | `boolean`                                                                                          | `false`  |
| dynamicDisabled        | 动态禁用                                                                                     | `boolean \| ((renderCallbackParams: RenderCallbackParams<T>) => boolean)`                          | -        |
| useMaxLengthRule       | 是否使用默认的长度校验（input 100 字符， textarea 200 字符）                                 | `boolean`                                                                                          | `true`   |
| dynamicRules           | 动态校验规则                                                                                 | `Rule[] \| ((renderCallbackParams: RenderCallbackParams<T>) => RuleItem[])`                        | -        |

### FormProps

| 属性              | 说明                     | 类型            | 默认值 |
| ----------------- | ------------------------ | --------------- | ------ |
| AntV 表单项的属性 | 都支持                   | -               | -      |
| request           | 组件异步请求数据         | `RequestConfig` | -      |
| requestResult     | 请求结果                 | `any`           | -      |
| style             | 组件样式                 | `CSSProperties` | -      |
| vModelKey         | 手动指定v-model绑定的key | `string`        | -      |

```ts
// RequestConfig 类型
/** 组件异步请求配置 */
type RequestConfig =
  | PromiseFn<RenderCallbackParams, any>
  | {
      /** 指定监听的字段名, 当该字段值发生变化时会调用 callback */
      watchFields: string[];
      callback: PromiseFn<RenderCallbackParams, any>;
      options?: WatchOptions;
      /** debounce 请求防抖 */
      wait?: number;
    };
```

### Events

| 事件名          | 说明             | 回调参数 |
| --------------- | ---------------- | -------- |
| register        | 组件注册触发     | -        |
| reset           | 重置触发         | 表单的值 |
| submit          | 提交触发         | 表单的值 |
| advancedChange  | 展开收起变化触发 | -        |
| formModelChange | 值发生改变触发   | 表单的值 |

### Methods

| 方法名               | 说明                                                                                       | 参数                                                                     | 返回值          |
| -------------------- | ------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------ | --------------- |
| setFormModel         | 设置某个字段的值                                                                           | key 字段, value 值                                                       | -               |
| setFormModels        | 设置formModel的值                                                                          | values 表单数据                                                          | -               |
| replaceFormModels    | 替换formModel的值                                                                          | values 表单数据                                                          | -               |
| getFormModelByField  | 获取某个字段的值                                                                           | key 字段                                                                 | value           |
| delFormModel         | 删除某个字段的值                                                                           | key 字段                                                                 | `boolean`       |
| setFormProps         | 设置表单配置                                                                               | `Partial<FormProps>`                                                     | -               |
| handleFormValues     | 处理表单数据                                                                               | values 表单数据                                                          | values 表单数据 |
| handleRangeTimeValue | 处理时间范围                                                                               | values 表单数据                                                          | values 表单数据 |
| getFormValues        | 获取表单数据                                                                               | -                                                                        | values 表单数据 |
| setFieldsValue       | 设置字段的值                                                                               | values 表单数据                                                          | -               |
| resetSchema          | 重置表单                                                                                   | values 表单数据                                                          | -               |
| removeAllSchema      | 删除所有的 Schema                                                                          | `{string、string[]} fields 排除的字段`                                   | -               |
| appendSchemaByField  | 插入到指定 filed 后面，如果没传指定 field，则插入到最后,当 first = true 时插入到第一个位置 | schemaItem, prefixField ,first                                           | -               |
| removeSchemaByFiled  | 根据 field 删除 Schema                                                                     | `{string、string[]} fields, isInclude 是否是field包含的字符串 默认false` | `boolean`       |
| getSchemaByFiled     | 根据 field 查找 Schema                                                                     | `{string、string[]} fields`                                              | -               |
| updateSchema         | 更新formItemSchema                                                                         | `{Partial<FormSchema> ,Partial<FormSchema>[]} data`                      | -               |
| resetForm            | 重置表单                                                                                   | -                                                                        | -               |
| scrollToField        | 滚动到指定表单项                                                                           | name 表单项的name , options 滚动配置                                     | -               |
| validateFields       | 校验表单                                                                                   | nameList 校验指定的表单项                                                | -               |
| validate             | 校验整个表单                                                                               | -                                                                        | values 表单数据 |
| clearValidate        | 清除校验                                                                                   | nameList 校验指定的表单项                                                | -               |
| handleSubmit         | 提交表单                                                                                   | -                                                                        | `Promise`       |
| handleEnterPress     | 监听键盘事件                                                                               | `e 事件 KeyboardEvent`                                                   | -               |

### 插槽

支持自定义渲染的插槽

| 名称          | 说明                 |
| ------------- | -------------------- |
| formHeader    | 表头                 |
| formFooter    | 表尾                 |
| default       | 传入之后以传入的为主 |
| advanceBefore | 展开图标之前         |
| resetBefore   | 重置按钮之前         |
| submitBefore  | 提交按钮之前         |
| submitAfter   | 提交按钮之后         |
