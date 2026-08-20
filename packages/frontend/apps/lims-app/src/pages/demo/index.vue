<template>
  <BMBasicPage
    title="标题文字"
    @left-click="leftClick"
    @right-click="rightClick"
    @cancel="cancelPage"
    @confirm="confirmPage"
  >
    <template #titleRight>
      <view>右侧内容</view>
    </template>
    <h1>Icon</h1>
    <view
      style="height: 100px;
      border:1px solid #000;
    display: flex;
    align-items: center;
    justify-content: center;"
    >
      <BMIcon name="lurukongzhi" size="24.07rpx" color="#2871FF" />
      <BMIcon name="lurukongzhi" size="24.07rpx" />
    </view>
    <text>
      阿里巴巴图标库增加图标后需修改修改两个地方：å
      1、执行npm run update:iconfont命令
      2、修改src/static/iconfont/newIconfont.css文件
    </text>

    <view style="display: flex;justify-content: center; ">
      <view style="width: 500px;">
        <BMDataSelect
          v-model="dataSelectValue"
          :disabled="false"
          :options="signOptions"
          label="签名"
          :field-names="{
            value: 'value',
            label: 'text',
            id: 'userId',
          }"
        />
      </view>
    </view>
    <h1>签名</h1>
    <view style="background-color: white;padding: 10px;">
      <BMSign
        v-model="signValue"
        :field-names="{
          value: 'value',
          label: 'text',
          id: 'userId',
        }"
        :label-list="[
          {
            label: '撒上',
            // 签名动作
            signatureAction: 0,
            options: null,
          },
          {
            label: '大大',
            // 签名动作
            signatureAction: 0,
            options: [],
          },
        ]"
      />
    </view>
    <h1>DatePicker: 时间选择</h1>
    <BMFormDatePicker v-model="datePickerValue" format-date="yyyy-MM-dd" />
    <BMFormDatePicker v-model="datePickerValue" disabled />
    <h1>RangePicker: 时间范围选择</h1>
    <BMFormRangePicker v-model="rangePickerValue" />
    <BMFormRangePicker v-model="rangePickerValue" disabled />
    <h1>扫描组件：</h1>
    <BMScan
      v-model="value"
      type="input"
      @success="onScanSuccess"
      @fail="onScanFail"
      @complete="onScanComplete"
      @select="onScanSelect"
      @confirm="onScanConfirm"
    />
    <br>
    <BMScan
      v-model="value"
      type="select"
      @success="onScanSuccess"
      @fail="onScanFail"
      @complete="onScanComplete"
      @select="onScanSelect"
      @confirm="onScanConfirm"
    />
    <h1>输入框：</h1>
    <wd-row :gutter="20">
      <wd-col :span="12">
        <wd-input v-model="value" readonly type="text" placeholder="请输入" />
      </wd-col>
      <wd-col :span="12">
        <wd-input v-model="value" type="text" placeholder="请输入" disabled />
      </wd-col>
    </wd-row>
    <br>
    <wd-row :gutter="20">
      <wd-col :span="12">
        <wd-input
          v-model="value"
          type="text"
          placeholder="请输入"
          suffix-icon="fangjiannji"
        />
      </wd-col>
      <wd-col :span="12">
        <wd-input
          v-model="value"
          type="text"
          placeholder="请输入"
          disabled
          suffix-icon="scan"
        />
      </wd-col>
    </wd-row>
    <br>
    <wd-row :gutter="20">
      <wd-col :span="12">
        <wd-input v-model="value" show-password />
      </wd-col>
      <wd-col :span="12">
        <wd-input v-model="value" show-password disabled />
      </wd-col>
    </wd-row>
    <br>
    <wd-row :gutter="20">
      <wd-col :span="12">
        <wd-input
          v-model="value"
          type="text"
          placeholder="请输入"
          use-suffix-slot
        >
          <template #suffix>
            <view class="input-text-icon">
              <wd-button type="text">
                操作按钮
              </wd-button>
            </view>
          </template>
        </wd-input>
      </wd-col>
      <wd-col :span="12">
        <wd-input
          v-model="value"
          type="text"
          placeholder="请输入"
          use-suffix-slot
          disabled
        >
          <template #suffix>
            <view class="input-text-icon">
              <wd-button type="text" disabled>
                操作按钮
              </wd-button>
            </view>
          </template>
        </wd-input>
      </wd-col>
    </wd-row>
    <br>
    <h1>文本域：</h1>
    <wd-row :gutter="20">
      <wd-col :span="12">
        <wd-textarea v-model="value" placeholder="请填写评价" />
      </wd-col>
      <wd-col :span="12">
        <wd-textarea v-model="value" placeholder="请填写评价" disabled />
      </wd-col>
    </wd-row>
    <br>
    <br>
    <h1>选择器：</h1>
    <wd-row :gutter="20">
      <wd-col :span="12">
        <BMFormSelect
          v-model="selectValue"
          title="多选弹窗"
          :options="checkboxModalOptions"
          :field-names="{
            label: 'label',
            value: 'key',
            subLabel: 'subLabel',
          }"
          @select="onSelect"
          @confirm="onConfirm"
        />
      </wd-col>
      <wd-col :span="12">
        <BMFormSelect v-model="selectValue" disabled />
      </wd-col>
      <br>
      <wd-col :span="12">
        <BMFormSelect v-model="value">
          <template #right>
            <wd-icon
              name="saomiao"
              size="14.06rpx"
              color="#434C59"
              class-prefix="bmos-app-icon"
              @click.stop="select1"
            />
          </template>
        </BMFormSelect>
      </wd-col>
      <wd-col :span="12">
        <BMFormSelect v-model="value">
          <template #right>
            <view @click.stop="select1">
              <wd-button type="text">
                文字按钮
              </wd-button>
            </view>
          </template>
        </BMFormSelect>
      </wd-col>
    </wd-row>
    <br>
    <h1>Form</h1>
    <BMForm ref="formRef" v-bind="formProps" />
    <wd-button size="small" @click="submit">
      提交
    </wd-button>
    <br>
    <br>
    <h1>Table</h1>
    <view
      :style="{
        height: '500px',
      }"
    >
      <BMTable
        ref="tableRef"
        v-bind="tableProps"
        @selection-change="selectionChange"
      />
    </view>
    <br>
    <h1>信息展示</h1>
    <BMInfoDisplay
      title="标题标题"
      :basic-items="[
        {
          label: '设备编号',
          field: 'equipmentNo',
        },
        {
          label: '设备标签',
          field: 'equipmentLabels',
          tag: 'success',
        },
        {
          label: '所在地点',
          field: 'place',
        },
        {
          label: '设备规格',
          field: 'specifications',
        },
        {
          label: '设备地点',
          field: 'equipmentLocation',
        },
        {
          label: '规格型号',
          field: 'specificationModel',
        },
      ]"
      :info-data="{
        equipmentNo: '0801001',
        equipmentLabels:
          '很长的文字也可能有英文哦 A generation ago it was rare to hear the Koran recited',
        place: '称量间',
        specifications: '这是一个规格',
        equipmentLocation: '设备的好地点',
        specificationModel: '规格型号是多少',
      }"
    />
    <h1>数据信息展示</h1>
    <BMDataInfoDisplay
      title="标题标题"
      :basic-items="[
        {
          label: '物料代码999',
          field: 'materialCode',
        },
        {
          label: '理论用量',
          field: 'theoreticalDosage',
        },
        {
          label: '已预定暂存量',
          field: 'bookedQuantity',
        },
      ]"
      :info-data="{
        materialCode: {
          value: 'YH101001',
        },
        theoreticalDosage: {
          value: '8888.0000kg',
          waring: true,
        },
        bookedQuantity: {
          value: '8888.0000kg',
          waring: false,
        },
      }"
    />

    <h1>按钮：</h1>
    L:
    <wd-row :gutter="20">
      <wd-col :span="4">
        <wd-button size="large">
          主要按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="success" size="large">
          次要按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="info" size="large">
          信息按钮
        </wd-button>
      </wd-col>
      <wd-col :span="8">
        <wd-button type="info" size="large" icon="picture">
          图标按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="warning" size="large">
          警示按钮
        </wd-button>
      </wd-col>
    </wd-row>
    M:
    <wd-row :gutter="20">
      <wd-col :span="4">
        <wd-button size="medium">
          主要按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="success" size="medium">
          信息按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="info" size="medium">
          信息按钮
        </wd-button>
      </wd-col>
      <wd-col :span="8">
        <wd-button type="info" size="medium" icon="picture">
          图标按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="warning" size="medium">
          警示按钮
        </wd-button>
      </wd-col>
    </wd-row>
    S:
    <wd-row :gutter="20">
      <wd-col :span="4">
        <wd-button size="small">
          主要按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="success" size="small">
          信息按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="info" size="small">
          信息按钮
        </wd-button>
      </wd-col>
      <wd-col :span="8">
        <wd-button type="info" size="small" icon="picture">
          图标按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="warning" size="small">
          警示按钮
        </wd-button>
      </wd-col>
    </wd-row>
    Disabled:
    <wd-row :gutter="20">
      <wd-col :span="4">
        <wd-button size="small" disabled>
          主要按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="success" size="small" disabled>
          信息按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="info" size="small" disabled>
          信息按钮
        </wd-button>
      </wd-col>
      <wd-col :span="8">
        <wd-button type="info" size="small" disabled icon="picture">
          图标按钮
        </wd-button>
      </wd-col>
      <wd-col :span="4">
        <wd-button type="warning" size="small" disabled>
          警示按钮
        </wd-button>
      </wd-col>
    </wd-row>
    <h1>文字按钮：</h1>
    <wd-button type="text">
      文字按钮
    </wd-button>
    <wd-button type="text" disabled>
      文字按钮
    </wd-button>
    <wd-notify :safe-height="90" />
    <h1>Notify 消息通知：</h1>
    <wd-button
      @click="
        showNotify({
          type: 'primary',
          message:
            '通知内容是独立访客就少得可怜附件是的了咖啡就是圣诞快乐房价是的艾弗森看到了附件撒大苏打发撒多了几分收到啦开发就是大',
        })
      "
    >
      primary
    </wd-button>
    <wd-button @click="showNotify({ type: 'success', message: '通知内容' })">
      success
    </wd-button>
    <wd-button @click="showNotify({ type: 'danger', message: '通知内容' })">
      danger
    </wd-button>
    <wd-button @click="showNotify({ type: 'warning', message: '通知内容' })">
      warning
    </wd-button>
    <br>
    <h1>TabBar:</h1>
    <BMTabBar :total="101" />
    <h1>分页</h1>
    <wd-pagination v-model="value1" :total="110" @change="handleChange" />
    <h1>tag标签</h1>
    <wd-tag custom-class="smallTag" type="default">
      小标签项
    </wd-tag>&nbsp;
    <wd-tag custom-class="smallTag" type="primary">
      小标签项
    </wd-tag>&nbsp;
    <wd-tag custom-class="smallTag" type="danger">
      小标签项jjjjjjsdkjfhsdjkf的刷卡积分函数达克警方海上大风
    </wd-tag>&nbsp; <wd-tag type="success">
      大标签项
    </wd-tag>&nbsp;
    <wd-tag type="warning">
      标签项jjjjjjsdkjfhsdjkf的刷卡积分函数达克警方海上大风
    </wd-tag>&nbsp;
    <br>
    <br>
    <wd-tag custom-class="smallTag" type="default" plain>
      小标签项
    </wd-tag>&nbsp;
    <wd-tag custom-class="smallTag" type="primary" plain>
      小标签项
    </wd-tag>&nbsp;
    <wd-tag custom-class="smallTag" type="danger" plain>
      标签项jjjjjjsdkjfhsdjkf的刷卡积分函数达克警方海上大风
    </wd-tag>&nbsp; <wd-tag type="success" plain>
      大标签项
    </wd-tag>&nbsp;
    <wd-tag type="warning" plain>
      标签项jjjjjjsdkjfhsdjkf的刷卡积分函数达克警方海上大风
    </wd-tag>&nbsp;
    <h1>侧边栏:</h1>
    <view style="height: 292.97rpx">
      <wd-sidebar v-model="active">
        <wd-sidebar-item custom-class="categoryItems" disabled label="分类项1">
          <template #icon>
            <wd-icon
              class-prefix="bmos-app-icon"
              name="gongxu2"
              size="14.07rpx"
              color="#B6B9BF"
            />
          </template>
        </wd-sidebar-item>
        <wd-sidebar-item
          :value="0"
          label="通知内容士大夫看的撒发射点JFK老师的的说法来解释的啊"
        />
        <wd-sidebar-item :value="1" label="通知内容士大夫士大夫士大夫" />
        <wd-sidebar-item :value="2" label="标签名称" />
        <wd-sidebar-item
          custom-class="categoryItems"
          disabled
          label="分类项2绝对是个可是大家发撒经营活动"
        >
          <template #icon>
            <wd-icon
              class-prefix="bmos-app-icon"
              name="gongxu2"
              size="14.07rpx"
              color="#B6B9BF"
            />
          </template>
        </wd-sidebar-item>
        <wd-sidebar-item :value="3" label="标签名称2" />
      </wd-sidebar>
    </view>
    <h1>分段器</h1>
    <wd-segmented
      v-model:value="currentSegmented"
      :options="[
        {
          value: '评论',
        },
        {
          value: '点赞',
        },
        {
          value: 'Правила нумерации производственных партий',
        },
        {
          value: '打赏',
        },
      ]"
    />
    <h1>tab标签页</h1>
    <wd-tabs>
      <wd-tab title="选项三" />
      <wd-tab title="Правила нумерации производственных партий" />
      <wd-tab title="选项一" />
    </wd-tabs>
    <h1>单选</h1>
    <wd-radio-group v-model="value1">
      <wd-radio :value="1">
        单选框1
      </wd-radio>
      <wd-radio :value="2">
        单选框2
      </wd-radio>
    </wd-radio-group>
    <h1>按钮式单选</h1>
    <wd-radio-group v-model="value1" shape="button">
      <wd-radio :value="1">
        沃特
      </wd-radio>
      <wd-radio :value="2">
        商家后台
      </wd-radio>
    </wd-radio-group>
    <wd-radio-group v-model="value1" shape="dot">
      <wd-radio :value="1">
        沃特
      </wd-radio>
      <wd-radio :value="2">
        商家后台
      </wd-radio>
    </wd-radio-group>
    <h1>复选框</h1>
    <wd-checkbox-group v-model="checkboxValue" inline>
      <wd-checkbox model-value="jingmai">
        沃特
      </wd-checkbox>
      <wd-checkbox model-value="shop">
        商家后台
      </wd-checkbox>
      <wd-checkbox model-value="shop2" disabled>
        禁用
      </wd-checkbox>
    </wd-checkbox-group>
    <h1>搜索框</h1>
    <BMInputSearch
      v-model="searchValue"
      placeholder="请输入"
      @search="search"
    />
    <h1>开关</h1>
    <wd-switch v-model="switchChecked" active-color="#13ce66" />
    <wd-switch v-model="switchChecked" active-color="#13ce66" disabled />
    <h1>计数器</h1>
    <wd-input-number v-model="value1" />
    <h1>新iconfont</h1>
    <wd-icon
      class-prefix="bmos-app-icon"
      name="gongxu"
      size="14.07rpx"
      color="#2871FF"
    />
    <h1>步骤条</h1>
    <wd-steps :active="1" align-center>
      <wd-step
        title="Правила нумерации производственных партий"
        status="finished"
      />
      <wd-step title="步骤2" status="process" />
      <wd-step title="步骤3" />
      <wd-step title="步骤4" />
    </wd-steps>
    <h1>Modal 弹窗：</h1>
    <wd-button @click="show1 = true">
      小号弹框
    </wd-button>
    <wd-button @click="show2 = true">
      中号弹框
    </wd-button>
    <wd-button @click="show3 = true">
      大号弹框
    </wd-button>
    <wd-button @click="show4 = true">
      超大号弹框
    </wd-button>
    <BMModal
      v-model="show1"
      title="标题文字（小）"
      size="small"
      position="right"
      closable
      :close-on-click-modal="false"
    >
      <view style="width: 100px; height: 100%; background-color: aqua">
        内容
      </view>
    </BMModal>
    <BMModal v-model="show2" title="标题文字（中）" size="medium">
      <view>内容</view>
    </BMModal>
    <BMModal v-model="show3" title="标题文字（大）" size="large">
      <view>内容</view>
    </BMModal>
    <BMModal v-model="show4" title="标题文字（超大）" size="xLarge" closable>
      <view>内容</view>
    </BMModal>
    <h1>MessageBox 弹窗通知：</h1>
    <wd-button @click="showMessageBox = true">
      打开弹窗通知
    </wd-button>
    <BMMessageBox
      v-model="showMessageBox"
      title="超长的dfaslkfj谁打裂缝吉fdsfljsdlfjsdl就圣诞快乐房价是大量粉丝d萨大卡拉放进塑料袋放进阿斯顿title"
      content="这是内容这是内容这是fdglfdk内容内容,这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdg这是内容这是内容这是fdglfdk内容内容这是内容这这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容是内容这是fdglfdk内容内容这是内容这是内容这是fdglfdk内容内容lfdk内容内容这是内容这是内容这是fdglfdk内容内容容这是内容这是fdglfdk内容内容"
      :show-cancel-button="false"
    />

    <h1>RadioModal 单选弹窗：</h1>
    <wd-button @click="showRadioModal1 = true">
      打开单选弹窗
    </wd-button>
    <wd-button @click="showRadioModal2 = true">
      打开单选弹窗（带subtext）
    </wd-button>
    <BMRadioModal
      v-model="radioModalValue"
      v-model:open="showRadioModal1"
      title="单选弹窗"
      :options="radioModalOptions"
      :field-names="{
        label: 'label',
        value: 'key',
      }"
    />
    <BMRadioModal
      v-model="radioModalValue"
      v-model:open="showRadioModal2"
      title="单选弹窗2"
      :options="radioModalOptions"
      :field-names="{
        label: 'label',
        value: 'key',
      }"
      ,
      :sub-labels="[
        {
          key: 'time',
          label: '时间',
        },
        {
          key: 'batch',
          label: '批号',
        },
        {
          key: 'batch',
          label: '批号',
        },
        {
          key: 'batch',
          label: '批号',
        },
        {
          key: 'batch',
          label: '批号',
        },
      ]"
    />
    <h1>CheckBoxModal 多选弹窗：</h1>
    <wd-button @click="showCheckboxModal = true">
      打开多选弹窗
    </wd-button>
    <BMCheckboxModal
      v-model="checkBoxModalValue"
      v-model:open="showCheckboxModal"
      title="多选弹窗"
      :options="checkboxModalOptions"
      :field-names="{
        label: 'label',
        value: 'key',
        subLabel: 'subLabel',
      }"
    />
    <h1>TreeModal 树弹框：</h1>
    <wd-button @click="showTreeModal = true">
      打开树弹框
    </wd-button>
    <BMTreeModal
      v-model="treeModalValue"
      v-model:open="showTreeModal"
      title="树弹框111"
      :tree-data="treeModalData"
      :field-names="{
        name: 'showName',
        key: 'id',
        checkKey: 'categoryFlag',
        parentId: 'parentId',
        children: 'children',
      }"
      mode="multiple"
    />
  </BMBasicPage>
</template>

<script setup>
import {
  BMBasicPage,
  BMCheckboxModal,
  BMDataInfoDisplay,
  BMDataSelect,
  BMForm,
  BMFormDatePicker,
  BMFormRangePicker,
  BMFormSelect,
  BMIcon,
  BMInfoDisplay,
  BMInputSearch,
  BMMessageBox,
  BMModal,
  BMRadioModal,
  BMScan,
  BMSign,
  BMTabBar,
  BMTable,
  BMTreeModal,
} from '@/BMComponents';

import { ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import { useForm } from './hooks/form.jsx';
import { useTable } from './hooks/table.jsx';

const { formRef, formProps, submit } = useForm();
const { tableRef, tableProps, selectionChange } = useTable();
const { showNotify } = useNotify();
const value = ref('');
const value1 = ref(1);
const searchValue = ref('');
const checkboxValue = ref(['jingmai']);
const currentSegmented = ref('评论');
const switchChecked = ref(true);
const show1 = ref(false);
const show2 = ref(false);
const show3 = ref(false);
const show4 = ref(false);
const showMessageBox = ref(false);
const showRadioModal1 = ref(false);
const showRadioModal2 = ref(false);
const radioModalValue = ref('3');
const radioModalOptions = [
  { label: '选项1', key: '1', time: '2024-06-30', batch: '12154565656' },
  { label: '选项2', key: '2' },
  { label: '选项3', key: '3' },
  { label: '选项4', key: '4' },
  { label: '选项5', key: '5' },
  { label: '选项6', key: '6' },
  { label: '选项7', key: '7' },
  { label: '选项8', key: '8' },
  { label: '选项9', key: '9' },
  { label: '选项10', key: '10' },
  { label: '选项11', key: '11' },
  { label: '选项12', key: '12' },
  { label: '选项13', key: '13' },
  { label: '选项14', key: '14' },
  { label: '选项15', key: '15' },
  { label: '选项16', key: '16' },
  { label: '选项17', key: '17' },
  { label: '选项18', key: '18' },
  { label: '选项19', key: '19' },
  { label: '选项20', key: '20' },
];
const showCheckboxModal = ref(false);
const checkboxModalOptions = [
  {
    label: '选项1',
    key: '1',
    subLabel:
        '子标签的法律港口附近开了广泛大概费德勒告诉对方理发店给1子标签的法律港口附近开了广泛大概费德勒告诉对方理发店给1子标签的法律港口附近开了广泛大概费德勒告诉对方理发店给1',
  },
  { label: '选项2', key: '2', subLabel: '子标签2' },
  { label: '选项3', key: '3', subLabel: '子标签3' },
  { label: '选项4', key: '4', subLabel: '子标签4' },
  { label: '选项5', key: '5', subLabel: '子标签5' },
  { label: '选项6', key: '6' },
  { label: '选项7', key: '7' },
  { label: '选项8', key: '8' },
  { label: '选项9', key: '9' },
  { label: '选项10', key: '10' },
  { label: '选项11', key: '11' },
  { label: '选项12', key: '12' },
  { label: '选项13', key: '13' },
  { label: '选项14', key: '14' },
  { label: '选项15', key: '15' },
  { label: '选项16', key: '16' },
  { label: '选项17', key: '17' },
  { label: '选项18', key: '18' },
  { label: '选项19', key: '19' },
  { label: '选项20', key: '20' },
];

const showTreeModal = ref(false);
const treeModalValue = ref(['1802974919366152192']);
const treeModalData = ref([
  {
    id: '1802974839464660995',
    name: 'T-产品分类',
    categoryFlag: false,
    mergeCode: 'CPFL',
    showName: 'CPFL-T-产品分类',
    parentId: '0',
    productMark: null,
    children: [
      {
        id: '1802974919366152192',
        name: '1231',
        categoryFlag: true,
        mergeCode: 'CPFL123',
        showName:
            'CPFL123-斯柯达发货迪斯科解放和速度就发哈速度快上岛咖啡还是打发1231',
        parentId: '1802974839464660995',
        productMark: null,
        children: [],
        createTime: '2024-06-18 16:01:33',
      },
    ],
    createTime: '2024-06-18 14:46:28',
  },
  {
    id: '1802974839464660996',
    name: '原辅包-信息',
    categoryFlag: false,
    mergeCode: 'Csla',
    showName: 'Csla-原辅包-信息',
    parentId: '0',
    productMark: null,
    children: [],
    createTime: '2024-06-18 14:46:29',
  },
  {
    id: '1802974839464660992',
    name: 'T-检品分类',
    categoryFlag: false,
    mergeCode: 'JPFL',
    showName: 'JPFL-T-检品分类',
    parentId: '0',
    productMark: null,
    children: [],
    createTime: '2024-06-18 14:46:21',
  },
  {
    id: '1802974839464660993',
    name: 'T-原辅包分类',
    categoryFlag: false,
    mergeCode: 'YFBFL',
    showName: 'YFBFL-T-原辅包分类',
    parentId: '0',
    productMark: null,
    children: [],
    createTime: '2024-06-18 14:46:26',
  },
  {
    id: '1802974839464660994',
    name: 'T-中间品分类',
    categoryFlag: false,
    mergeCode: 'ZJPFL',
    showName: 'ZJPFL-T-中间品分类',
    parentId: '0',
    productMark: null,
    children: [],
    createTime: '2024-06-18 14:46:27',
  },
  {
    id: '1802974691376369664',
    name: 'c',
    categoryFlag: false,
    mergeCode: 'c',
    showName: 'c-c',
    parentId: '0',
    productMark: null,
    children: [
      {
        id: '1802974747223527424',
        name: '123',
        categoryFlag: true,
        mergeCode: 'c1231',
        showName: 'c1231-123',
        parentId: '1802974691376369664',
        productMark: null,
        children: [],
        createTime: '2024-06-18 16:00:52',
      },
    ],
    createTime: '2024-06-18 16:00:39',
  },
]);

const checkBoxModalValue = ref(['1', '2', '3']);
const leftClick = () => {
  console.log('leftClick');
};
const rightClick = () => {
  console.log('rightClick');
};
const onScanSuccess = (res) => {
  console.log('onScanSuccess', res);
};
const onScanFail = (res) => {
  console.log('onScanFail', res);
};
const onScanComplete = (res) => {
  console.log('onScanComplete', res);
};
const onScanSelect = (res) => {
  console.log('onScanSelect', res);
};
const onScanConfirm = (res) => {
  console.log('onScanConfirm', res);
};
const handleChange = ({ value }) => {
  console.log(value);
};
const search = (val) => {
  console.log('搜索框的值', val);
};
const active = ref(0);
const selectValue = ref(['4', '5']); // 单选 sting | 多选 array
const onSelect = () => {
  console.log('onSelect-选择器选择事件触发');
};
const onConfirm = (data) => {
  console.log('onConfirm-选择器确认事件触发', data);
};

const select1 = () => {
  console.log('select1');
};

const cancelPage = () => {
  console.log('cancelPage');
};
const confirmPage = () => {
  console.log('confirmPage');
};

const datePickerValue = ref(new Date());
const rangePickerValue = ref([]);

const signOptions = ref([
  {
    text:
        '钟杰就是开大会上课减肥还是看见回复时间恢复时间粉红色看电视剧哈佛好大方点上课减肥很多事',
    value: 'zhongjie',
    userId: '1',
  },
  {
    text: '李四',
    value: 'lisi',
    userId: '2',
  },
  {
    text: '王五',
    value: 'wangwu',
    userId: '3',
  },
  {
    text: '赵六',
    value: 'zhaoliu',
    userId: '4',
  },
]);

const dataSelectValue = ref(null);
const signValue = ref({});
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  padding: 56.25rpx 9.38rpx 9.38rpx;
  box-sizing: border-box;
  background-color: beige;
}
</style>
