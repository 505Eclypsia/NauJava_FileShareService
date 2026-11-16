package ru.Vlad.NauJava.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.Vlad.NauJava.entity.Packet;
import ru.Vlad.NauJava.entity.Role;
import ru.Vlad.NauJava.entity.ShareLink;
import ru.Vlad.NauJava.entity.User;
import ru.Vlad.NauJava.repository.PacketRepository;
import ru.Vlad.NauJava.repository.ShareLinkRepository;
import ru.Vlad.NauJava.repository.UserRepository;
import ru.Vlad.NauJava.service.PacketServiceImpl;
import ru.Vlad.NauJava.service.UserServiceImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PacketControllerRestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PacketRepository packetRepository;

    @Autowired
    private ShareLinkRepository shareLinkRepository;

    @Autowired
    private PacketServiceImpl packetService;

    @Autowired
    private UserRepository userRepository;

    private Packet packet;

    private String code;

    private Long ownerId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        User user = new User();
        user.setUsername("rest-user-" + UUID.randomUUID());
        user.setPassword("x");
        user.setRole(Role.USER);
        user.setCreatedAt(Instant.now());
        user = userRepository.save(user);
        ownerId = user.getId();

        packet = new Packet();
        packet.setName("RestTestPacket");
        packet.setStorageUUID(UUID.randomUUID());
        packet.setCreatedAt(Instant.now());
        packet.setChangedAt(Instant.now());
        packet.setOwner(user);
        packet = packetRepository.save(packet);

        ShareLink link = new ShareLink();
        code = "rest-test-code-" + UUID.randomUUID();
        link.setCode(code);
        link.setMaxDownloads(1);
        link.setCreatedAt(Instant.now());
        link.setExpiresAt(Instant.now().plusSeconds(3600));
        link.setPacket(packet);
        shareLinkRepository.save(link);
    }

    @AfterEach
    void tearDown() {
        packetService.deletePacket(packet.getId());
        userRepository.deleteById(ownerId);
    }

    @Test
    void findByShareLinkCode_existing_returns200() {
        given().auth().preemptive().basic("admin", "admin")
                .queryParam("shareLinkCode", code)
                .when().get("/custom/packets/findByShareLinkCode")
                .then().statusCode(200)
                .body("name", equalTo("RestTestPacket"));
    }

    @Test
    void findByShareLinkCode_unknown_returns404() {
        given().auth().preemptive().basic("admin", "admin")
                .queryParam("shareLinkCode", "no-such-code")
                .when().get("/custom/packets/findByShareLinkCode")
                .then().statusCode(404);
    }

    @Test
    void findByShareLinkCode_withoutParam_returns400() {
        given().auth().preemptive().basic("admin", "admin")
                .when().get("/custom/packets/findByShareLinkCode")
                .then().statusCode(400);
    }

    @Test
    void findByShareLinkCode_withoutAuth_returns401() {
        given().queryParam("shareLinkCode", "rest-test-code")
                .when().get("/custom/packets/findByShareLinkCode")
                .then().statusCode(401);
    }

    @Test
    void findByOwnerAndCreatedAtBetween_invalidDate_returns400() {
        given().auth().preemptive().basic("admin", "admin")
                .queryParam("ownerId", ownerId)
                .queryParam("start", "не-дата")
                .queryParam("end", "2026-12-31T00:00:00Z")
                .when().get("/custom/packets/findByOwnerAndCreatedAtBetween")
                .then().statusCode(400);
    }

    @Test
    void findByOwnerAndCreatedAtBetween_returns200() {
        given().auth().preemptive().basic("admin", "admin")
                .queryParam("ownerId", ownerId)
                .queryParam("start", Instant.now().minus(Duration.ofMinutes(5)).toString())
                .queryParam("end", Instant.now().toString())
                .when().get("/custom/packets/findByOwnerAndCreatedAtBetween")
                .then().statusCode(200)
                .body("size()", equalTo(1))
                .body("[0].name", equalTo("RestTestPacket"))
                .body("[0].owner.id", equalTo(ownerId.intValue()));
    }
}