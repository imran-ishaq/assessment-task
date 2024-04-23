import React, { useState } from "react";
import axios from "axios";
import styles from "./styles.module.css";
import FileList from "./FileList";

const FileSelection = () => {
  const [file, setFile] = useState(null);
  const [fetchedFileDetails, setFetchedFileDetails] = useState([]);
  const [isLoadingFetch, setIsLoadingFetch] = useState(false);
  const [isLoadingUpload, setIsLoadingUpload] = useState(false);
  {
    /*The following function fetch all the file details stored in my s3 bucket*/
  }
  const fetchFilesFromS3 = async () => {
    setIsLoadingFetch(true);
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
      setIsLoadingFetch(false);
    }
  };
  {
    /*Handles the file name being changed when a file is choosen*/
  }
  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };
  {
    /*Handle that a file must be choosen and then upload it to s3 bucket via api*/
  }
  const handleUpload = async () => {
    const fileName = file.name;
    const fileExtension = fileName.split(".").pop(); // Extract file extension

    if (!file || fileExtension.toLowerCase() !== "json") {
      alert("Please select a file or file is not json");
      return;
    }

    setIsLoadingUpload(true);
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
      setFile(null);
      document.getElementById("input").value = "";
    } catch (error) {
      console.error("Error uploading file:", error);
      alert("Error uploading file");
    } finally {
      setIsLoadingUpload(false);
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
          disabled={isLoadingUpload || isLoadingFetch}
        >
          {isLoadingUpload ? "Uploading..." : "Upload"}
        </button>
        <button
          onClick={fetchFilesFromS3}
          className={styles.fetchButton}
          disabled={isLoadingFetch || isLoadingUpload}
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
