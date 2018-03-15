package com.kafka.streams.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
@ComponentScan("com.kafka.streams")
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Serializable>> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Serializable> multiListenerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		multiListenerFactory.setConsumerFactory(consumerFactory());
		multiListenerFactory.setMessageConverter(new StringJsonMessageConverter());
		multiListenerFactory.setConcurrency(env.getRequiredProperty("consumer.concurrency", Integer.class));
		multiListenerFactory.getContainerProperties().setIdleEventInterval(env.getRequiredProperty("consumer.idleEventInterval.ms", Long.class));
		multiListenerFactory.getContainerProperties().setPollTimeout(env.getRequiredProperty("consumer.pollTimeout.ms", Long.class));
        
        return multiListenerFactory;
    }
	
	@Bean
    public ConsumerFactory<String, Serializable> consumerFactory() {
        Map<String, Object> configProps = getConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(configProps);
    }
    
	@Bean
	public KafkaListenerContainerFactory<?> batchFactory() {
	    ConcurrentKafkaListenerContainerFactory<String, Serializable> factory =
	            new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory());
	    factory.setMessageConverter(new StringJsonMessageConverter());
	    factory.setBatchListener(true);
	    return factory;
	}
	
	private Map<String, Object> getConsumerConfig() {
		Map<String, Object> configProps = new HashMap<>();
        configProps.put(
          ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
          env.getProperty("consumer.servers"));
        configProps.put(
        		ConsumerConfig.GROUP_ID_CONFIG, 
        		env.getProperty("consumer.groupId"));
        configProps.put(
        		ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 
                StringDeserializer.class);
              configProps.put(
            		  ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 
            		  StringDeserializer.class);
        configProps.put(
        		ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, 
        		env.getProperty("consumer.no-offset-strategy"));
        configProps.put(
        		ConsumerConfig.CLIENT_ID_CONFIG, 
                env.getProperty("consumer.clientId"));
        configProps.put(
        		ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 
                env.getProperty("consumer.session.timeout.ms"));
        configProps.put(
        		ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, 
                env.getProperty("consumer.autocommit"));
        configProps.put(
                ProducerConfig.BATCH_SIZE_CONFIG, 
                env.getProperty("producer.batchSize.kb"));
        configProps.put(
                ProducerConfig.LINGER_MS_CONFIG, 
                env.getProperty("producer.linger.ms"));
        configProps.put(
                ProducerConfig.BUFFER_MEMORY_CONFIG, 
                env.getProperty("producer.buffer"));
		return configProps;
	}
 
}
