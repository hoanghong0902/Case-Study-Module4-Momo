package com.codegym.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferMoneyToBankRequestDto {

    private String bankAccountNumber;

    private long money;
}
