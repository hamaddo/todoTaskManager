package com.hamaddo.client.views.app;

import javax.swing.*;

public class Menubar extends JMenuBar {
    private  AppView _view;

    private final JMenu _controlsMenu = new JMenu("Управление");
    private final JMenuItem _startItem = new JMenuItem("Запуск");
    private final JMenuItem _toggleTimerItem = new JMenuItem("Таймер");
    private final JMenuItem _stopItem = new JMenuItem("Стоп");
}
