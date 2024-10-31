import React, { useState } from "react";
import Link from "next/link";
import { Todo } from "@/services/models";
import { deleteTodo } from "@/services/api";
import { toast } from "react-toastify";
import router from "next/router";

interface TodoActionsProps {
    todo: Todo;
}

const TodoActions: React.FC<TodoActionsProps> = ({ todo }) => {
    const [showModal, setShowModal] = useState(false);

    const handleDelete = async () => {
        const response = await deleteTodo(todo.id);
        setShowModal(false);
        if (response && response?.errorMessage) {
            toast.error(response.errorMessage);
            return;
        }

        toast.success("Todo deleted successfully.");
        router.push("/todos");
    };

    return (
        <div>
            <div className="d-flex gap-2">
                <Link className="nav-link p-0" href={`/todos/update/${todo.id}`}>
                    <button className="btn btn-warning">Update</button>
                </Link>
                <button className="btn btn-danger" onClick={() => setShowModal(true)}>
                    Delete
                </button>
            </div>

            {/* Confirmation Modal */}
            {showModal && (
                <div className="modal fade show" style={{ display: "block" }} role="dialog">
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Confirm Deletion</h5>
                                <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
                            </div>
                            <div className="modal-body">
                                <p>Are you sure you want to delete this todo?</p>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>
                                    Cancel
                                </button>
                                <button type="button" className="btn btn-danger" onClick={handleDelete}>
                                    Confirm Delete
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default TodoActions;