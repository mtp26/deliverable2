package deliverable2;

import static org.junit.Assert.*;

import org.junit.*;

import static org.mockito.Mockito.*;

import com.google.common.net.InternetDomainName;

public class InternetDomainNameTest {

	//The from method should normalize the argument string and return an InternetDomainName Object with that value
	@Test
	public void testFromValidStr() {
		InternetDomainName myDomainName = InternetDomainName.from("ABC.com.cn");
		assertEquals(myDomainName.toString(), "abc.com.cn");
	}
	
	//The from method should throw IllegalArgumentException if argument is not a valid domain name
	@Test(expected=IllegalArgumentException.class)
	public void testFromInvalidStr() {
		InternetDomainName myDomainName = InternetDomainName.from("ABC.com.cn@123");
	}
	
	//Domain name should equals to itself
	@Test
	public void testEqualSelf() {
		InternetDomainName domainName1 = InternetDomainName.from("abc.com.cn");
		InternetDomainName domainName2 = InternetDomainName.from("abc.com.cn");
		assertTrue(domainName1.equals(domainName2));
	}
	
	//Domain name should be case insensitive
	@Test
	public void testEqualUpperLowerCase() {
		InternetDomainName domainName1 = InternetDomainName.from("ABC.com.cn");
		InternetDomainName domainName2 = InternetDomainName.from("abc.com.cn");
		assertTrue(domainName1.equals(domainName2));
	}
	
	//Different Domain names should not be equal
	@Test
	public void testNotEqual() {
		InternetDomainName domainName1 = InternetDomainName.from("abc.com");
		InternetDomainName domainName2 = InternetDomainName.from("abc.com.cn");
		assertFalse(domainName1.equals(domainName2));
	}
	
	//two same objects should have the same hash code
	@Test
	public void testHashCode() {
		InternetDomainName domainName1 = InternetDomainName.from("abc.com");
		InternetDomainName domainName2 = domainName1;
		int hashCode1 = domainName1.hashCode();
		int hashCode2 = domainName2.hashCode();
		assertEquals(hashCode1, hashCode2);
	}
	
	//a domain name in PSL should be a public suffix
	@Test
	public void testIsPublicSuffix() {
		InternetDomainName myDomainName = InternetDomainName.from("com.cn");
		assertTrue(myDomainName.isPublicSuffix());
	}
	
	//a domain name not in  should be not a public suffix
	@Test
	public void testIsNotPublicSuffix() {
		InternetDomainName myDomainName = InternetDomainName.from("abc.com.cn");
		assertFalse(myDomainName.isPublicSuffix());
	}
	
	//domain name from Public Suffix List should be a public suffix
	@Test
	public void testPSL() {
		DomainList mockedPL = mock(DomainList.class);
		when(mockedPL.get(anyInt())).thenReturn("gov.co");
		InternetDomainName myDomainName = InternetDomainName.from((String) mockedPL.get(123));
		assertTrue(myDomainName.isPublicSuffix());
		verify(mockedPL).get(123);
	}
	
	//domain name from Non-Public Suffix List should not be a public suffix
	@Test
	public void testNPSL() {
		DomainList mockedNPL = mock(DomainList.class);
		when(mockedNPL.get(anyInt())).thenReturn("gov.abc");
		InternetDomainName myDomainName = InternetDomainName.from((String) mockedNPL.get(0));
		assertFalse(myDomainName.isPublicSuffix());
		verify(mockedNPL).get(0);
	}
	
	//a domain from Domain Name List should has public suffix
	@Test
	public void testHasPublicSuffix() {
		DomainList mockedDomainNameList = mock(DomainList.class);
		when(mockedDomainNameList.get(anyInt())).thenReturn("foo.gov.co");
		InternetDomainName myDomainName = InternetDomainName.from((String) mockedDomainNameList.get(123));
		assertTrue(myDomainName.hasPublicSuffix());
		verify(mockedDomainNameList).get(123);
	}
	
	//a domain from Non-Domain Name List should not has public suffix
	@Test
	public void testNotHasPublicSuffix() {
		DomainList mockedNonDomainNameList = mock(DomainList.class);
		when(mockedNonDomainNameList.get(anyInt())).thenReturn("foo.gov.abc");
		InternetDomainName myDomainName = InternetDomainName.from((String) mockedNonDomainNameList.get(0));
		assertFalse(myDomainName.hasPublicSuffix());
		verify(mockedNonDomainNameList).get(0);
	}
	
	//should return the public suffix portion of a domain name in Domain Name List
	@Test
	public void testPublicSuffix() {
		DomainList mockedDomainNameList = mock(DomainList.class);
		when(mockedDomainNameList.get(anyInt())).thenReturn("foo.gov.co");
		InternetDomainName myDomainName = InternetDomainName.from((String) mockedDomainNameList.get(123));
		assertEquals(myDomainName.publicSuffix().toString(),"gov.co");
		verify(mockedDomainNameList).get(123);
	}
	
	//should return null when retrieving the public suffix of a domain name in Non-Domain Name List
	@Test
	public void testNonePublicSuffix() {
		DomainList mockedNonDomainNameList = mock(DomainList.class);
		when(mockedNonDomainNameList.get(anyInt())).thenReturn("foo.gov.abc");
		InternetDomainName myDomainName = InternetDomainName.from((String) mockedNonDomainNameList.get(0));
		assertNull(myDomainName.publicSuffix());
		verify(mockedNonDomainNameList).get(0);
	}
	
	//a domain name in Domain Name List should be under the public suffix
	@Test
	public void testIsUnderPublicSuffix() {
		DomainList mockedDomainNameList = mock(DomainList.class);
		when(mockedDomainNameList.get(anyInt())).thenReturn("www.google.com");
		InternetDomainName myDomainName = InternetDomainName.from((String) mockedDomainNameList.get(123));
		assertTrue(myDomainName.isUnderPublicSuffix());
		verify(mockedDomainNameList).get(123);
	}
	
	//a domain name in Non-Domain Name List should not be under the public suffix
	@Test
	public void testIsNotUnderPublicSuffix() {
		DomainList mockedNonDomainNameList = mock(DomainList.class);
		when(mockedNonDomainNameList.get(anyInt())).thenReturn("google");
		InternetDomainName myDomainName = InternetDomainName.from((String) mockedNonDomainNameList.get(0));
		assertFalse(myDomainName.isUnderPublicSuffix());
		verify(mockedNonDomainNameList).get(0);
	}
	

}
