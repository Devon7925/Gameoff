package com.automateeverything.main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_3;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;

import java.nio.FloatBuffer;

import com.automateeverything.control.Agent;
import com.automateeverything.control.InputType;
import com.automateeverything.control.Player;
import com.automateeverything.control.systems.agent.BetterJump;
import com.automateeverything.control.systems.agent.MoveRight;
import com.automateeverything.control.systems.agent.ReverseTime;
import com.automateeverything.control.systems.control.FollowControl;
import com.automateeverything.control.systems.control.OrbitControl;
import com.automateeverything.control.systems.control.ZoomControl;
import com.automateeverything.mesh.Object3D;
import com.automateeverything.mesh.World;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

public class Main {
	// JOML matrices
	Matrix4f projMatrix = new Matrix4f();

	// FloatBuffer for transferring matrices to OpenGL
	FloatBuffer fb = BufferUtils.createFloatBuffer(16);

	Window window;
	Player player = new Player();
	Agent agent;
	World world = new World();
	Clock clock = new Clock();

	void run() {
		init();
		while (!window.shouldClose())
			loop();
		finish();
	}

	void init() {
		window = new Window(400, 300, "Test");

		Globals.inputmap.put("perp button", GLFW_MOUSE_BUTTON_2, InputType.MOUSE);
		Globals.inputmap.put("orbit button", GLFW_MOUSE_BUTTON_3, InputType.MOUSE);
		Globals.inputmap.put("left", GLFW_KEY_A, InputType.KEY);
		Globals.inputmap.put("time", GLFW_KEY_A, InputType.KEY);
		Globals.inputmap.put("right", GLFW_KEY_D, InputType.KEY);
		Globals.inputmap.put("jump", GLFW_KEY_W, InputType.KEY);

		Body characterBody = new Body();
		Convex shape = new Circle(0.25);
		BodyFixture f = new BodyFixture(shape);
		f.setDensity(5);
		f.setFriction(1);
		characterBody.addFixture(f);
		characterBody.setMass(MassType.FIXED_ANGULAR_VELOCITY);
		characterBody.translate(0, 30);
		Object3D character = new Object3D("ico.obj",
				new Shader("resources/shaders/screen.vert", "resources/shaders/screen.frag"), new Vector3f(0, 30, 0),
				characterBody);
		agent = new Agent(character);
		agent.addControl(new MoveRight());
		agent.addControl(new BetterJump(window, character));
		agent.addControl(new ReverseTime(window, character));
		world.add(character);

		Body levelBody = SvgLoader.decompose("level.svg", 1.5, 0.06);
		levelBody.setMass(MassType.INFINITE);
		Object3D level = new Object3D("level.obj",
				new Shader("resources/shaders/screen.vert", "resources/shaders/screen.frag"), new Vector3f(),
				levelBody);
		world.add(level);
		player.addControl(new FollowControl(character));
		player.addControl(new OrbitControl());
		player.addControl(new ZoomControl());

		GL.createCapabilities();

		// Set the clear color
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		// Enable depth testing
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);

		window.onResize(window -> {
			// Build the projection matrix. Watch out here for integer division
			// when computing the aspect ratio!
			projMatrix.setPerspective((float) Math.toRadians(40), window.getAspect(), 0.01f, 10000.0f);
			glMatrixMode(GL_PROJECTION);
			glLoadMatrixf(projMatrix.get(fb));

			// Make the viewport always fill the whole window.
			glViewport(0, 0, window.width, window.height);
		});
	}

	void loop() {
		update();
		render();
	}

	void update() {
		clock.update();
		window.update();
		agent.update(window);
		player.update(window);
		world.update();
	}

	void render() {
		glMatrixMode(GL_MODELVIEW);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		world.render(player);

		window.swapBuffers();
	}

	void finish() {
		window.clean();
	}

	public static void main(String[] args) {
		new Main().run();
	}
}