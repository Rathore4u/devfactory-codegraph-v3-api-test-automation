package com.codegraph.v3.restapi.automation.models.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class S3Config {
    String key;
    String secret;
    String layersBucket;
    String tmpBucket;
    String region;
}
