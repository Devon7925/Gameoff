package com.automateeverything.main;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_3;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import java.util.HashMap;

import com.automateeverything.control.Agent;
import com.automateeverything.control.InputType;
import com.automateeverything.control.Player;
import com.automateeverything.control.systems.agent.BetterJump;
import com.automateeverything.control.systems.agent.MoveLeft;
import com.automateeverything.control.systems.agent.MoveRight;
import com.automateeverything.control.systems.agent.Pound;
import com.automateeverything.control.systems.agent.ReverseTime;
import com.automateeverything.control.systems.control.FollowControl;
import com.automateeverything.control.systems.control.OrbitControl;
import com.automateeverything.control.systems.control.ZoomControl;
import com.automateeverything.loaders.MaterialLoader;
import com.automateeverything.loaders.ObjLoader;
import com.automateeverything.mesh.Object3D;
import com.automateeverything.mesh.World;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.joml.Vector3f;

public class Main {

	Window window;
	Player player;
	Agent agent;
	Speaker speaker;
	World world = new World();
	Clock clock = new Clock();
	HashMap<Integer, Material> materials;

	void run() {
		init();
		while (!window.shouldClose())
			loop();
		finish();
	}

	void init() {
		window = new Window(400, 300, "Test");
		player = new Player(window);

		Globals.inputmap.put("perp button", GLFW_MOUSE_BUTTON_2, InputType.MOUSE);
		Globals.inputmap.put("orbit button", GLFW_MOUSE_BUTTON_3, InputType.MOUSE);
		Globals.inputmap.put("left", GLFW_KEY_Q, InputType.KEY);
		Globals.inputmap.put("time", GLFW_KEY_A, InputType.KEY);
		Globals.inputmap.put("right", GLFW_KEY_D, InputType.KEY);
		Globals.inputmap.put("jump", GLFW_KEY_W, InputType.KEY);
		Globals.inputmap.put("pound", GLFW_KEY_S, InputType.KEY);

		materials = MaterialLoader.decompose("materials.mtls");

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
		agent.addControl(new MoveLeft());
		agent.addControl(new Pound(window, character));
		agent.addControl(new BetterJump(window, character));
		agent.addControl(new ReverseTime());
		world.add(character);

		ObjLoader.decompose("level", materials).forEach(n -> world.add(n));

		player.addControl(new FollowControl(character));
		player.addControl(new OrbitControl());
		player.addControl(new ZoomControl());

		speaker = new Speaker();
		speaker.play("Song_1.ogg");
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
		speaker.finish();
	}

	public static void main(String[] args) {
		new Main().run();
	}
}