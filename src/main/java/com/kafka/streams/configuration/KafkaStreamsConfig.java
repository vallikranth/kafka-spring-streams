package com.kafka.streams.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;

@Configuration
@ComponentScan("com.kafka.streams")
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfig {
	
	@Autowired
	private Environment env;
	
	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, env.getRequiredProperty("streams.applicationId"));
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, env.getRequiredProperty("streams.bootstrap.servers"));
        props.put(StreamsConfig.CLIENT_ID_CONFIG, env.getRequiredProperty("streams.applicationId"));
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, env.getProperty("streams.replication.factor"));
        props.put(StreamsConfig.STATE_DIR_CONFIG, env.getProperty("streams.state.dir"));
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, env.getProperty("streams.concurrency"));
        props.put(StreamsConfig.REQUEST_TIMEOUT_MS_CONFIG, env.getProperty("streams.request.timeout.ms"));
        return new StreamsConfig(props);
    }
 
}
