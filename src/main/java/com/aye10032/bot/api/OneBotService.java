package com.aye10032.bot.api;

import com.aye10032.config.FeignRequestInterceptor;
import com.aye10032.foundation.entity.onebot.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "onebot", url = "${bot.onebot.api.url}", configuration = FeignRequestInterceptor.class)
public interface OneBotService {

    @GetMapping("/get_login_info")
    QQResponse<QQLoginInfo> getLoginInfo();

    @PostMapping("/send_group_msg")
    QQResponse<QQSendMessageResponse> sendGroupMsg(@RequestBody QQSendGroupMessageRequest request);

    @PostMapping("/send_private_msg")
    QQResponse<QQSendMessageResponse> sendPrivateMsg(@RequestBody QQSendPrivateMessageRequest request);

    @PostMapping("/set_group_ban")
    QQResponse<String> setGroupBan(@RequestBody QQSetGroupBanRequest request);

    // set_group_whole_ban
    @PostMapping("/set_group_whole_ban")
    QQResponse<String> setGroupWholeBan(@RequestBody QQSetGroupWholeBanRequest request);

    // set_friend_add_request
    @PostMapping("/set_friend_add_request")
    QQResponse<String> setFriendAddRequest(@RequestBody QQSetFriendAddRequest request);

    // get_group_info
    @PostMapping("/get_group_info")
    QQResponse<QQGroupInfo> getGroupInfo(@RequestBody QQGetGroupInfoRequest request);

    // get_group_list
    @PostMapping("/get_group_list")
    QQResponse<List<QQGroupInfo>> getGroupList(@RequestBody QQGetGroupListRequest request);

    // get_stranger_info
    @PostMapping("/get_stranger_info")
    QQResponse<QQStrangerInfo> getStrangerInfo(@RequestBody QQGetStrangerInfoRequest request);

    // delete_msg
    @PostMapping("/delete_msg")
    QQResponse<String> deleteMsg(@RequestBody QQMessageIdRequest request);

    // set_essence_msg
    @PostMapping("/set_essence_msg")
    QQResponse<Map<String, Object>> setEssenceMsg(@RequestBody QQMessageIdRequest request);

}
