package ru.otus.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PlayFrame {
    public static void main(String[] args) throws IOException, InterruptedException {
        ImageIcon avatar = loadImage("/player.png", 60, 60);
        ImageIcon shirt = loadImage("/card-shirt.png", 100, 100);
        ImageIcon card = loadImage("/card/3.png", 100, 100);
        ImageIcon card2 = loadImage("/card/4.png", 100, 100);

        PlayerComponent playerReal = new PlayerComponent("Maxim", avatar, false);
        PlayerComponent playerComputer1 = new PlayerComponent("Computer1", avatar, true);

        CardComponent cardComponent = new CardComponent(card);
        CardComponent cardComponent2 = new CardComponent(card2);

        CardComponent cardComponent3 = new CardComponent(card);
        CardComponent cardComponent4 = new CardComponent(card2);

        CardComponent cardComponent5 = new CardComponent(card);
        CardComponent cardComponent6 = new CardComponent(card2);

        CardComponent cardShirt = new CardComponent(shirt);

        JFrame frame = new JFrame("Black Jack");

//        JPanel cards = new JPanel();
        JPanel cards = new JPanel();
        cards.setLayout(new BoxLayout(cards, BoxLayout.X_AXIS));
//        cardComponents.add(cardShirt);
//        cardComponents.add(cardComponent);
//        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));

//        frame.setLayout(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

//        JPanel panel = new JPanel() {
//            @Override
//            public void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                try {
//                    Image image = ImageIO.read(PlayFrame.class.getResourceAsStream("/background-table.png"));
//                    g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        };
//
//        frame.setContentPane(panel);

        frame.add(BorderLayout.SOUTH, playerReal);
        frame.add(BorderLayout.NORTH, playerComputer1);

//        cards.add(cardShirt);
//        cards.add(cardComponent);
//        cardComponent.setVisible(false);

//        frame.add(BorderLayout.EAST, cardShirt);

//        cards.add(cardShirt);
//        cards.add(cardComponent);

        frame.add(BorderLayout.EAST, cardShirt);

//        frame.add(cardComponent);

//        Thread.sleep(1000);

//        move(cardComponent, new Point(10, 10));

//        cardComponent.setBounds(0, 0, cardComponent.getWidth(), cardComponent.getHeight());

        frame.setVisible(true);

//        animate(cardComponent, new Point(10, 10), 10, 50);
    }

    private static ImageIcon loadImage(String name, int scaledWidth, int scaledHeight) {
        try {
            Image image = ImageIO.read(PlayFrame.class.getResourceAsStream(name));
            return new ImageIcon(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void animate(JComponent component, Point newPoint, int frames, int interval) {
        Rectangle compBounds = component.getBounds();

        Point oldPoint = new Point(compBounds.x, compBounds.y);
        Point animFrame = new Point((newPoint.x - oldPoint.x) / frames, (newPoint.y - oldPoint.y) / frames);

        new Timer(interval, new ActionListener() {
            int currentFrame = 0;
            public void actionPerformed(ActionEvent e) {
                component.setBounds(oldPoint.x + (animFrame.x * currentFrame),
                        oldPoint.y + (animFrame.y * currentFrame),
                        compBounds.width,
                        compBounds.height);

                if (currentFrame != frames)
                    currentFrame++;
                else
                    ((Timer)e.getSource()).stop();
            }
        }).start();
    }

    private static void move(JComponent component, Point newPoint) {
        Rectangle compBounds = component.getBounds();

                component.setBounds(newPoint.x,
                        newPoint.y,
                        compBounds.width,
                        compBounds.height);
    }

    private static Point pointCard(JComponent component) {
        int x = component.getX();
        int y = component.getY();

        return new Point(x, y);
    }
}
