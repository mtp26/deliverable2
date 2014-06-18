package deliverable2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.InetAddress;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.net.InetAddresses;

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
	
	// Should be able to convert an IPv4 address string into an InetAddress object that represents the original string
	// Note that the toString() method for InetAddress returns a forward slash on addresses, so that character is ignored in this test
	@Test
	public void testForStringIPv4() {
		String strAddr = "10.0.0.1";
		InetAddress addr = InetAddresses.forString(strAddr);
		assertEquals(addr.toString().substring(1), strAddr);
	}
	
	// Should be able to convert an IPv6 address string into an InetAddress object that represents the original string
	// Note that the toString() method for InetAddress returns a forward slash on addresses, so that character is ignored in this test
	@Test
	public void testForStringIPv6() {
		String strAddr = "fe80:0:0:0:202:b3ff:fe1e:8329";
		InetAddress addr = InetAddresses.forString(strAddr);
		assertEquals(addr.toString().substring(1), strAddr);
	}
	
	// An illegal argument exception should be generated when string input other than an IPv4 or IPv6 address is given
	@Rule public ExpectedException AsdfArgEx = ExpectedException.none();
	@Test
	public void testForStringNull() {
		AsdfArgEx.expect(IllegalArgumentException.class);
		InetAddress addr = InetAddresses.forString("asdf");
	}
	
	// A null pointer exception should be generated when an attempt is made to convert a null string into an InetAddress
	@Rule public ExpectedException NullPtrStrEx = ExpectedException.none();
	@Test
	public void testForAddrStrNull() {
		NullPtrStrEx.expect(NullPointerException.class);
		InetAddress addr = InetAddresses.forString(null);
	}
	
	// A null pointer exception should be generated when an attempt is made to convert an InetAddress object 
	//  with a null reference into a string.
	@Rule public ExpectedException NullArgEx = ExpectedException.none();
	@Test
	public void testToAddrNullReference() {
		NullArgEx.expect(IllegalArgumentException.class);
		InetAddress mockAddr = getMockAddr("24.0.0.1");
		String strAddr = InetAddresses.toAddrString(mockAddr);
	}
	
	// Converting from an address string using forString to an InetAddress and then back to an address string using 
	//  toAddrString should produce a string that matches the original.
	// Define a string and convert it to an InetAddress using forString.
	// Convert the generated InetAddress to a string using toAddrString.
	// This generated string should be the same as the original address string.
	@Test
	public void testAddrStrCompare() {
		String addrStr = "172.0.0.1";
		InetAddress addr = InetAddresses.forString(addrStr);
		String addrStr2 = InetAddresses.toUriString(addr);
		assertEquals(addrStr, addrStr2);
	}
	
	// The toAddrString and toURIString methods should produce the same value for IPv4 addresses
	// Create an IPv4 address from the same address string.
	// Feed the address into toAddString and toURIString
	// The resulting string should be equal to each other and the original address string
	@Test
	public void testToAddrStringAndURIStringEq() {
		InetAddress addr = InetAddresses.forString("192.168.0.1");
		assertEquals("toAddrString and toURIString should produce the same string given the same IPv4 address input",
				     InetAddresses.toAddrString(addr), InetAddresses.toUriString(addr));
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
}
