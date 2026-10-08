package com.cnj49.propertymaintenance.controller;

import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.repository.PropertyRepository;
import com.cnj49.propertymaintenance.support.TestFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Form sua bat dong san phai giu dung ngay van hanh va cac truong gia. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin", roles = "ADMIN")
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestFixtures fixtures;
    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    void editForm_rendersOperationDateInIsoFormatForDateInput() throws Exception {
        Property property = fixtures.property("PROP-T80");
        property.setOperationDate(LocalDate.of(2022, 10, 7));
        propertyRepository.save(property);

        mockMvc.perform(get("/properties/{id}/edit", property.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("value=\"2022-10-07\"")));
    }

    @Test
    void update_savesOperationDateAndPriceFieldsFromForm() throws Exception {
        Property property = fixtures.property("PROP-T81");

        mockMvc.perform(post("/properties/{id}", property.getId()).with(csrf())
                        .param("name", "BĐS sau khi sửa")
                        .param("propertyType", "MINI_APARTMENT")
                        .param("address", "456 Test Street")
                        .param("numberOfFloors", "6")
                        .param("numberOfUnits", "12")
                        .param("area", "120")
                        .param("status", "ACTIVE")
                        .param("operationDate", "2020-05-15")
                        .param("basePrice", "1000000000")
                        .param("annualIncreaseRate", "6.5"))
                .andExpect(status().is3xxRedirection());

        Property saved = propertyRepository.findById(property.getId()).orElseThrow();
        assertThat(saved.getOperationDate()).isEqualTo(LocalDate.of(2020, 5, 15));
        assertThat(saved.getBasePrice()).isEqualByComparingTo(BigDecimal.valueOf(1_000_000_000L));
        assertThat(saved.getAnnualIncreaseRate()).isEqualByComparingTo(new BigDecimal("6.5"));
    }
}
