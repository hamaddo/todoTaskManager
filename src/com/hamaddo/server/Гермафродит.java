package com.hamaddo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Гермафродит {
    public interface ЗвонокЕслиКтоТоПостучалСнизу {
        void постучалиСнизу(String стук);
    }

    private static Гермафродит экземпляр;

    private ServerSocket хозяин;
    private Socket крестьянин;
    private PrintWriter выход;
    private BufferedReader вход;
    private ЗвонокЕслиКтоТоПостучалСнизу звонокЕслиКтоТоПостучалСнизу;

    private static boolean правда = true;

    private Гермафродит() {
    }

    private Гермафродит(InetAddress адрес, int порт) throws IOException {
        крестьянин = new Socket(адрес, порт);
        выход = new PrintWriter(крестьянин.getOutputStream());
        вход = new BufferedReader(new InputStreamReader(крестьянин.getInputStream()));

        new Thread(() -> {
            while (правда) {
                String стук;
                try {
                    стук = вход.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if (стук == null)
                    return;

                звонокЕслиКтоТоПостучалСнизу.постучалиСнизу(стук);
            }
        }).start();
    }

    private Гермафродит(int порт) throws IOException {
        хозяин = new ServerSocket(порт);
        крестьянин = хозяин.accept();
        выход = new PrintWriter(крестьянин.getOutputStream());
        вход = new BufferedReader(new InputStreamReader(крестьянин.getInputStream()));

        new Thread(() -> {
            while (правда) {
                String стук;
                try {
                    стук = вход.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                звонокЕслиКтоТоПостучалСнизу.постучалиСнизу(стук);
            }
        }).start();
    }

    public void установитьЗвонокЕслиКтоТоПостучалСнизу(ЗвонокЕслиКтоТоПостучалСнизу вотЭтоВотЕслиКтоТоПостучал) {
        звонокЕслиКтоТоПостучалСнизу = вотЭтоВотЕслиКтоТоПостучал;
    }

    public void постучать(String стук) {
        выход.println(стук);
        выход.flush();
    }

    public static void сделатьХозяйна(int порт) throws IOException {
        экземпляр = new Гермафродит(порт);
    }

    public static void сделатьКрестьянина(String адресХозяйна, int порт) throws IOException {
        экземпляр = new Гермафродит(InetAddress.getByName(адресХозяйна), порт);
    }

    public static Гермафродит получитьЭкземпляр() {
        return экземпляр;
    }

    public void stop() throws IOException {
        крестьянин.close();

        if (хозяин != null)
            хозяин.close();
    }
}
