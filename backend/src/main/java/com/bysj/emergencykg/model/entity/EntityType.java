package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("entity_type")
public class EntityType {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String typeCode;

    private String typeName;

    private String color;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
