package ru.Vlad.NauJava.service;

import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.User;

public interface PacketService {
    Packet createPacket (String name, User user);
    void deletePacket (Long id);
    Packet findByShareLinkCode (String code);
}
