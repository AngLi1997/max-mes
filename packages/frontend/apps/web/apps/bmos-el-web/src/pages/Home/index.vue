<template>
  <div class="home-container" :style="style">
    <div v-for="item in outsideList" :key="item.path" class="outside-item" @click="toOutside(item)">
      <img :src="item.icon" alt="" />
      <span>{{ item.name }}</span>
    </div>
  </div>
</template>

<script setup lang="tsx">
  import { computed } from 'vue';
  import { getLogoUrl } from '@bmos/utils';
  import { sso } from '@bmos/messager';
  import sysIcon from '@/assets/img/sysIcon.png';
  import { getParameter, getPermissionMenuList } from '@/services';

  const { getUserToken } = sso;

  const style = computed(() => {
    return {
      backgroundImage: `url('${getLogoUrl('home-bg.png')}')`,
    };
  });

  const openTypeEnum = {
    ROUTER: 1, // 路由跳转
    NEW_WINDOW: 2, // 新页面
  };

  type OutsideType = {
    name: string;
    icon: string;
    path: string;
    open: number;
    id: string;
  };

  const outsideJson = ref<any>({});

  const getOutsideIpByCode = async (code: string) => {
    try {
      if (code && outsideJson.value?.[code]) {
        return Promise.resolve(outsideJson.value?.[code]);
      } else {
        return Promise.reject();
      }
    } catch (error) {
      return Promise.reject();
    }
  };

  const getUrl = async (item: any) => {
    const outsideIp = await getOutsideIpByCode(item.id);
    const backUrl: string = item.outsideUrl;
    const backToken = getUserToken(); //token
    let temp: any;
    if (backUrl?.includes('?')) {
      temp = outsideIp + backUrl + `&token=${backToken}`;
    } else {
      temp = outsideIp + backUrl + `?token=${backToken}`;
    }
    return temp;
  };

  const router = useRouter();
  const toOutside = (item: any) => {
    //open为NEW_WINDOW时需单独网页打开
    try {
      if (item.open === openTypeEnum.NEW_WINDOW) {
        window.open(item.path);
      } else {
        router.push({
          name: `outside${item.id}`,
        });
      }
    } catch (error) {
      //
      console.log('error', error);
    }
  };

  const outsideList = ref<OutsideType[] | []>([]);
  onMounted(async () => {
    try {
      const { data: json } = await getParameter('platform.sys.outside_url	');
      outsideJson.value = JSON.parse(json?.value || '{}');
      const { data } = await getPermissionMenuList({ rootMenuCode: 220, containsFunc: true });
      for (let item of data) {
        if (item.isOutside !== 0) {
          const path = await getUrl(item);
          // @ts-ignore
          outsideList.value.push({
            name: item.name,
            icon: item.icon ?? sysIcon,
            path,
            open: item.isOutside,
            id: item.id,
            parentId: item.parentId,
          });
        }
      }
    } catch (error) {
      console.log(error);
    }
  });
</script>
<style scoped lang="less">
  .home-container {
    width: 100%;
    height: 100%;
    padding: 80px 100px;
    background-repeat: no-repeat;
    background-size: cover;
    display: flex;
    align-items: flex-start;
    justify-content: flex-start;
    flex-wrap: wrap;
    gap: 100px;

    .outside-item {
      cursor: pointer;
      display: flex;
      flex-direction: column;
      align-items: center;
      img {
        display: block;
        width: 80px;
        height: 80px;
        border-radius: 10px;
        box-shadow: 0px 0px 8px 0px #0000001a;
      }
      span {
        margin-top: 16px;
        // color: #fff;
        font-family: Source Han Sans CN;
        font-weight: 400;
        font-size: 16px;
        letter-spacing: 0%;
        text-align: center;
      }
    }
  }
</style>
