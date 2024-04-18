import React, { useState } from "react";
import axios from "axios";

const UploadJsonToS3 = () => {
  const [file, setFile] = useState(null);
  const [fetchedFileDetails, setFetchedFileDetails] = useState([]);
  const [FileData, setFileData] = useState({});
  const [showdata, setShowData] = useState(false);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      alert("Please select a file");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axios.post(
        "http://localhost:8080/api/file/upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      console.log("File uploaded successfully:", response.data);
      alert("File uploaded successfully");
    } catch (error) {
      console.error("Error uploading file:", error);
      alert("Error uploading file");
    }
  };

  const fetchFilesFromS3 = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/file/fetch");
      setFetchedFileDetails(response.data);
    } catch (error) {
      console.error("Error fetching file Details from S3:", error);
      alert("Error fetching file Details from S3");
    }
  };

  const handleViewButtonClick = async (id) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/file/data/${id}`
      );
      setFileData(response.data);
      setShowData(true);
      console.log(response.data);
    } catch (error) {
      console.error("Error fetching file data:", error);
      alert("Error fetching file data");
    }
  };

  return (
    <div
      style={{
        backgroundColor: "powderblue",
        padding: "20px",
        borderRadius: "10px",
        maxWidth: "800px",
        margin: "auto",
      }}
    >
      <h2 style={{ textAlign: "center", marginBottom: "20px" }}>
        File Management System
      </h2>
      <div style={{ display: "flex", marginBottom: "10px" }}>
        <input
          type="file"
          accept=".json"
          onChange={handleFileChange}
          style={{ flex: "1", marginRight: "10px" }}
        />
        <button onClick={handleUpload}>Upload</button>
      </div>
      <button onClick={fetchFilesFromS3}>Fetch Files</button>
      {fetchedFileDetails.length > 0 && (
        <div style={{ marginTop: "20px" }}>
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr>
                <th
                  style={{
                    border: "1px solid black",
                    textAlign: "left",
                    padding: "8px",
                  }}
                >
                  ID
                </th>
                <th
                  style={{
                    border: "1px solid black",
                    textAlign: "left",
                    padding: "8px",
                  }}
                >
                  Name
                </th>
                <th
                  style={{
                    border: "1px solid black",
                    textAlign: "center",
                    padding: "8px",
                  }}
                >
                  Actions
                </th>
              </tr>
            </thead>
            <tbody>
              {fetchedFileDetails.map((fileDetail) => (
                <tr key={fileDetail.id}>
                  <td
                    style={{
                      border: "1px solid black",
                      textAlign: "left",
                      padding: "8px",
                    }}
                  >
                    {fileDetail.id}
                  </td>
                  <td
                    style={{
                      border: "1px solid black",
                      textAlign: "left",
                      padding: "8px",
                    }}
                  >
                    {fileDetail.name}
                  </td>
                  <td
                    style={{
                      border: "1px solid black",
                      textAlign: "center",
                      padding: "8px",
                    }}
                  >
                    <button
                      onClick={() => handleViewButtonClick(fileDetail.id)}
                      style={{
                        backgroundColor: "tomato",
                        color: "white",
                        padding: "5px 10px",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                      }}
                    >
                      View
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            {FileData && showdata && (
              <div className="card">
                <h3>Company</h3>
                <p>Id: {FileData.company.id}</p>
                <p>Address: {FileData.company.address}</p>
                <p>City: {FileData.company.city}</p>
                <p>Country: {FileData.company.country}</p>
              </div>
            )}
            {FileData && showdata && (
              <div className="card">
                <h3>Location</h3>
                <p>Id: {FileData.location.id}</p>
                <p>Address: {FileData.location.address}</p>
                <p>City: {FileData.location.city}</p>
                <p>Country: {FileData.location.country}</p>
              </div>
            )}
            {FileData && showdata && (
              <div className="card">
                <h3>Device</h3>
                <p>Id: {FileData.device.id}</p>
                <p>Sensor Use: {FileData.device.sensor_use}</p>
                <p>Status: {FileData.device.status}</p>
                <p>Thing Name: {FileData.device.thing_name}</p>
              </div>
            )}
            {FileData && showdata && (
              <div className="card">
                <h3>Device Type</h3>
                <p>Id: {FileData.device_type.id}</p>
                <p>Description: {FileData.device_type.description}</p>
                <p>Codec: {FileData.device_type.codec}</p>
                <p>Model: {FileData.device_type.model}</p>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default UploadJsonToS3;
