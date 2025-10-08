package com.mpt.journal.domain.model;

import com.mpt.journal.data.ClassToMapConverter;

import java.util.Arrays;

public class LocalizedName {
    private String ru;
    private String en;

    public LocalizedName(String ru_value, String en_value) {
        ru = ru_value;
        en = en_value;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocalizedName)
            try {
                var map1 = ClassToMapConverter.convertToMap(this);
                var map2 = ClassToMapConverter.convertToMap(obj);

                return map1.keySet().stream().anyMatch(key ->
                        map2.containsKey(key) && map2.getOrDefault(key, null) == map1.get(key)
                );
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        else if (obj instanceof String) {
            try {
                var map1 = ClassToMapConverter.convertToMap(this);
                return map1.values().stream().anyMatch(value ->
                        value.equals(obj)
                );
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return super.equals(obj);
    }
}
