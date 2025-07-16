"use client";

import { useEffect } from "react";

export default function LoginPage() {
  useEffect(() => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  }, []);

  return <p>Redirecting to Google Login...</p>;
}
