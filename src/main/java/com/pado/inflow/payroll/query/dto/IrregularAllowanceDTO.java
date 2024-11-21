package com.pado.inflow.payroll.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IrregularAllowanceDTO {
    private Long irregularAllowanceId;
    private String irregularAllowanceName;
    private int amount;
}
