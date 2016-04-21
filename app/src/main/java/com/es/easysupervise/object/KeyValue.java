package com.es.easysupervise.object;

/**
 * Created by xiaoxi on 15-12-15.
 */
public class KeyValue {

    public String Key;
    public String Value;

    public String getKey() {
        return Key;
    }

    public String getValue() {
        return Value;
    }

    public void setKey(String key) {
        Key = key;
    }

    public void setValue(String value) {
        Value = value;
    }

    public KeyValue(String key,String value)
    {
        this.Key = key;
        this.Value = value;
    }
    public KeyValue()
    {

    }
}
