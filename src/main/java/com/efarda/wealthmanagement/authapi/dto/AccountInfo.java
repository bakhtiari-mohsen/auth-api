package com.efarda.wealthmanagement.authapi.dto;

import com.efarda.wealthmanagement.common.dto.BaseDTO;
import com.efarda.wealthmanagement.common.enums.Education;
import com.efarda.wealthmanagement.common.enums.KycTag;
import com.efarda.wealthmanagement.common.valueobject.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

import static java.util.Objects.isNull;

@Getter
@Setter
public class AccountInfo extends BaseDTO {
    private AccountId id;
    private Mobile mobile;
    private NationalityInformation nationalityInformation;
    private PostalCode postalCode;
    private EmailAddress email;
    private Education education;
    private IbanNumber ibanNumber;
    private String address;
    private String profileImageUrl;
    private int roleWeight = 0;
    private Set<KycTag> kycTags;
    private boolean acceptedTermAndConditions = false;
    private boolean emailConfirmed = false;
    public boolean phoneConfirmed;
    private BankResponse bank;

    public int age() {
        if (isNull(this.nationalityInformation.getBirthDate())) return 0;
        return LocalDate.now().getYear() - this.nationalityInformation.getBirthDate().getValue().getYear();
    }

    public boolean hasAllKycTags(Set<KycTag> kycTags) {
        return !this.kycTags.isEmpty() && this.kycTags.containsAll(kycTags);
    }

}