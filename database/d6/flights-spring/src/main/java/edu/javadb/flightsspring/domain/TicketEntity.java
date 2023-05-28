package edu.javadb.flightsspring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tickets")
public class TicketEntity {
    @Id
    @Column(name = "ticket_no", length = 13)
    private String ticketNo;

    @Column(name = "book_ref", length = 6)
    private String bookRef;

    @Column(name = "passenger_id")
    private String passengerId;

    @Column(name = "passenger_name")
    private String passengerName;

    @Column(name = "contact_data")
    private String contactData;
}
