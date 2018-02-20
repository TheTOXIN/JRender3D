package com.toxin.render;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Render {

    private List<Triangle> figure = new ArrayList<>();

    public List<Triangle> renderTetrahedron() {
        figure.clear();

        figure.add(new Triangle(
            new Vertex(100, 100, 100),
            new Vertex(-100, -100, 100),
            new Vertex(-100, 100, -100),
            Color.BLUE
            ));

        figure.add(new Triangle(
            new Vertex(100, 100, 100),
            new Vertex(-100, -100, 100),
            new Vertex(100, -100, -100),
            Color.RED
        ));

        figure.add(new Triangle(
            new Vertex(-100, 100, -100),
            new Vertex(100, -100, -100),
            new Vertex(100, 100, 100),
            Color.YELLOW
        ));

        figure.add(new Triangle(
            new Vertex(-100, 100, -100),
            new Vertex(-100, -100, -100),
            new Vertex(-100, -100, 100),
            Color.GREEN
        ));

        return figure;
    }

}
