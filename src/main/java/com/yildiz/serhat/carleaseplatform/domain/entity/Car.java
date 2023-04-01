package com.yildiz.serhat.carleaseplatform.domain.entity;

import com.yildiz.serhat.carleaseplatform.controller.request.CarRequestDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.math.BigDecimal;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Car extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "make", nullable = false)
    private String make;
    @NotNull
    @Column(name = "model", nullable = false)
    private String model;
    @NotNull
    @Column(name = "version", nullable = false)
    private String version;
    @NotNull
    @Column(name = "number_doors", nullable = false)
    private int numberOfDoors;
    @NotNull
    @Column(name = "co2_emission", nullable = false)
    private String emission;
    @NotNull
    @Column(name = "gross_price", nullable = false)
    private BigDecimal grossPrice;
    @NotNull
    @Column(name = "nett_price", nullable = false)
    private BigDecimal nettPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    private LeaseRate leaseRate;

    public static Car buildCarFromRequest(CarRequestDTO request) {
        return Car.builder()
                .make(request.make())
                .model(request.model())
                .version(request.version())
                .numberOfDoors(request.numberOfDoors())
                .emission(request.emission())
                .grossPrice(request.grossPrice())
                .nettPrice(request.nettPrice())
                .leaseRate(LeaseRate.builder()
                        .mileAge(request.leaseRate().mileAge())
                        .duration(request.leaseRate().duration())
                        .interestRate(request.leaseRate().interestRate())
                        .build())
                .build();
    }

    public static Car updateCar(Car car, CarRequestDTO requestDTO) {
        car.setEmission(requestDTO.emission());
        car.setMake(requestDTO.make());
        car.setModel(requestDTO.model());
        car.setGrossPrice(requestDTO.grossPrice());
        car.setNettPrice(requestDTO.nettPrice());
        car.setNumberOfDoors(requestDTO.numberOfDoors());
        car.setVersion(requestDTO.version());

        if (nonNull(car.getLeaseRate())) {
            car.getLeaseRate().setMileAge(requestDTO.leaseRate().mileAge());
            car.getLeaseRate().setDuration(requestDTO.leaseRate().duration());
            car.getLeaseRate().setInterestRate(requestDTO.leaseRate().interestRate());
        }

        if (isNull(car.getLeaseRate())) {
            LeaseRate rate = LeaseRate.builder()
                    .mileAge(requestDTO.leaseRate().mileAge())
                    .duration(requestDTO.leaseRate().duration())
                    .interestRate(requestDTO.leaseRate().interestRate())
                    .build();
            car.setLeaseRate(rate);
        }
        return car;
    }
}
