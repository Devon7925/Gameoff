package com.automateeverything.loaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.automateeverything.main.Material;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.decompose.Bayazit;

/**
 * SvgLoader
 */
public class SvgLoader {

    static double density = 1, friction = 1;
    static String name;

    public static HashMap<String, Body> decompose(String svg, HashMap<Integer, Material> materials) {
        String text = null;
        try {
            text = new String(Files.readAllBytes(Paths.get(svg)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (text == null)
            throw new IllegalStateException();
        text = text.replaceAll("(?s).*<svg.*?>", "");
        text = text.replaceAll("(?s)<\\/svg.*?>.*", "");
        text = text.replaceAll("(?s)<clipPath.*?>.*?<\\/clipPath.*?>", "");
        text = text.replaceAll("<g.*?>", "");
        text = text.replaceAll("<path fill.*?>", "");
        text = text.replaceAll("(?<=<)path", "");
        text = text.replaceAll("(?<=<)(.*?) stroke=\"#(\\d+)\"", "$1a$2");
        text = text.replaceAll("(?<=<)(.*?) stroke-width=\"(\\d+(\\.\\d+)?)\"", "$1t$2");
        text = text.replaceAll("(?<=<)(.*?) stroke-linejoin=\".*?\"", "$1");
        text = text.replaceAll("(?<=<)(.*?) stroke-linecap=\".*?\"", "$1");
        text = text.replaceAll("(?<=<)(.*?) fill-rule=\".*?\"", "$1");
        text = text.replaceAll("(?<=<)(.*?) d=\"(.*?)z\"", "$1$2");
        text = text.replaceAll("\\s{2,}", "");
        text = text.replaceAll("<title>", "n");
        text = text.replaceAll("<", "");
        HashMap<String, Body> bodys = new HashMap<>();
        for (String bodyCode : text.split("/g>")) {
            Body body = new Body();
            for (String line : bodyCode.split("/[a-zA-Z]*?>")) {
                LoadMode loadMode = LoadMode.NULL;
                String value = "";
                for (char c : line.toCharArray()) {
                    value += c;
                    for (LoadMode posMode : LoadMode.values()) {
                        if (c == posMode.c) {
                            value = value.substring(0, value.length() - 1);
                            finish(loadMode, value, materials, body);
                            value = "";
                            loadMode = posMode;
                        }
                    }
                }
                finish(loadMode, value, materials, body);
            }
            bodys.put(name, body);
        }
        return bodys;
    }

    private static void finish(LoadMode loadMode, String value, HashMap<Integer, Material> materials, Body body) {
        if (loadMode == LoadMode.MATERIAL) {
            Material material = materials.get(Integer.parseInt(value));
            density = material.getDensity();
            friction = material.getFriction();
        } else if (loadMode == LoadMode.TYPE) {
            String[] properties = value.split("\\.");
            if (properties[0].equals("1"))
                if (properties[1].equals("1"))
                    body.setMassType(MassType.FIXED_ANGULAR_VELOCITY);
                else
                    body.setMassType(MassType.NORMAL);
            else if (properties[1].equals("1"))
                body.setMassType(MassType.INFINITE);
            else
                body.setMassType(MassType.FIXED_LINEAR_VELOCITY);

        } else if (loadMode == LoadMode.POINTS) {
            List<Vector2> differences = Arrays.asList(value.split("l")).stream().map(n -> n.split("\\s"))
                    .map(n -> new Vector2(Double.valueOf(n[0]), Double.valueOf(n[1]))).collect(Collectors.toList());
            List<Vector2> points = new ArrayList<>(differences.size());
            for (int i = 0; i < differences.size(); i++) {
                points.add(new Vector2());
                for (int j = 0; j <= i; j++) {
                    points.set(i, points.get(i).add(differences.get(j)));
                }
                points.set(i, new Vector2(points.get(i).x, -points.get(i).y));
            }
            new Bayazit().decompose(points.toArray(new Vector2[0])).forEach(n -> {
                BodyFixture f = new BodyFixture(n);
                f.setDensity(density);
                f.setFriction(friction);
                body.addFixture(f);
            });
        }else if (loadMode == LoadMode.NAME){
            name = value;
        }
    }

    enum LoadMode {
        NULL('`'), POINTS('m'), MATERIAL('a'), TYPE('t'), NAME('n');

        char c;

        private LoadMode(char c) {
            this.c = c;
        }
    }
}