import React, { useState } from "react";
import { useRouter } from 'next/router';

function SearchForm() {
    const router = useRouter();
    const [query, setQuery] = useState("");

    const handleSearch = async (e: React.SyntheticEvent) => {
        e.preventDefault();
        const targetPath = query === "" ? `/todos` : `/todos?page=1&query=${query}`;
        await router.push(targetPath);
    };

    return (
        <div className="pb-3">
            <form method="get" onSubmit={handleSearch}>
                <div className="row g-3 align-items-center">
                    <div className="col">
                        <input
                            className="form-control"
                            type="search"
                            name="query"
                            value={query}
                            onChange={(e) => setQuery(e.target.value)}
                        />
                    </div>
                    <div className="col-auto">
                        <button type="submit" className="btn btn-primary">Search</button>
                    </div>
                </div>
            </form>
        </div>
    );
}

export default SearchForm;