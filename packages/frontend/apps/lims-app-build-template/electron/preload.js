const { contextBridge, ipcRenderer, shell } = require("electron");
const path = require("path");

contextBridge.exposeInMainWorld(
  "package",
  require(path.join(__dirname, "package.json"))
);

contextBridge.exposeInMainWorld("openBrowser", {
  openExternalLink: (link) => {
    // 当需要打开外部链接时，使用shell.openExternal
    shell.openExternal(link);
  },
});

// 串口相关API
contextBridge.exposeInMainWorld("serialPortAPI", {
  getSerialPortList: () => ipcRenderer.invoke("get-serial-port-list"), // 获取串口列表
// 打开串口并监听数据
  openSerialPort: (portData,callback) => {
    ipcRenderer.send("open-serial-port",portData);
    ipcRenderer.on('serial-port-data', callback);
}, 
  closeSerialPort: () => {
    ipcRenderer.send("close-serial-port");
    ipcRenderer.removeAllListeners('serial-port-data');
  }, // 关闭串口
  onSerialPortScan: () => ipcRenderer.send("serial-port-scan"), // 串口扫描
})
contextBridge.exposeInMainWorld("autoUpdate", {
  update: (link) => {
    return ipcRenderer.invoke("update", link);
  },
});
