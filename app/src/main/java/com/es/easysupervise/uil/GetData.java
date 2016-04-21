package com.es.easysupervise.uil;

import android.util.Xml;

import com.es.easysupervise.object.KeyValue;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by xiaoxi on 15-12-15.
 */
public class GetData {

    /**
     * 读取省
     * @param xml
     * @return
     * @throws Exception
     */
    public static ArrayList<KeyValue> getCanton(InputStream xml) throws Exception {
        ArrayList<KeyValue> keyValues = null;
        KeyValue keyValue = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "GBK"); //为Pull解释器设置要解析的XML数据
        int event = pullParser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    keyValues = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("CODEID".equals(pullParser.getName())) {
                        keyValue = new KeyValue();
                        String key = pullParser.nextText();
                        keyValue.setKey(key);
                    }
                    if ("CODENAME".equals(pullParser.getName())) {
                        String value = pullParser.nextText();
                        keyValue.setValue(value);
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("ROW".equals(pullParser.getName())) {
                        keyValues.add(keyValue);
                        keyValue = null;
                    }
                    break;

            }

            event = pullParser.next();
        }

        return keyValues;
    }

    public static ArrayList<KeyValue> getMediaType(InputStream xml) throws Exception {
        ArrayList<KeyValue> keyValues = null;
        KeyValue keyValue = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "GBK"); //为Pull解释器设置要解析的XML数据
        int event = pullParser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    keyValues = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("TYPE_ID".equals(pullParser.getName())) {
                        keyValue = new KeyValue();
                        String key = pullParser.nextText();
                        keyValue.setKey(key);
                    }
                    if ("TYPE_NAME".equals(pullParser.getName())) {
                        String value = pullParser.nextText();
                        keyValue.setValue(value);
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("ROW".equals(pullParser.getName())) {
                        keyValues.add(keyValue);
                        keyValue = null;
                    }
                    break;

            }

            event = pullParser.next();
        }

        return keyValues;
    }

    public static ArrayList<KeyValue> getAreaType(InputStream xml) throws Exception {
        ArrayList<KeyValue> keyValues = null;
        KeyValue keyValue = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "GBK"); //为Pull解释器设置要解析的XML数据
        int event = pullParser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    keyValues = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("TYPE_ID".equals(pullParser.getName())) {
                        keyValue = new KeyValue();
                        String key = pullParser.nextText();
                        keyValue.setKey(key);
                    }
                    if ("TYPE_NAME".equals(pullParser.getName())) {
                        String value = pullParser.nextText();
                        keyValue.setValue(value);
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("ROW".equals(pullParser.getName())) {
                        keyValues.add(keyValue);
                        keyValue = null;
                    }
                    break;

            }

            event = pullParser.next();
        }

        return keyValues;
    }

    public static ArrayList<KeyValue> getAdStatus(InputStream xml) throws Exception {
        ArrayList<KeyValue> keyValues = null;
        KeyValue keyValue = null;
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "GBK"); //为Pull解释器设置要解析的XML数据
        int event = pullParser.getEventType();

        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    keyValues = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("fwmb_ID".equals(pullParser.getName())) {
                        keyValue = new KeyValue();
                        String key = pullParser.nextText();
                        keyValue.setKey(key);
                    }
                    if ("fwmb_NAME".equals(pullParser.getName())) {
                        String value = pullParser.nextText();
                        keyValue.setValue(value);
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("ROW".equals(pullParser.getName())) {
                        keyValues.add(keyValue);
                        keyValue = null;
                    }
                    break;

            }

            event = pullParser.next();
        }

        return keyValues;
    }


}
