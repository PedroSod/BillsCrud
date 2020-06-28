package com.bills.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class BillOutputDTO {

    private String name;
    private Double originalAmount;
    private Double correctedAmount;
    private Integer overDueDays;
    private LocalDate paymentDate;

}
