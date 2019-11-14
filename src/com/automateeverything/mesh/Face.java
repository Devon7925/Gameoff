package com.automateeverything.mesh;

import java.util.ArrayList;
import java.util.Arrays;

import org.joml.Vector3f;

/**
 * Face
 */
public class Face {
    final private ArrayList<Integer> indicies;
    final private Vector3f color;

    public Face(Vector3f color, Integer... indicies) {
        this.indicies = new ArrayList<>(Arrays.asList(indicies));
        this.color = color;
    }

    public Face(Integer... indicies) {
        this(new Vector3f(0.5f, 0.5f, 0.5f), indicies);
    }

    public ArrayList<Integer> getIndicies() {
        return indicies;
    }

    public Vector3f getColor() {
        return color;
    }
}