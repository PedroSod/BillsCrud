package com.bills.dto;

import lombok.*;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BillInputDTO {

    @NotBlank
    private String name;
    @NotNull
    private Double originalAmount;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private LocalDate paymentDate;
}
