package com.xiao.csms.server;


import com.xiao.csms.client.ClientService;
import com.xiao.csms.connector.Connector;
import com.xiao.csms.connector.ConnectorService;
import com.xiao.csms.exceptions.ResourceNotFoundException;
import com.xiao.csms.reservation.ReservationService;
import com.xiao.csms.transaction.TransactionService;
import eu.chargetime.ocpp.*;
import eu.chargetime.ocpp.feature.profile.*;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.model.SessionInformation;
import eu.chargetime.ocpp.model.core.*;
import eu.chargetime.ocpp.model.firmware.*;
import eu.chargetime.ocpp.model.localauthlist.GetLocalListVersionRequest;
import eu.chargetime.ocpp.model.localauthlist.SendLocalListRequest;
import eu.chargetime.ocpp.model.localauthlist.UpdateType;
import eu.chargetime.ocpp.model.remotetrigger.TriggerMessageRequest;
import eu.chargetime.ocpp.model.remotetrigger.TriggerMessageRequestType;
import eu.chargetime.ocpp.model.reservation.CancelReservationRequest;
import eu.chargetime.ocpp.model.reservation.ReserveNowRequest;
import eu.chargetime.ocpp.model.smartcharging.SetChargingProfileRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CentralSystem {
    private static final Logger logger = LoggerFactory.getLogger(CentralSystem.class);

    private IServerAPI server;

    @Autowired private ConnectorService connectorService;
    @Autowired public TransactionService transactionService;
    @Autowired private ReservationService reservationService;

    private String currentIdentifier;
    private UUID currentSessionIndex;

    private boolean isStarted;

    public ServerCoreEventHandler createServerCoreEventHandler() {

        return new ServerCoreEventHandler() {
            @Override
            public AuthorizeConfirmation handleAuthorizeRequest(
                    UUID sessionIndex, AuthorizeRequest request) {

                logger.info(String.valueOf(request));
                
                AuthorizeConfirmation confirmation = new AuthorizeConfirmation();
                IdTagInfo tagInfo = new IdTagInfo();
                tagInfo.setStatus(AuthorizationStatus.Accepted);
                ZonedDateTime calendar = ZonedDateTime.parse("2023-01-01T01:01:01.988Z");
                tagInfo.setExpiryDate(calendar);
                confirmation.setIdTagInfo(tagInfo);

                return confirmation;
            }

            @Override
            public BootNotificationConfirmation handleBootNotificationRequest(
                    UUID sessionIndex, BootNotificationRequest request) {

                logger.info(String.valueOf(request));

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

                logger.info(String.valueOf(request));

                DataTransferConfirmation confirmation = new DataTransferConfirmation();
                confirmation.setStatus(DataTransferStatus.Accepted);
                return confirmation;
            }

            @Override
            public HeartbeatConfirmation handleHeartbeatRequest(
                    UUID sessionIndex, HeartbeatRequest request) {
                HeartbeatConfirmation confirmation = new HeartbeatConfirmation();
                confirmation.setCurrentTime(ZonedDateTime.now());
                return confirmation;
            }

            @Override
            public MeterValuesConfirmation handleMeterValuesRequest(
                    UUID sessionIndex, MeterValuesRequest request) {
                logger.info(String.valueOf(request));

                // ToDo: Display SoC
                MeterValue[] meterValues = request.getMeterValue();
                if(meterValues.length > 0){
                    SampledValue[] sampledValues = meterValues[0].getSampledValue();
                    logger.info("SampledValue MeterValue " + Arrays.toString(sampledValues));
                }

                return new MeterValuesConfirmation();
            }

            @Override
            public StartTransactionConfirmation handleStartTransactionRequest(
                    UUID sessionIndex, StartTransactionRequest request) {
                logger.info(String.valueOf(request));

                // handle events
                IdTagInfo tagInfo = new IdTagInfo();
                tagInfo.setStatus(AuthorizationStatus.Accepted);

                StartTransactionConfirmation confirmation = new StartTransactionConfirmation();
                confirmation.setIdTagInfo(tagInfo);
                // Random tansactionId
                int tid = 10000 + (int)(Math.random()*10000);
                confirmation.setTransactionId(tid);
                // save to DB
                transactionService.saveStartRequest(request, tid);
                return confirmation;
            }

            @Override
            public StatusNotificationConfirmation handleStatusNotificationRequest(
                    UUID sessionIndex, StatusNotificationRequest request) {

                logger.info(String.valueOf(request));

                // save connectorInfo to MySQL
                Connector connector = new Connector();
                connector.setConnectorId(request.getConnectorId());
                connector.setErrorCode(String.valueOf(request.getErrorCode()));
                connector.setStatus(String.valueOf(request.getStatus()));
                connector.setTimeStamp(String.valueOf(request.getTimestamp()));
                connectorService.save(connector);

                // handle events
                return new StatusNotificationConfirmation();
            }

            @Override
            public StopTransactionConfirmation handleStopTransactionRequest(
                    UUID sessionIndex, StopTransactionRequest request) {

                logger.info(String.valueOf(request));

//                try {
//                    TimeUnit.SECONDS.sleep(10);
//                    MeterValue[] transactionData = request.getTransactionData();
//                    // ToDo: Display SoC
//                    if(transactionData.length > 0){
//                        SampledValue[] sampledValues = transactionData[0].getSampledValue();
//                        logger.info("SampledValue STOP " + Arrays.toString(sampledValues));
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


                transactionService.stop(request);

                return new StopTransactionConfirmation();
            }
        };
    }

    public ServerEvents generateServerEventsHandler() {
        return new ServerEvents() {
            @Override
            public void newSession(UUID sessionIndex, SessionInformation information) {
                currentSessionIndex = sessionIndex;
                currentIdentifier = information.getIdentifier();
            }

            @Override
            public void lostSession(UUID identity) {
                currentSessionIndex = null;
                currentIdentifier = null;
            }
        };
    }

    public CentralSystem(){
        ServerCoreProfile serverCoreProfile =
                new ServerCoreProfile(createServerCoreEventHandler());

        JSONConfiguration configuration =
                JSONConfiguration.get().setParameter(JSONConfiguration.REUSE_ADDR_PARAMETER, true);
        server = new JSONServer(serverCoreProfile, configuration);

        initializeServer();
        isStarted = false;
    }

    private void initializeServer(){
        ServerSmartChargingProfile smartChargingProfile = new ServerSmartChargingProfile();
        server.addFeatureProfile(smartChargingProfile);
    }

    public boolean isClosed() {
        return server.isClosed();
    }

    public boolean conncted(){
        return currentIdentifier != null ;
    }

    public void clientLost(){ server.closeSession(currentSessionIndex);}

    public void start() throws Exception{
        final String host="0.0.0.0";
        if(!isStarted){
            int port = 8887;
            server.open(host, port, new ServerEvents() {
                @Override
                public void newSession(UUID sessionIndex, SessionInformation sessionInformation) {
                    logger.debug(String.format("New session: %s information: %s", sessionIndex,
                            sessionInformation.getIdentifier()));
                    currentSessionIndex = sessionIndex;
                    currentIdentifier = sessionInformation.getIdentifier();
                }

                @Override
                public void lostSession(UUID sessionIndex) {
                    logger.debug("Session {} lost connection", sessionIndex);
                    currentSessionIndex = null;
                    currentIdentifier = null;
                }
            });
            logger.info("Server started on {}:{}", host, port);
            isStarted = true;
        }
    }

    public void stopped() {
        server.close();
        logger.info("Server stopped");
        isStarted = false;
    }

    public void sendChangeAvailabilityRequest(int connectorId, AvailabilityType type)
            throws Exception {
        ChangeAvailabilityRequest request = new ChangeAvailabilityRequest();
        request.setType(type);
        request.setConnectorId(connectorId);
        send(request);
    }

    public void sendChangeConfigurationRequest(String key, String value) throws Exception {
        ChangeConfigurationRequest request = new ChangeConfigurationRequest();
        request.setKey(key);
        request.setValue(value);
        send(request);
    }

    public void sendClearCacheRequest() throws Exception {
        ClearCacheRequest request = new ClearCacheRequest();
        send(request);
    }

    public void sendDataTransferRequest(String vendorId, String messageId, String data)
            throws Exception {
        DataTransferRequest request = new DataTransferRequest();
        request.setVendorId(vendorId);
        request.setMessageId(messageId);
        request.setData(data);
        send(request);
    }

    public void sendGetConfigurationRequest(String... key) throws Exception {
        GetConfigurationRequest request = new GetConfigurationRequest();
        request.setKey(key);
        send(request);
    }

    public void sendRemoteStartTransactionRequest(int connectorId, String idTag) throws Exception {
        RemoteStartTransactionRequest request = new RemoteStartTransactionRequest();
        request.setIdTag(idTag);
        request.setConnectorId(connectorId);
        send(request);
    }

    public void sendRemoteStartTransactionWithProfileRequest(int connectorId, String idTag)
            throws Exception {
        RemoteStartTransactionRequest request = new RemoteStartTransactionRequest();
        request.setIdTag(idTag);
        request.setConnectorId(connectorId);

        ChargingSchedule schedule =
                new ChargingSchedule(
                        ChargingRateUnitType.A,
                        new ChargingSchedulePeriod[] {new ChargingSchedulePeriod(1, 32d)});
        ChargingProfile profile =
                new ChargingProfile(
                        1,
                        1,
                        ChargingProfilePurposeType.ChargePointMaxProfile,
                        ChargingProfileKindType.Recurring,
                        schedule);
        request.setChargingProfile(profile);
        send(request);
    }

    public void sendRemoteStopTransactionRequest(int transactionId) throws Exception {
        RemoteStopTransactionRequest request = new RemoteStopTransactionRequest();
        request.setTransactionId(transactionId);
        send(request);
    }

    public void sendRemoteStopTransactionRequest() throws Exception {
        RemoteStopTransactionRequest request = new RemoteStopTransactionRequest();
        request.setTransactionId(transactionService.getMaxTransactionId());
        send(request);
    }

    public void sendResetRequest(ResetType type) throws Exception {
        ResetRequest request = new ResetRequest();
        request.setType(type);
        send(request);
    }

    public void sendGetDiagnosticsRequest(String location) throws Exception {
        GetDiagnosticsRequest request = new GetDiagnosticsRequest();
        request.setLocation(location);
        send(request);
    }

    public void sendUpdateFirmwareRequest(String location, ZonedDateTime retrieveDate)
            throws Exception {
        UpdateFirmwareRequest request = new UpdateFirmwareRequest(location, retrieveDate);
        send(request);
    }
    public void sendReserveNowRequest(
            Integer connectorId, ZonedDateTime expiryDate, String idTag, Integer reservationId)
            throws Exception {
        ReserveNowRequest request =
                new ReserveNowRequest(connectorId, expiryDate, idTag, reservationId);
        reservationService.save(request);
        send(request);
    }

    public void sendCancelReservationRequest(Integer reservationId) throws Exception {
        CancelReservationRequest request = new CancelReservationRequest(reservationId);
        send(request);
    }

    public void sendGetLocalListVersionRequest() throws Exception {
        GetLocalListVersionRequest request = new GetLocalListVersionRequest();
        send(request);
    }

    public void sendSendLocalListRequest(int listVersion, UpdateType updateType) throws Exception {
        SendLocalListRequest request = new SendLocalListRequest(listVersion, updateType);
        send(request);
    }

    public void sendSetChargingProfileRequest(Integer connectorId, ChargingProfile csChargingProfiles)
            throws Exception {
        SetChargingProfileRequest request =
                new SetChargingProfileRequest(connectorId, csChargingProfiles);
        send(request);
    }

    public void sendUnlockConnectorRequest(int connectorId) throws Exception {
        UnlockConnectorRequest request = new UnlockConnectorRequest();
        request.setConnectorId(connectorId);
        send(request);
    }

    public void sendTriggerMessage(TriggerMessageRequestType type, Integer connectorId)
            throws Exception {
        TriggerMessageRequest request = new TriggerMessageRequest(type);
        try {
            request.setConnectorId(connectorId);
        } catch (PropertyConstraintException e) {
            e.printStackTrace();
        }

        send(request);
    }

    public void send(Request request) throws Exception {
        server.send(currentSessionIndex, request);
    }

    public UUID getCurrentSessionIndex() {
        return currentSessionIndex;
    }

    public void setCurrentSessionIndex(UUID currentSessionIndex) {
        this.currentSessionIndex = currentSessionIndex;
    }
}
