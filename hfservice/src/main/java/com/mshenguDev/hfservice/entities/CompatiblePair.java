package com.mshenguDev.hfservice.entities;

import jakarta.persistence.*;

@Entity
public class CompatiblePair {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "component_A")
    private Component componentA;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "component_B")
    private Component componentB;

    private CompatibilityStatus status;

    public CompatiblePair(){};
    public CompatiblePair(Component componentA, Component componentB) {
        this.componentA = componentA;
        this.componentB = componentB;
        this.status = CompatibilityStatus.COMPATIBLE;
    }

    public void setComponentA(Component componentA) {
        this.componentA = componentA;
    }

    public void setComponentB(Component componentB) {
        this.componentB = componentB;
    }

    public void setStatus(CompatibilityStatus status) {
        this.status = status;
    }

    public Component getComponentA(){return componentA;}
    public Component getComponentB(){return componentB;}
    public CompatibilityStatus getStatus(){return status;}




}
