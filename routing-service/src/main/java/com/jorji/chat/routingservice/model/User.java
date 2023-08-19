package com.jorji.chat.routingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    String uuid;
    String username;
    String contactId;
}
