package com.bills.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class BillOutputDTO {

    private String name;
    private Double originalAmount;
    private Double correctedAmount;
    private Long overDueDays;
    private LocalDate paymentDate;

}
