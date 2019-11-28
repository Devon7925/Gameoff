package com.automateeverything.control;

import java.util.ArrayList;

import com.automateeverything.control.systems.control.ControlSystem;
import com.automateeverything.main.Window;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Player
 */
public class Player {

	private Matrix4f viewMatrix = new Matrix4f();
    public Vector3f focus = new Vector3f();
    public float angleX = (float) (Math.PI/2);
    public float angleY = 0;
    public float r = 1;
    private ArrayList<ControlSystem> controls = new ArrayList<>();

    public void update(Window window) {
        controls.forEach(n -> n.run(this, window));
    }

    public Matrix4f getViewMatrix() {
        Vector3f pos = getPos();
        viewMatrix.setLookAt(pos.x, pos.y, pos.z, focus.x, focus.y, focus.z, 0.0f, 1.0f, 0.0f);
        return viewMatrix;
    }

    public Vector3f getPos() {
        return new Vector3f(0.0f, 0.0f, 10.0f * r).rotateX(-angleY).rotateY(angleX).add(focus);
    }

    public void addControl(ControlSystem control) {
        controls.add(control);
    }

    public void move(Vector3f dp) {
        focus.add(dp);
    }
}