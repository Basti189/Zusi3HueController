package app.wolfware;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {

    private final static String configFilePath = "./zusi.cfg";

    private static String application_name = "HueJava";

    private static String ip = "127.0.0.1";

    private static String port = "1436";

    private static String hue_group = "";

    public static void init() {
        try {
            FileInputStream fis = new FileInputStream(configFilePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            Properties props = new Properties();
            props.load(isr);

            application_name = props.getProperty("application_name");
            ip = props.getProperty("ip");
            port = props.getProperty("port");
            hue_group = props.getProperty("hue_group");

            isr.close();
            fis.close();
        } catch (IOException fnf) {
            System.out.println("No config");
            save();
            System.exit(0);
        }
    }

    public static void save() {
        Properties props = new Properties();

        props.setProperty("application_name", application_name);
        props.setProperty("ip", ip);
        props.setProperty("port", port);
        props.setProperty("hue_group", hue_group);

        Writer fstream = null;
        try {
            fstream = new OutputStreamWriter(new FileOutputStream(configFilePath), StandardCharsets.UTF_8);
            props.store(fstream, "config");
            fstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getApplication_name() {
        return application_name;
    }

    public static String getIp() {
        return ip;
    }

    public static String getPort() {
        return port;
    }

    public static String getHueGroup() {
        return hue_group;
    }
}
