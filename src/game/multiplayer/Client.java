package game.multiplayer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import game.elements.Objects;
import java.io.*;
import java.net.Socket;
public class Client {
    //fields
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public Client() {
        try {   
            socket = new Socket("127.0.0.1",6666);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Couldn't connect to server");
        }
    }

    public void receiveData(Objects objects){
        try {
//            Tank tank = (Tank)ois.readObject();
//            System.out.println(tank.getX());
            //******************
//            byte[] bytes = new byte[1024];
//            int rd = socket.getInputStream().read();
//            int index = 0;
//            while(rd != -1)
//            {
//                bytes[index++] = (byte)rd;
//                rd = socket.getInputStream().read();
//            }
            //***********
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String str = br.readLine();
            TransferringData updatedObjects = (TransferringData) (new XStream(new StaxDriver())).fromXML(str);
            updateObjects(objects,updatedObjects);
//            System.out.println(updatedObjects.getPlayers().get(0).getX());
            //***********
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("couldn't read objects");
        } /*catch (ClassNotFoundException e) {
            System.out.println("Class not found while reading!");
        }*/
    }

    private void updateObjects(Objects objects, TransferringData data){
        objects.setPlayers(data.getPlayers());
        objects.setBullets(data.getBullets());
        objects.setRobots(data.getRobots());
        objects.setTanks(data.getTanks());
        objects.setTurrets(data.getTurrets());
    }
}
