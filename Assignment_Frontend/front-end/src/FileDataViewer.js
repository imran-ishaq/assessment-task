import React from "react";
import styles from "./styles.module.css";

const FileDataViewer = ({ data }) => {
  return (
    <div>
      <div className={styles.fileDataViewer}>
        <h3>Company</h3>
        <table>
          <tbody>
            {Object.entries(data.company).map(([key, value]) => (
              <tr key={key}>
                <td>{key}</td>
                <td>{value}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className={styles.fileDataViewer}>
        <h3>Location</h3>
        <table>
          <tbody>
            {Object.entries(data.location).map(([key, value]) => (
              <tr key={key}>
                <td>{key}</td>
                <td>{value}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className={styles.fileDataViewer}>
        <h3>Device</h3>
        <table>
          <tbody>
            {Object.entries(data.device).map(([key, value]) => (
              <tr key={key}>
                <td>{key}</td>
                <td>{value}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className={styles.fileDataViewer}>
        <h3>Device Type</h3>
        <table>
          <tbody>
            {Object.entries(data.device_type).map(([key, value]) => (
              <tr key={key}>
                <td>{key}</td>
                <td>{value}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className={styles.fileDataViewer}>
        <h3>Event</h3>
        <table>
          <tbody>
            {Object.entries(data.event_data).map(([key, value]) => (
              <tr key={key}>
                <td>{key}</td>
                <td>{value}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default FileDataViewer;
