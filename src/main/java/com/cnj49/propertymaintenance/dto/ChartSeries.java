package com.cnj49.propertymaintenance.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Cap nhan - gia tri de day sang Chart.js.
 * Controller tra ve doi tuong nay duoi dang JSON qua Thymeleaf inline javascript.
 */
public class ChartSeries {

    private final List<String> labels = new ArrayList<>();
    private final List<BigDecimal> values = new ArrayList<>();

    public void add(String label, BigDecimal value) {
        labels.add(label);
        values.add(value == null ? BigDecimal.ZERO : value);
    }

    public void add(String label, long value) {
        labels.add(label);
        values.add(BigDecimal.valueOf(value));
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<BigDecimal> getValues() {
        return values;
    }

    public boolean isEmpty() {
        return labels.isEmpty();
    }
}
