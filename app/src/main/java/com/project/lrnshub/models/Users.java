package com.project.lrnshub.models;

import lombok.Data;

@Data
public class Users {
    String docID;
    String email;
    String password;
    String name;
    int userType;
}
