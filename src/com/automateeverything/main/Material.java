package com.automateeverything.main;

/**
 * Materials
 */
public class Material {
    double density;
    double friction;

    public Material(double density, double friction) {
        this.density = density;
        this.friction = friction;
    }

    public double getDensity() {
        return density;
    }

    public double getFriction() {
        return friction;
    }
}