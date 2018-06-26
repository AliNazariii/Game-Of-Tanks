package game.elements;

/**
 * a weapon that can provide damage to other objects.
 * two kind of guns are available: 1- MissileGun launcher 2- Machine gun
 *each gun has a damage field which is the amount of damage that it provides.
 * each gun has an specific amount of ammo.
 */
public class Gun extends GameObject{
    //fields
    protected int damage;
    protected int ammo;

    //constructor
    public Gun(Tank tank){
        super(tank.x,tank.y);

    }

    //methods

    /**
     * shooting bullet from gun(creating a bullet).
     * @param tankX
     * @param tankY
     * @param mouseX
     * @param mouseY
     * @return the bullet must be stored in arrayList so it is returned to be added to list in GameState Class.
     */
    public Bullet shoot(double tankX, double tankY, int mouseX, int mouseY){
        Bullet bullet = new Bullet(tankX,tankY,mouseX,mouseY,damage);
        return bullet;
    }

    /**
     * set damage for the gun.
     * @param damage amount of damage it must provide.
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * how much ammunition gun must have.
     * @param ammo amount of ammo for th gun
     */
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    /**
     * how much damage th gun provides.
     * @return amount of damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * how much ammunition has.
     * @return amount of ammo
     */
    public int getAmmo() {
        return ammo;
    }
}
