import React, { useState } from "react";
import axios from "axios";
import styles from "./styles.module.css";
import FileList from "./FileList";

const FileSelection = () => {
  const [file, setFile] = useState(null);
  const [fetchedFileDetails, setFetchedFileDetails] = useState([]);

  const fetchFilesFromS3 = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/file/fetch");
      setFetchedFileDetails(response.data);
      console.log(response.data);
    } catch (error) {
      console.error("Error fetching file Details from S3:", error);
      alert("Error fetching file Details from S3");
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
        <button onClick={handleUpload} className={styles.uploadButton}>
          Upload
        </button>
        <button onClick={fetchFilesFromS3} className={styles.fetchButton}>
          Fetch Files
        </button>
      </div>
      {fetchedFileDetails.length > 0 && <FileList files={fetchedFileDetails} />}
    </div>
  );
};

export default FileSelection;
