package com.bmos.lims2.server.eln.record.dto;

import com.aspose.words.Comment;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.Node;
import com.bmos.lims2.server.eln.record.enums.RecordFormatType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 记录格式化结果
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/1/6 16:57
 */
@Data
@ApiModel("记录格式化结果")
public class RecordFormatResult {

    /**
     * 格式化类型
     */
    @ApiModelEnumProperty(value = "格式化类型", enumClass = RecordFormatType.class)
    private RecordFormatType formatType;

    /**
     * 记录异常总数
     */
    @ApiModelProperty(value = "记录异常总数", example = "0")
    private AtomicInteger total = new AtomicInteger(0);

    /**
     * 处理异常总数
     */
    @ApiModelProperty(value = "处理异常总数", example = "0")
    private AtomicInteger handleCount = new AtomicInteger(0);

    /**
     * 未处理的异常总数
     * @return
     */
    public Integer getUnHandleCount(){
        return this.total.get() - this.handleCount.get();
    }

    public RecordFormatResult(RecordFormatType formatType) {
        this.formatType = formatType;
    }

    /**
     * 处理并标记
     *
     * @param node
     * @param builder
     */
    public void handle(Node node, DocumentBuilder builder) {
        if (node == null){
            return;
        }
        this.total.getAndIncrement();
        this.handleCount.getAndIncrement();
        this.saveComment(node, builder, formatType.getMsg());
    }

    /**
     * 只标记 不处理
     *
     * @param node
     * @param builder
     */
    public void ignore(Node node, DocumentBuilder builder) {
        if (node == null){
            return;
        }
        this.total.getAndIncrement();
        this.saveComment(node, builder, formatType.getMsg());
    }

    /**
     * 保存注释
     *
     * @param node        节点
     * @param builder     builder
     * @param commentText 注释文本
     */
    private void saveComment(Node node, DocumentBuilder builder, String commentText) {
        Comment comment = new Comment(builder.getDocument());
        comment.setText(commentText);
        builder.moveTo(node);
        builder.getCurrentParagraph().appendChild(comment);
    }
}
