import React, { useState, useEffect } from "react";
interface RequestObject {
  url: string;
  method: string;
  headers: {
    [key: string]: string[];
  };
  body: any;
  uriVariables: any[];
  timestamp: number;
}

interface RequestResponsePair {
  request: RequestObject;
  response: string;
}

interface PairBlockProps {
  pair: RequestResponsePair;
  serviceName: string;
  toggleEditResponse: () => void;
  editResponse: boolean;
  setEditResponse: React.Dispatch<React.SetStateAction<boolean>>;
}

const ResponseBlock: React.FC<PairBlockProps> = ({
  pair,
  serviceName,
  toggleEditResponse,
  editResponse,
  setEditResponse,
}) => {
  const [responseValue, setResponseValue] = useState<string>(pair.response);
  const SET_RESPOSNE_URI = import.meta.env.VITE_API_SET_RESPOSNE_URI || "http://localhost:30082/api/setStubResponse";
  useEffect(() => {
    if (pair.response !== responseValue) {
      setResponseValue(pair.response);
    }
  }, [pair.response]);

  const setStubResponse = async (
    testStubId: string,
    serviceName: string,
    stubResponse: string
  ): Promise<any> => {
    try {
      const response = await fetch(SET_RESPOSNE_URI, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          testStubId,
          serviceName,
          stubResponse,
        }),
      });
  
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      console.log(await response.text());
    } catch (error) {
      console.error("Error setting stub response:", error);
      throw error;
    }
  };
  
  const saveEditedResponse = () => {
    try {
      JSON.parse(responseValue);
      setStubResponse(pair.request.headers?.testStubID[0], serviceName, responseValue);
    } catch (error) {
      console.error("Invalid JSON format:", error);
      return;
    }
    setEditResponse(false);
  };

  return (
    <div>
      {editResponse ? (
        <div>
          {/* Editable Response Section */}
          <div className="bg-gray-50 p-4 rounded shadow">
            <h4 className="font-semibold mb-2">Response Data (Editing)</h4>
            <textarea
              className="w-full p-2 border rounded text-sm text-gray-700"
              value={responseValue}
              onChange={(e) => setResponseValue(e.target.value)}
              rows={10} // adjust rows as needed
            />
          </div>
          <div className="mt-4">
            <button
              onClick={() => saveEditedResponse()}
              className="bg-green-500 hover:bg-green-600 text-white font-medium py-2 px-4 rounded mr-2"
            >
              Save Response
            </button>
            <button
              onClick={() => toggleEditResponse()}
              className="bg-gray-500 hover:bg-gray-600 text-white font-medium py-2 px-4 rounded"
            >
              Cancel
            </button>
          </div>
        </div>
      ) : (
        <div>
          {/* Read-only Response Section */}
          <div className="bg-gray-50 p-4 rounded shadow">
            <h4 className="font-semibold mb-2">Response Data</h4>
            <pre className="text-sm text-gray-700 whitespace-pre-wrap">
              {(() => {
                try {
                  const parsed = JSON.parse(pair.response);
                  return JSON.stringify(parsed, null, 2);
                } catch (error) {
                  return pair.response;
                }
              })()}
            </pre>
          </div>
          <button
            onClick={toggleEditResponse}
            className="bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded mt-4"
          >
            Modify Response
          </button>
        </div>
      )}
    </div>
  );
};

export default ResponseBlock;
