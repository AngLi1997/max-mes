<template>
  <div :id="`playWnd${index}`" class="playWnd" style="" />
</template>

<script>
  import { encrypt } from '@bmos/utils';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';

  // 声明公用变量
  let oWebControl = null;
  let initCount = 0;
  let pubKey = '';
  export default {
    /**
     * index :当前组件所属页面 设备管理 为空 ||实时监控 realTime
     * type : 回放 || 直播
     * code : 监控点编号 监控设备页面String || 实时监控 Array
     * time 回放时间 暂时废弃
     */
    props: ['mode', 'code', 'index'],
    data() {
      return {
        swfWidth: 618,
        swfHeight: 395,
        // swfWidth: 240,
        // swfHeight: 270,
        playText: '',
      };
    },
    mounted() {
      this.initPlugin(); // 初始化video界面
      window.addEventListener('resize', this.SetDocOffset);
    },
    unmounted() {
      // 组件销毁后
      this.destoryWnd();
      window.removeEventListener('resize', this.SetDocOffset);
    },
    beforeUnmount() {
      this.destoryWnd();
    },
    methods: {
      // 下载插件
      frontDownload() {
        let a = document.createElement('a'); // 创建一个<a></a>标签
        const path = location.pathname.replace('/index.html', '');
        a.href = path + '/public/haikang/VideoWebPlugin.exe'; // 给a标签的href属性值加上地址，注意，这里是绝对路径，不用加 点.
        a.download = 'VideoWebPlugin.exe'; // 设置下载文件文件名，这里加上.xlsx指定文件类型，pdf文件就指定.fpd即可
        a.style.display = 'none'; // 障眼法藏起来a标签
        document.body.appendChild(a); // 将a标签追加到文档对象中
        a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
        a.remove(); // 一次性的，用完就删除a标签
      },
      // 创建播放实例
      initPlugin() {
        let that = this;
        oWebControl = new window.WebControl({
          szPluginContainer: `playWnd${that.index}`, // 指定容器id
          iServicePortStart: 15900, // 指定起止端口号，建议使用该值
          iServicePortEnd: 15903,
          szClassId: '23BF3B0A-2C56-4D97-9C03-0CB103AA8F11', // 用于 IE10 使用 ActiveX 的 clsid
          cbConnectSuccess() {
            // 创建WebControl实例成功
            oWebControl
              .JS_StartService('window', {
                // WebControl实例创建成功后需要启动服务
                dllPath: './VideoPluginConnect.dll', // 值"./VideoPluginConnect.dll"写死
              })
              .then(
                function () {
                  // 启动插件服务成功
                  oWebControl.JS_SetWindowControlCallback({
                    // 设置消息回调
                    cbIntegrationCallBack: that.cbIntegrationCallBack,
                  });

                  oWebControl.JS_CreateWnd(`playWnd${that.index}`, that.swfWidth, that.swfHeight).then(function () {
                    // JS_CreateWnd创建视频播放窗口，宽高可设定
                    that.init(); // 创建播放实例成功后初始化
                  });
                },
                function () {
                  // 启动插件服务失败
                },
              );
          },
          cbConnectError() {
            // 创建WebControl实例失败
            oWebControl = null;
            message.warning('插件未启动，正在尝试启动，请稍候...');
            that.playText = t('插件未启动，正在尝试启动，请稍候...');
            // that.frontDownload()
            // document.querySelector('#playWnd').innerHTML('插件未启动，正在尝试启动，请稍候...')
            // $('#playWnd').html('插件未启动，正在尝试启动，请稍候...')
            window.WebControl.JS_WakeUp('VideoWebPlugin://'); // 程序未启动时执行error函数，采用wakeup来启动程序
            initCount++;
            if (initCount < 3) {
              setTimeout(function () {
                that.initPlugin();
              }, 3000);
            } else {
              message.warning(t('插件启动失败，请检查插件是否安装！'));
              // console.log(initCount)
              that.playText = t('插件启动失败，请检查插件是否安装！');
              if (initCount == 3) {
                that.frontDownload();
              }
              // document.querySelector('#playWnd').innerHTML('插件启动失败，请检查插件是否安装！')
              // $('#playWnd').html('插件启动失败，请检查插件是否安装！')
            }
          },
          cbConnectClose: () => {
            // 异常断开：bNormalClose = false
            // JS_Disconnect正常断开：bNormalClose = true
            console.log('cbConnectClose');
            oWebControl = null;
          },
        });
      },
      init() {
        const that = this;
        this.getPubKey(() => {
          // //////////////////////////////// 请自行修改以下变量值  ////////////////////////////////////
          let appkey = '22762755'; //综合安防管理平台提供的appkey，必填    20689287
          let secret = this.setEncrypt('7m55QAP40IqEEikLlk54'); //综合安防管理平台提供的secret，必填  akFLZLlK5mTXHHGhmZBh
          let ip = '192.168.119.253'; //综合安防管理平台IP地址，必填  172.16.0.33
          let playMode = 0; //初始播放模式：0-预览，1-回放
          let port = 4443; // 443
          let snapDir = 'D:\\SnapDir'; //抓图存储路径
          let videoDir = 'D:\\VideoDir'; //紧急录像或录像剪辑存储路径
          let layout = '1x1'; //playMode指定模式的布局
          let enableHTTPS = 1; //是否启用HTTPS协议与综合安防管理平台交互，这里总是填1
          let encryptedFields = 'secret'; //加密字段，默认加密领域为secret
          let showToolbar = 1; //是否显示工具栏，0-不显示，非0-显示
          let showSmart = 1; //是否显示智能信息（如配置移动侦测后画面上的线框），0-不显示，非0-显示
          let buttonIDs = '0,16,256,257,258,259,260,512,513,514,515,516,517,768,769';
          // //////////////////////////////// 请自行修改以上变量值  ////////////////////////////////////
          oWebControl
            .JS_RequestInterface({
              funcName: 'init',
              argument: JSON.stringify({
                appkey, // API网关提供的appkey
                secret, // API网关提供的secret
                ip, // API网关IP地址
                playMode, // 播放模式（决定显示预览还是回放界面）
                port, // 端口
                snapDir, // 抓图存储路径
                videoDir, // 紧急录像或录像剪辑存储路径
                layout, // 布局
                enableHTTPS, // 是否启用HTTPS协议
                encryptedFields, // 加密字段
                showToolbar, // 是否显示工具栏
                showSmart, // 是否显示智能信息
                buttonIDs, // 自定义工具条按钮
              }),
            })
            .then(function (oData) {
              oWebControl.JS_Resize(that.swfWidth, that.swfHeight); // 初始化后resize一次，规避firefox下首次显示窗口后插件窗口未与DIV窗口重合问题
              if (that.mode == 0) {
                that.startPreview(that.code);
              } else {
                that.startPlayback(that.code);
              }
              that.playText = '';
            });
        });
      },
      // 获取公钥
      getPubKey(callback) {
        oWebControl
          .JS_RequestInterface({
            funcName: 'getRSAPubKey',
            argument: JSON.stringify({
              keyLength: 1024,
            }),
          })
          .then(function (oData) {
            // console.log(oData);
            if (oData.responseMsg.data) {
              pubKey = oData.responseMsg.data;
              callback();
            }
          });
      },
      // RSA加密
      setEncrypt(value) {
        return encrypt(value, pubKey);
      },
      // 回调的消息
      cbIntegrationCallBack(oData) {
        const { responseMsg: type, responseMsg: msg } = oData;

        if (type === 'error') {
          console.log(type, msg, this.dateFormat(new Date(), 'yyyy-MM-dd hh:mm:ss'));
        } else {
          console.log(type, msg, this.dateFormat(new Date(), 'yyyy-MM-dd hh:mm:ss'));
        }
      },
      // 预览
      startPreview(cameraCode) {
        // 点击查询后显示
        oWebControl.JS_ShowWnd();
        let cameraIndexCode = cameraCode; // 获取输入的监控点编号值，必填
        let streamMode = 0; // 主子码流标识：0-主码流，1-子码流
        let transMode = 1; // 传输协议：0-UDP，1-TCP
        let gpuMode = 0; // 是否启用GPU硬解，0-不启用，1-启用
        let wndId = -1; // 播放窗口序号（在2x2以上布局下可指定播放窗口）

        cameraIndexCode = cameraIndexCode.replace(/(^\s*)/g, '');

        oWebControl.JS_RequestInterface({
          funcName: 'startPreview',
          argument: JSON.stringify({
            cameraIndexCode, // 监控点编号
            streamMode, // 主子码流标识
            transMode, // 传输协议
            gpuMode, // 是否开启GPU硬解
            wndId, // 可指定播放窗口
          }),
        });
      },
      // 停止全部预览
      stopAllPreview() {
        oWebControl.JS_RequestInterface({
          funcName: 'stopAllPreview',
        });
      },
      // 回放
      startPlayback(cameraCode) {
        let cameraIndexCode = cameraCode; // 获取输入的监控点编号值，必填
        let startTimeStamp = new Date().getTime() - 24 * 60 * 60 * 1000; // 回放开始时间戳，必填
        let endTimeStamp = new Date().getTime(); // 回放结束时间戳，必填
        // let startTimeStamp = new Date(this.startTime.replace('-', '/').replace('-', '/')).getTime() // 回放开始时间戳，必填
        // let endTimeStamp = new Date(this.endTime.replace('-', '/').replace('-', '/')).getTime() // 回放结束时间戳，必填
        let recordLocation = 1; // 录像存储位置：0-中心存储，1-设备存储
        let transMode = 1; // 传输协议：0-UDP，1-TCP
        let gpuMode = 0; // 是否启用GPU硬解，0-不启用，1-启用
        let wndId = -1; // 播放窗口序号（在2x2以上布局下可指定播放窗口）

        oWebControl.JS_RequestInterface({
          funcName: 'startPlayback',
          argument: JSON.stringify({
            cameraIndexCode, // 监控点编号
            startTimeStamp: Math.floor(startTimeStamp / 1000).toString(), // 录像查询开始时间戳，单位：秒
            endTimeStamp: Math.floor(endTimeStamp / 1000).toString(), // 录像结束开始时间戳，单位：秒
            recordLocation, // 录像存储类型：0-中心存储，1-设备存储
            transMode, // 传输协议：0-UDP，1-TCP
            gpuMode, // 是否启用GPU硬解，0-不启用，1-启用
            wndId, // 可指定播放窗口
          }),
        });
      },
      // 停止全部回放
      stopAllPlayback() {
        oWebControl.JS_RequestInterface({
          funcName: 'stopAllPlayback',
        });
      },
      // 调整插件窗口大小、位置接
      SetDocOffset() {
        if (oWebControl != null) {
          oWebControl.JS_Resize(this.swfWidth, this.swfHeight);
        }
      },
      // 插件窗口销毁
      destoryWnd() {
        if (oWebControl != null) {
          oWebControl.JS_HideWnd(); // 先让窗口隐藏，规避可能的插件窗口滞后于浏览器消失问题
          oWebControl.JS_RequestInterface({
            funcName: 'destroyWnd',
          }); // 销毁当前播放的视频
          oWebControl.JS_Disconnect(); // 断开与插件服务连接
        }
      },

      // 格式化时间
      dateFormat(oDate, fmt) {
        let o = {
          'M+': oDate.getMonth() + 1, // 月份
          'd+': oDate.getDate(), // 日
          'h+': oDate.getHours(), // 小时
          'm+': oDate.getMinutes(), // 分
          's+': oDate.getSeconds(), // 秒
          'q+': Math.floor((oDate.getMonth() + 3) / 3), // 季度
          S: oDate.getMilliseconds(), // 毫秒
        };
        if (/(y+)/.test(fmt)) {
          fmt = fmt.replace(RegExp.$1, (oDate.getFullYear() + '').substr(4 - RegExp.$1.length));
        }
        for (let k in o) {
          if (new RegExp('(' + k + ')').test(fmt)) {
            fmt = fmt.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length));
          }
        }
        return fmt;
      },
    },
  };
</script>

<style lang="less" scoped>
  .playWnd {
    /* margin: 30px 0 0 50px; */
    //   width: 650px;
    //   height: 500px;
    //   width: 200px;
    //   height: 125px;
    width: 100%;
    height: 100%;
    /* border: 1px solid red; */
  }
</style>
