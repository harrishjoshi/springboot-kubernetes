import Todos from '@/components/Todos';
import { fetchTodos } from '@/services/api';
import { TodosResponse } from '@/services/models';
import { GetServerSideProps } from 'next';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface HomeProps {
    todos: TodosResponse;
    query?: string;
}

export default function Home({ todos, query }: HomeProps) {
    return (
        <div>
            <ToastContainer />
            {/* <SearchForm /> */}
            <Todos todos={todos} query={query} />
        </div>
    );
}

export const getServerSideProps: GetServerSideProps = async (context) => {
    const { page = 1, query = "" } = context.query;
    const todos = await fetchTodos(parseInt(String(page)), String(query));
    
    return {
        props: {
            todos,
            query,
        },
    };
};