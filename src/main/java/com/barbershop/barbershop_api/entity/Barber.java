package com.barbershop.barbershop_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import lombok.Setter;

@Entity
@Table(name = "barbers")
public class Barber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(nullable = false)
    private String specialty;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getSpecialty() {
        return specialty;
    }

    public Barber() {
    }

    public Barber(User user, String specialty) {
        this.user = user;
        this.specialty = specialty;
    }

}

