<!-- 白俄数字看板配置 -->
<template>
  <div class="content">
    <div class="left_list">
      <div
        v-for="item in configList"
        :key="item.code"
        :class="{ board_item: true, isClick: configCode == item.code }"
        @click="changeConfigCode(item.code)">
        {{ item.title }}
      </div>
    </div>
    <div class="form_list_box">
      <BMTableTitle :title="t('看板配置')" />
      <div class="form_scroll_box">
        <div v-for="(item, index) in pointList" :key="item.formCode" class="draw_config_box">
          <div class="form_title_box">
            <div class="form_title">{{ t('数据点') }}{{ index + 1 }}</div>
            <BMIcons class="delete-icon" icon="Delete" @click="() => deleteConditionList(item)" />
          </div>
          <BMForm :ref="(el: any) => getFormRefs(el, item)" v-bind="formProps"></BMForm>
        </div>
      </div>
      <div class="btn_box">
        <Button class="add_btn" @click="addPoint">{{ t('新增数据点') }}</Button>
        <Button class="add_btn" type="primary" @click="saveData">{{ t('保存') }}</Button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useData } from './hooks/useData';
  import { BMTableTitle, BMForm } from '@bmos/components';
  import { BMIcons } from '@bmos/icons';

  const {
    pointList,
    getFormRefs,
    formProps,
    deleteConditionList,
    addPoint,
    configList,
    changeConfigCode,
    configCode,
    saveData,
  } = useData();
</script>
<style scoped lang="less">
  .content {
    background-color: #fff;
    width: 100%;
    height: 100%;
    display: flex;
    .left_list {
      width: 240px;
      box-sizing: border-box;
      border-right: 1px solid #e1e3e5;
      padding: 16px;
      .board_item {
        padding: 10px;
        cursor: pointer;
        border-radius: 8px;
        margin-bottom: 5px;
      }
      .isClick {
        color: #3d6ef6;
        background: #ebf1ff;
      }
    }
    .form_list_box {
      width: calc(100% - 240px);
      padding: 16px;
      .form_scroll_box {
        height: calc(100% - 100px);
        overflow: auto;
      }
      .draw_config_box {
        padding: 16px;
        background-color: rgba(247, 248, 249, 1);
        margin-bottom: 20px;
        .form_title_box {
          display: flex;
          align-items: center;
          justify-content: space-between;
        }
      }
      .btn_box {
        margin-top: 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
      }
    }
  }
</style>
