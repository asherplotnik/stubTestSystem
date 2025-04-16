import type { Route } from "./+types/home";
import { TestSystem } from "../testSystem/testSystem";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Test System App" },
    { name: "description", content: "Welcome to React Router!" },
  ];
}

export default function Home() {
  return <TestSystem />;
}
