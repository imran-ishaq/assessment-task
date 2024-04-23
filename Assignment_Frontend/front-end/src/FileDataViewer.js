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
        <h3>Event Data</h3>
        <table>
          <tbody>
            <tr>
              <td>ID</td>
              <td>{data.event_data.id}</td>
            </tr>
            <tr>
              <td>Correlation ID</td>
              <td>{data.event_data.correlation_id}</td>
            </tr>
            <tr>
              <td>Device ID</td>
              <td>{data.event_data.device_id}</td>
            </tr>
            <tr>
              <td>User ID</td>
              <td>{data.event_data.user_id}</td>
            </tr>
            <tr>
              <td>Payload</td>
              <td>
                <ol>
                  {data.event_data.payload.map((reading, index) => (
                    <li key={index}>
                      Name: {reading.name}, Sensor ID: {reading.sensor_id},
                      Value: {reading.value}, Timestamp: {reading.timestamp}
                    </li>
                  ))}
                </ol>
              </td>
            </tr>
            <tr>
              <td>Gateways</td>
              <td>
                <ol>
                  {data.event_data.gateways.map((gateway, index) => (
                    <li key={index}>
                      Gweui: {gateway.gweui}, Rssi: {gateway.rssi}, Snr:{" "}
                      {gateway.snr}
                    </li>
                  ))}
                </ol>
              </td>
            </tr>
            <tr>
              <td>FCNT</td>
              <td>{data.event_data.fcnt}</td>
            </tr>
            <tr>
              <td>FPORT</td>
              <td>{data.event_data.fport}</td>
            </tr>
            <tr>
              <td>Raw Payload</td>
              <td>{data.event_data.raw_payload}</td>
            </tr>
            <tr>
              <td>Raw Format</td>
              <td>{data.event_data.raw_format}</td>
            </tr>
            <tr>
              <td>Client Id</td>
              <td>{data.event_data.client_id}</td>
            </tr>
            <tr>
              <td>Hardware ID</td>
              <td>{data.event_data.hardware_id}</td>
            </tr>
            <tr>
              <td>Timestamp</td>
              <td>{data.event_data.timestamp}</td>
            </tr>
            <tr>
              <td>Application ID</td>
              <td>{data.event_data.application_id}</td>
            </tr>
            <tr>
              <td>Device Type ID</td>
              <td>{data.event_data.device_type_id}</td>
            </tr>
            <tr>
              <td>Lora Datarate</td>
              <td>{data.event_data.lora_datarate}</td>
            </tr>
            <tr>
              <td>FREQ</td>
              <td>{data.event_data.freq}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default FileDataViewer;
