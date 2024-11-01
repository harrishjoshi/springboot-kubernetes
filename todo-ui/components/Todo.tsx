import React from "react";
import { Todo as TodoResponse } from "@/services/models";
import TodoActions from "./TodoActions";

interface TodoProps {
    todo: TodoResponse;
}

export default function Todo({ todo }: TodoProps) {
    return (
        <div>
            <div className="alert alert-primary" role="alert">
                <h5>{todo.title}</h5>
                <p>{todo.description}</p>
                <p>Status: {todo.status}</p>
                <p>Created At: {new Date(todo.createdAt).toLocaleString()}</p>
                {todo.updatedAt && (
                    <p>Updated At: {new Date(todo.updatedAt).toLocaleString()}</p>
                )}
                <TodoActions todo={todo} />
            </div>
        </div>
    );
}
