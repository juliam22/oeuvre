package Main;
import Inputs.KeyBoardInputs;
import Inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private float xDelta = 100, yDelta = 100;
    private BufferedImage img, subimg;

    public GamePanel() {

        mouseInputs = new MouseInputs(this);

        importImg();

        setPanelSize();
        addKeyListener(new KeyBoardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

    }

    public void importImg() {
        InputStream IS = getClass().getResourceAsStream("/player_sprites.png");

        try {
            img = ImageIO.read(IS);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
    }

    public void changeXDelta(int value) {
        this.xDelta += value;
    }

    public void changeYDelta(int value) {
        this.yDelta += value;
    }

    public void setRectPosition(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        subimg = img.getSubimage(64, 320, 64, 40);
        g.drawImage(subimg, (int)xDelta, (int)yDelta, 128, 80, null);

    }

}
