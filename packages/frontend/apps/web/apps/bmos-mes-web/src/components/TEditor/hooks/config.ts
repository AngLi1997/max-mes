import { getLanguage, t } from '@bmos/i18n';
export const initConfig = (useEditor: any, utilsData: any) => {
  const {
    emit,
    props,
    isTableBoxFlag,
    clickElement,
    bodyElement,
    changeClickNodeList,
    setRightClickElement,
    TEditorContentValue,
  } = useEditor;
  const { styleMap } = utilsData;
  const langs = {
    ru_RU: 'ru',
    en_US: '',
    zh_CN: 'zh_CN',
  } as any;
  const init = {
    selector: '#editor',
    language: langs[getLanguage() || 'zh_CN'],
    height: '100%',
    width: '100%',
    promotion: false,
    branding: false,
    resize: false, // 隐藏拖拽放大
    validate: false, // 禁用格式验证
    valid_elements: '*[*]', // 允许所有元素和属性
    extended_valid_elements: '*[*]',
    elementpath: false, // 隐藏底栏的元素路径
    autosave_ask_before_unload: false,
    doctype: '<!DOCTYPE html>',
    schema: 'html5',
    plugins:
      'print preview searchreplace directionality visualblocks fullscreen link media template code codesample table charmap hr pagebreak nonbreaking anchor insertdatetime advlist lists textpattern emoticons', //引入工具插件
    toolbar:
      'code undo redo restoredraft| setheader setfooter | cut copy paste pastetext | forecolor backcolor bold italic underline strikethrough link anchor | alignleft aligncenter alignright alignjustify outdent indent | \
    styleselect formatselect fontselect fontsizeselect | bullist numlist | blockquote subscript superscript removeformat | \
    table media charmap emoticons hr pagebreak insertdatetime print preview | fullscreen | lineheight', //工具栏显示
    contextmenu: 'table fileman', // 自定义菜单的名称
    menubar: 'file edit insert format table',
    object_resizing: true,
    images_upload_handler(blobInfo: any) {
      // 图片blob转base64
      return Promise.resolve('data:' + blobInfo.blob().type + ';base64,' + blobInfo.base64());
    },
    contextmenu_never_use_native: true, // 阻止浏览器右键出现
    init_instance_callback: (editor: any) => {
      // 加载完内容回调
      emit('rendered');
      // 获取编辑器document
      bodyElement.value = editor.getDoc();
    },
    setup(editor: any) {
      editor.on('click', function (e: any) {
        emit('update:isClick', true);
        let element = e.target; // 获取被点击的元素
        if (element.tagName == 'TEXTAREA' && element.classList.contains('record-component')) {
          // 如果是组件,添加点击状态
          changeClickNodeList(element.id);
          emit('content-click', element.id);
        }
        clickElement.value = element;
        // 判断父级是不是表格
        // const brElements = element.getElementsByTagName('br');
        while (element) {
          if (element.nodeName === 'TABLE') {
            isTableBoxFlag.value = true;
            break;
          }
          isTableBoxFlag.value = false;
          element = element.parentNode;
        }
        if (props.isRage) {
          // 批量模式开启
          emit('addTemplate');
        }
      });
      editor.on('keydown', function (event: any) {
        if (event.ctrlKey) {
          emit('update:ctrlDown', true);
          if (event.keyCode == 90) {
            // 撤回时
          }
        }
      });
      editor.on('keyup', function (event: any) {
        if (!event.ctrlKey) {
          emit('update:ctrlDown', false);
        }
      });
      editor.on('input', function (e: any) {
        // 这里的代码会在每次输入时被调用，包括删除操作
        if (e.inputType == 'deleteContentBackward') {
          emit('delete-content');
        }
      });
      editor.on('undo', function () {
        // 撤回操作,校验组件是否重新加入
        emit('delete-content');
      });
      editor.ui.registry.addButton('setheader', {
        text: t('添加页眉'),
        onAction: () => {
          let content = TEditorContentValue.value;
          if (!content.includes('<!-- remove_header_flag -->')) {
            content =
              `<!-- remove_header_flag --><p> </p><hr class="fhhr" style="margin:5px 0;"/><!-- remove_header_flag -->` +
              content;
          } else {
            const list = content.split('<!-- remove_header_flag -->');
            if (list.length == 3) {
              content =
                `<!-- remove_header_flag --><p> </p><hr class="fhhr" style="margin:5px 0;"/><!-- remove_header_flag -->` +
                list[2];
            }
            if (list.length == 2) {
              content =
                `<!-- remove_header_flag --><p> </p><hr class="fhhr" style="margin:5px 0;"/><!-- remove_header_flag -->` +
                list[1];
            }
          }
          TEditorContentValue.value = content;
        },
      });
      editor.ui.registry.addButton('setfooter', {
        text: t('添加页脚'),
        onAction: () => {
          let content = TEditorContentValue.value;
          if (!content.includes('<!-- remove_footer_flag -->')) {
            content =
              content +
              `<!-- remove_footer_flag --><hr class="fhhr" style="margin:5px 0;"/><p> </p><!-- remove_footer_flag -->`;
          } else {
            const list = content.split('<!-- remove_footer_flag -->');
            if (list.length == 3) {
              content =
                list[0] +
                `<!-- remove_footer_flag --><hr class="fhhr" style="margin:5px 0;"/><p> </p><!-- remove_footer_flag -->`;
            }
            if (list.length == 2) {
              content =
                list[0] +
                `<!-- remove_footer_flag --><hr class="fhhr" style="margin:5px 0;"/><p> </p><!-- remove_footer_flag -->`;
            }
          }
          TEditorContentValue.value = content;
        },
      });
      // 右键事件
      editor.on('contextmenu', (e: any) => {
        const all = editor.ui.registry.getAll();
        all.menuItems.fileman.enabled = e.target.type == 'textarea';
        setRightClickElement.value = e.target.type == 'textarea' ? e.target : null;
      });
      editor.on('change', function () {
        emit('delete-content');
      });

      editor.ui.registry.addMenuItem('fileman', {
        text: t('宽高设置'),
        onAction: () => {
          let initData = {};
          if (styleMap.value.length > 1) {
            // 选中了多个节点
            initData = {
              width: '',
              height: '',
              size: '',
              family: '',
            };
          } else if (setRightClickElement.value != null) {
            initData = {
              width: setRightClickElement.value.clientWidth - 4,
              height: setRightClickElement.value.clientHeight - 4,
              size: setRightClickElement.value.style.fontSize.split('px')[0],
              family: setRightClickElement.value.style.fontFamily,
            };
          }
          editor.windowManager.open({
            title: t('宽高设置'),
            body: {
              type: 'panel',
              items: [
                {
                  type: 'input',
                  name: 'width',
                  label: t('宽度'),
                },
                {
                  type: 'input',
                  name: 'height',
                  label: t('高度'),
                },
                {
                  type: 'input',
                  name: 'size',
                  label: t('字号'),
                },
                {
                  type: 'selectbox',
                  name: 'family',
                  label: t('字体'),
                  items: [
                    { value: 'SimSun', text: 'SimSun' },
                    { value: 'SimHei', text: 'SimHei' },
                    { value: 'YaHei', text: 'YaHei' },
                    { value: 'FangSong', text: 'FangSong' },
                  ],
                },
              ],
            },
            initialData: initData,
            width: 450,
            height: 180,
            onSubmit: (params: any) => {
              const { width, height, size, family } = params.getData();
              if (isNaN(width * 1) || isNaN(height * 1) || isNaN(size * 1)) {
                editor.windowManager.alert(t('请输入数字'));
                return;
              }
              if (styleMap.value.length > 1) {
                // 批量模式
                styleMap.value.forEach((item: string) => {
                  let dom = bodyElement.value.getElementById(item);
                  if (width != '') {
                    dom.style.width = width + 'px';
                  }
                  if (height != '') {
                    dom.style.height = height + 'px';
                  }
                  if (size != '') {
                    dom.style.fontSize = size + 'px';
                  }
                  if (family != '') {
                    dom.style.fontFamily = family;
                  }
                });
              } else {
                setRightClickElement.value.style.width = width + 'px';
                setRightClickElement.value.style.height = height + 'px';
                setRightClickElement.value.style.fontSize = size + 'px';
                setRightClickElement.value.style.fontFamily = family;
              }
              params.close();
            },
            buttons: [
              {
                text: t('确定'),
                type: 'submit',
                subtype: 'primary',
                onclick: 'submit',
              },
              { text: 'Close', type: 'cancel', onclick: 'close' },
            ],
          });
        },
      });
    },
    content_style: `.isClick { 
      border: 1px solid #2871FF !important;
      color: #2871FF;
      background: #EBF1FF;
    }
    .isClick.business-button-class { 
      border: 1px solid #2871FF !important;
      color: #2871FF !important;
      background: #D9E5FF !important;
    }
    body {
      margin: 0;
      box-sizing: border-box;
    }
    html {
      -ms-overflow-style: none;
      scrollbar-width: none;
    }
    p {
      min-height: 1px;
      margin: 0;
    }
    textarea {
      outline: none;
    }`,
    license_key: 'gpl',
    line_height_formats: '1 1.2 1.4 1.6 2', //行高
    font_size_formats: '12px 14px 16px 18px 20px 22px 24px 28px 32px 36px 48px 56px 72px', //字体大小
    font_family_formats:
      '微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;',
  };
  return {
    init,
    clickElement,
  };
};
