package com.hamaddo.common;

import com.hamaddo.server.Гермафродит;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.stream.Collectors;

public class TodoData {

    private static final TodoData instance = new TodoData();
    private static final String filename = "TodoListItems.txt";

    private ObservableList<TodoItem> todoItems;
    private final DateTimeFormatter formatter;

    private ListChangeListener<TodoItem> todoItemListener;

    public static TodoData getInstance() {
        return instance;
    } //получение instance

    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    } //форматирование даты

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }//получение списка событий

    public void addTodoItem(TodoItem item) {//добавление события
        todoItems.add(item);
    }

    public void deleteTodoItem(TodoItem item) {//удаление события
        todoItems.remove(item);
    }

    public void loadTodoItems() throws IOException {

        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String input;
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, formatter);
                TodoItem todoItem = new TodoItem(shortDescription, details, date);
                todoItems.add(todoItem);
            }

        }

        todoItemListener = change -> Гермафродит.получитьЭкземпляр().постучать(todoItems.stream()
                .map(todoItem -> todoItem.getShortDescription() + "\u0017" + todoItem.getDetails() + "\u0017" + todoItem.getDeadline().toString())
                .collect(Collectors.joining("\u0004")) + "\u0004");
        todoItems.addListener(todoItemListener);

        Гермафродит.получитьЭкземпляр().установитьЗвонокЕслиКтоТоПостучалСнизу(стук -> {
            List<TodoItem> receivedItems = Arrays.stream(стук.split("\u0004")).map(s -> {
                String[] split = s.split("\u0017");

                return new TodoItem(split[0], split[1], LocalDate.parse(split[2]));
            }).collect(Collectors.toList());

            Platform.runLater(() -> {
                todoItems.removeListener(todoItemListener);

                todoItems.clear();
                todoItems.addAll(receivedItems);

                todoItems.addListener(todoItemListener);
            });
        });
    } //загрузка событий из файла

    public void storeTodoItems() throws IOException {

        Path path = Paths.get(filename);
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (TodoItem item : todoItems) {
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadline().format(formatter)));
                bw.newLine();
            }

        }
    }//запись событий в файл
}
