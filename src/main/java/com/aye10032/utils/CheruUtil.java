package com.aye10032.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheruUtil {

    char[] chars = "切卟叮咧哔唎啪啰啵嘭噜噼巴拉蹦铃".toCharArray();
    private String split_text = "\\b";
    private String word_text = "\\pP";
    private Pattern pattern;

    public CheruUtil() {
        pattern = Pattern.compile(word_text);
    }

    public String toCheru(String msg) {
        String[] msgs = msg.split(split_text);
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("切噜～♪");

        for (String word : msgs) {
            Matcher matcher = pattern.matcher(word);
            if (!matcher.find()) {
                byte[] bs = Charset.forName("GB18030").encode(word).array();
                int bit;
                StringBuilder sb = new StringBuilder();
                sb.append("切");
                for (int i = 0; i < bs.length / 2; i++) {
                    bit = bs[i] & 0x0f;
                    sb.append(chars[bit]);

                    bit = (bs[i] & 0x0f0) >> 4;
                    sb.append(chars[bit]);
                }
                stringBuilder.append(sb.toString());
            } else {
                stringBuilder.append(word);
            }
        }
        return stringBuilder.toString();
    }

    public String toStr(String msg) {
        StringBuilder stringBuilder = new StringBuilder();

        msg = msg.substring(msg.indexOf("切噜～") + 4);
        if (msg.length()%4 != 1){
            stringBuilder.append("格式不对切噜");
        }else {
            String[] msgs = msg.split(split_text);

            for (String word : msgs) {
                Matcher matcher = pattern.matcher(word);
                if (!matcher.find()) {
                    if (word.startsWith("切")) {
                        word = word.substring(1);
                        char[] thischar = word.toCharArray();
                        List<Byte> bytes = new ArrayList<>();
                        for (int i = 0; i < thischar.length; i += 2) {
                            int x1 = Arrays.binarySearch(chars, thischar[i]);
                            int x2 = Arrays.binarySearch(chars, thischar[i + 1]);
                            int x = x2 << 4 | x1;
                            bytes.add((byte) x);
                        }
                        byte[] bt = new byte[bytes.size()];
                        for (int i = 0; i < bt.length; i++) {
                            bt[i] = bytes.get(i);
                        }
                        stringBuilder.append(Charset.forName("GB18030").decode(ByteBuffer.wrap(bt)));
                    }
                } else {
                    stringBuilder.append(word);
                }
            }
        }
        return stringBuilder.toString();
    }

}
