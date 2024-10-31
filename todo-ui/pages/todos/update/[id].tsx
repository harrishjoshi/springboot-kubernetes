import React, { useEffect, useState } from "react";
import router, { useRouter } from "next/router";
import { Todo } from "@/services/models";
import { fetchTodoById, updateTodo } from "@/services/api";
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import Link from "next/link";

interface UpdateTodoProps {
    todo: Todo;
}

const UpdateTodo: React.FC<UpdateTodoProps> = ({ todo }) => {
    const [title, setTitle] = useState(todo.title);
    const [description, setDescription] = useState(todo.description);
    const [status, setStatus] = useState(todo.status);
    const [errors, setErrors] = useState<{ title?: string; description?: string; status?: string }>({});

    const handleUpdate = async (e: React.SyntheticEvent) => {
        e.preventDefault();

        // Reset errors
        setErrors({});

        // Validate fields
        const newErrors: { title?: string; description?: string; status?: string } = {};
        if (!title) newErrors.title = "Please enter a title.";
        if (!description) newErrors.description = "Please enter a description.";
        if (!status) newErrors.status = "Please select a status.";

        if (Object.keys(newErrors).length) {
            setErrors(newErrors);
            return;
        }

        const payload = { title, description, status };
        const response = await updateTodo(todo.id, payload);
        if (response?.errorMessage) {
            toast.error(response.errorMessage);
            return;
        }

        toast.success("Todo updated successfully.");
        setTimeout(() => {
            router.push("/todos");
        }, 1500);  
    };

    return (
        <div>
            <ToastContainer />
            <div className="container d-flex justify-content-center mt-5">
                <div className="row w-100">
                    <div className="col-2"></div> {/* Left padding */}
                    <div className="col-8">
                        <div className="card">
                            <div className="card-header text-center">
                                <h2>Update Todo</h2>
                            </div>
                            <div className="card-body">
                                <form onSubmit={handleUpdate}>
                                    <div className="mb-3">
                                        <label htmlFor="title" className="form-label">Title</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            id="title"
                                            value={title}
                                            onChange={(e) => setTitle(e.target.value)}
                                        />
                                        {errors.title && <div className="text-danger">{errors.title}</div>}
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="description" className="form-label">Description</label>
                                        <textarea
                                            className="form-control"
                                            id="description"
                                            rows={3}
                                            value={description}
                                            onChange={(e) => setDescription(e.target.value)}
                                        />
                                        {errors.description && <div className="text-danger">{errors.description}</div>}
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="status" className="form-label">Status</label>
                                        <select
                                            className="form-control"
                                            id="status"
                                            value={status}
                                            onChange={(e) => setStatus(e.target.value)}>
                                            <option value="PENDING">PENDING</option>
                                            <option value="IN_PROGRESS">IN_PROGRESS</option>
                                            <option value="COMPLETED">COMPLETED</option>
                                            <option value="CANCELLED">CANCELLED</option>
                                        </select>
                                        {errors.status && <div className="text-danger">{errors.status}</div>}
                                    </div>
                                    <button type="submit" className="btn btn-primary me-2">Update</button>
                                    <Link href="/todos">
                                        <button type="button" className="btn btn-secondary">Cancel</button>
                                    </Link>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div className="col-2"></div> {/* Right padding */}
                </div>
            </div>
        </div>
    );
};

const UpdateTodoWrapper = () => {
    const router = useRouter();
    const { id } = router.query;
    const [todo, setTodo] = useState<Todo | null>(null);

    useEffect(() => {
        const fetchTodo = async () => {
            if (id) {
                const response = await fetchTodoById(id);
                if (response?.errorMessage) {
                    toast.error(response.errorMessage);
                    router.push("/todos")
                }
                setTodo(response);
            }
        };

        fetchTodo();
    }, [id]);

    if (!todo) return <>Loading....</>

    return <UpdateTodo todo={todo} />;
};

export default UpdateTodoWrapper;