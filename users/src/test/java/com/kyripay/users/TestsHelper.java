package com.kyripay.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyripay.users.dto.User;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.io.IOException;

class TestsHelper {

    static com.kyripay.users.dto.User addUser(TestRestTemplate restTemplate) {
        User user;
        try {
            user = new ObjectMapper().readValue(userCreationBodySample(), User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return restTemplate.postForEntity("/api/v1/users", user, User.class).getBody();
    }

    static String userCreationBodySample() {
        try {
            return IOUtils.toString(TestsHelper.class.getResourceAsStream("/com.kyripay.users.test/user.json"));
        } catch (IOException e) {
            throw new RuntimeException("cannot read user resource", e);
        }
    }


}
