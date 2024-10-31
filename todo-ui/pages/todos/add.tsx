import { saveTodo } from "@/services/api";
import Link from "next/link";
import React, { useState } from "react";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export default function AddTodo() {
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [errors, setErrors] = useState<{ title?: string; description?: string }>({});

    const handleSubmit = async (e: React.SyntheticEvent) => {
        e.preventDefault();

        // Reset errors
        setErrors({});

        // Validate fields
        const newErrors: { title?: string; description?: string } = {};
        if (!title) newErrors.title = "Please enter a title.";
        if (!description) newErrors.description = "Please enter a description.";

        if (Object.keys(newErrors).length) {
            setErrors(newErrors);
            return;
        }

        const payload = { title, description };
        const response = await saveTodo(payload);
        if (!response?.errorMessage) {
            toast.error(response.errorMessage);
            return;
        }

        console.log("Add todo Response: ", response)
        setTitle("");
        setDescription("");
        toast.success("Todo saved successfully.");
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
                                <h2>Create New Todo</h2>
                            </div>
                            <div className="card-body">
                                <div className="card-text">
                                    <form onSubmit={handleSubmit}>
                                        <div className="mb-3">
                                            <label htmlFor="title" className="form-label">Title</label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                id="title"
                                                value={title}
                                                onChange={e => setTitle(e.target.value)}
                                            />
                                            {errors.title && <div className="text-danger">{errors.title}</div>}
                                        </div>
                                        <div className="mb-3">
                                            <label htmlFor="description" className="form-label">Description</label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                id="description"
                                                value={description}
                                                onChange={e => setDescription(e.target.value)}
                                            />
                                            {errors.description && <div className="text-danger">{errors.description}</div>}
                                        </div>
                                        <button type="submit" className="btn btn-primary me-2">Submit</button>
                                        <Link href="/todos">
                                            <button type="button" className="btn btn-secondary">Cancel</button>
                                        </Link>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-2"></div> {/* Right padding */}
                </div>
            </div>

        </div>
    );
}