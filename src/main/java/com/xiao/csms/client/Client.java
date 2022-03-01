package com.xiao.csms.client;

import com.xiao.csms.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.sql.Date;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "id_token")
    String idToken;

    @Column(name = "last_time")
    String lastTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) throws ResourceNotFoundException {
        if(idToken == null || idToken.length() == 0){
            throw new ResourceNotFoundException("idToken from Authorize.req is null");
        }
        this.idToken = idToken;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString(){
        return "Client{\n"+
                "id = " + id + "\n" +
                "idToken = " + idToken + "\n" +
                "time = " + lastTime + "\n" +
                "}";
    }
}
