package com.automateeverything.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.geometry.decompose.Bayazit;

/**
 * SvgLoader
 */
public class SvgLoader {

    public static Body decompose(String svg, double density, double friction) {
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
        text = text.replaceAll("(?s).*<g.*?>", "");
        text = text.replaceAll("(?s)<\\/g.*?>.*", "");
        text = text.replaceAll("<path fill.*?>", "");
        text = text.replaceAll("<path stroke.*?d=", "");
        text = text.replaceAll("fill-rule.*?\\/>", "");
        text = text.replaceAll("\\s{2,}", "");
        text = text.replaceAll("\"\"", "\"\n\"");
        text = text.replaceAll("\"", "");
        text = text.replaceAll("m|z", "");
        Body body = new Body();
        for (String line : text.split("\n")) {
            List<Vector2> differences = Arrays.asList(line.split("l")).stream().map(n -> n.split("\\s"))
                    .map(n -> new Vector2(Double.valueOf(n[0]), Double.valueOf(n[1]))).collect(Collectors.toList());
            List<Vector2> points = new ArrayList<>(differences.size());
            for (int i = 0; i < differences.size(); i++) {
                points.add(new Vector2());
                for (int j = 0; j <= i; j++) {
                    points.set(i, points.get(i).add(differences.get(j)));
                }
                points.set(i, new Vector2(points.get(i).x,-points.get(i).y));
            }
            new Bayazit().decompose(points.toArray(new Vector2[0])).forEach(n -> {
                BodyFixture f = new BodyFixture(n);
                f.setDensity(density);
                f.setFriction(friction);
                body.addFixture(f);
            });
        }
        return body;
    }
}