package com.hamaddo;

import com.hamaddo.client.Гермафродит;
import com.hamaddo.common.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Todo List");
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        if (args.length == 2 && args[0].equals("--connect")) {
            Гермафродит.сделатьКрестьянина(args[1], 1339);
        } else {
            Гермафродит.сделатьХозяйна(1339);
        }

        launch(args);
    }

    @Override
    public void stop() throws Exception {
        try {
            TodoData.getInstance().storeTodoItems();
            Гермафродит.получитьЭкземпляр().stop();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() throws Exception {
        try {
            TodoData.getInstance().loadTodoItems();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
