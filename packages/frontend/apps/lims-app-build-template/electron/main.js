const { app, BrowserWindow, ipcMain, shell, screen, net } = require("electron");
const fs = require("fs");
const electron = require("electron");
const path = require("path");
const url = require("url");
const { SerialPort } = require('serialport'); 

// 添加Windows DPI缩放兼容性
if (process.platform === 'win32') {
  app.commandLine.appendSwitch('high-dpi-support', 'true');
  app.commandLine.appendSwitch('force-device-scale-factor', '1');
  app.commandLine.appendSwitch('disable-web-security');
}

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let win;
const Menu = electron.Menu;

function createWindow() {
  // Create the browser window.
  //   Menu.setApplicationMenu(null)

  //解决点击窗口最大化样式整体过大问题
  const WIDTH = 1280;
  const HEIGHT = 800;
  const aspectRatio = WIDTH / HEIGHT; // 窗口宽高比

  // 获取主显示器信息
  const primaryDisplay = screen.getPrimaryDisplay();
  const { width: screenWidth, height: screenHeight } = primaryDisplay.bounds;

  win = new BrowserWindow({
    fullscreen: false, // 取消全屏
    width: WIDTH,  // 固定宽度 1280
    height: HEIGHT, // 固定高度 800
    frame: false, // 无边框窗口
    webPreferences: {
      nodeIntegration: true, // 使渲染进程拥有node环境
      preload: path.join(__dirname, "preload.js"),
    },
    resizable: false, // 禁止窗口大小调整
    icon: path.join(__dirname, "logo.ico"), // 设置ico
    center: true, // 窗口居中显示
    show: false, // 先不显示窗口
  });
    // win.setMenu(null);
  win.once("ready-to-show", () => {
    // 限制窗口最小尺寸（int整形）, 无边框模式下，不考虑标题栏高度
    win.setMinimumSize(WIDTH / 2, HEIGHT / 2);
    
    // 显示窗口，保持固定大小
    win.show();
  });



  // 控制等比缩放 - 只在非全屏模式下生效
  win.on("will-resize", resizeWindow);

  function resizeWindow(event, newBounds) {
    const wins = event.sender;
    if (!wins || wins.isFullScreen()) {
      return; // 全屏模式下不处理resize事件
    }
    event.preventDefault(); // 拦截，使窗口先不变
    const currentSize = wins.getSize();
    console.log(currentSize);
    console.log(newBounds)
    const widthChanged = currentSize[0] !== newBounds.width; // 判断是宽变了还是高变了，两者都变优先按宽适配
    // ! 虽然搞不懂为何有.0625rem偏差，但是可以解决问题(Windows 10)
    if (widthChanged) {
      wins.setContentSize(
        newBounds.width - 1,
        parseInt(newBounds.width / aspectRatio + 0.5) - 1
      );
    } else {
      wins.setContentSize(
        parseInt(aspectRatio * newBounds.height + 0.5) - 1,
        newBounds.height - 1
      );
    }
  }

  // 监听屏幕方向变化
  screen.on('display-metrics-changed', () => {
    const size = screen.getPrimaryDisplay().workAreaSize;
    if (size.width>size.height){
      win.setContentSize(WIDTH, HEIGHT);
    }else {
      win.setContentSize(size.width, size.height);
    }
  });

  // and load the index.html of the app.
  win.loadURL(
    url.format({
      pathname: path.join(__dirname, "index.html"),
      protocol: "file:",
      slashes: true,
    })
  );
  //cpu

  // Open the DevTools.
//   win.webContents.openDevTools();
  // Emitted when the window is closed.
  win.on("closed", () => {
    // Dereference the window object, usually you would store windows
    // in an array if your app supports multi windows, this is the time
    // when you should delete the corresponding element.
    win = null;
  });
}

// 核心代码
const gotTheLock = app.requestSingleInstanceLock();

if (!gotTheLock) {
  app.quit();
} else {
  app.on("second-instance", (event, commandLine, workingDirectory) => {
    // 当运行第二个实例时,将会聚焦到myWindow这个窗口
    if (win) {
      if (win.isMinimized()) win.restore();
      win.focus();
    }
  });
  app.whenReady().then(createWindow)
}
// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
// app.on("ready", createWindow);

// Quit when all windows are closed.
app.on("window-all-closed", () => {
  // On macOS it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== "darwin") {
    app.quit();
  }
});

app.on("activate", () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (win === null) {
    createWindow();
  }
});

app.on("ready", ()=>{
  // 监听串口相关事件
  ipcMain.on("open-serial-port", openSerialPortPort);
  ipcMain.on("close-serial-port", closeSerialPortPort);
  ipcMain.on("serial-port-scan", sendSerialPortScan);
  ipcMain.handle("get-serial-port-list", getSerialPortList);
  ipcMain.handle('axios-request', async (event) => {
    try {
      const response = await axios({
        baseURL: 'http://172.30.1.160/api',
        method: 'get',
        url: '/app/platform/param/app/version'
      });
      return response.data;
    } catch (error) {
      console.error(error);
    }
  });
  ipcMain.handle('update',async (event, link) => {
    try {
      const savePath = path.join(app.getPath('temp'), 'Bmos.exe');
      const request = net.request(link);
      const file = fs.createWriteStream(savePath);
  
      request.on('response', (response) => {
        response.on('data', chunk => file.write(chunk));
        response.on('end', () => {
          file.end();
          shell.openPath(savePath); // 打开安装程序
          app.quit();
        });
      });
      request.end();
    } catch (error) {
      console.error(error);
    }
  });
  
});

let port = null;

function getSerialPortList() {
  return SerialPort.list();
}
let portIsOpen = false;
// 打开扫码串口
function openSerialPortPort(event,portData) {
    if (port ||!portData) {
      return;
    }
    //  port =new SerialPort({
    //   path: 'COM7',
    //   baudRate: 115200,
    //   dataBits: 8,
    //   stopBits: 1,
    //   parity: 'none',
    //   flowControl: false,
    // });
    port =new SerialPort(portData);
    port.on('data', (data) => {
      const value = data.toString('utf-8');
      win.webContents.send('serial-port-data', value);
    });
    port.on('open', () => {
    });
    port.on('error', (error) => {
      portIsOpen = false;
    });
}
// 扫码串口扫码命令
function sendSerialPortScan() {
  if (!port) return;
  let buf = Buffer.from([0x16,0x54,0x0D]);
  port.write(buf);
}
let closingSerialPort = false;
// 关闭扫码串口
async function closeSerialPortPort() {
  if (closingSerialPort || !portIsOpen || !port || !port.isOpen) {
    return
  }
  closingSerialPort = true;
  await port.close();
  closingSerialPort = false;
  portIsOpen = false;
  port = null;
}

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
