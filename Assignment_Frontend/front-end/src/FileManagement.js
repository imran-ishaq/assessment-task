import React from "react";
import FileSelection from "./FileSelection";
import styles from "./styles.module.css";

const FileManagement = () => {
  return (
    <div className={styles.container}>
      <h1>File Management System</h1>
      {/*display the file selection and uploading section*/}
      <FileSelection />
    </div>
  );
};

export default FileManagement;
