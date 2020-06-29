package com.bills.repository;

import com.bills.model.BillsInterestRules;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillsInterestRulesRepository extends CrudRepository<BillsInterestRules, Long> {

    Optional<BillsInterestRules>
    findByUnderDayLimitLessThanEqualAndOverDayLimitGreaterThanEqual(Long firstValue, Long secondValue );
}
