package com.toxin.render;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class Main {
    public static void main(String[] args) {
        System.out.println("3D TEXT!!!");

        Render render = new Render();

        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        JSlider headingSlider = new JSlider(0, 360, 180);
        pane.add(headingSlider, BorderLayout.SOUTH);

        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(pitchSlider, BorderLayout.EAST);

        JPanel renderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;

                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                double heading = Math.toRadians(headingSlider.getValue());
                double pitch = Math.toRadians(pitchSlider.getValue());

                Matrix headingTrandform = new Matrix(new double[] {
                    Math.cos(heading),  0, Math.sin(heading),
                    0,                  1, 0,
                    -Math.sin(heading), 0, Math.cos(heading)
                });

                Matrix pitchTransform = new Matrix(new double[] {
                    1,  0,               0,
                    0,  Math.cos(pitch), Math.sin(pitch),
                    0, -Math.sin(pitch), Math.cos(pitch)
                });

                Matrix transform = headingTrandform.multiply(pitchTransform);

                g2.translate(getWidth() / 2, getHeight() / 2);
                g2.setColor(Color.WHITE);

                for(Triangle t : render.renderTetrahedron()) {
                    Vertex v1 = transform.transform(t.getV1());
                    Vertex v2 = transform.transform(t.getV2());
                    Vertex v3 = transform.transform(t.getV3());

                    Path2D path = new Path2D.Double();

                    path.moveTo(v1.getX(), v1.getY());
                    path.lineTo(v2.getX(), v2.getY());
                    path.lineTo(v3.getX(), v3.getY());

                    path.closePath();
                    g2.draw(path);
                }
            }
        };

        headingSlider.addChangeListener(e -> renderPanel.repaint());
        pitchSlider.addChangeListener(e -> renderPanel.repaint());

        pane.add(renderPanel, BorderLayout.CENTER);

        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
