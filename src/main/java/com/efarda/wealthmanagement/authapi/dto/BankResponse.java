package com.efarda.wealthmanagement.authapi.dto;

import com.efarda.wealthmanagement.common.valueobject.Bank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankResponse {
  private String persianName;
  private String code;
  private String englishName;
  private String swiftCode;
  private String[] accountPrefixes;
  private String englishBankName;
  private String website;
  private String logoUrl;

  public BankResponse(Bank bank) {
    this.persianName = bank.getName();
    this.code = bank.getCode();
    this.englishName = bank.getEnglishName();
    this.swiftCode = bank.getSwiftCode();
    this.accountPrefixes = bank.getCardPrefixList();
    this.englishBankName = bank.getEnglishName();
    this.website = bank.getWebsite();
    this.logoUrl = bank.getLogoUrl();
  }
}
