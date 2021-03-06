package com.tr069.acs.handler;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.tr069.acs.handler.InformHandler;
import com.tr069.acs.message.request.InformMessage;
import com.tr069.acs.message.response.InformResponse;
import com.tr069.common.handler.RequestParser;
import com.tr069.common.handler.ResponseBuilder;

public class InformHandlerTest {

	public String xml;
	@Before
	public void init(){
		xml = "<?xml version=\"1.0\"?>" +
				"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:cwmp=\"urn:dslforum-org:cwmp-1-0\">" +
				"<soap:Header>" +
				"<cwmp:ID soap:mustUnderstand=\"1\">1234</cwmp:ID>" +
				"<cwmp:HoldRequests soap:mustUnderstand=\"1\">0</cwmp:HoldRequests>"+
				"<cwmp:NoMoreRequests>1</cwmp:NoMoreRequests>"+
				"</soap:Header>" +
				"<soap:Body>" +
				"<cwmp:Inform>" +
				"<DeviceId xsi:type=\"cwmp:DeviceIdStruct\">" +
				"<Manufacturer xsi:type=\"xsd:string(64)\" xsi:name=\"\">STB</Manufacturer>" +
				"<OUI xsi:type=\"xsd:string(6)\">00A900</OUI>" +
				"<ProductClass xsi:type=\"xsd:string(64)\">S7200</ProductClass>" +
				"<SerialNumber xsi:type=\"xsd:string(64)\">BC20BA05616C</SerialNumber>" +
				"</DeviceId>" +
				"<Event d=\"d\" soap:arrayType=\"cwmp:EventStruct[1]\">" +
				"<EventStruct>" +
				"<EventCode xsi:type=\"xsd:string(64)\">1 BOOT</EventCode>" +
				"<CommandKey></CommandKey>" +
				"</EventStruct>" +
				"</Event>" +
				"<MaxEnvelopes xsi:type=\"xsd:unsigendInt\">1</MaxEnvelopes>" +
				"<CurrentTime xsi:type=\"xsd:dateTime\">2000-01-01T00:01:20</CurrentTime>" +
				"<RetryCount xsi:type=\"xsd:unsigendInt\">0</RetryCount>" +
				"<ParameterList soap:arrayType=\"cwmp:ParameterValueStruct[2]\">" +
				"<ParameterValueStruct>" +
				"<Name xsi:type=\"xsd:string(256)\">Device.ManagementServer.UDPConnectionRequestAddress</Name>" +
				"<Value xsi:type=\"xsd:string(256)\">Device.ManagementServer.UDPConnectionRequestAddress</Value>" +
				"</ParameterValueStruct>" +
				"<ParameterValueStruct>" +
				"<Name xsi:type=\"xsd:string(256)\">Device.X_CU_STB.STBInfo.STBID</Name>" +
				"<Value xsi:type=\"xsd:string(32)\">00000100001500101151BC20BA04AC8A</Value>" +
				"</ParameterValueStruct>" +
				"</ParameterList>" +
				"</cwmp:Inform>" +
				"</soap:Body>" +
				"</soap:Envelope>";
	}
	
	@Test
	public void parseTest(){
		RequestParser<InformMessage> rp = new InformHandler();
		InformMessage msg = rp.parseXml(xml);
		Assert.assertEquals("Manufacturer wrong", "STB", msg.getDeviceId().getManufacturer());
		Assert.assertEquals("event size wrong", 1, msg.getEvent().size());
		Assert.assertEquals("EventCode wrong", "1 BOOT", msg.getEvent().get(0).getEventCode().getValue());
		Assert.assertEquals("Parameter size wrong", 2, msg.getParameterList().size());
		Assert.assertEquals("name wrong", "Device.ManagementServer.UDPConnectionRequestAddress", msg.getParameterList().get(0).getName());
		Assert.assertEquals("name wrong", "Device.ManagementServer.UDPConnectionRequestAddress", msg.getParameterList().get(0).getValue());
		Assert.assertEquals("name wrong", "Device.X_CU_STB.STBInfo.STBID", msg.getParameterList().get(1).getName());
		Assert.assertEquals("name wrong", "00000100001500101151BC20BA04AC8A", msg.getParameterList().get(1).getValue());
	}
	
	@Test
	public void buildTest(){
		ResponseBuilder<InformResponse> rb = new InformHandler();
		InformResponse ir = new InformResponse();
		ir.setMaxEnvelopes(1);
		String soap = rb.build(ir);
		Assert.assertEquals("soap wrong", "<soap:Body><cwmp:InformResponse><MaxEnvelopes>1</MaxEnvelopes></cwmp:InformResponse></soap:Body>", soap);
	}
}
