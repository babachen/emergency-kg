package com.bysj.emergencykg.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("relation_type")
public class RelationType {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String relationCode;

    private String relationName;

    private String description;

    private String directionDesc;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
