package com.aye10032.foundation.entity.base.dream;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author dazo66
 * @date 2023-08-10
 */
@Data
public class Dream {
    /**
     * 
     */
    @TableId
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