package com.cnj49.propertymaintenance.support;

import com.cnj49.propertymaintenance.entity.*;
import com.cnj49.propertymaintenance.enums.*;
import com.cnj49.propertymaintenance.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Tao nhanh du lieu Property/Unit/Category/Contractor dung chung cho cac test nghiep vu. */
@Component
public class TestFixtures {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private MaintenanceCategoryRepository categoryRepository;
    @Autowired
    private ContractorRepository contractorRepository;

    public Property property(String code) {
        Property p = new Property();
        p.setCode(code);
        p.setName("BĐS test " + code);
        p.setPropertyType(PropertyType.MINI_APARTMENT);
        p.setAddress("123 Test Street");
        p.setNumberOfFloors(5);
        p.setNumberOfUnits(10);
        p.setArea(BigDecimal.valueOf(100));
        p.setOperationDate(LocalDate.now().minusYears(1));
        p.setStatus(PropertyStatus.ACTIVE);
        return propertyRepository.save(p);
    }

    public Unit unit(Property property, String code) {
        Unit u = new Unit();
        u.setProperty(property);
        u.setCode(code);
        u.setName("Phòng " + code);
        u.setFloorNumber(1);
        u.setUnitType(UnitType.ROOM);
        u.setArea(BigDecimal.valueOf(25));
        u.setStatus(UnitStatus.OCCUPIED);
        return unitRepository.save(u);
    }

    public MaintenanceCategory category(String name) {
        return categoryRepository.save(new MaintenanceCategory(name, "Danh mục test " + name));
    }

    public Contractor contractor(String code) {
        Contractor c = new Contractor();
        c.setContractorCode(code);
        c.setCompanyName("Nhà thầu " + code);
        c.setContactPerson("Người liên hệ");
        c.setPhone("0912345678");
        c.setEmail(code.toLowerCase() + "@test.vn");
        c.setSpecialization(Specialization.MULTI_SERVICE);
        c.setRating(BigDecimal.valueOf(4.5));
        c.setStatus(ContractorStatus.ACTIVE);
        return contractorRepository.save(c);
    }
}
