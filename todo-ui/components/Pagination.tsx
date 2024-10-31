import React from "react";
import Link from 'next/link';
import { TodosResponse } from "../services/models";

interface PaginationProps {
    todos: TodosResponse;
    query?: string;
}

function Pagination({ todos, query }: PaginationProps) {
    const path = "/todos";
    const queryParams = query ? { query: query } : {};
    const firstPage = { pathname: path, query: { page: 1, ...queryParams } };
    const previousPage = { pathname: path, query: { page: todos.currentPage - 1, ...queryParams } };
    const nextPage = { pathname: path, query: { page: todos.currentPage + 1, ...queryParams } };
    const lastPage = { pathname: path, query: { page: todos.totalPages, ...queryParams } };

    return (
        <div>
            <nav aria-label="Page navigation">
                <ul className="pagination justify-content-center">
                    <li className={`page-item ${todos.hasPrevious ? "" : "disabled"}`}>
                        <Link className="page-link" href={firstPage}>
                            First
                        </Link>
                    </li>

                    <li className={`page-item ${todos.hasPrevious ? "" : "disabled"}`}>
                        <Link className="page-link" href={previousPage}>
                            Previous
                        </Link>
                    </li>

                    <li className={`page-item ${todos.hasNext ? "" : "disabled"}`}>
                        <Link className="page-link" href={nextPage}>
                            Next
                        </Link>
                    </li>

                    <li className={`page-item ${todos.hasNext ? "" : "disabled"}`}>
                        <Link className="page-link" href={lastPage}>
                            Last
                        </Link>
                    </li>
                </ul>
            </nav>
        </div>
    );
}

export default Pagination;