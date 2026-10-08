package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.ChartSeries;
import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.repository.PropertyRepository;
import com.cnj49.propertymaintenance.support.TestFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Du bao gia thue/gia tri bat dong san qua 5 nam theo ty le tang gia hang nam. */
@SpringBootTest
@Transactional
class ReportServiceTest {

    @Autowired
    private ReportService reportService;
    @Autowired
    private TestFixtures fixtures;
    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    void propertyPriceProjection_appliesAnnualRateCompoundedOverFiveYears() {
        Property property = fixtures.property("PROP-T90");
        property.setBasePrice(BigDecimal.valueOf(100_000_000));
        property.setAnnualIncreaseRate(BigDecimal.valueOf(10));
        propertyRepository.save(property);

        List<Object[]> rows = reportService.propertyPriceProjection();

        Object[] row = rows.stream()
                .filter(r -> property.getName().equals(r[0]))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Khong tim thay dong du bao cho property vua tao"));

        // gia nam 5 = 100,000,000 * 1.1^5 = 161,051,000
        BigDecimal projectedYear5 = (BigDecimal) row[2];
        assertThat(projectedYear5.setScale(0, RoundingMode.HALF_UP))
                .isEqualByComparingTo(BigDecimal.valueOf(161_051_000L));

        // so lan tang gia = 1.61051
        BigDecimal multiplier = (BigDecimal) row[3];
        assertThat(multiplier.setScale(2, RoundingMode.HALF_UP))
                .isEqualByComparingTo(BigDecimal.valueOf(1.61));
    }

    @Test
    void propertyPriceProjection_skipsPropertiesWithoutBasePrice() {
        Property property = fixtures.property("PROP-T91");
        // basePrice mac dinh null/0 - khong khai bao gia

        List<Object[]> rows = reportService.propertyPriceProjection();

        assertThat(rows).noneMatch(r -> property.getName().equals(r[0]));
    }

    @Test
    void portfolioValueByYear_returnsSixPoints_currentPlusFiveYears() {
        Property property = fixtures.property("PROP-T92");
        property.setBasePrice(BigDecimal.valueOf(200_000_000));
        property.setAnnualIncreaseRate(BigDecimal.valueOf(5));
        propertyRepository.save(property);

        ChartSeries series = reportService.portfolioValueByYear();

        assertThat(series.getLabels()).hasSize(6);
        assertThat(series.getLabels().get(0)).isEqualTo("Hiện tại");
        assertThat(series.getLabels().get(5)).isEqualTo("Năm 5");
    }
}
