"use client";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    // Optional: Check if user is logged in, else redirect
    // For now, just redirect to dashboard
    router.push("/dashboard");
  }, [router]);

  return <p>Redirecting...</p>;
}
