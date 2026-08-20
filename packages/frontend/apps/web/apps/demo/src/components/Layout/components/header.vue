<template>
  <LayoutHeader style="background: #fff;">
    <Dropdown :trigger="['contextmenu']">
      <div>
        <Tabs v-model:activeKey="activeKey" hide-add type="editable-card" @edit="onEdit">
          <template #leftExtra>
              ddd
          </template>
          <template #rightExtra>
              ddd
          </template>
          <template #moreIcon>
            moreIcon
          </template>
          <TabPane key="fff" tab="首页"></TabPane>
          <TabPane key="fff1" tab="首页1"></TabPane>
        </Tabs>
      </div>
      <template #overlay>
        <slot name="tabBarExtraContent">
          <Menu @click="menuClick">
            <MenuItem key="1">2nd menu item</MenuItem>
            <MenuItem key="2">2nd menu item</MenuItem>
            <MenuItem key="3">3rd menu item</MenuItem>
          </Menu>
        </slot>
      </template>
    </Dropdown>
  </LayoutHeader>
</template>
<script setup lang="ts">
  import { LayoutHeader, Dropdown, Menu, MenuItem, TabPane, Tabs, MenuProps } from 'ant-design-vue';
  import { ref } from 'vue';
  const activeKey = ref('fff')
  const menuClick:MenuProps['onClick'] = ({ item, key, keyPath })=>{
    console.log( item, key, keyPath );
  }

  


  const onEdit = ()=>{

  }
</script>
