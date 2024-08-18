package com.aye10032.foundation.entity.base.dream;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author dazo66
 * @date 2023-08-10
 */
@Data
@TableName("dream")
public class Dream {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String element;

    /**
     *
     */
    private Long fromQq;

    /**
     *
     */
    private Date date;

    private String qqName;

}