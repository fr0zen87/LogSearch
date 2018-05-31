package com.example.logsearch.utils.formatters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String text) {
        return LocalDateTime.parse(text);
    }

    @Override
    public String marshal(LocalDateTime object) {
        return object != null ? object.toString() : null;
    }
}
