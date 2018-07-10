/*** In The Name of Allah ***/
package game.template.bufferstrategy;

import game.elements.ObjectId;
import game.elements.Objects;

import java.awt.*;
import javax.swing.*;
import java.awt.EventQueue;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 * Program start.
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class Main
{
    public static void main(String[] args)
    {
        // Initialize the global thread-pool
        ThreadPool.init();
//        System.out.println(new Date().getTime());
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(new Date().getTime());
        // After the player clicks 'PLAY' ...
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Scanner scanner = new Scanner(System.in);
                GameFrame frame = new GameFrame("Normal Tanks ( Beta )");
                frame.setLocationRelativeTo(null); // put frame at center of screen
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.initBufferStrategy();
                // Create and execute the game-loop
                GameLoop game = new GameLoop(frame);
                System.out.println("Which game? 1-Single 2-Coop");
//                int a = scanner.nextInt();
                int a = 1;
//                scanner.nextLine();
                if(a == 1){
                    System.out.println("Single Started");
                    game.setGameType(ObjectId.SinglePlayer);
                }
                else if(a == 2){
                    System.out.println("Coop started");
                    game.setGameType(ObjectId.TwoPlayer);
                    System.out.println("1-Host or 2-Client");
                    a = scanner.nextInt();
                    scanner.nextLine();
                    if( a == 1 ){
                        System.out.println("U R Host");
                        game.setPlayerType(ObjectId.ServerPlayer);
                        frame.setTitle("Server");
                    }
                    else if( a == 2 ){
                        System.out.println("U R Client");
                        frame.setTitle("Client");
                        game.setPlayerType(ObjectId.ClientPlayer);
                    }
                }
                game.init();
                ThreadPool.execute(game);
                // and the game starts ...
            }
        });
    }
}
