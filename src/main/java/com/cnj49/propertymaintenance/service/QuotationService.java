package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.QuotationForm;
import com.cnj49.propertymaintenance.entity.Quotation;

import java.util.List;
import java.util.Optional;

/** Quan ly bao gia va quy trinh phe duyet bao gia. */
public interface QuotationService {

    List<Quotation> findByRequest(Long requestId);

    List<Quotation> findByContractor(Long contractorId);

    Quotation findById(Long id);

    QuotationForm toForm(Quotation quotation);

    /** Them bao gia; totalAmount duoc tinh lai, khong lay tu form. */
    Quotation create(QuotationForm form);

    /** Chi sua duoc bao gia con o trang thai PENDING. */
    Quotation update(Long id, QuotationForm form);

    /**
     * BR02 + BR03: duyet mot bao gia, cac bao gia PENDING con lai chuyen REJECTED.
     * Yeu cau bao tri chuyen sang QUOTATION_APPROVED.
     */
    Quotation approve(Long id);

    Quotation reject(Long id);

    /** Chi xoa duoc bao gia chua duyet. */
    void delete(Long id);

    Optional<Quotation> findApprovedByRequest(Long requestId);
}
