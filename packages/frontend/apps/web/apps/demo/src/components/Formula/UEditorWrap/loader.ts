export const loadScript = (link: string) => {
  return new Promise<void>((resolve, reject) => {
    window.$loadEventBus.on(link, resolve);
    if (window.$loadEventBus.listeners[link].requested === false) {
      window.$loadEventBus.listeners[link].requested = true;
      // 如果这个资源从未被请求过，就手动创建脚本去加载
      const script = document.createElement('script');
      script.src = link;
      script.onload = () => {
        window.$loadEventBus.emit(link);
      };
      script.onerror = reject;
      document.getElementsByTagName('head')[0].appendChild(script);
    }
  });
};

// 动态创建 link 标签来加载 CSS 文件
export const loadCss = (link: string) => {
  return new Promise<void>((resolve, reject) => {
    window.$loadEventBus.on(link, resolve);
    if (window.$loadEventBus.listeners[link].requested === false) {
      window.$loadEventBus.listeners[link].requested = true;
      const css = document.createElement('link');
      css.type = 'text/css';
      css.rel = 'stylesheet';
      css.href = link;
      css.onload = () => {
        window.$loadEventBus.emit(link);
      };
      css.onerror = reject;
      document.getElementsByTagName('head')[0].appendChild(css);
    }
  });
};