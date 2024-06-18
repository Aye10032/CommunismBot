package com.aye10032.foundation.entity.onebot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author dazo
 */
@Data
public class QQResponse<T> {

    @JsonProperty("status")
    private String status;
    @JsonProperty("retcode")
    private Integer retcode;
    @JsonProperty("data")
    private T data;
    @JsonProperty("message")
    private String message;
    @JsonProperty("wording")
    private String wording;


}
