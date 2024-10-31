import "@/styles/globals.css";
import 'bootstrap/dist/css/bootstrap.min.css'
import type { AppProps } from "next/app";
import Head from "next/head";
import Navbar from "@/components/Navbar";

export default function App({ Component, pageProps }: AppProps) {
  return (
    <>
        <Head>
            <title>Todo App</title>
        </Head>
        <Navbar />
        <main>
            <Component {...pageProps} />
        </main>
    </>
  );
}
