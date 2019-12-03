package com.automateeverything.control;

import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.automateeverything.control.systems.control.ControlSystem;
import com.automateeverything.main.Window;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

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
    
	// JOML matrices
	Matrix4f projMatrix = new Matrix4f();

	// FloatBuffer for transferring matrices to OpenGL
	FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    public Player(Window window) {
		window.onResize(win -> {
			// Build the projection matrix. Watch out here for integer division
			// when computing the aspect ratio!
			projMatrix.setPerspective((float) Math.toRadians(40), win.getAspect(), 0.01f, 10000.0f);
			glMatrixMode(GL_PROJECTION);
			glLoadMatrixf(projMatrix.get(fb));

			// Make the viewport always fill the whole window.
			glViewport(0, 0, win.width, win.height);
		});

		// Set the clear color
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		// Enable depth testing
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
	}

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