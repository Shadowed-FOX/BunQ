package com.blackfox.bunq.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@Table(name = "client_receivers")
@IdClass(ClientReceiverPK.class)
public class ClientReceiver {
    @Id
    private int client_id;
    @Id
    private int receiver_id;

    public ClientReceiver() {
    }

    public ClientReceiver(int client_id, int receiver_id) {
        this.client_id = client_id;
        this.receiver_id = receiver_id;
    }

    public int getClientId() {
        return client_id;
    }

    public int getReceiverId() {
        return receiver_id;
    }

    public Client getReceiverAsClient() {
        try {
            return HibernateUtil.getClient(receiver_id);
        } catch (ClientNotFoundException ex) {
            Logger.getLogger(ClientReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
