package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("file_storage")
public class FileStorage {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String businessType;

    private String originalName;

    private String storedName;

    private String filePath;

    private String fileUrl;

    private Long fileSize;

    private String contentType;

    private Long uploaderId;

    private LocalDateTime createTime;
}
