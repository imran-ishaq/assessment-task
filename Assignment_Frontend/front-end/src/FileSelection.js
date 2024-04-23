import React, { useState } from "react";
import axios from "axios";
import styles from "./styles.module.css";
import FileList from "./FileList";

const FileSelection = () => {
  const [file, setFile] = useState(null);
  const [fetchedFileDetails, setFetchedFileDetails] = useState([]);
  const [isLoadingFetch, setIsLoadingFetch] = useState(false); // New state for fetching spinner
  const [isLoadingUpload, setIsLoadingUpload] = useState(false); // New state for upload spinner

  const fetchFilesFromS3 = async () => {
    setIsLoadingFetch(true); // Start fetch spinner
    try {
      const response = await axios.get("http://localhost:8080/api/file/fetch");
      if (response.data.length === 0) {
        alert("No file is uploaded to s3 bucket");
      }
      setFetchedFileDetails(response.data);
    } catch (error) {
      console.error("Error fetching file Details from S3:", error);
      alert("Error fetching file Details from S3");
    } finally {
      setIsLoadingFetch(false); // Stop fetch spinner
    }
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      alert("Please select a file");
      return;
    }

    setIsLoadingUpload(true); // Start upload spinner
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
      document.getElementById("input").value = "";
    } catch (error) {
      console.error("Error uploading file:", error);
      alert("Error uploading file");
    } finally {
      setIsLoadingUpload(false); // Stop upload spinner
    }
  };

  return (
    <div className={styles.fileSelection}>
      <input
        type="file"
        accept=".json"
        onChange={handleFileChange}
        className={styles.fileInput}
        id="input"
      />
      <div>
        <button
          onClick={handleUpload}
          className={styles.uploadButton}
          disabled={isLoadingUpload || isLoadingFetch} // Disable upload button when loading
        >
          {isLoadingUpload ? "Uploading..." : "Upload"}
        </button>
        <button
          onClick={fetchFilesFromS3}
          className={styles.fetchButton}
          disabled={isLoadingFetch || isLoadingUpload} // Disable fetch button when loading
        >
          {isLoadingFetch ? "Fetching..." : "Fetch Files"}
        </button>
      </div>
      {(isLoadingFetch || isLoadingUpload) && (
        <div className={styles.spinner}></div>
      )}
      {fetchedFileDetails.length > 0 && <FileList files={fetchedFileDetails} />}
    </div>
  );
};

export default FileSelection;
