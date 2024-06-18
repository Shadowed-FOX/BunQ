package com.blackfox.bunq.database;

import java.io.Serializable;

public class ClientReceiverPK implements Serializable {
    protected int client_id;
    protected int receiver_id;

    public ClientReceiverPK() {
    }

    public ClientReceiverPK(int client_id, int receiver_id) {
        this.client_id = client_id;
        this.receiver_id = receiver_id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof ClientReceiverPK))
            return false;

        final ClientReceiverPK pk = (ClientReceiverPK) other;
        if (pk.client_id != client_id)
            return false;
        if (pk.receiver_id != this.receiver_id)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = client_id * 20;
        result += receiver_id;
        return result;
    }
}
