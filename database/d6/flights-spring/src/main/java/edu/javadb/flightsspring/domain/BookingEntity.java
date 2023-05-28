package edu.javadb.flightsspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bookings")
public class BookingEntity {

    @Id
    @Column(name = "book_ref", length = 6)
    private String bookRef;

    @Column(name = "book_date")
    private Timestamp bookDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
}
