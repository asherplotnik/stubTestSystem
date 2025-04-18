import React, { useEffect, useState } from "react";
import ResponseBlock from "./responseBlock";

export function TestSystem() {
  const [currentStage, setCurrentStage] = useState(1);
  const API_GET_SERVICES_URI =
    import.meta.env.VITE_API_GET_SERVICES_URI ||
    "http://localhost:30082/api/get";
  const API_CREATE_TEST_RESOURCE =
    import.meta.env.VITE_API_CREATE_TEST_RESOURCE ||
    "http://localhost:30082/api/createTestPod";
  const API_DELETE_TEST_RESOURCE =
    import.meta.env.VITE_API_DELETE_TEST_RESOURCE ||
    "http://localhost:30082/api/deleteTestResource";
  const generateTestStubID = () => Math.floor(10000 + Math.random() * 90000);
  const [testStubID, setTestStubID] = useState<number | null>(null);
  const [fetchedRequests, setFetchedRequests] = useState<ServiceResponseMap | null>(null);
  const [editResponse, setEditResponse] = useState(false);
  const [currentServiceName, setCurrentServiceName] = useState<string>("");
  const [testResource, setTestResource] = useState<string>("");
  const [isWaiting, setIsWaiting] = useState(false);
  
  interface RequestObject {
    path: string;
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

  interface ServiceResponseMap {
    [service: string]: RequestResponsePair[];
  }

  useEffect(() => {
    setTestStubID(generateTestStubID());
  }, []);

  useEffect(() => {
    if (currentStage === 2 && fetchedRequests) {
      const entry = Object.entries(fetchedRequests).find(([name, pairs]) =>
        pairs.some((pair) => pair.request.headers?.testStubID != null)
      );
      if (entry) {
        setCurrentServiceName(entry[0]);
      }
    }
  }, [currentStage, fetchedRequests]);

  const toggleEditResponse = () => {
    setEditResponse((prevState) => !prevState);
  };

  const fetchRequests = async (): Promise<ServiceResponseMap> => {
    try {
      const response = await fetch(API_GET_SERVICES_URI, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = (await response.json()) as ServiceResponseMap;
      setFetchedRequests(data);
      setCurrentStage(2);
      return data;
    } catch (error) {
      console.error("Error fetching data:", error);
      throw error;
    }
  };

  const createTestPod = async () => {
    try {
      setTestResource("");
      setIsWaiting(true);
      const response = await fetch(
        `${API_CREATE_TEST_RESOURCE}?name=${encodeURIComponent(
          currentServiceName
        )}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.text();
      setTestResource(data);
    } catch (error) {
      console.error("Error fetching data:", error);
      throw error;
    } finally {
      setIsWaiting(false);
    }
  };

  const deleteTestPod = async () => {
    try {
      const response = await fetch(
        `${API_DELETE_TEST_RESOURCE}?resourceName=${encodeURIComponent(
          currentServiceName + "-test"
        )}`,
        {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      await response.text();
      window.location.reload();
    
    } catch (error) {
      console.error("Error fetching data:", error);
      throw error;
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <div className="w-full max-w-[60%] bg-white shadow-md rounded-md p-6">
        <h1 className="text-3xl font-bold text-center mb-6">
          Test Stub System
        </h1>

        {/* Stage One: Instruct the user to make a request via Postman */}
        {currentStage === 1 && (
          <div>
            <h2 className="text-xl font-semibold mb-4">
              Stage 1: Setup Request
            </h2>
            <p className="mb-4">
              Please use Postman (or your preferred API client) to send a
              request to your API endpoint.
              <br />
              Ensure you include the header:
              <code className="bg-gray-200 p-1 rounded ml-1">
                testStubID: {testStubID}
              </code>
            </p>
            <div className="flex space-x-4">
              <button
                onClick={() => setTestStubID(generateTestStubID())}
                className="bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded"
              >
                Refresh testStubID
              </button>
              <button
                onClick={() => fetchRequests()}
                className="bg-green-500 hover:bg-green-600 text-white font-medium py-2 px-4 rounded"
              >
                I've sent my request
              </button>
            </div>
          </div>
        )}

        {/* Stage Two: Display the request breakdown and a block for the JSON response */}
        {currentStage === 2 && (
          <div>
            <h2 className="text-xl font-semibold mb-4">
              Stage 2: Request &amp; Response Details
            </h2>
            <div className="grid grid-cols-1 gap-4">
              {fetchedRequests &&
                Object.entries(fetchedRequests).map(([serviceName, pairs]) => {
                  const validPairs = pairs.filter(
                    (pair) => pair.request.headers?.testStubID != null
                  );
                  if (validPairs.length === 0) return null;
                  return (
                    <div
                      key={serviceName}
                      className="border p-4 rounded shadow"
                    >
                      <h3 className="font-semibold mb-2">{serviceName}</h3>
                      {validPairs.map((pair, index) => (
                        <div key={index} className="mb-4">
                          {/* Request Details */}
                          <div className="bg-gray-50 p-4 rounded shadow mb-2">
                            <h4 className="font-semibold mb-2">
                              Request Details
                            </h4>
                            <ul className="text-sm text-gray-700 space-y-1">
                              <li>
                                <strong>URI:</strong> {pair.request.path}
                              </li>
                              <li>
                                <strong>Method:</strong> {pair.request.method}
                              </li>
                              <li>
                                <strong>Params:</strong> {`{ ... }`}
                              </li>
                              <li>
                                <strong>Body:</strong> {`{ ... }`}
                              </li>
                              <li>
                                <strong>Headers:</strong> testStubID:{" "}
                                {pair.request.headers.testStubID?.join(", ")}
                              </li>
                              <li>
                                <strong>Timestamp: </strong>
                                {pair.request.timestamp}
                              </li>
                            </ul>
                          </div>
                          {/* Response Data */}
                          <ResponseBlock
                            key={index}
                            pair={pair}
                            serviceName={serviceName}
                            editResponse={editResponse}
                            setEditResponse={setEditResponse}
                            toggleEditResponse={toggleEditResponse}
                          />
                        </div>
                      ))}
                    </div>
                  );
                })}
            </div>
            <div className="mt-4">
              <button
                onClick={() => setCurrentStage(3)}
                className="bg-green-500 hover:bg-green-600 text-white font-medium py-2 px-4 rounded"
              >
                Proceed to Create Stub
              </button>
            </div>
          </div>
        )}
        {/* Stage Three: Allow editing of the response stub */}
        {currentStage === 3 && (
          <div>
            <h2 className="text-xl font-semibold mb-4">
              Stage 3: Create Test Resource
            </h2>
            <button
              onClick={() => createTestPod()}
              disabled={isWaiting || testResource !== ""}
              className="bg-green-500 hover:bg-green-600 disabled:bg-gray-400 disabled:opacity-50 disabled:cursor-not-allowed text-white font-medium py-2 px-4 rounded mb-4"
            >
              {isWaiting
                ? "Creating..."
                : testResource === ""
                ? "Create Test Resource"
                : "Test Resource Created"}
            </button>

            {isWaiting && (
              <p className="text-sm text-gray-500 mb-4">
                Waiting for response...
              </p>
            )}

            <div className="bg-gray-100 p-4 rounded shadow mb-4">
              <p className="text-sm text-gray-700 mb-2">
                Copy this test Pod address:
              </p>
              <strong className="block font-mono bg-gray-200 px-2 py-1 rounded">
                {testResource}
              </strong>
            </div>

            {testResource !== "" && (
              <button
                onClick={() => setCurrentStage(4)}
                className="bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded mt-4"
              >
                Proceed to Stage 4
              </button>
            )}
          </div>
        )}

        {/* Stage Four: Final verification and terminating the test resource */}
        {currentStage === 4 && (
          <div>
            <h2 className="text-xl font-semibold mb-4">
              Stage 4: Final Verification
            </h2>
            <p className="mb-4">
              Now use the new testPod address in your API client (e.g., Postman)
              to verify the stub response.
            </p>
            <button
              onClick={deleteTestPod}
              className="bg-red-500 hover:bg-red-600 text-white font-medium py-2 px-4 rounded"
            >
              Terminate Test &amp; Restart
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
