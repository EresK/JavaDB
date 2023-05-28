package edu.javadb.flightsspring.controllertest;

import edu.javadb.flightsspring.domain.TotalAmountEntity;
import edu.javadb.flightsspring.repos.TotalAmountRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/total_amount")
public class TotalAmountController {
    private final TotalAmountRepository totalAmountRepository;

    @GetMapping
    public Page<TotalAmountEntity> totalAmount(Pageable pageable) {
        return totalAmountRepository.findAll(pageable);
    }
}
