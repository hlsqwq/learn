package com.hls.configuration;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mqttconfig {
    String serverUrl;
    String clientId;
    String userName;
    String passwd;
}
