"use client";
import { useEffect, useState } from "react";
import api from "@/utils/api";

export default function Dashboard() {
  const [user, setUser] = useState<{ name: string; email: string } | null>(
    null
  );

  useEffect(() => {
    api
      .get("/api/user/me")
      .then((res) => setUser(res.data))
      .catch((err) => console.error("Not logged in", err));
  }, []);

  return (
    <div className="p-4">
      <h1 className="text-xl font-bold mb-2">Welcome to DailyFix</h1>
      {user ? (
        <div className="bg-gray-100 p-4 rounded shadow">
          <p>
            <strong>Name:</strong> {user.name}
          </p>
          <p>
            <strong>Email:</strong> {user.email}
          </p>
        </div>
      ) : (
        <p>Loading user info...</p>
      )}
    </div>
  );
}
