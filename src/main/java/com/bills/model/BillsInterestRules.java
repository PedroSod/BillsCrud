package com.bills.model;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class BillsInterestRules implements Serializable {

    private static final long serialVersionUID = -5540486332503158234L;

    @Id
    private Long id;
    @NotNull
    @Column(name = "fine_bill")
    private Double fineBill;
    @NotNull
    @Column(name = "interest_on_day_Overdue")
    private Double interestOnDayOverdue;
    @NotNull
    @Column(name = "under_day_limit")
    private Integer underDayLimit;
    @NotNull
    @Column(name = "over_day_limit")
    private Integer overDayLimit;
}
