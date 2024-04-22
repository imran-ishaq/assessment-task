import React from "react";
import FileSelection from "./FileSelection";
import styles from "./styles.module.css";

const UploadJsonToS3 = () => {
  return (
    <div className={styles.container}>
      <h1>File Management System</h1>
      <FileSelection />
    </div>
  );
};

export default UploadJsonToS3;
