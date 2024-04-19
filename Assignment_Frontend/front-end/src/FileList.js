import React, { useState } from "react";
import axios from "axios";
import styles from "./styles.module.css";
import FileDataViewer from "./FileDataViewer";

const FileList = ({ files }) => {
  const [showdata, setShowData] = useState(false);
  const [FileData, setFileData] = useState({});
  const handleViewButtonClick = async (id) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/file/data/${id}`
      );
      if (Object.keys(response.data).length === 0) {
        alert("Wrong Format: File cannot be viewed");
      } else {
        console.log(response.data);
        setFileData(response.data);
        setShowData(true);
      }
    } catch (error) {
      console.error("Error fetching file data:", error);
      alert("Error fetching file data");
    }
  };
  return (
    <div className={styles.fileList}>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>URL</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {files.map((file) => (
            <tr key={file.id}>
              <td>{file.id}</td>
              <td>{file.url}</td>
              <td>
                <button onClick={() => handleViewButtonClick(file.id)}>
                  View
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {showdata && <FileDataViewer data={FileData} />}
    </div>
  );
};

export default FileList;
