package app.wolfware;

import app.wolfware.Interfaces.ZusiData;
import app.wolfware.Interfaces.ZusiEvent;
import app.wolfware.Values.FstAnz;
import app.wolfware.bridge.HueBridge;

public class Zusi3HueController {

    private Zusi3Schnittstelle zusi;
    private HueBridge bridge;

    public static void main(String[] args) {
        new Zusi3HueController();
    }
    public Zusi3HueController() {
        Config.init();

        zusi = new Zusi3Schnittstelle(Config.getIp(), Config.getPort(), Config.getApplication_name());
        zusi.register(this);
        zusi.setDebugOutput(true);
        zusi.requestFuehrerstandsbedienung(false);
        zusi.requestProgrammdaten(false);
        zusi.reqFstAnz(FstAnz.Aussenhelligkeit);
        zusi.connect();

        bridge = new HueBridge();
        bridge.init();
        bridge.getAllLights();
        bridge.getAllRooms();
    }

    @ZusiEvent(0x00)
    public void onConnectionChanged(int status, int count) {
        if (status == 0) {
            System.out.println("Verbindung getrennt");
        } else if (status == 1) {
            System.out.println("Verbunden");
        } else if (status == 2) {
            System.out.println("Verbindung zum Server verloren");
        } else if (status == 3) {
            System.out.println("Verbindungsversuch " + count);
        }
    }

    @ZusiEvent(0x10)
    public void onConnectionCreated(String version, String verbindungsinfo, boolean client_aktzeptiert) {
        System.out.println("Zusi-Version: " + version);
        System.out.println("Zusi-Verbindungsinfo: " + verbindungsinfo);
        System.out.println("Client ok? " + client_aktzeptiert);
    }

    @ZusiData(FstAnz.Aussenhelligkeit)
    public void onBrightnessChanged(int lightning) {
        lightning /= 2;
        System.out.println("Helligkeit: " + lightning + "%");
        int lightForHue = (int) (lightning*2.55D);
        if (lightForHue < 10) {
            lightForHue = 10;
        }
        bridge.setRoomBrightness(bridge.room.get(Config.getHueGroup()), lightForHue);
    }
}
