/*
package com.aye10032;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class TeamspeakBot {

    Zibenbot bot;
    public TS3Api api;

    public TeamspeakBot(Zibenbot zibenbot){
        bot = zibenbot;
    }

    public void setup() {
        final TS3Config config = new TS3Config();
        config.setHost("106.14.197.120");
        config.setEnableCommunicationsLogging(true);

        final TS3Query query = new TS3Query(config);
        query.connect();

        api = query.getApi();
        try {
            api.login("serveradmin", "+mj5iqBQ");
        }catch (Exception e){
            System.out.println(e);
        }
        api.selectVirtualServerById(1);
        api.setNickname("Bot");
        api.sendChannelMessage(api.getChannelByNameExact("MHW", true).getId(), "Bot is online!");

        // Get our own client ID by running the "whoami" command
        final int clientId = api.whoAmI().getId();

        // Listen to chat in the channel the query is currently in
        // As we never changed the channel, this will be the default channel of the server
        api.registerEvent(TS3EventType.TEXT_CHANNEL, 0);
        // Register the event listener
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                // Only react to channel messages not sent by the query itself
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    bot.teamspeakMsg(e.getTargetClientId(), e.getInvokerId(), e.getMessage());
                }
            }
        });
    }



}
*/
