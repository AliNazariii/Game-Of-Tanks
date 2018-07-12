/*** In The Name of Allah ***/
package game.template.bufferstrategy;

import game.Utils.SharedData;
import game.Utils.Utility;
import game.elements.*;
import game.map.Camera;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.*;

/**
 * The window on which the rendering is performed.
 * This structure uses the modern BufferStrategy approach for
 * double-buffering; actually, it performs triple-buffering!
 * For more information on BufferStrategy check out:
 * http://docs.oracle.com/javase/tutorial/extra/fullscreen/bufferstrategy.html
 * http://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferStrategy.html
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class GameFrame extends JFrame
{

    //    public static final int GAME_HEIGHT = 520;                  // custom game resolution
//    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio
//    public static final int GAME_HEIGHT = 1000;
//    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 1024;
    public static final int GAME_WIDTH = 1820;

    private BufferStrategy bufferStrategy;

    public GameFrame(String title)
    {
        super(title);
        this.setUndecorated(false);
        if (false) // Full screen mode
        {
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(this.MAXIMIZED_BOTH);
        }
        else // Window mode
        {
            // Size of the frame.
            this.setSize(GAME_WIDTH, GAME_HEIGHT);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }
        // Initialize the JFrame ...
        //
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Utility.cursor, new Point(20, 14), "custom cursor"));
    }

    /**
     * This must be called once after the JFrame is shown:
     * frame.setVisible(true);
     * and before any rendering is started.
     */
    public void initBufferStrategy()
    {
        // Triple-buffering
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();
    }


    /**
     * Game rendering with triple-buffering using BufferStrategy.
     */
    public void render(GameState state)
    {
        // Get a new graphics context to render the current frame
        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        try
        {
            graphics.translate(-state.camera.getX(), -state.camera.getY());
            // Do the rendering
            doRendering(graphics, state);

            graphics.translate(state.camera.getX(), state.camera.getY());

            drawIndependents(graphics, state);
        }
        finally
        {
            // Dispose the graphics, because it is no more needed
            graphics.dispose();
        }
        // Display the buffer
        bufferStrategy.show();
        // Tell the system to do the drawing NOW;
        // otherwise it can take a few extra ms and will feel jerky!
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Rendering all game elements based on the game state.
     */
    private void doRendering(Graphics2D g2d, GameState state)
    {
        AffineTransform gameTransform = g2d.getTransform();

        runAnimations();

        //render map
        state.objects.getMap().render(g2d);

        drawBullets(state, g2d, gameTransform);
        drawPlayers(state, g2d, gameTransform);
        drawAITanks(state, g2d, gameTransform);
        drawTurrets(state, g2d, gameTransform);
        drawBuriedRobots(state, g2d, gameTransform);
        drawUpgrades(state, g2d);
    }

    private void drawBullets(GameState state, Graphics2D g2d, AffineTransform gameTransform)
    {
        //draw bullets
        ArrayList<Bullet> bullets = state.objects.getBullets();
        for (int i = 0; i < bullets.size(); i++)
        {
            AffineTransform bulletTransform = g2d.getTransform();
            double bulletAngle;
            if (bullets.get(i).isThrown())
            {
                bulletAngle = bullets.get(i).getThrownAngle();
            }
            else
            {
                bulletAngle = bullets.get(i).getShootDirectionAngle();
                if (bullets.get(i).getTargetX() < bullets.get(i).getX())
                {
                    bulletAngle += Math.PI;
                }
                bullets.get(i).setThrownAngle(bulletAngle);
                bullets.get(i).setThrown(true);
            }
            bulletTransform.rotate(bulletAngle, (int) bullets.get(i).getX() + 50, (int) bullets.get(i).getY() + 50);
            g2d.setTransform(bulletTransform);
            if (bullets.get(i).getId().equals(ObjectId.HeavyBullet))
            {
                g2d.drawImage(Utility.heavyBullet, (int) bullets.get(i).getX() + 52, (int) bullets.get(i).getY() + 50, null);
            }
            else
            {
                g2d.drawImage(Utility.lightBullet, (int) bullets.get(i).getX() + 52, (int) bullets.get(i).getY() + 50, null);
            }
            g2d.setTransform(gameTransform);
        }
        //
    }

    private void drawIndependents(Graphics2D g2d, GameState state)
    {
        //draw number of bullets
        g2d.drawImage(Utility.numberOfHeavyBullet, 10, 10, null);
        String heavyBullets;
        int numberOfHeavyBullets = state.objects.getPlayers().get(0).getMissileGun().getAmmo();
        if (numberOfHeavyBullets / 10 == 0)
        {
            heavyBullets = "0" + numberOfHeavyBullets;
        }
        else
        {
            heavyBullets = String.valueOf(numberOfHeavyBullets);
        }
        g2d.setFont(new Font("Titillium Web", Font.BOLD, 20));
        g2d.setColor(Color.green);
        g2d.drawString(heavyBullets, 40, 60);

        g2d.drawImage(Utility.numberOfLightBullet, 10, 85, null);
        String lightBullets;
        int numberOfLightBullets = state.objects.getPlayers().get(0).getMachineGun().getAmmo();
        if (numberOfLightBullets / 10 == 0)
        {
            lightBullets = "0" + numberOfLightBullets;
        }
        else
        {
            lightBullets = String.valueOf(numberOfLightBullets);
        }
        g2d.setFont(new Font("Titillium Web", Font.BOLD, 20));
        g2d.setColor(Color.green);
        g2d.drawString(lightBullets, 40, 135);
        //
    }

    private void runAnimations()
    {
        Utility.tankAnimation.runAnimation();
        Utility.buriedRobotAnimation.runAnimation();
    }

    private void drawBuriedRobots(GameState state, Graphics2D g2d, AffineTransform gameTransform)
    {
        //draw robots
        ArrayList<BuriedRobot> robots = state.objects.getRobots();
        for (int i = 0; i < robots.size(); i++)
        {
            if (robots.get(i).isActivated())
            {
                AffineTransform gunTrans = g2d.getTransform();
                gunTrans.rotate(state.objects.getRobots().get(i).getAngle(),
                        state.objects.getRobots().get(i).getX() + 50,
                        state.objects.getRobots().get(i).getY() + 50);

                g2d.setTransform(gunTrans);
                Utility.buriedRobotAnimation.drawAnimation(g2d, (int) robots.get(i).getX(), (int) robots.get(i).getY(), 0);
                g2d.setTransform(gameTransform);
            }
        }
        //
    }

    private void drawAITanks(GameState state, Graphics2D g2d, AffineTransform gameTransform)
    {
        //draw tanks
        ArrayList<AITank> tanks = state.objects.getTanks();
        for (int i = 0; i < tanks.size(); i++)
        {
            int centerX = (int) state.objects.getTanks().get(i).getX() + state.objects.getTanks().get(i).TANK_WIDTH / 2; //this is the X center of the player
            int centerY = (int) state.objects.getTanks().get(i).getY() + state.objects.getTanks().get(i).TANK_HEIGHT / 2; //this is the Y center of the player

            AffineTransform tankTrans = g2d.getTransform();
            tankTrans.rotate(state.objects.getTanks().get(i).getTankAngle(), centerX, centerY);
            g2d.setTransform(tankTrans);

            g2d.drawImage(Utility.AITank,
                    (int) state.objects.getTanks().get(i).getX(), //this is the X upper left corner of the tile
                    (int) state.objects.getTanks().get(i).getY(), //this is the Y upper left corner of the tile
                    null);
            g2d.setTransform(gameTransform);
        }
        //
    }

    private void drawTurrets(GameState state, Graphics2D g2d, AffineTransform gameTransform)
    {
        //draw turrets
        ArrayList<Turret> turrets = state.objects.getTurrets();
        for (int i = 0; i < turrets.size(); i++)
        {
            //before
            //            g2d.drawImage(Utility.turret, null, (int) turrets.get(i).getX() - turrets.get(i).TURRET_WIDTH / 2, (int) turrets.get(i).getY() - turrets.get(i).TURRET_HEIGHT / 2);
            //after
            g2d.drawImage(Utility.tmpTurret, null, (int) turrets.get(i).getX(), (int) turrets.get(i).getY());

            AffineTransform gunTrans = g2d.getTransform();
            gunTrans.rotate(state.objects.getTurrets().get(i).getGunAngle(),
                    state.objects.getTurrets().get(i).getX() + 50,
                    state.objects.getTurrets().get(i).getY() + 50);

            g2d.setTransform(gunTrans);
            g2d.drawImage(Utility.tmpTurretGun,
                    (int) state.objects.getTurrets().get(i).getX() + 20, //this is the X upper left corner of the tile
                    (int) state.objects.getTurrets().get(i).getY(), //this is the Y upper left corner of the tile
                    null);
            g2d.setTransform(gameTransform);
        }
        //
    }

    private void drawPlayers(GameState state, Graphics2D g2d, AffineTransform gameTransform)
    {
        //draw player tank
        for (int i = 0; i < state.objects.getPlayers().size(); i++)
        {
            int centerX = (int) state.objects.getPlayers().get(i).getX() + state.objects.getPlayers().get(i).TANK_WIDTH / 2; //this is the X center of the player
            int centerY = (int) state.objects.getPlayers().get(i).getY() + state.objects.getPlayers().get(i).TANK_HEIGHT / 2; //this is the Y center of the player

            AffineTransform tankTransform = g2d.getTransform();
            tankTransform.rotate(state.objects.getPlayers().get(i).getTankAngle(), centerX, centerY);
            g2d.setTransform(tankTransform);

            if (!state.isKeyDOWN() & !state.isKeyLEFT() & !state.isKeyRIGHT() & !state.isKeyUP())
            {
                g2d.drawImage(Utility.tank02,
                        (int) state.objects.getPlayers().get(i).getX(), //this is the X upper left corner of the tile
                        (int) state.objects.getPlayers().get(i).getY(), //this is the Y upper left corner of the tile
                        null);
            }
            else
            {
                Utility.tankAnimation.drawAnimation(g2d, (int) state.objects.getPlayers().get(i).getX(), (int) state.objects.getPlayers().get(i).getY(), 0);
            }
            g2d.setTransform(gameTransform);
            //draw the Gun of the Player Tank and handle its rotation
            if (state.objects.getPlayers().get(i).getSelectedGun().getId().equals(ObjectId.MissileGun))
            {
                AffineTransform gunTransform = g2d.getTransform();
                //we know that atan2 return radian :)
                if (i == 0)
                {
                    double playerGunAngle = Math.atan2((state.getMouseY() - centerY), (state.getMouseX() - centerX));
                    gunTransform.rotate(playerGunAngle, centerX, centerY);
                    state.objects.getPlayers().get(0).setGunAngle(playerGunAngle); //set angle in tank info
                }
                else if (SharedData.getData().gameType.equals(ObjectId.TwoPlayer) && i == 1)
                {
                    gunTransform.rotate(state.objects.getPlayers().get(1).getGunAngle(), centerX, centerY);
                }
                g2d.setTransform(gunTransform);
                g2d.drawImage(Utility.gun01,
                        (int) state.objects.getPlayers().get(i).getX() + 18,
                        (int) state.objects.getPlayers().get(i).getY() + 5,
                        null);
            }
            else if (state.objects.getPlayers().get(i).getSelectedGun().getId().equals(ObjectId.MachineGun))
            {
                AffineTransform gunTransform = g2d.getTransform();
                if (i == 0)
                {
                    double playerGunAngle = Math.atan2((state.getMouseY() - centerY), (state.getMouseX() - centerX));
                    gunTransform.rotate(playerGunAngle, centerX, centerY);
                    state.objects.getPlayers().get(0).setGunAngle(playerGunAngle); //set angle in tank info
                }
                else if (i == 1)
                {
                    gunTransform.rotate(state.objects.getPlayers().get(1).getGunAngle(), centerX, centerY);
                }
                g2d.setTransform(gunTransform);
                g2d.drawImage(Utility.gun02,
                        (int) state.objects.getPlayers().get(i).getX() + 18,
                        (int) state.objects.getPlayers().get(i).getY(),
                        null);
            }
            g2d.setTransform(gameTransform);
        }
        //
    }

    private void drawUpgrades(GameState state, Graphics2D g2d)
    {
        for (int i = 0; i < state.objects.getUpgrades().size(); i++)
        {
            if (state.objects.getUpgrades().get(i).getActivation())
            {
                if (state.objects.getUpgrades().get(i).getId().equals(ObjectId.MissileGunUpgrade))
                {
                    g2d.drawImage(Utility.MissileGunUpgrade, null, (int) state.objects.getUpgrades().get(i).getX(), (int) state.objects.getUpgrades().get(i).getY());
                }
                else if (state.objects.getUpgrades().get(i).getId().equals(ObjectId.MachineGunUpgrade))
                {
                    g2d.drawImage(Utility.MachineGunUpgrade, null, (int) state.objects.getUpgrades().get(i).getX(), (int) state.objects.getUpgrades().get(i).getY());
                }
            }
        }
    }
}
