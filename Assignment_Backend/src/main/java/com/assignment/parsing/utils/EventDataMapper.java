package com.assignment.parsing.utils;

import com.assignment.parsing.dto.EventDataDTO;
import com.assignment.parsing.dto.GatewayDTO;
import com.assignment.parsing.dto.SensorReadingDTO;
import com.assignment.parsing.entity.EventData;
import com.assignment.parsing.entity.Gateway;
import com.assignment.parsing.entity.SensorReading;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class EventDataMapper {
    public EventDataDTO convertEventDataToDTO(EventData eventData) {
        EventDataDTO eventDataDTO = new EventDataDTO();
        eventDataDTO.setId(eventData.getId());
        eventDataDTO.setCorrelation_id(eventData.getCorrelation_id());
        eventDataDTO.setDevice_id(eventData.getDevice_id());
        eventDataDTO.setUser_id(eventData.getUser_id());
        List<SensorReadingDTO> sensorReadingDTOs = eventData.getPayload().stream()
                .filter(sensorReading -> sensorReading.getTimestamp() <= eventData.getTimestamp())
                .map(this::convertSensorReadingToDTO)
                .collect(Collectors.toList());
        eventDataDTO.setPayload(sensorReadingDTOs);

        List<GatewayDTO> gatewayDTOs = eventData.getGateways().stream()
                .map(this::convertGatewayToDTO)
                .collect(Collectors.toList());
        eventDataDTO.setGateways(gatewayDTOs);
        eventDataDTO.setFcnt(eventData.getFcnt());
        eventDataDTO.setFport(eventData.getFport());
        eventDataDTO.setRaw_format(eventData.getRaw_format());
        eventDataDTO.setRaw_payload(eventData.getRaw_payload());
        eventDataDTO.setClient_id(eventData.getClient_id());
        eventDataDTO.setHardware_id(eventData.getHardware_id());
        eventDataDTO.setTimestamp(eventData.getTimestamp());
        eventDataDTO.setApplication_id(eventData.getApplication_id());
        eventDataDTO.setDevice_type_id(eventData.getDevice_type_id());
        eventDataDTO.setLora_datarate(eventData.getLora_datarate());
        eventDataDTO.setFreq(eventData.getFreq());
        return eventDataDTO;
    }
    private SensorReadingDTO convertSensorReadingToDTO(SensorReading sensorReading) {
        SensorReadingDTO sensorReadingDTO = new SensorReadingDTO();
        sensorReadingDTO.setId(sensorReading.getId());
        sensorReadingDTO.setName(sensorReading.getName());
        sensorReadingDTO.setSensor_id(sensorReading.getSensor_id());
        sensorReadingDTO.setType(sensorReading.getType());
        sensorReadingDTO.setUnit(sensorReading.getUnit());
        sensorReadingDTO.setValue(sensorReading.getValue());
        sensorReadingDTO.setChannel(sensorReading.getChannel());
        sensorReadingDTO.setTimestamp(sensorReading.getTimestamp());
        return sensorReadingDTO;
    }
    private GatewayDTO convertGatewayToDTO(Gateway gateway) {
        GatewayDTO gatewayDTO = new GatewayDTO();
        gatewayDTO.setId(gateway.getId());
        gatewayDTO.setGweui(gateway.getGweui());
        gatewayDTO.setTime(gateway.getTime());
        gatewayDTO.setRssi(gateway.getRssi());
        gatewayDTO.setSnr(gateway.getSnr());
        return gatewayDTO;
    }
}

