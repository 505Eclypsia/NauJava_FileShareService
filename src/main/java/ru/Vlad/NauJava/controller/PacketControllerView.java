package ru.Vlad.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.repository.PacketRepository;

@Controller
@RequestMapping("/custom/packets/view")
public class PacketControllerView
{
    private PacketRepository packetRepository;

    @Autowired
    public PacketControllerView(PacketRepository packetRepository){
        this.packetRepository = packetRepository;
    }

    @GetMapping("/list")
    public String packetListView(Model model)
    {
        Iterable<Packet> products = packetRepository.findAll();
        model.addAttribute("packets", products);
        return "packetList";
    }
}
