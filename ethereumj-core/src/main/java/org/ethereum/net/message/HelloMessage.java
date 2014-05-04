package org.ethereum.net.message;

import org.bouncycastle.util.encoders.Hex;
import org.ethereum.net.RLP;
import org.ethereum.net.rlp.RLPItem;
import org.ethereum.net.rlp.RLPList;

import java.nio.ByteBuffer;

/**
 * www.ethereumJ.com
 * User: Roman Mandeleil
 * Created on: 06/04/14 14:56
 */
public class HelloMessage extends Message {

    private final byte commandCode = 0x00;

    private byte protocolVersion;
    private byte networkId;
    private String clientId;
    private byte capabilities;
    private short peerPort;
    private byte[] peerId;


    public HelloMessage(RLPList rawData) {

        super(rawData);
    }

    public HelloMessage(byte protocolVersion, byte networkId, String clientId, byte capabilities, short peerPort, byte[] peerId) {
        this.protocolVersion = protocolVersion;
        this.networkId = networkId;
        this.clientId = clientId;
        this.capabilities = capabilities;
        this.peerPort = peerPort;
        this.peerId = peerId;
    }

    @Override
    public void parseRLP() {

        RLPList paramsList = (RLPList) rawData.getElement(0);

        // the message does no distinguish between the 0 and null so here I check command code for null
        // todo: find out if it can be 00
        if (((RLPItem)(paramsList).getElement(0)).getData() != null){

            throw new Error("HelloMessage: parsing for mal data");
        }


        this.protocolVersion  =            ((RLPItem) paramsList.getElement(1)).getData()[0];

        byte[] networkIdBytes = ((RLPItem) paramsList.getElement(2)).getData();
        this.networkId        = networkIdBytes == null ? 0 : networkIdBytes[0] ;

        this.clientId         = new String(((RLPItem) paramsList.getElement(3)).getData());
        this.capabilities     =            ((RLPItem) paramsList.getElement(4)).getData()[0];

        ByteBuffer bb = ByteBuffer.wrap(((RLPItem) paramsList.getElement(5)).getData());
        this.peerPort         = bb.getShort();

        this.peerId           =            ((RLPItem) paramsList.getElement(6)).getData();

        this.parsed = true;
        // todo: what to do when mal data ?
    }


    public byte[] getPayload(){

        byte[] command         = RLP.encodeByte(this.commandCode);
        byte[] protocolVersion = RLP.encodeByte(this.protocolVersion);
        byte[] networkId       = RLP.encodeByte(this.networkId);
        byte[] clientId        = RLP.encodeString(this.clientId);
        byte[] capabilities    = RLP.encodeByte(this.capabilities);
        byte[] peerPort        = RLP.encodeShort(this.peerPort);
        byte[] peerId          = RLP.encodeElement(this.peerId);

        byte[] data = RLP.encodeList(command, protocolVersion, networkId,
                clientId, capabilities, peerPort, peerId);

        return data;
    }


    public byte getCommandCode() {

        if (!parsed) parseRLP();
        return commandCode;
    }

    public byte getProtocolVersion() {

        if (!parsed) parseRLP();
        return protocolVersion;
    }

    public byte getNetworkId() {

        if (!parsed) parseRLP();
        return networkId;
    }

    public String getClientId() {

        if (!parsed) parseRLP();
        return clientId;
    }

    public byte getCapabilities() {

        if (!parsed) parseRLP();
        return capabilities;
    }

    public short getPeerPort() {

        if (!parsed) parseRLP();
        return peerPort;
    }

    public byte[] getPeerId() {

        if (!parsed) parseRLP();
        return peerId;
    }

    public String toString(){

        return "Hello Message [ command=" + this.commandCode + " " +
                " protocolVersion=" + this.protocolVersion + " " +
                " networkId=" + this.networkId + " " +
                " clientId= " + this.clientId + " " +
                " capabilities= " + this.capabilities + " " +
                " peerPort= " + this.peerPort + " " +
                " peerId= " + Hex.toHexString(this.peerId) + " " +
                "]";

    }
}
