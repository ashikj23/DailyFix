// src/components/ChatBox.tsx
import api from "@/utils/api";

type Props = {
  roomId: string;
  messages: any[];
};

export default function ChatBox({ roomId, messages }: Props) {
  const generateSummary = () => {
    api
      .post("/api/ai/summarize", { roomId })
      .then((res) => alert("Summary:\n" + res.data.summary))
      .catch(console.error);
  };

  return (
    <main className="flex-1 p-4">
      <h3 className="text-lg mb-4">Messages for {roomId}</h3>
      <ul className="space-y-2">
        {messages.map((msg, idx) => (
          <li key={idx} className="bg-gray-100 p-2 rounded">
            {msg.content?.body}
          </li>
        ))}
      </ul>

      {roomId && (
        <button
          className="mt-4 bg-green-600 text-white px-4 py-2 rounded"
          onClick={generateSummary}
        >
          Generate Summary
        </button>
      )}
    </main>
  );
}
