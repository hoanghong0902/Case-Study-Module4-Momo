package com.codegym.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferMoneyToMomoRequestDto {

    private String phoneNumber;

    private long money;
}
