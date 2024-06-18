/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blackfox.bunq.database;

import jakarta.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@Table(name = "client_receivers")
public class ClientReceiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Column(name = "receiver_id", nullable = false)
    private int receiverId;

    public ClientReceiver(Client _client, int _receiverId) {
        client = _client;
        receiverId = _receiverId;
    }
        public ClientReceiver() {

    } 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getReceiverId() {
        return receiverId;
    }
    public Client getReceiverAsClient()
    {
        try {
           return HibernateUtil.getClient(receiverId);
        } catch (ClientNotFoundException ex) {
            Logger.getLogger(ClientReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

}
