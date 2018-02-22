package com.toxin.render;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

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

                //g2.translate(getWidth() / 2, getHeight() / 2);
                g2.setColor(Color.WHITE);

                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                double[] zBuffer = new double[img.getWidth() * img.getHeight()];
                for (int q = 0; q < zBuffer.length; q++) {
                    zBuffer[q] = Double.NEGATIVE_INFINITY;
                }

                for(Triangle t : render.renderTetrahedron()) {
                    Vertex v1 = transform.transform(t.getV1());
                    Vertex v2 = transform.transform(t.getV2());
                    Vertex v3 = transform.transform(t.getV3());

                    renderLine(v1, v2, v3, g2);
                    renderPolygon(v1, v2, v3, g2);

                    v1.x += getWidth() / 2 + 100;
                    v1.y += getHeight() / 2;
                    v2.x += getWidth() / 2 + 100;
                    v2.y += getHeight() / 2;
                    v3.x += getWidth() / 2 + 100;
                    v3.y += getHeight() / 2;

                    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
                    int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
                    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
                    int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

                    double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
                    for (int y = minY; y <= maxY; y++) {
                        for (int x = minX; x <= maxX; x++) {
                            double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                            double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                            double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                                double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                                int zIndex = y * img.getWidth() + x;
                                if (zBuffer[zIndex] < depth) {
                                    img.setRGB(x, y, t.color.getRGB());
                                    zBuffer[zIndex] = depth;
                                }
                            }
                        }
                    }
                }

                g2.drawImage(img, 0,0, null);
            }
        };

        headingSlider.addChangeListener(e -> renderPanel.repaint());
        pitchSlider.addChangeListener(e -> renderPanel.repaint());

        pane.add(renderPanel, BorderLayout.CENTER);

        frame.setSize(800, 400);
        frame.setVisible(true);
    }

    public static void renderLine(Vertex v1, Vertex v2, Vertex v3, Graphics2D g2) {
        Path2D path = new Path2D.Double();

        path.moveTo(v1.getX() + 200, v1.getY() + 200);
        path.lineTo(v2.getX() + 200, v2.getY() + 200);
        path.lineTo(v3.getX() + 200, v3.getY() + 200);

        path.closePath();
        g2.draw(path);
    }

    private static void renderPolygon(Vertex v1, Vertex v2, Vertex v3, Graphics2D g2) {

    }
}
