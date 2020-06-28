package com.bills.service;

import com.bills.exception.BillInterestRulesNotFoundException;
import com.bills.model.BillsInterestRules;
import com.bills.repository.BillsInterestRulesRepository;
import org.springframework.stereotype.Service;

@Service
public class BillInterestRulesService {

    private BillsInterestRulesRepository billsInterestRulesRepository;

    public BillInterestRulesService(BillsInterestRulesRepository billsInterestRulesRepository) {
        this.billsInterestRulesRepository = billsInterestRulesRepository;
    }

    public BillsInterestRules findBillsInterestRules(Integer overDueDays){
        return billsInterestRulesRepository.findByUnderDayLimitLessThanEqualAndOverDayLimitGreaterThanEqual(overDueDays, overDueDays)
                .orElseThrow(() -> new BillInterestRulesNotFoundException());
    }
}
