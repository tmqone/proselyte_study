package com.tmq.transaction_service.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmq.person.service.dto.DepositOrWithdrawalInitRequest;
import com.tmq.person.service.dto.InitTransactionRequest;
import com.tmq.person.service.dto.TransferInitRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule initTransactionRequestModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InitTransactionRequest.class, new JsonDeserializer<>() {
            @Override
            public InitTransactionRequest deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                ObjectMapper mapper = (ObjectMapper) p.getCodec();
                ObjectNode node = p.readValueAsTree();
                if (node.has("source_wallet_uid")) {
                    return mapper.treeToValue(node, TransferInitRequest.class);
                }
                return mapper.treeToValue(node, DepositOrWithdrawalInitRequest.class);
            }
        });
        return module;
    }
}