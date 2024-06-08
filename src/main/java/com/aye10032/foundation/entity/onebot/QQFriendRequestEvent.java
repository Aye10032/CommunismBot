package com.aye10032.foundation.entity.onebot;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dazo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QQFriendRequestEvent extends QQRequestEvent {

    private String comment;
    private String flag;

}
