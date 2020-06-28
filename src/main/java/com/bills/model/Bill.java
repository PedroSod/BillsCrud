package com.bills.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Bill implements Serializable {

    private static final long serialVersionUID = 2008155343465429527L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Column(name = "original_amount")
    private Double originalAmount;
    @NotNull
    @Column(name = "corrected_amount")
    private Double correctedAmount;
    @NotNull
    @Column(name = "overdue_days")
    private Integer overDueDays;
    @NotNull
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    @NotNull
    @Column(name = "due_date")
    private LocalDate dueDate;
}
