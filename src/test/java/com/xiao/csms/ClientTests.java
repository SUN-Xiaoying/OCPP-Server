package com.xiao.csms;

import com.xiao.csms.client.ClientRepo;
import com.xiao.csms.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;

@SpringBootTest
class ClientTests {
    @Autowired
    ClientService service;
    @Autowired
    ClientRepo repo;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    @Test
//    public void testFindIdToken() throws ClientNotFoundException {
//        Client re = service.findByIdToken("testSave");
//
//        assertThat(re.getId()).isEqualTo(1);
//    }
//
//    @Test
//    public void testSave() throws ClientNotFoundException {
//        service.save("testSave");
//    }
//
//    @Test
//    public void testUpdate() throws ClientNotFoundException {
//        service.updateClient("testId");
//        Client re = service.findByIdToken("testId");
//        assertThat(re.getLastTime()).isEqualTo(ZonedDateTime.now().format(formatter).toString());
//    }
}
