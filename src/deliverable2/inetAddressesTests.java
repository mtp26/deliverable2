package deliverable2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.InetAddress;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.net.InetAddresses;

//@RunWith(MockitoJUnitRunner.class)
public class inetAddressesTests {
	
	private InetAddress getMockAddr(String addr) {
		    InetAddress mockAddr = mock(InetAddress.class);
		    byte[] byteAddr = {(byte) 0xff};
		    doReturn(addr).when(mockAddr).getHostAddress();
		    doReturn(byteAddr).when(mockAddr).getAddress();
		    return mockAddr;
	}
	  
	// An all-zeros IPv4 address should be verified as a valid 
	@Test
	public void zerosAddress() {
		assertTrue("Must work in all zeros case",
				    InetAddresses.isInetAddress("0.0.0.0"));
	}
	
	@Test
	public void validSubnetMask() {
		assertTrue("Must work with subnet masks",
				    InetAddresses.isInetAddress("255.255.255.255"));
	}
	
	@Test
	public void invalidSubnetMask() {
		assertFalse("Must not work with an address that is out of bounds",
			     	InetAddresses.isInetAddress("256.256.256.256"));
	}
	
	@Test
	public void privateAddress() {
		assertTrue("Must work with private address space",
			     	InetAddresses.isInetAddress("192.168.0.1"));
	}
	
	@Test
	public void publicAddress() {
		assertTrue("Must work with public address space",
			     	InetAddresses.isInetAddress("8.8.8.8"));
	}
	
	@Test
	public void isMaximumIPv4() {
		//InetAddress testNetMask = InetAddresses.forString("255.255.255.255");
		InetAddress testNetMask = getMockAddr("255.255.255.255");
		assertTrue("Maximum range for IPv4",
					InetAddresses.isMaximum(testNetMask));
	}
	
	@Test
	public void isNotMaximumIPv4() {
		InetAddress testNotMaximum = InetAddresses.forString("0.0.0.0");
		assertFalse("Not the maximum for IPv4",
					InetAddresses.isMaximum(testNotMaximum));
	}
	
	@Test
	public void isMaximumIPv6() {
		InetAddress testNetMask = InetAddresses.forString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
		assertTrue("Maximum range for IPv4",
					InetAddresses.isMaximum(testNetMask));
	}
	
	@Test
	public void isNotMaximumIPv6() {
		InetAddress testNotMaximum = InetAddresses.forString("0000:0000:0000:0000:0000:0000:0000:0000");
		assertFalse("Not the maximum for IPv4",
					InetAddresses.isMaximum(testNotMaximum));
	}
	
	@Test
	public void incrementTestIPv4() {
		InetAddress addr1 = InetAddresses.forString("192.168.0.1");
		InetAddress addr2 = InetAddresses.forString("192.168.0.2");
		assertEquals("Increments IPv4 addresses correctly", 
				     InetAddresses.increment(addr1), addr2);
	}
	
	@Test
	public void incrementTestIPv6() {
		InetAddress addr1 = InetAddresses.forString("0000:0000:0000:0000:0000:0000:0000:0000");
		InetAddress addr2 = InetAddresses.forString("0000:0000:0000:0000:0000:0000:0000:0001");
		assertEquals("Increments IPv6 addresses correctly", 
				     InetAddresses.increment(addr1), addr2);
	}
	
	@Rule public ExpectedException exception = ExpectedException.none();
	@Test
	public void incrementTopNetmask() {
		InetAddress topNetMask = InetAddresses.forString("255.255.255.255");
		exception.expect(IllegalArgumentException.class);
		InetAddresses.increment(topNetMask);
	}
	
	@Test
	public void testToURIString() {
		InetAddress inAddress = getMockAddr("192.168.0.1");
		//System.out.println(InetAddresses.toAddrString(inAddress));
		//assertEquals(InetAddresses.toAddrString(inAddress), "192.168.0.1");
		//inAddress.InetAddresses.increment(inAddress);
		//InetAddresses.coerceToInteger(inAddress);
	}
}
