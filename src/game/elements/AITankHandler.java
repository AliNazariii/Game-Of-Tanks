package game.elements;

import java.util.ArrayList;

/**
 * Managing tank movement and shooting. it ticks each time when updating game state.
 * it cause tank to find a way to approach player and hit that.
 */
public class AITankHandler {

    //fields

    //constructor
    public AITankHandler(){

    }

    //methods
    public void tick(Objects objects){
        move(objects.getTanks(),objects.getTanks().get(0));
        fire(objects.getTanks(),objects.getTanks().get(0),objects);
    }

    public void move(ArrayList<Tank> tanks, Tank target){
        for(int i = 1 ; i < tanks.size() ; i++){ // starts from 1 because player tank is must be skipped
            if(tanks.get(i).getType() == 0){
                continue;
            }

            if(target.x > tanks.get(i).x && (Math.abs(target.x - tanks.get(i).x) > 200)) {
                tanks.get(i).setX(tanks.get(i).getX() + tanks.get(i).getVelX());
            }
            else if (target.x == tanks.get(i).x) {

            }
            else if(target.x < tanks.get(i).x && (Math.abs(target.x - tanks.get(i).x ))> 200){
                tanks.get(i).setX(tanks.get(i).getX() - tanks.get(i).getVelX());
            }


            if(target.y >= tanks.get(i).y) {
                tanks.get(i).setY(tanks.get(i).getY() + tanks.get(i).getVelY());
            }
            else if(target.y < tanks.get(i).y) {
                tanks.get(i).setY(tanks.get(i).getY() - tanks.get(i).getVelY());
            }
            else if(target.y == tanks.get(i).y){

            }
        }
    }

    public void fire(ArrayList<Tank> tanks,Tank target, Objects objects){ // starts from 1 because player tank is must be skipped
        for(int i = 1 ; i < tanks.size() ; i++){
            objects.addBullet(tanks.get(i).getSelectedGun().shoot(tanks.get(i).x,tanks.get(i).y,target.x,target.y));
        }
    }

}
