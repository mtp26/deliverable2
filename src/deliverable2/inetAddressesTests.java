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
	
	// Get a mocked address from a string
	private InetAddress getMockAddr(String addr) {
		    InetAddress mockAddr = mock(InetAddress.class);
		    doReturn(addr).when(mockAddr).getHostAddress();
		    return mockAddr;
	}
	
	// Get a mock address from a byte array
	private InetAddress getMockByteAddr(byte[] byteAddr) {
	    InetAddress mockAddr = mock(InetAddress.class);
	    doReturn(byteAddr).when(mockAddr).getAddress();
	    return mockAddr;
	}
	  
	// An all-zeros IPv4 address should be valid. This covers the bottom of the IPv4 address range.
	@Test
	public void testZerosIPv4Address() {
		assertTrue("Must work in all zeros IPv4 case",
				    InetAddresses.isInetAddress("0.0.0.0"));
	}
	
	// The universal broadcast address (all-ones) should be valid. This covers the top of the IPv4 address range.
	@Test
	public void testBroadcastAllIPv4Address() {
		assertTrue("Must work with broadcast address",
				    InetAddresses.isInetAddress("255.255.255.255"));
	}
	
	// An address that is outside of the IPv4 address range should not be valid
	@Test
	public void testOutOfBoundsIPv4Addressk() {
		assertFalse("Must not work with an address that is out of bounds",
			     	InetAddresses.isInetAddress("256.256.256.256"));
	}
	
	// An address in the private IPv4 address space should be valid
	@Test
	public void testPrivateIPv4Address() {
		assertTrue("Must work with private address space",
			     	InetAddresses.isInetAddress("192.168.0.1"));
	}
	
	// An address in the public IPv4 address space should be valid
	@Test
	public void testPublicIpV4Address() {
		assertTrue("Must work with public address space",
			     	InetAddresses.isInetAddress("8.8.8.8"));
	}
	
	// An IPv4 address with all bits set to one should be the maximum range for IPv4
	@Test
	public void testIsMaximumIPv4() {
		byte[] allOnesAddr = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
		InetAddress addr = getMockByteAddr(allOnesAddr);
		assertTrue("Maximum range for IPv4",
					InetAddresses.isMaximum(addr));
	}
	
	// An IPv4 address with all bits set to zero should not be the maximum range for IPv4
	@Test
	public void testIsNotMaximumIPv4() {
		byte[] allZerosAddr = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
		InetAddress addr = getMockByteAddr(allZerosAddr);
		assertFalse("Not the maximum for IPv4",
					InetAddresses.isMaximum(addr));
	}
	
	// An IPv6 address with all bits set to one should be the maximum range for IPv6
	@Test
	public void testIsMaximumIPv6() {
		byte[] allOnesAddr = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
							  (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
		InetAddress addr = getMockByteAddr(allOnesAddr);
		assertTrue("Maximum range for IPv6",
					InetAddresses.isMaximum(addr));
	}
	
	// An IPv6 address with all bits set to zero should be the maximum range for IPv6
	@Test
	public void testIsNotMaximumIPv6() {
		byte[] allZerosAddr = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				               (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
		InetAddress addr = getMockByteAddr(allZerosAddr);
		assertFalse("Not the maximum range for IPv6",
		InetAddresses.isMaximum(addr));
	}
	
	// Create two IPv4 addresses, with one being one value higher in the IPv4 address space.
	// Increment the smaller address and compare the addresses.
	// They should be equal to each other.
	@Test
	public void testIPv4Increment() {
		InetAddress addr1 = InetAddresses.forString("192.168.0.1");
		InetAddress addr2 = InetAddresses.forString("192.168.0.2");
		assertEquals("Increments IPv4 addresses correctly", 
				     InetAddresses.increment(addr1), addr2);
	}
	
	// Create two IPv6 addresses, with one being one value higher in the IPv6 address space.
	// Increment the smaller address and compare the addresses.
	// They should be equal to each other.
	@Test
	public void testIPv6increment() {
		InetAddress addr1 = InetAddresses.forString("0000:0000:0000:0000:0000:0000:0000:0000");
		InetAddress addr2 = InetAddresses.forString("0000:0000:0000:0000:0000:0000:0000:0001");
		assertEquals("Increments IPv6 addresses correctly", 
				     InetAddresses.increment(addr1), addr2);
	}
	
	// An IllegalArgumentException should be thrown if an IPv4 address is already at the top of the address space and is incremented
	@Rule public ExpectedException IPv4ArgEx = ExpectedException.none();
	@Test
	public void testIPv4TopIncrement() {
		InetAddress topNetMask = InetAddresses.forString("255.255.255.255");
		IPv4ArgEx.expect(IllegalArgumentException.class);
		InetAddresses.increment(topNetMask);
	}
	
	// An IllegalArgumentException should be thrown if an IPv6 address is already at the top of the address space and is incremented
	@Rule public ExpectedException IPv6ArgEx = ExpectedException.none();
	@Test
	public void testIPv6TopIncrement() {
		InetAddress topNetMask = InetAddresses.forString("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
		IPv6ArgEx.expect(IllegalArgumentException.class);
		InetAddresses.increment(topNetMask);
	}
	
	@Test
	public void testToURIString() {
		//InetAddress inAddress = getMockAddr("192.168.0.1");
		
		/* InetAddress topNetMask = InetAddresses.forString("255.255.255.255");
		InetAddress spyAddr = spy(topNetMask);
	    when(spyAddr.getHostAddress()).thenReturn("192.168.0.1");
	    */

		//System.out.println(InetAddresses.toAddrString(spyAddr));
		//assertEquals(InetAddresses.toAddrString(inAddress), "192.168.0.1");
		//inAddress.InetAddresses.increment(inAddress);
		//InetAddresses.coerceToInteger(inAddress);
	}
}
