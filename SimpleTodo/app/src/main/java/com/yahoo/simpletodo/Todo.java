package com.yahoo.simpletodo;

public class Todo {
    public long id;
    public String todoItem;
    public String todoPriority;

    public Todo() {
    }

    public Todo(long id, String todoItem, String todoPriority) {
        this.id = id;
        this.todoItem = todoItem;
        this.todoPriority = todoPriority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTodoItem() {
        return todoItem;
    }

    public void setTodoItem(String todoItem) {
        this.todoItem = todoItem;
    }

    public String getTodoPriority() {
        return todoPriority;
    }

    public void setTodoPriority(String todoPriority) {
        this.todoPriority = todoPriority;
    }
}
