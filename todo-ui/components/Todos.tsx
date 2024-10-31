import React from "react";
import { TodosResponse } from "../services/models";
import Todo from "./Todo";

interface TodosProps {
    todos: TodosResponse;
    query?: string;
}

export default function Todos({ todos, query }: TodosProps) {
    return (
        <div className="row">
            {todos.data.map((todo, index) => (
                <div key={todo.id} className="col-6 mb-3">
                    <Todo todo={todo} />
                </div>
            ))}
        </div>
    );
}