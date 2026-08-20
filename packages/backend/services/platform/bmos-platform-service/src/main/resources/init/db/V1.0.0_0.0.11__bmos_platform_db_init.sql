alter table bp_tag_define
    add max_field_size int default 10 not null comment '字段允许的最大长度（限制标签打印样式）' after preview_html;
