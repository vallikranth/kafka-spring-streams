package com.kafka.streams.apps;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import com.kafka.streams.model.Address;
import com.kafka.streams.model.Person;
import com.kafka.streams.model.PersonWitAddress;

@Component
public class PersonAddressJoiner {
	private static final Logger logger = LoggerFactory.getLogger(PersonAddressJoiner.class);
	
	private final Serde<Person> personSerde = new JsonSerde<>(Person.class);
	private final Serde<Address> addressSerde = new JsonSerde<>(Address.class);
	private final Serde<PersonWitAddress> personWithAddressSerde = new JsonSerde<>(PersonWitAddress.class);
	
 	@Bean
    public KStream<String, Person> kStream(StreamsBuilder kStreamBuilder) {
	KStream<String, Person> personStream = kStreamBuilder.stream("PersonsTopic", Consumed.with(Serdes.String(), personSerde)).peek((k,v) -> logger.info("Read Person Key:{} Value:{}",k,v));
	
	//Global Ktable join. Every streams consumer has entire Address topic data. Triggered only by person event. Enables join by value fields
	GlobalKTable<String, Address> addressGlobalTable = kStreamBuilder.globalTable("AddressTopic", Consumed.with(Serdes.String(), addressSerde));
	
	//person.ID is the key for persons topic and address.ID is the key for Address topic. Join by person.addressId = Address.Id
	KStream<String, PersonWitAddress> personWithAddressStream = personStream.join(addressGlobalTable, (personId, personValue) -> String.valueOf(personValue.getAddressId()), (person, address) -> {return getPersonAddressObject(person, address);});
	
	personWithAddressStream.foreach((k,v) -> logger.info("PersonWithAddress Key:{} Value:{}", k,v));
	
	//send this data to a new topic
	personWithAddressStream.to("personWithAddressTopic", Produced.with(Serdes.String(), personWithAddressSerde));
	return personStream;
		 
	}
 	 
 	 private PersonWitAddress getPersonAddressObject(Person p, Address a) {
 		PersonWitAddress pa = new PersonWitAddress();
 		pa.setAddressId(a.getId());
 		pa.setCity(a.getCity());
 		pa.setState(a.getState());
 		pa.setZip(a.getZip());
 		pa.setFirstName(p.getFirstName());
 		p.setLastName(p.getLastName());
 		return pa;
 	 }
}
