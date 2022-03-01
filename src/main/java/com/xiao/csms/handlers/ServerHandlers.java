package com.xiao.csms.handlers;

import com.xiao.csms.client.ClientNotFoundException;
import com.xiao.csms.client.ClientService;
import com.xiao.csms.exceptions.ResourceNotFoundException;
import eu.chargetime.ocpp.ServerEvents;
import eu.chargetime.ocpp.feature.profile.ServerCoreEventHandler;
import eu.chargetime.ocpp.feature.profile.ServerFirmwareManagementEventHandler;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.model.SessionInformation;
import eu.chargetime.ocpp.model.core.*;
import eu.chargetime.ocpp.model.firmware.DiagnosticsStatusNotificationConfirmation;
import eu.chargetime.ocpp.model.firmware.DiagnosticsStatusNotificationRequest;
import eu.chargetime.ocpp.model.firmware.FirmwareStatusNotificationConfirmation;
import eu.chargetime.ocpp.model.firmware.FirmwareStatusNotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiConsumer;

@Service

public class ServerHandlers {
    private boolean riggedToFail;
    private Request receivedRequest;
    private Confirmation receivedConfirmation;
    private String currentIdentifier;
    private UUID currentSessionIndex;

    @Autowired public ClientService clientService;

    public ServerHandlers(ClientService clientService) {
        this.clientService = clientService;
    }

    public ServerCoreEventHandler createServerCoreEventHandler() {

        return new ServerCoreEventHandler() {
            @Override
            public AuthorizeConfirmation handleAuthorizeRequest(
                    UUID sessionIndex, AuthorizeRequest request) {
                // debug
                System.out.println(request);

                // handle events
                receivedRequest = request;
                String idTag = request.getIdTag();

                AuthorizeConfirmation confirmation = new AuthorizeConfirmation();
                IdTagInfo tagInfo = new IdTagInfo();
                tagInfo.setStatus(AuthorizationStatus.Accepted);
                ZonedDateTime calendar = ZonedDateTime.parse("2023-01-01T01:01:01.988Z");
                tagInfo.setExpiryDate(calendar);
                confirmation.setIdTagInfo(tagInfo);

                // Check local Auth Cache
                try {
                    clientService.save(idTag);
                } catch (ResourceNotFoundException e) {
                    System.err.println(e);
                }

                //Test changeAvailability
            ChangeAvailabilityRequest changeAvailabilityRequest = new ChangeAvailabilityRequest(11111, AvailabilityType.Operative);
            try {
                sendChangeAvailabilityRequest(1,AvailabilityType.Inoperative);
            } catch (Exception e) {
                e.printStackTrace();
            }
                return confirmation;
            }

            @Override
            public BootNotificationConfirmation handleBootNotificationRequest(
                    UUID sessionIndex, BootNotificationRequest request) {
                receivedRequest = request;
                BootNotificationConfirmation confirmation = new BootNotificationConfirmation();
                try {
                    confirmation.setInterval(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                confirmation.setCurrentTime(ZonedDateTime.now());
                confirmation.setStatus(RegistrationStatus.Accepted);
                return confirmation;
            }

            @Override
            public DataTransferConfirmation handleDataTransferRequest(
                    UUID sessionIndex, DataTransferRequest request) {
                receivedRequest = request;
                DataTransferConfirmation confirmation = new DataTransferConfirmation();
                confirmation.setStatus(DataTransferStatus.Accepted);
                return confirmation;
            }

            @Override
            public HeartbeatConfirmation handleHeartbeatRequest(
                    UUID sessionIndex, HeartbeatRequest request) {
                // Debug
                System.out.println(request);

                receivedRequest = request;
                HeartbeatConfirmation confirmation = new HeartbeatConfirmation();
                confirmation.setCurrentTime(ZonedDateTime.now());
                return confirmation;
            }

            @Override
            public MeterValuesConfirmation handleMeterValuesRequest(
                    UUID sessionIndex, MeterValuesRequest request) {
                receivedRequest = request;
                return new MeterValuesConfirmation();
            }

            @Override
            public StartTransactionConfirmation handleStartTransactionRequest(
                    UUID sessionIndex, StartTransactionRequest request) {
                // Debug
                System.out.println(request);

                // Handle Events
                receivedRequest = request;
                IdTagInfo tagInfo = new IdTagInfo();
                tagInfo.setStatus(AuthorizationStatus.Accepted);

                StartTransactionConfirmation confirmation = new StartTransactionConfirmation();
                confirmation.setIdTagInfo(tagInfo);
                confirmation.setTransactionId(42);
                return confirmation;
            }

            @Override
            public StatusNotificationConfirmation handleStatusNotificationRequest(
                    UUID sessionIndex, StatusNotificationRequest request) {
                // Test
                System.out.println(request);

                // Handle Events
                receivedRequest = request;
                StatusNotificationConfirmation confirmation = new StatusNotificationConfirmation();
                return confirmation;
            }

            @Override
            public StopTransactionConfirmation handleStopTransactionRequest(
                    UUID sessionIndex, StopTransactionRequest request) {
                receivedRequest = request;
                StopTransactionConfirmation confirmation = new StopTransactionConfirmation();
                return confirmation;
            }
        };
    }
    public void sendChangeAvailabilityRequest(int connectorId, AvailabilityType type)
            throws Exception {
        ChangeAvailabilityRequest request = new ChangeAvailabilityRequest();
        request.setType(type);
        request.setConnectorId(connectorId);
//        server.send(currentSessionIndex, request);
    }

    public ServerFirmwareManagementEventHandler createServerFirmwareManagementEventHandler() {
        return new ServerFirmwareManagementEventHandler() {
            @Override
            public DiagnosticsStatusNotificationConfirmation handleDiagnosticsStatusNotificationRequest(
                    UUID sessionId, DiagnosticsStatusNotificationRequest request) {
                receivedRequest = request;
                DiagnosticsStatusNotificationConfirmation confirmation =
                        new DiagnosticsStatusNotificationConfirmation();
                return confirmation;
            }

            @Override
            public FirmwareStatusNotificationConfirmation handleFirmwareStatusNotificationRequest(
                    UUID sessionId, FirmwareStatusNotificationRequest request) {
                receivedRequest = request;
                FirmwareStatusNotificationConfirmation confirmation =
                        new FirmwareStatusNotificationConfirmation();
                return confirmation;
            }
        };
    }

    public ServerEvents generateServerEventsHandler() {
        return new ServerEvents() {
            @Override
            public void newSession(UUID sessionIndex, SessionInformation information) {
                currentSessionIndex = sessionIndex;

                System.out.println("[SERVER] Session: " + sessionIndex);
                currentIdentifier = information.getIdentifier();
            }

            @Override
            public void lostSession(UUID identity) {
                currentSessionIndex = null;
                currentIdentifier = null;
                // clear
                receivedConfirmation = null;
                receivedRequest = null;
            }
        };
    }

    public BiConsumer<Confirmation, Throwable> generateWhenCompleteHandler() {
        return (confirmation, throwable) -> receivedConfirmation = confirmation;
    }
    
    public String getCurrentIdentifier() {
        return currentIdentifier;
    }

    public UUID getCurrentSessionIndex() {
        return currentSessionIndex;
    }

}

